package com.mrk.myordershop.dao;

import com.mrk.myordershop.bean.Retailer;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

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
