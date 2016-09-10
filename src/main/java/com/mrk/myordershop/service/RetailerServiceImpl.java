package com.mrk.myordershop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrk.myordershop.bean.Retailer;
import com.mrk.myordershop.config.annotation.PersistTransactional;
import com.mrk.myordershop.config.annotation.ReadTransactional;
import com.mrk.myordershop.dao.RetailerDAO;
import com.mrk.myordershop.dao.UserDAO;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

@Service
public class RetailerServiceImpl implements RetailerService {

	@Autowired
	private RetailerDAO retailerDAO;
	@Autowired
	private UserDAO userDao;

	@Override
	@ReadTransactional
	public Retailer getRetailer(String retailerId)
			throws EntityDoseNotExistException {
		return retailerDAO.getRetailer(retailerId);
	}

	@Override
	@PersistTransactional
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
