package com.mrkinnoapps.myordershopadmin.service;

import com.mrkinnoapps.myordershopadmin.bean.entity.Retailer;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;

public interface RetailerService {

	Retailer getRetailer(String retailerId) throws EntityDoseNotExistException;

	Retailer updateRetailer(Retailer retailer)
			throws EntityDoseNotExistException;

}
