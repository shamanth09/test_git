package com.mrkinnoapps.myordershopadmin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mrkinnoapps.myordershopadmin.bean.entity.Device;
import com.mrkinnoapps.myordershopadmin.bean.entity.OauthAccessToken;
import com.mrkinnoapps.myordershopadmin.bean.entity.OauthRefreshToken;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.dao.DeviceDAO;
import com.mrkinnoapps.myordershopadmin.dao.OauthAccessTokenDAO;
import com.mrkinnoapps.myordershopadmin.dao.OauthRefreshTokenDAO;
import com.mrkinnoapps.myordershopadmin.dao.UserDAO;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;

@Service
public class DeviceServiceImpl implements DeviceService {

	@Autowired
	private DeviceDAO deviceDAO;
	@Autowired
	private OauthAccessTokenDAO accessTokenDAO;
	@Autowired
	private OauthRefreshTokenDAO oauthRefreshTokenDAO;
	@Autowired
	private UserDAO userDAO;

	@Override
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
	public List<Device> findByUserId(String userId) {
		return deviceDAO.findByUserId(userId);
	}

	@Override
	public void unregister(String clientId, User currentUser) throws EntityDoseNotExistException {
		List<Device> devices = deviceDAO.findByClientAndUserId(clientId, currentUser.getId());
		for (Device device : devices) {
			deviceDAO.delete(device);
		}
	}

	@Override
	public void delete(int Id) throws EntityDoseNotExistException {
		Device device = deviceDAO.get(Id);
		deviceDAO.delete(device);
		User user = userDAO.get(device.getUserId());
		OauthAccessToken oauthAccessToken = accessTokenDAO.getOauthAccessToken(user.getEmail());
		if (oauthAccessToken != null) {
			accessTokenDAO.delete(oauthAccessToken);
			OauthRefreshToken refreshToken = oauthRefreshTokenDAO
					.getOauthRefreshToken(oauthAccessToken.getRefreshToken());
			if (refreshToken != null)
				oauthRefreshTokenDAO.delete(refreshToken);
		}
	}

	@Override
	public int deviceCount(String clientId) {
		return deviceDAO.getTotalCount( clientId);
	}

	@Override
	public Page<Device> getList(Pageable pageable) {
		return deviceDAO.getList(pageable);
	}
}
