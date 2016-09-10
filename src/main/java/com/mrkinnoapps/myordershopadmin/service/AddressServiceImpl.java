package com.mrkinnoapps.myordershopadmin.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrkinnoapps.myordershopadmin.bean.constant.ActiveFlag;
import com.mrkinnoapps.myordershopadmin.bean.entity.Address;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.dao.AddressDAO;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	private AddressDAO addressDAO;

	/*
	 * Naveen Apr 3, 2015
	 */
	@Override
	public void save(Address address, User user) {
		address.setUser(user);
		addressDAO.save(address);
	}

	/*
	 * Naveen Mar 28, 2015
	 */
	@Override
	public Address getAddressByUserId(String userId)
			throws EntityDoseNotExistException {
		return addressDAO.getAddresByUserId(userId);
	}

	/*
	 * Naveen Mar 30, 2015
	 */
	@Override
	public Address getAddress(int id, User user)
			throws EntityDoseNotExistException {
		return addressDAO.getAddress(id);
	}

	/*
	 * Naveen Apr 3, 2015
	 */
	@Override
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
	public Address deleteAddress(int adderssId, User user) {
		Address address = null;
		try {
			address = addressDAO.getAddress(adderssId);
			address.setActiveFlag(ActiveFlag.INACTIVE);
			addressDAO.save(address);
		} catch (EntityDoseNotExistException e) {
			e.printStackTrace();
		}

		return address;
	}
}
