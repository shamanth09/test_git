package com.mrkinnoapps.myordershopadmin.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrkinnoapps.myordershopadmin.bean.constant.RelationStatus;
import com.mrkinnoapps.myordershopadmin.bean.constant.Role;
import com.mrkinnoapps.myordershopadmin.bean.dto.RelationFilter;
import com.mrkinnoapps.myordershopadmin.bean.dto.SearchFilter.SearchIn;
import com.mrkinnoapps.myordershopadmin.bean.dto.UserSearchResource;
import com.mrkinnoapps.myordershopadmin.bean.entity.Relation;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;

public interface RelationDAO {

	void save(Relation relation);

	void update(Relation relation);

	Relation get(Long id, User currentUser) throws EntityDoseNotExistException;

	Relation getByPrimaryAndSecondaryUserId(String primaryUserId,
			String secondaryUserId) throws EntityDoseNotExistException;

	Relation getByUsers(String userId1, String userId2)
			throws EntityDoseNotExistException;

	Page<Relation> findByPrimaryOrSecondaryUserId(String userId,
			Pageable pageable, RelationFilter filter);

	void delete(Relation relation);

	List<UserSearchResource> search(Role role, String query, SearchIn field,
			User user);

	int getRelatonCount(User user, Role roleWsaler, RelationStatus RelationStatus);

}
