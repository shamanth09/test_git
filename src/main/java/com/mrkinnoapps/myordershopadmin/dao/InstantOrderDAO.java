package com.mrkinnoapps.myordershopadmin.dao;

import com.mrkinnoapps.myordershopadmin.bean.entity.InstantOrder;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;

public interface InstantOrderDAO extends SearchDAO {

	void save(InstantOrder instantOrder) throws EntityNotPersistedException;

	InstantOrder getInstantOrder(int orderId)
			throws EntityDoseNotExistException;

	void update(InstantOrder instantOrder);

}
