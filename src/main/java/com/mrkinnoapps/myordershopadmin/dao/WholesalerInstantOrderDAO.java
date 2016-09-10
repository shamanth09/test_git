package com.mrkinnoapps.myordershopadmin.dao;

import com.mrkinnoapps.myordershopadmin.bean.entity.WholesalerInstantOrder;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;

public interface WholesalerInstantOrderDAO extends SearchDAO {

	public void save(WholesalerInstantOrder instantOrder)
			throws EntityNotPersistedException;

	public void update(WholesalerInstantOrder instantOrder);

	public WholesalerInstantOrder get(int orderId)
			throws EntityDoseNotExistException;

}
