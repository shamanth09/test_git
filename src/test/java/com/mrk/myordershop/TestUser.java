package com.mrk.myordershop;

import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mrk.myordershop.bean.Address;
import com.mrk.myordershop.bean.Retailer;
import com.mrk.myordershop.bean.UserRole;
import com.mrk.myordershop.constant.ActiveFlag;
import com.mrk.myordershop.constant.Role;
import com.mrk.myordershop.dao.UserDAO;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;
import com.mrk.myordershop.service.RetailerService;

public class TestUser {

	private UserDAO userDao;
	private RetailerService retailerService;

	public TestUser() {
		ApplicationContext appContext = new ClassPathXmlApplicationContext(
				"META-INF/spring/application-context.xml");
		userDao = (UserDAO) appContext.getBean(UserDAO.class);
		retailerService = appContext.getBean(RetailerService.class);
	}

	private UserRole getRoles(Role role) {
		UserRole userRole = null;
		try {
			userRole = userDao.getUserRole(role);
		} catch (EntityDoseNotExistException e) {
			e.printStackTrace();
			userRole = new UserRole();
			userRole.setRole(role);
			userRole.setDiscription("eeee");
			userDao.saveUserRole(userRole);
		}
		return userRole;
	}

	private Retailer createReUser() {
		Retailer user = new Retailer();
		user.setActiveFlag(ActiveFlag.ACTIVE);
		user.setDob(new Date());
		user.setEmail("naveen");
		user.setPassword("123");
		user.getUserRoles().add(this.getRoles(Role.ROLE_WSALER));
		user.setAddress(this.createAddress());
		return user;
	}

	public Address createAddress() {
		Address address = new Address();
		address.setPincode("265662");
		address.setStreet("asfdsaf");
		address.setTitle("ttttttf");
		return address;
	}

	public static void main(String arg[]) throws EntityDoseNotExistException {
		TestUser testUser = new TestUser();
		Retailer user = testUser.createReUser();
		try {
			testUser.userDao.save(user);
		} catch (EntityNotPersistedException e) {
			e.printStackTrace();
		}

		System.out.println("saved...");
	}
}
