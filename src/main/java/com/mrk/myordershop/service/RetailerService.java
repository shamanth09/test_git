package com.mrk.myordershop.service;

import com.mrk.myordershop.bean.Retailer;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

public interface RetailerService {

	Retailer getRetailer(String retailerId) throws EntityDoseNotExistException;

	Retailer updateRetailer(Retailer retailer)
			throws EntityDoseNotExistException;

}
