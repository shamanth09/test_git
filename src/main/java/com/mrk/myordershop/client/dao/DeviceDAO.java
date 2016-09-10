package com.mrk.myordershop.client.dao;

import java.util.List;

import com.mrk.myordershop.bean.Device;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

public interface DeviceDAO {

	void save(Device device);

	void update(Device device);

	Device get(int id) throws EntityDoseNotExistException;

	Device getByDeviceId(String deviceId) throws EntityDoseNotExistException;

	List<Device> findByUserId(String userId);

	List<Device> findByClientAndUserId(String clientId, String userId);

	void delete(Device device);
}
