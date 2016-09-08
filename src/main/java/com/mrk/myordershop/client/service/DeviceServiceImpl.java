package com.mrk.myordershop.client.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrk.myordershop.bean.Device;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.client.dao.DeviceDAO;
import com.mrk.myordershop.config.annotation.PersistTransactional;
import com.mrk.myordershop.config.annotation.ReadTransactional;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

@Service
public class DeviceServiceImpl implements DeviceService {

	@Autowired
	private DeviceDAO deviceDAO;

	@Override
	@PersistTransactional
	public void register(Device device, User currentUser) {
		try {
			Device deviceFdb = deviceDAO.getByDeviceId(device.getDeviceToken());
			deviceFdb.setUserId(device.getUserId());
			deviceDAO.update(deviceFdb);
		} catch (EntityDoseNotExistException e) {
			deviceDAO.save(device);
		}
	}

	@Override
	@ReadTransactional
	public List<Device> findByUserId(String userId) {
		return deviceDAO.findByUserId(userId);
	}

	@Override
	@PersistTransactional
	public void unregister(String clientId, User currentUser)
			throws EntityDoseNotExistException {
		List<Device> devices = deviceDAO.findByClientAndUserId(clientId,
				currentUser.getId());
		for (Device device : devices) {
			deviceDAO.delete(device);
		}
	}

}
