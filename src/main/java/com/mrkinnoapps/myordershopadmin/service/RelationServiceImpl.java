package com.mrkinnoapps.myordershopadmin.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mrkinnoapps.myordershopadmin.bean.constant.RelationStatus;
import com.mrkinnoapps.myordershopadmin.bean.constant.Role;
import com.mrkinnoapps.myordershopadmin.bean.dto.RelationFilter;
import com.mrkinnoapps.myordershopadmin.bean.dto.RelationSummary;
import com.mrkinnoapps.myordershopadmin.bean.dto.SearchFilter.SearchIn;
import com.mrkinnoapps.myordershopadmin.bean.dto.UserSearchResource;
import com.mrkinnoapps.myordershopadmin.bean.entity.Relation;
import com.mrkinnoapps.myordershopadmin.bean.entity.Retailer;
import com.mrkinnoapps.myordershopadmin.bean.entity.Supplier;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.bean.entity.Wholesaler;
import com.mrkinnoapps.myordershopadmin.dao.RelationDAO;
import com.mrkinnoapps.myordershopadmin.dao.UserDAO;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;

@Service
public class RelationServiceImpl implements RelationService {

	Logger log = Logger.getLogger(RelationServiceImpl.class);
	@Autowired
	private RelationDAO relationDAO;
	@Autowired
	private NotificationService clientNotifierService;
	@Autowired
	private UserDAO userDAO;

	@Override
	public Relation addRelation(User currentUser, User secondaryUser) {
		Relation relation = null;
		try {
			relation = relationDAO.getByUsers(currentUser.getId(),
					secondaryUser.getId());
			relation.setStatus(RelationStatus.REQUESTED);
			relation.setPrimaryUser(currentUser);
			relation.setSecondaryUser(secondaryUser);
			relationDAO.update(relation);
		} catch (EntityDoseNotExistException e) {
			// e.printStackTrace();
			relation = new Relation();
			relation.setPrimaryUser(currentUser);
			relation.setSecondaryUser(secondaryUser);
			relation.setCreateTimeStamp(new Date());
			relation.setStatus(RelationStatus.REQUESTED);
			relationDAO.save(relation);
		}
		clientNotifierService.notifyAddRelation(currentUser, relation);
		return relation;
	}

	@Override
	public RelationSummary findByUser(User currentUser, Pageable pageable,
			RelationFilter filter) {
		RelationSummary relationSummary = new RelationSummary();
		List<RelationStatus> list = new ArrayList<RelationStatus>(EnumSet.allOf(RelationStatus.class));
		if (currentUser instanceof Retailer || currentUser instanceof Supplier){
			filter.setUserRole(Role.ROLE_WSALER);
			for (RelationStatus relationStatus : list) {
				int i = relationDAO.getRelatonCount(currentUser, Role.ROLE_WSALER,
						relationStatus);
				if(relationStatus == RelationStatus.ACCEPTED){
					relationSummary.setTotalRetailerOrAccepted(i);
				}
				if(relationStatus == RelationStatus.REJECTED){
					relationSummary.setTotalWholesalerOrRejected(i);
				}
				if(relationStatus == RelationStatus.REQUESTED){
					relationSummary.setTotalSupplierOrRequest(i);
				}
			}
			
		Page<Relation> page = relationDAO.findByPrimaryOrSecondaryUserId(currentUser.getId(),
				pageable, filter);
		relationSummary.setWholesalers(page);
		}else if (currentUser instanceof Wholesaler) {
			filter.setUserRole(Role.ROLE_RETAIL);
			for (RelationStatus relationStatus : list) {
				int i = relationDAO.getRelatonCount(currentUser, Role.ROLE_RETAIL,
						relationStatus);
				if(relationStatus == RelationStatus.ACCEPTED){
					relationSummary.setTotalRetailerOrAccepted(i);
				}
				if(relationStatus == RelationStatus.REJECTED){
					relationSummary.setTotalWholesalerOrRejected(i);
				}
				if(relationStatus == RelationStatus.REQUESTED){
					relationSummary.setTotalSupplierOrRequest(i);
				}
			}
			Page<Relation> retailers = relationDAO.findByPrimaryOrSecondaryUserId(currentUser.getId(),
					pageable, filter);
			relationSummary.setRetailers(retailers);
			filter.setUserRole(Role.ROLE_SUPPLIER);
			for (RelationStatus relationStatus : list) {
				int i = relationDAO.getRelatonCount(currentUser, Role.ROLE_SUPPLIER,
						relationStatus);
				if(relationStatus == RelationStatus.ACCEPTED){
					relationSummary.setAccepted(i);
				}
				if(relationStatus == RelationStatus.REJECTED){
					relationSummary.setRejected(i);
				}
				if(relationStatus == RelationStatus.REQUESTED){
					relationSummary.setRequest(i);
				}
			}
			Page<Relation> suppliers = relationDAO.findByPrimaryOrSecondaryUserId(currentUser.getId(),
					pageable, filter);
			relationSummary.setSuppliers(suppliers);
		}
		return relationSummary;
	}

