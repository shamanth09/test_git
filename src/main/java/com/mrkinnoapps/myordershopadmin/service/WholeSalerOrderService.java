package com.mrkinnoapps.myordershopadmin.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrkinnoapps.myordershopadmin.bean.constant.OrderStatus;
import com.mrkinnoapps.myordershopadmin.bean.dto.SearchFilter;
import com.mrkinnoapps.myordershopadmin.bean.dto.SearchResource;
import com.mrkinnoapps.myordershopadmin.bean.dto.WholesalerOrderFilter;
import com.mrkinnoapps.myordershopadmin.bean.entity.Cancellation;
import com.mrkinnoapps.myordershopadmin.bean.entity.InstantWholesalerOrder;
import com.mrkinnoapps.myordershopadmin.bean.entity.Order;
import com.mrkinnoapps.myordershopadmin.bean.entity.Supplier;
import com.mrkinnoapps.myordershopadmin.bean.entity.Wholesaler;
import com.mrkinnoapps.myordershopadmin.bean.entity.WholesalerOrder;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;

public interface WholeSalerOrderService {
	
	void save(WholesalerOrder order) throws EntityDoseNotExistException;
	
	void delete(WholesalerOrder order) throws EntityDoseNotExistException;

	InstantWholesalerOrder save(InstantWholesalerOrder iwOrder,
			Wholesaler wholesaler) throws EntityDoseNotExistException, EntityNotPersistedException ;
	
	WholesalerOrder getOrder(Integer orderId) throws EntityDoseNotExistException;
	WholesalerOrder getOrder(Integer orderId,String activeId) throws EntityDoseNotExistException;
	
	WholesalerOrder get(int orderId, Wholesaler wholesaler)
			throws EntityDoseNotExistException;

	WholesalerOrder get(int orderId, Supplier supplier)
			throws EntityDoseNotExistException;

	List<WholesalerOrder> getByOrderNo(String orderNo, Wholesaler wholesaler)
			throws EntityDoseNotExistException;

	Page<WholesalerOrder> get(Pageable pageable, Wholesaler wholesaler,
			WholesalerOrderFilter filter);

	Page<WholesalerOrder> get(Pageable pageable, Supplier supplier,
			WholesalerOrderFilter filter);
	
	Page<WholesalerOrder> get( Pageable pageable, String activeId,WholesalerOrderFilter filter);
	
	WholesalerOrder updateOrderStatus(int orderId, OrderStatus status,
			Supplier supplier) throws EntityDoseNotExistException;
	
	WholesalerOrder cancelOrder(int orderId, Cancellation cancellation, Wholesaler wholesaler)
			throws EntityDoseNotExistException;
	
	WholesalerOrder updateOrderStatus(int orderId, OrderStatus status,
			Wholesaler wholesaler) throws EntityDoseNotExistException;

	List<SearchResource> search(SearchFilter filter, Wholesaler wholesaler);

	List<SearchResource> search(SearchFilter filter, Supplier supplier);
	
	public void update(WholesalerOrder wholesalerOrder)throws EntityDoseNotExistException;

}
