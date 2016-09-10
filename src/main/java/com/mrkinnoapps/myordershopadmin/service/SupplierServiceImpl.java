package com.mrkinnoapps.myordershopadmin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrkinnoapps.myordershopadmin.bean.entity.Supplier;
import com.mrkinnoapps.myordershopadmin.dao.SupplierDAO;
import com.mrkinnoapps.myordershopadmin.dao.UserDAO;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;

@Service
public class SupplierServiceImpl implements SupplierService {

	@Autowired
	private SupplierDAO supplierDAO;
	@Autowired
	private UserDAO userDao;

	@Override
	public Supplier getSupplier(String supplierId)
			throws EntityDoseNotExistException {
		return supplierDAO.getSupplier(supplierId);
	}

	@Override
	public Supplier updateSupplier(Supplier supplier)
			throws EntityDoseNotExistException {
		Supplier supplierfdb = null;
		supplierfdb = supplierDAO.getSupplier(supplier.getId());
		supplierfdb.setEmail(supplier.getEmail());
		supplierfdb.setMobile(supplier.getMobile());
		supplierfdb.setName(supplier.getName());
		supplierDAO.updateSupplier(supplierfdb);
		return supplierfdb;
	}

	@Override
	public void save(Supplier supplier)  throws EntityNotPersistedException{

		supplierDAO.save(supplier);
	}

}
