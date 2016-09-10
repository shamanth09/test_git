package com.mrkinnoapps.myordershopadmin.dao;

import com.mrkinnoapps.myordershopadmin.bean.entity.Supplier;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;

public interface SupplierDAO {

	/**
	 * Naveen
	 * Mar 28, 2015
	 * void
	 */
	void save(Supplier supplier) throws EntityNotPersistedException;
	/**
	 * Naveen
	 * Mar 28, 2015
	 * Supplier
	 */
	Supplier getSupplier(String userId) throws EntityDoseNotExistException;
	/**
	 * Naveen
	 * Mar 28, 2015
	 * void
	 */
	void updateSupplier(Supplier supplier);
}
