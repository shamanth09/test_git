package com.mrkinnoapps.myordershopadmin.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrkinnoapps.myordershopadmin.bean.constant.Role;
import com.mrkinnoapps.myordershopadmin.bean.dto.NotRegisteredUser;
import com.mrkinnoapps.myordershopadmin.bean.dto.Register;
import com.mrkinnoapps.myordershopadmin.bean.dto.UserFilter;
import com.mrkinnoapps.myordershopadmin.bean.dto.UserSearchFilter;
import com.mrkinnoapps.myordershopadmin.bean.dto.UserSearchResource;
import com.mrkinnoapps.myordershopadmin.bean.entity.Image;
import com.mrkinnoapps.myordershopadmin.bean.entity.Token;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.bean.entity.UserRole;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;
import com.mrkinnoapps.myordershopadmin.exception.InvalidDataException;
import com.mrkinnoapps.myordershopadmin.util.searchengine.SearchResult;

/**
 * UserService.java Naveen Apr 10, 2015
 */
public interface UserService {

	void saveUserRole(UserRole user);

	void save(User user) throws EntityNotPersistedException,
			EntityDoseNotExistException;

	void activateUser(String token) throws EntityDoseNotExistException,
			EntityNotPersistedException;

	User update(User user) throws EntityDoseNotExistException;

	User updateUserImage(String id, Image image)
			throws EntityDoseNotExistException;

	User register(Register register) throws EntityNotPersistedException;

	UserRole getUserRole(Role role) throws EntityDoseNotExistException;

	Page<User> find(UserFilter filter, Pageable pageable, User user);

	User getActiveUser(String id) throws EntityDoseNotExistException;

	User get(String id) throws EntityDoseNotExistException;

	void changePassword(String password, String userId)
			throws InvalidDataException, EntityDoseNotExistException;

	List<UserSearchResource> search(UserSearchFilter filter, User user,
			Pageable pageable);

	List<User> getByRole(Role roleWsaler);

	User forgotPassword(String emailOrMobile, String actionUrl)
			throws EntityDoseNotExistException, EntityNotPersistedException;

//	User changePassword(String password, String token)
//			throws EntityDoseNotExistException, EntityNotPersistedException;

	void resentToken(String userId, Token.Type type, String actionUrl)
			throws EntityDoseNotExistException;

	User getUserByMobieOrEmail(String mobieOrEmail)
			throws EntityDoseNotExistException;

	List<UserRole> getUserRoles() throws EntityDoseNotExistException;

	void changeRole(String userId, UserRole role) throws EntityNotPersistedException, EntityDoseNotExistException;

	void changeUserStatus(String userId, User user) throws EntityDoseNotExistException, EntityNotPersistedException;

	List<SearchResult> searchUser(UserFilter filter, String q);
	
	public void delete(User user);
    
	public Page<NotRegisteredUser> getNotRegisteredUsers(Pageable pageable);

}
