package com.mrk.myordershop.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.UserRole;
import com.mrk.myordershop.bean.dto.SearchFilter.SearchIn;
import com.mrk.myordershop.bean.dto.UserFilter;
import com.mrk.myordershop.bean.dto.UserSearchResource;
import com.mrk.myordershop.constant.ActiveFlag;
import com.mrk.myordershop.constant.Role;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;

public interface UserDAO {

	void save(User user) throws EntityNotPersistedException;

	void saveUserRole(UserRole user);

	void update(User user);

	User get(String id, ActiveFlag activeFlag)
			throws EntityDoseNotExistException;

	User get(String id) throws EntityDoseNotExistException;
	
	User getUserByMobieOrEmail(String mobieOrEmail, ActiveFlag activeFlag)
			throws EntityDoseNotExistException;
	
	User getUserByMobieOrEmail(String mobieOrEmail)
			throws EntityDoseNotExistException;

	UserRole getUserRole(Role role) throws EntityDoseNotExistException;

	Page<User> find(UserFilter filter, Pageable pageable, User user);

	List<User> getByRole(Role role);

	List<UserSearchResource> search(Role role, String query, SearchIn field,
			User user, String[] excludeUsers);

	void delete(User user);
}
