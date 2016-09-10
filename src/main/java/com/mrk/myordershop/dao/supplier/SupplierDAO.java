package com.mrk.myordershop.dao.supplier;

import com.mrk.myordershop.bean.Supplier;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;

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
