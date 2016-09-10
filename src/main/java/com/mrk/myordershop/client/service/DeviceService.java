package com.mrk.myordershop.client.service;

import java.util.List;

import com.mrk.myordershop.bean.Device;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

public interface DeviceService {

	void register(Device device, User currentUser);

	List<Device> findByUserId(String userId);

	void unregister(String clientId, User currentUser)
			throws EntityDoseNotExistException;
}