	@Override
	public Relation acceptRelation(String userId, User currentUser)
			throws EntityDoseNotExistException {
		Relation relation = relationDAO.getByPrimaryAndSecondaryUserId(userId,
				currentUser.getId());
		relation.setStatus(RelationStatus.ACCEPTED);
		relationDAO.update(relation);
		clientNotifierService.notifyChangeRelationStatus(currentUser, relation);
		return relation;
	}

	@Override
	public Relation rejectRelation(String userId, User currentUser)
			throws EntityDoseNotExistException {
		Relation relation = relationDAO.getByPrimaryAndSecondaryUserId(userId,
				currentUser.getId());
		relation.setStatus(RelationStatus.REJECTED);
		relationDAO.update(relation);
		clientNotifierService.notifyChangeRelationStatus(currentUser, relation);
		return relation;
	}

	@Override
	public void removeRelation(String userId, User currentUser)
			throws EntityDoseNotExistException {
		Relation relation = null;
		try {
			relation = relationDAO.getByPrimaryAndSecondaryUserId(userId,
					currentUser.getId());
		} catch (EntityDoseNotExistException e) {
			relation = relationDAO.getByPrimaryAndSecondaryUserId(
					currentUser.getId(), userId);
		}
		relationDAO.delete(relation);
		clientNotifierService.notifyDeleteRelation(currentUser, relation);
	}

	@Override
	public Relation get(String userId, User currentUser)
			throws EntityDoseNotExistException {
		Relation relation = null;
		try {
			relation = relationDAO.getByPrimaryAndSecondaryUserId(userId,
					currentUser.getId());
		} catch (EntityDoseNotExistException e) {
			relation = relationDAO.getByPrimaryAndSecondaryUserId(
					currentUser.getId(), userId);
		}
		return relation;
	}

	@Override
	public List<User> searchUser(Role role, String query, SearchIn field,
			User user) {
		List<User> users = new ArrayList<User>();
		List<UserSearchResource> searchPage = relationDAO.search(role, query,
				field, user);
		for (UserSearchResource userSearchResource : searchPage) {
			try {
				Relation relation = relationDAO.getByUsers(user.getId(),
						userSearchResource.getResultId());
				relation.setCurrentUserEmail(user.getEmail());
				users.add(relation.getUser());
			} catch (EntityDoseNotExistException e) {
				// e.printStackTrace();
			}
		}
		log.debug(users.size());
		return users;
	}

	@Override
	public RelationSummary getActiveRelationCount(String userId) throws EntityDoseNotExistException {
		User user = userDAO.get(userId);
		RelationSummary relationSummary = new RelationSummary();
		if (user instanceof Retailer || user instanceof Supplier){
			int total = relationDAO.getRelatonCount(user, Role.ROLE_WSALER,
					RelationStatus.ACCEPTED);
			relationSummary.setTotalWholesalerOrRejected(total);
		} else if (user instanceof Wholesaler) {
			int retailerCount = relationDAO.getRelatonCount(user, Role.ROLE_RETAIL,
					RelationStatus.ACCEPTED);
			int supplierCount = relationDAO.getRelatonCount(user, Role.ROLE_SUPPLIER,
					RelationStatus.ACCEPTED);
			relationSummary.setTotalRetailerOrAccepted(retailerCount);
			relationSummary.setTotalSupplierOrRequest(supplierCount);
		}
		return relationSummary;
	}

	@Override
	public Relation getRelation(Long relationId, User currentUser) throws EntityDoseNotExistException {
		Relation relation = null;
		try {
			relation = relationDAO.get(relationId, currentUser);
		} catch (EntityDoseNotExistException e) {
			throw e;
		}
		return relation;
	}

	@Override
	public void update(Relation relation,User currentUser) throws EntityDoseNotExistException {
			Relation relation2 = relationDAO.get(relation.getId(), currentUser);
			relation2.setStatus(relation.getStatus());
			relationDAO.update(relation2);
	}

	/* 
	 * Mallinath Jun 2, 2016
	 */
	@Override
	public Page<Relation> findByPrimaryOrSecondaryUserId(String userId,
			Pageable pageable, RelationFilter filter) {
		return relationDAO.findByPrimaryOrSecondaryUserId(userId, pageable, filter);
	}

}
