package com.mrk.myordershop.util.dbcreator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mrk.myordershop.bean.UserRole;
import com.mrk.myordershop.config.annotation.PersistTransactional;
import com.mrk.myordershop.constant.Role;
import com.mrk.myordershop.dao.UserDAO;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

/**
 * RetailerCreate.java Naveen Apr 7, 2015
 */
@Component
public class UserRoleCreate {
	private static final Logger log = Logger.getLogger(UserRoleCreate.class);

	@Autowired
	private UserDAO userDao;

	@PersistTransactional
	public void create() {
		for (Role role : Role.values()) {
			UserRole userRole = null;
			try {
				userRole = userDao.getUserRole(role);
			} catch (EntityDoseNotExistException e) {
				userRole = new UserRole();
				userRole.setRole(role);
				userRole.setDiscription("role on " + role);
				userDao.saveUserRole(userRole);
				log.debug(role + " created");
			}
		}
	}

}
