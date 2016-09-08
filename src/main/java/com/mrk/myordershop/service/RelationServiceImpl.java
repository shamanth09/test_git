package com.mrk.myordershop.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mrk.myordershop.bean.Relation;
import com.mrk.myordershop.bean.RelationSetting;
import com.mrk.myordershop.bean.Retailer;
import com.mrk.myordershop.bean.Supplier;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.dto.RelationFilter;
import com.mrk.myordershop.bean.dto.SearchFilter.SearchIn;
import com.mrk.myordershop.bean.dto.UserSearchResource;
import com.mrk.myordershop.config.annotation.PersistTransactional;
import com.mrk.myordershop.config.annotation.ReadTransactional;
import com.mrk.myordershop.constant.RelationStatus;
import com.mrk.myordershop.constant.Role;
import com.mrk.myordershop.dao.RelationDAO;
import com.mrk.myordershop.dao.UserDAO;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.notify.annotation.Notify;
import com.mrk.myordershop.notify.annotation.NotifyType;

@Service
public class RelationServiceImpl implements RelationService {

	Logger log = Logger.getLogger(RelationServiceImpl.class);
	@Autowired
	private RelationDAO relationDAO;
	@Autowired
	private UserDAO userDAO;

	@Override
	@Notify(tier = NotifyType.relationrequested, currentUserPosition = 0)
	@PersistTransactional
	public Relation addRelation(User currentUser, User secondaryUser) throws EntityDoseNotExistException {
		secondaryUser = userDAO.get(secondaryUser.getId());
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
		return relation;
	}

	@Override
	@ReadTransactional
	public Page<Relation> findByUser(User currentUser, Pageable pageable,
			RelationFilter filter) {
		return relationDAO.findByPrimaryOrSecondaryUserId(currentUser.getId(),
				pageable, filter);
	}

	@PersistTransactional
	@Override
	public Relation updateSetting(String userId, RelationSetting setting,
			User currentUser) throws EntityDoseNotExistException {
		Relation relation = relationDAO.getByUsers(userId, currentUser.getId());
		if (relation.getSetting() == null)
			relation.setSetting(new RelationSetting());
		relation.getSetting().setAdvance(setting.getAdvance());
		relation.getSetting().setBlocked(setting.isBlocked());
		relation.getSetting().setRateCut(setting.getRateCut());
		relation.getSetting().setMinWeight(setting.getMinWeight());
		relationDAO.update(relation);
		return relation;
	}

	@Notify(tier = NotifyType.relationstatuschange, currentUserPosition = 1)
	@PersistTransactional
	@Override
	public Relation acceptRelation(String userId, User currentUser)
			throws EntityDoseNotExistException {
		Relation relation = relationDAO.getByPrimaryAndSecondaryUserId(userId,
				currentUser.getId());
		relation.setStatus(RelationStatus.ACCEPTED);
		relationDAO.update(relation);
		// clientNotifierService.notifyChangeRelationStatus(currentUser,
		// relation);
		return relation;
	}

	@Override
	@Notify(tier = NotifyType.relationstatuschange, currentUserPosition = 1)
	@PersistTransactional
	public Relation rejectRelation(String userId, User currentUser)
			throws EntityDoseNotExistException {
		Relation relation = relationDAO.getByPrimaryAndSecondaryUserId(userId,
				currentUser.getId());
		relation.setStatus(RelationStatus.REJECTED);
		relationDAO.update(relation);
		// clientNotifierService.notifyChangeRelationStatus(currentUser,
		// relation);
		return relation;
	}

	@Notify(tier = NotifyType.relationremoved, currentUserPosition = 1)
	@PersistTransactional
	@Override
	public void removeRelation(String userId, User currentUser)
			throws EntityDoseNotExistException {
		Relation relation = null;
		relation = relationDAO.getByUsers(userId, currentUser.getId());
		relationDAO.delete(relation);
		// clientNotifierService.notifyDeleteRelation(currentUser, relation);
	}

	@Override
	@ReadTransactional
	public Relation get(String userId, User currentUser)
			throws EntityDoseNotExistException {
		Relation relation = relationDAO.getByUsers(userId, currentUser.getId());
		return relation;
	}

	@Override
	@ReadTransactional
	public List<User> searchUser(Role role, List<RelationStatus> status,
			String query, SearchIn field, User user) {
		List<User> users = new ArrayList<User>();
		List<UserSearchResource> searchPage = relationDAO.search(role, status,
				query, field, user);
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
	@ReadTransactional
	public Map<String, Object> getActiveRelationCount(String userId,
			User currentUser) throws EntityDoseNotExistException {
		User user = userDAO.get(userId);
		Map<String, Object> result = new HashMap<String, Object>();
		if ((currentUser instanceof Retailer) && (user instanceof Wholesaler)) {
			int total = relationDAO.getRelatonCount(user, Role.ROLE_RETAIL,
					RelationStatus.ACCEPTED);
			result.put(Role.ROLE_RETAIL.toString(), total);
		} else if ((currentUser instanceof Supplier)
				&& (user instanceof Wholesaler)) {
			int total = relationDAO.getRelatonCount(user, Role.ROLE_SUPPLIER,
					RelationStatus.ACCEPTED);
			result.put(Role.ROLE_SUPPLIER.toString(), total);
		} else if ((currentUser instanceof Wholesaler)
				&& ((user instanceof Supplier) || (user instanceof Retailer))) {
			int total = relationDAO.getRelatonCount(user, Role.ROLE_WSALER,
					RelationStatus.ACCEPTED);
			result.put(Role.ROLE_WSALER.toString(), total);
		} else if (currentUser instanceof Wholesaler) {
			int total = relationDAO.getRelatonCount(currentUser,
					Role.ROLE_RETAIL, RelationStatus.ACCEPTED);
			result.put(Role.ROLE_RETAIL.toString(), total);

			total = relationDAO.getRelatonCount(currentUser,
					Role.ROLE_SUPPLIER, RelationStatus.ACCEPTED);
			result.put(Role.ROLE_SUPPLIER.toString(), total);
		} else if ((currentUser instanceof Supplier)
				|| (currentUser instanceof Retailer)) {
			int total = relationDAO.getRelatonCount(currentUser,
					Role.ROLE_WSALER, RelationStatus.ACCEPTED);
			result.put(Role.ROLE_WSALER.toString(), total);
		}
		return result;
	}
}
