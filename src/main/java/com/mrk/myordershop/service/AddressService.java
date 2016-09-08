package com.mrk.myordershop.service;

import com.mrk.myordershop.bean.Address;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

public interface AddressService {

	/**
	 * Naveen Apr 3, 2015 void
	 * 
	 */
	void save(Address address, User user);

	Address getAddressByUserId(String userId)
			throws EntityDoseNotExistException;

	/**
	 * Naveen Mar 30, 2015 Address
	 * 
	 */
	Address getAddress(int id, User user) throws EntityDoseNotExistException;

	/**
	 * Naveen Apr 3, 2015 void
	 * 
	 */
	Address updateAddress(int addressId,Address address, User user);

	/**
	 * Naveen Apr 3, 2015 void
	 * 
	 */
	Address deleteAddress(int adderssId, User user);

}
