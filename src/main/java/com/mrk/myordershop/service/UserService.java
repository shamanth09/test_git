package com.mrk.myordershop.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrk.myordershop.bean.Image;
import com.mrk.myordershop.bean.Register;
import com.mrk.myordershop.bean.Token;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.UserRole;
import com.mrk.myordershop.bean.dto.UserCredential;
import com.mrk.myordershop.bean.dto.UserFilter;
import com.mrk.myordershop.bean.dto.UserSearchFilter;
import com.mrk.myordershop.bean.dto.UserSearchResource;
import com.mrk.myordershop.constant.Role;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;
import com.mrk.myordershop.exception.InvalidDataException;

/**
 * UserService.java Naveen Apr 10, 2015
 */
public interface UserService {

	void saveUserRole(UserRole user);

	void save(User user) throws EntityNotPersistedException;

	void activateUser(String token) throws EntityDoseNotExistException;

	User update(String id, User user) throws EntityDoseNotExistException;

	User updateUserImage(String id, Image image)
			throws EntityDoseNotExistException;

	User register(Register register) throws EntityNotPersistedException;

	UserRole getUserRole(Role role) throws EntityDoseNotExistException;

	Page<User> find(UserFilter filter, Pageable pageable, User user);

	User getActiveUser(String id) throws EntityDoseNotExistException;
	
	User get(String id) throws EntityDoseNotExistException;

	void changePassword(UserCredential userCredential, User user)
			throws InvalidDataException;

	List<UserSearchResource> search(UserSearchFilter filter, User user,
			Pageable pageable);

	List<User> getByRole(Role roleWsaler);

	User forgotPassword(String emailOrMobile, String actionUrl)
			throws EntityDoseNotExistException;

	User changePassword(String password, String token)
			throws EntityDoseNotExistException;

	void resentToken(String userId, Token.Type type, String actionUrl)
			throws EntityDoseNotExistException;
	
	User getUserByMobieOrEmail(String mobieOrEmail)
			throws EntityDoseNotExistException;

}
