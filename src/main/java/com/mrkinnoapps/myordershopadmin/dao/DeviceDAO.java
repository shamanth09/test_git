package com.mrkinnoapps.myordershopadmin.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrkinnoapps.myordershopadmin.bean.entity.Device;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;

public interface DeviceDAO {

	void save(Device device);

	void update(Device device);

	Device get(int id) throws EntityDoseNotExistException;

	Device getByDeviceId(String deviceId) throws EntityDoseNotExistException;

	List<Device> findByUserId(String userId);

	List<Device> findByClientAndUserId(String clientId, String userId);

	void delete(Device device);

	int getTotalCount(String clientId);

	Page<Device> getList(Pageable pageable);
}
