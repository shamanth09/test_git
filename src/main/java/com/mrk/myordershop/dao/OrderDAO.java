package com.mrk.myordershop.dao;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrk.myordershop.bean.Order;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.dto.OrderFilter;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;

public interface OrderDAO extends SearchDAO {

	void save(Order order) throws EntityNotPersistedException;

	void update(Order order);

	Order getOrder(int orderId, User user) throws EntityDoseNotExistException;

	Order getOrder(int orderId) throws EntityDoseNotExistException;

	Page<Order> getOrders(Pageable pageable, User user, OrderFilter filter);

	void delete(Order order);

	Page<Order> deliversBy(Date date, Pageable pageable, Wholesaler wholesaler);

	void updateCustomerNumberAndUserReference(String mobile, User user);
	
	public void updateCustomerNumber(String oldMobile, String newMobile);
	
	// fetch user by passing newMobile in service if user is present call updateCustomerNumber.
}
