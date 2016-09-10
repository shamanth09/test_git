package com.mrkinnoapps.myordershopadmin.dao;

import com.mrkinnoapps.myordershopadmin.bean.entity.Address;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;

/**
 * AddressDAO.java Naveen Mar 30, 2015
 */
public interface AddressDAO {

	void save(Address address);

	void update(Address address);

	Address getAddresByUserId(String userId) throws EntityDoseNotExistException;

	/**
	 * Naveen Mar 30, 2015 Address
	 * 
	 */
	Address getAddress(int id) throws EntityDoseNotExistException;
	
	void delete(Address address);
}
