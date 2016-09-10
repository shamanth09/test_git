package com.mrkinnoapps.myordershopadmin.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrkinnoapps.myordershopadmin.bean.constant.ActiveFlag;
import com.mrkinnoapps.myordershopadmin.bean.constant.Role;
import com.mrkinnoapps.myordershopadmin.bean.dto.SearchFilter.SearchIn;
import com.mrkinnoapps.myordershopadmin.bean.dto.NotRegisteredUser;
import com.mrkinnoapps.myordershopadmin.bean.dto.UserFilter;
import com.mrkinnoapps.myordershopadmin.bean.dto.UserSearchResource;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.bean.entity.UserRole;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;
import com.mrkinnoapps.myordershopadmin.util.searchengine.SearchResult;

public interface UserDAO {

	void save(User user) throws EntityNotPersistedException;

	void update(User user);

	User get(String id, ActiveFlag activeFlag)
			throws EntityDoseNotExistException;

	User get(String id) throws EntityDoseNotExistException;

	Page<User> find(UserFilter filter, Pageable pageable, User user);

	List<User> getByRole(Role role);

	User getUserByMobieOrEmail(String mobieOrEmail, ActiveFlag activeFlag)
			throws EntityDoseNotExistException;

	User getUserByMobieOrEmail(String mobieOrEmail)
			throws EntityDoseNotExistException;

	List<UserSearchResource> search(Role role, String query, SearchIn field,
			User user, String[] excludeUsers);

	void delete(User user);

	void saveUserRole(UserRole user);

	UserRole getUserRoleByRole(Role role) throws EntityDoseNotExistException;

	List<UserRole> getUserRoles() throws EntityDoseNotExistException;

	public UserRole getUserRole(int id) throws EntityDoseNotExistException;

	void changePassword(String password, String userID) throws EntityDoseNotExistException;

	List<SearchResult> searchUser(UserFilter filter, String q);

	/**
	 * Mallinath Jul 22, 2016
	 */
	public Page<NotRegisteredUser> getNotRegisteredUsers(Pageable pageable);

	/**
	 * Mallinath Jul 21, 2016
	 */

}
