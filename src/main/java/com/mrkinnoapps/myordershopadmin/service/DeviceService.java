package com.mrkinnoapps.myordershopadmin.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrkinnoapps.myordershopadmin.bean.entity.Device;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;

public interface DeviceService {

	void register(Device device, User currentUser);

	List<Device> findByUserId(String userId);

	void unregister(String clientId, User currentUser)
			throws EntityDoseNotExistException;
	
	void delete(int Id) throws EntityDoseNotExistException;
	
	int deviceCount(String clientId);

	Page<Device> getList(Pageable pageable);
}
