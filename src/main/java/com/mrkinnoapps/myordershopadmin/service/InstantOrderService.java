package com.mrkinnoapps.myordershopadmin.service;

import com.mrkinnoapps.myordershopadmin.bean.entity.Image;
import com.mrkinnoapps.myordershopadmin.bean.entity.InstantOrder;
import com.mrkinnoapps.myordershopadmin.bean.entity.Retailer;
import com.mrkinnoapps.myordershopadmin.bean.entity.Wholesaler;
import com.mrkinnoapps.myordershopadmin.bean.entity.WholesalerInstantOrder;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;

public interface InstantOrderService {

	void saveInstantOrder(InstantOrder instantOrder, Retailer retailer)
			throws EntityNotPersistedException;

	InstantOrder saveInstantOrderImage(int orderId, Image image,
			Retailer retailer) throws EntityDoseNotExistException,
			EntityNotPersistedException;

	void saveInstantOrder(WholesalerInstantOrder instantOrder,
			Wholesaler wholesaler) throws EntityNotPersistedException;

	WholesalerInstantOrder getWholesalerInstantOrder(int orderId,
			Wholesaler wholesaler) throws EntityDoseNotExistException;

	void saveWholesalerInstantOrderImage(int orderId, Image image,
			Wholesaler wholesaler) throws EntityDoseNotExistException,
			EntityNotPersistedException;
}
