package com.mrkinnoapps.myordershopadmin.service;

import com.mrkinnoapps.myordershopadmin.bean.entity.Supplier;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;

public interface SupplierService {

	void save(Supplier supplier) throws EntityNotPersistedException;

	Supplier getSupplier(String supplierId) throws EntityDoseNotExistException;

	Supplier updateSupplier(Supplier supplier) throws EntityDoseNotExistException;

}
