package com.mrk.myordershop.security.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.client.service.DeviceService;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.security.oauth.resolver.Owner;

@Controller
@RequestMapping("/api/v1")
public class OAuthNavController {

	@Autowired
	private DefaultTokenServices tokenServices;
	@Autowired
	private DeviceService notifierService;

	@RequestMapping(value = "/revoke")
	public ResponseEntity revokeToken(@Owner User currentUser) {

		try {
			notifierService.unregister(getClientId(), currentUser);
		} catch (EntityDoseNotExistException e) {
			e.printStackTrace();
		}

		tokenServices.revokeToken(getAccessToken().getValue());
		return new ResponseEntity(HttpStatus.OK);
	}

	private String getClientId() {
		OAuth2AccessToken accessToken = tokenServices
				.getAccessToken((OAuth2Authentication) SecurityContextHolder
						.getContext().getAuthentication());
		String clientId = tokenServices.getClientId(accessToken.getValue());
		return clientId;
	}

	private OAuth2AccessToken getAccessToken() {
		return tokenServices
				.getAccessToken((OAuth2Authentication) SecurityContextHolder
						.getContext().getAuthentication());
	}
}
