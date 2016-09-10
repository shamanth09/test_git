package com.mrkinnoapps.myordershopadmin.dao;

import com.mrkinnoapps.myordershopadmin.bean.entity.Retailer;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;

public interface RetailerDAO {

	/**
	 * Naveen
	 * Mar 28, 2015
	 * Retailer
	 */
	Retailer getRetailer(String userId) throws EntityDoseNotExistException;
	/**
	 * Naveen
	 * Mar 28, 2015
	 * void
	 */
	void updateRetailer(Retailer retailer);
}
