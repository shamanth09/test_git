package com.mrkinnoapps.myordershopadmin.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrkinnoapps.myordershopadmin.bean.dto.WholesalerOrderFilter;
import com.mrkinnoapps.myordershopadmin.bean.entity.Supplier;
import com.mrkinnoapps.myordershopadmin.bean.entity.Wholesaler;
import com.mrkinnoapps.myordershopadmin.bean.entity.WholesalerOrder;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;

public interface WholesalerOrderDAO extends SearchDAO {

	void save(WholesalerOrder order) throws EntityDoseNotExistException;

	void update(WholesalerOrder wholesalerOrder);

	WholesalerOrder get(int orderId, Wholesaler wholesaler)
			throws EntityDoseNotExistException;

	WholesalerOrder get(int orderId, Supplier supplier)
			throws EntityDoseNotExistException;

	List<WholesalerOrder> getByOrderNo(String orderNo, Wholesaler wholesaler);
	
	List<WholesalerOrder> getByOrderNo(String orderNo);

	List<WholesalerOrder> getByOrderNo(String orderNo, Supplier supplier);

	WholesalerOrder getCurrentWholesalerOrderByOrderId(int orderId,
			Wholesaler wholesaler) throws EntityDoseNotExistException;

	Page<WholesalerOrder> get(Pageable pageable, Wholesaler wholesaler,
			WholesalerOrderFilter filter);
	
	public Page<WholesalerOrder> get(Pageable pageable, WholesalerOrderFilter filter);

	Page<WholesalerOrder> get(Pageable pageable, Supplier supplier,
			WholesalerOrderFilter filter);
	
	void delete(WholesalerOrder wholesalerOrder);
	
	public WholesalerOrder getOrder(Integer orderId)throws EntityDoseNotExistException;

}
