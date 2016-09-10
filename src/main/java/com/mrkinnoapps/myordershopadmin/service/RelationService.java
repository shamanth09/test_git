package com.mrkinnoapps.myordershopadmin.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrkinnoapps.myordershopadmin.bean.constant.Role;
import com.mrkinnoapps.myordershopadmin.bean.dto.RelationFilter;
import com.mrkinnoapps.myordershopadmin.bean.dto.RelationSummary;
import com.mrkinnoapps.myordershopadmin.bean.dto.SearchFilter.SearchIn;
import com.mrkinnoapps.myordershopadmin.bean.entity.Relation;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;

public interface RelationService {

	Relation addRelation(User currentUser, User secondaryUser);

	Relation get(String userId, User currentUser)
			throws EntityDoseNotExistException;

	RelationSummary findByUser(User currentUser, Pageable pageable,
			RelationFilter filter);

	Relation acceptRelation(String userId, User currentUser)
			throws EntityDoseNotExistException;

	Relation rejectRelation(String userId, User currentUser)
			throws EntityDoseNotExistException;

	void removeRelation(String userId, User currentUser)
			throws EntityDoseNotExistException;

	List<User> searchUser(Role role, String query, SearchIn field, User user);

	RelationSummary getActiveRelationCount(String userId) throws EntityDoseNotExistException;

	Relation getRelation(Long relationId, User currentUser) throws EntityDoseNotExistException;

	void update(Relation relation, User currentUser) throws EntityDoseNotExistException;
	
	
	Page<Relation> findByPrimaryOrSecondaryUserId(String userId,
			Pageable pageable, RelationFilter filter);
	
}
