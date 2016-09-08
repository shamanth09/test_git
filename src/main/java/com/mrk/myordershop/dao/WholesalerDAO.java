package com.mrk.myordershop.dao;

import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

public interface WholesalerDAO {

	/**
	 * Naveen
	 * Mar 28, 2015
	 * void
	 */
	void saveWholesaler(Wholesaler wholesaler);
	/**
	 * Naveen
	 * Mar 28, 2015
	 * Wholesaler
	 */
	Wholesaler getWholesaler(String userId) throws EntityDoseNotExistException;
	/**
	 * Naveen
	 * Mar 28, 2015
	 * void
	 */
	void updateWholesaler(Wholesaler wholesaler);
}
