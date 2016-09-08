package com.mrk.myordershop.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrk.myordershop.bean.Cancellation;
import com.mrk.myordershop.bean.Image;
import com.mrk.myordershop.bean.InstantOrder;
import com.mrk.myordershop.bean.Order;
import com.mrk.myordershop.bean.OrderAcceptance;
import com.mrk.myordershop.bean.Retailer;
import com.mrk.myordershop.bean.Supplier;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.WholesalerInstantOrder;
import com.mrk.myordershop.bean.dto.OrderFilter;
import com.mrk.myordershop.bean.dto.SearchFilter;
import com.mrk.myordershop.bean.dto.SearchResource;
import com.mrk.myordershop.constant.OrderStatus;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;

public interface OrderService {

	Order getOrder(int orderId, User user) throws EntityDoseNotExistException;

	Page<Order> getOrders(Pageable pageable, Retailer retailer, OrderFilter filter);

	Page<Order> getOrders(Pageable pageable, Wholesaler wholesaler, OrderFilter filter);

	Order updateOrderStatus(int orderId, OrderStatus status, Wholesaler wholesaler) throws EntityDoseNotExistException;
	
	Order updateOrderStatus(int orderId, OrderStatus status, Supplier supplier) throws EntityDoseNotExistException;

	Order cancelOrder(int orderId, Cancellation cancellation, Retailer retailer) throws EntityDoseNotExistException;

	Order cancelOrder(int orderId, Cancellation cancellation, Wholesaler wholesaler) throws EntityDoseNotExistException;

	List<SearchResource> search(SearchFilter filter, Wholesaler wholesaler);

	List<SearchResource> search(SearchFilter filter, Retailer retailer);

	Page<Order> deliversBy(Date date, Pageable pageable, Wholesaler wholesaler);

	// below methods from instant order service
	void saveInstantOrder(InstantOrder instantOrder, Retailer retailer) throws EntityDoseNotExistException;

	InstantOrder saveInstantOrderImage(int orderId, Image image, Retailer retailer) throws EntityDoseNotExistException;

	void saveInstantOrder(WholesalerInstantOrder instantOrder, Wholesaler wholesaler)
			throws EntityNotPersistedException, EntityDoseNotExistException;

	void saveWholesalerInstantOrderImage(int orderId, Image image, Wholesaler wholesaler)
			throws EntityDoseNotExistException;

	Order orderApprove(int orderId, OrderAcceptance orderAcceptance, Wholesaler wholesaler)
			throws EntityDoseNotExistException;

}
