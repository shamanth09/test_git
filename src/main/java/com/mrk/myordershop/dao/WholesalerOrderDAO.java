package com.mrk.myordershop.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrk.myordershop.bean.Supplier;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.WholesalerOrder;
import com.mrk.myordershop.bean.dto.WholesalerOrderFilter;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

public interface WholesalerOrderDAO extends SearchDAO {

	void save(WholesalerOrder order) throws EntityDoseNotExistException;

	void update(WholesalerOrder wholesalerOrder);

	WholesalerOrder get(int orderId) throws EntityDoseNotExistException;

	WholesalerOrder get(int orderId, Supplier supplier) throws EntityDoseNotExistException;

	WholesalerOrder get(int orderId, Wholesaler supplier) throws EntityDoseNotExistException;

	List<WholesalerOrder> getByOrderNo(String orderNo, Wholesaler wholesaler);

	List<WholesalerOrder> getByOrderNo(String orderNo);

	List<WholesalerOrder> getByOrderNo(String orderNo, Supplier supplier);

	WholesalerOrder getCurrentWholesalerOrderByOrderId(int orderId, Wholesaler wholesaler)
			throws EntityDoseNotExistException;

	Page<WholesalerOrder> get(Pageable pageable, Wholesaler wholesaler, WholesalerOrderFilter filter);

	Page<WholesalerOrder> get(Pageable pageable, Supplier supplier, WholesalerOrderFilter filter);

	void delete(WholesalerOrder wholesalerOrder);

}
