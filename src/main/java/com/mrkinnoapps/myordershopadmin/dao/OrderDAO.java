package com.mrkinnoapps.myordershopadmin.dao;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrkinnoapps.myordershopadmin.bean.dto.OrderFilter;
import com.mrkinnoapps.myordershopadmin.bean.entity.Order;
import com.mrkinnoapps.myordershopadmin.bean.entity.Retailer;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.bean.entity.Wholesaler;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;

public interface OrderDAO extends SearchDAO {

	Order saveOrder(Order order);

	void update(Order order);

	Order getOrder(int orderId, User user) throws EntityDoseNotExistException;

	Order getOrder(int orderId) throws EntityDoseNotExistException;

	Order getOrder(String orderNo, User user)
			throws EntityDoseNotExistException;

	Page<Order> getOrders(Pageable pageable, Retailer retailer,
			OrderFilter filter);

	Page<Order> getOrders(Pageable pageable, Wholesaler wholesaler,
			OrderFilter filter);

	void delete(Order order);

	Page<Order> deliversBy(Date date, Pageable pageable, Wholesaler wholesaler);
	
	Integer getOrderCount(User user);

	User getUser(Integer orderId);

	Page<Order> getOrders(Pageable pageable, OrderFilter filter);
	

}
