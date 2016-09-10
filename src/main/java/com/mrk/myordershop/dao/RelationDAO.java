package com.mrk.myordershop.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrk.myordershop.bean.Relation;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.dto.RelationFilter;
import com.mrk.myordershop.bean.dto.SearchFilter.SearchIn;
import com.mrk.myordershop.bean.dto.UserSearchResource;
import com.mrk.myordershop.constant.RelationStatus;
import com.mrk.myordershop.constant.Role;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

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

	List<UserSearchResource> search(Role role, List<RelationStatus> status, String query, SearchIn field,
			User user);

	int getRelatonCount(User user, Role roleWsaler, RelationStatus RelationStatus);

}
