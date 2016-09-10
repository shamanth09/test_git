package com.mrkinnoapps.myordershopadmin.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrkinnoapps.myordershopadmin.bean.constant.OrderStatus;
import com.mrkinnoapps.myordershopadmin.bean.dto.OrderFilter;
import com.mrkinnoapps.myordershopadmin.bean.dto.SearchFilter;
import com.mrkinnoapps.myordershopadmin.bean.dto.SearchResource;
import com.mrkinnoapps.myordershopadmin.bean.entity.Cancellation;
import com.mrkinnoapps.myordershopadmin.bean.entity.Image;
import com.mrkinnoapps.myordershopadmin.bean.entity.Order;
import com.mrkinnoapps.myordershopadmin.bean.entity.Retailer;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.bean.entity.Wholesaler;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;

public interface OrderService {

	Order saveOrder(Order order);
	Order getOrder(int orderId, String userId) throws EntityDoseNotExistException;
	Order getOrder(int orderId) throws EntityDoseNotExistException;
	Page<Order> getOrders(Pageable pageable,String activeId,
			OrderFilter filter) throws EntityDoseNotExistException;

	Page<Order> getOrders(Pageable pageable, Wholesaler wholesaler,
			OrderFilter filter);

	Order getOrder(String orderNo, User user)
			throws EntityDoseNotExistException;

	Order updateOrderStatus(int orderId, OrderStatus status,
			Wholesaler wholesaler) throws EntityDoseNotExistException;

	Order cancelOrder(int orderId, Cancellation cancellation, Retailer retailer)
			throws EntityDoseNotExistException;

	Order cancelOrder(int orderId, Cancellation cancellation,
			Wholesaler wholesaler) throws EntityDoseNotExistException;

	List<SearchResource> search(SearchFilter filter, Wholesaler wholesaler);

	List<SearchResource> search(SearchFilter filter, Retailer retailer);

	Page<Order> deliversBy(Date date, Pageable pageable, Wholesaler wholesaler);

	User getUser(Integer orderId);
	
	Order updateImage(int orderId, Image image)
			throws EntityDoseNotExistException, EntityNotPersistedException;
	void update(Order order) throws EntityDoseNotExistException, EntityNotPersistedException;
	
	public void delete(Order order);
	
}
