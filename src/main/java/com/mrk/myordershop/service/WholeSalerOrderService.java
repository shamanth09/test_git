package com.mrk.myordershop.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrk.myordershop.bean.Cancellation;
import com.mrk.myordershop.bean.InstantWholesalerOrder;
import com.mrk.myordershop.bean.Supplier;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.WholesalerOrder;
import com.mrk.myordershop.bean.dto.SearchFilter;
import com.mrk.myordershop.bean.dto.SearchResource;
import com.mrk.myordershop.bean.dto.WholesalerOrderFilter;
import com.mrk.myordershop.constant.OrderStatus;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

public interface WholeSalerOrderService {

	InstantWholesalerOrder save(InstantWholesalerOrder iwOrder,
			Wholesaler wholesaler) throws EntityDoseNotExistException;

	WholesalerOrder get(int orderId, Wholesaler wholesaler)
			throws EntityDoseNotExistException;

	WholesalerOrder get(int orderId, Supplier supplier)
			throws EntityDoseNotExistException;

	Page<WholesalerOrder> get(Pageable pageable, Wholesaler wholesaler,
			WholesalerOrderFilter filter);

	Page<WholesalerOrder> get(Pageable pageable, Supplier supplier,
			WholesalerOrderFilter filter);

	WholesalerOrder updateOrderStatus(int orderId, OrderStatus status,
			Supplier supplier) throws EntityDoseNotExistException;
	
	WholesalerOrder cancelOrder(int orderId, Cancellation cancellation, Wholesaler wholesaler)
			throws EntityDoseNotExistException;
	
	WholesalerOrder updateOrderStatus(int orderId, OrderStatus status,
			Wholesaler wholesaler) throws EntityDoseNotExistException;

	List<SearchResource> search(SearchFilter filter, Wholesaler wholesaler);

	List<SearchResource> search(SearchFilter filter, Supplier supplier);

}
