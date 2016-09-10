package com.mrk.myordershop.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrk.myordershop.bean.Address;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.config.annotation.PersistTransactional;
import com.mrk.myordershop.config.annotation.ReadTransactional;
import com.mrk.myordershop.constant.ActiveFlag;
import com.mrk.myordershop.dao.AddressDAO;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	private AddressDAO addressDAO;

	/*
	 * Naveen Apr 3, 2015
	 */
	@Override
	@PersistTransactional
	public void save(Address address, User user) {
		address.setUser(user);
		addressDAO.save(address);
	}

	/*
	 * Naveen Mar 28, 2015
	 */
	@Override
	@ReadTransactional
	public Address getAddressByUserId(String userId)
			throws EntityDoseNotExistException {
		return addressDAO.getAddresByUserId(userId);
	}

	/*
	 * Naveen Mar 30, 2015
	 */
	@Override
	@ReadTransactional
	public Address getAddress(int id, User user)
			throws EntityDoseNotExistException {
		return addressDAO.getAddress(id);
	}

	/*
	 * Naveen Apr 3, 2015
	 */
	@Override
	@PersistTransactional
	public Address updateAddress(int addressId, Address address, User user) {
		Address addressfdb = null;
		try {
			addressfdb = addressDAO.getAddress(addressId);
			addressfdb.setTitle(address.getTitle());
			addressfdb.setStreet(address.getStreet());
			addressfdb.setArea(address.getArea());
			addressfdb.setLandmark(address.getLandmark());
			addressfdb.setCity(address.getCity());
			addressfdb.setState(address.getState());
			addressfdb.setCountry(address.getCountry());
			addressfdb.setPincode(address.getPincode());
			addressfdb.setUpdateTimestamp(new Date());
			addressDAO.update(addressfdb);
		} catch (EntityDoseNotExistException e) {
			e.printStackTrace();
		}

		return addressfdb;
	}

	/*
	 * Naveen Apr 3, 2015
	 */
	@Override
	@PersistTransactional
	public Address deleteAddress(int adderssId, User user) {
		Address address = null;
		try {
			address = addressDAO.getAddress(adderssId);
			address.setActiveFlag(ActiveFlag.INACTIVE);
			addressDAO.update(address);
		} catch (EntityDoseNotExistException e) {
			e.printStackTrace();
		}

		return address;
	}
}
