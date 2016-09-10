package com.mrkinnoapps.myordershopadmin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrkinnoapps.myordershopadmin.bean.entity.Retailer;
import com.mrkinnoapps.myordershopadmin.dao.RetailerDAO;
import com.mrkinnoapps.myordershopadmin.dao.UserDAO;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;

@Service
public class RetailerServiceImpl implements RetailerService {

	@Autowired
	private RetailerDAO retailerDAO;
	@Autowired
	private UserDAO userDao;

	@Override
	public Retailer getRetailer(String retailerId)
			throws EntityDoseNotExistException {
		return retailerDAO.getRetailer(retailerId);
	}

	@Override
	public Retailer updateRetailer(Retailer retailer)
			throws EntityDoseNotExistException {
		Retailer retailerfdb = null;
		retailerfdb = retailerDAO.getRetailer(retailer.getId());
		retailerfdb.setEmail(retailer.getEmail());
		retailerfdb.setMobile(retailer.getMobile());
		retailerfdb.setName(retailer.getName());
		retailerDAO.updateRetailer(retailerfdb);
		return retailerfdb;
	}

}
