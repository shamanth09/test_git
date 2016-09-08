package com.mrk.myordershop.client.controller;

import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mrk.myordershop.bean.Device;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.client.service.DeviceService;
import com.mrk.myordershop.security.oauth.resolver.Owner;

@Controller("apiDeviceController")
@RequestMapping("/api/v1/device")
public class DeviceController {

	@Autowired
	private DeviceService notifierService;
	@Autowired
	private DefaultTokenServices tokenServices;

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity registerDevice(
			@RequestParam("device_token") String deviceToken, @Owner User user) {
		Device device = new Device();
		device.setDeviceToken(deviceToken);
		device.setClientId(getClientId());
		device.setUserId(user.getId());
		device.setCreateTimestamp(new Date());
		notifierService.register(device, user);
		return new ResponseEntity(device, HttpStatus.OK);
	}

	@RequestMapping(value = "/web", method = RequestMethod.POST)
	public ResponseEntity registerWebDevice(@Owner User user) {
		if (user == null)
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		Device device = new Device();
		device.setDeviceToken(Base64.encodeBase64String(getRefereshToken()
				.getBytes()));
		device.setClientId(getClientId());
		device.setUserId(user.getId());
		device.setCreateTimestamp(new Date());
		notifierService.register(device, user);
		return new ResponseEntity(device, HttpStatus.OK);
	}

	private String getClientId() {
		OAuth2AccessToken accessToken = tokenServices
				.getAccessToken((OAuth2Authentication) SecurityContextHolder
						.getContext().getAuthentication());
		String clientId = tokenServices.getClientId(accessToken.getValue());
		return clientId;
	}

	private String getRefereshToken() {
		return tokenServices
				.getAccessToken(
						(OAuth2Authentication) SecurityContextHolder
								.getContext().getAuthentication())
				.getRefreshToken().getValue();
	}
}
