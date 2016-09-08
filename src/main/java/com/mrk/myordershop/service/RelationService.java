package com.mrk.myordershop.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrk.myordershop.bean.Relation;
import com.mrk.myordershop.bean.RelationSetting;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.dto.RelationFilter;
import com.mrk.myordershop.bean.dto.SearchFilter.SearchIn;
import com.mrk.myordershop.constant.RelationStatus;
import com.mrk.myordershop.constant.Role;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

public interface RelationService {

	Relation addRelation(User currentUser, User secondaryUser) throws EntityDoseNotExistException;

	Relation get(String userId, User currentUser)
			throws EntityDoseNotExistException;

	Page<Relation> findByUser(User currentUser, Pageable pageable,
			RelationFilter filter);

	Relation updateSetting(String userId, RelationSetting setting,
			User currentUser) throws EntityDoseNotExistException;

	Relation acceptRelation(String userId, User currentUser)
			throws EntityDoseNotExistException;

	Relation rejectRelation(String userId, User currentUser)
			throws EntityDoseNotExistException;

	void removeRelation(String userId, User currentUser)
			throws EntityDoseNotExistException;

	List<User> searchUser(Role role, List<RelationStatus> status, String query, SearchIn field, User user);

	Map<String, Object> getActiveRelationCount(String userId, User user)
			throws EntityDoseNotExistException;
}
