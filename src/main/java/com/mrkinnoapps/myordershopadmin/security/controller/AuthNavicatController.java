package com.mrkinnoapps.myordershopadmin.security.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuthNavicatController {

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "app/login/login";
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(Authentication authentication) {
		Map<String, String> dashBoardUrls = new HashMap<String, String>();
		dashBoardUrls.put("ROLE_ADMIN", "/v1/admin/dashboard");

		String url = null;
		Collection<? extends GrantedAuthority> grants = authentication
				.getAuthorities();
		for (GrantedAuthority grantedAuthority : grants) {
			url = dashBoardUrls.get(grantedAuthority.getAuthority());
		}
		if (url == null)
			return "/common/errors/access_denied";

		return "redirect:" + url;
	}

	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public String accessDenied() {
		return "redirect:/home";
	}
}
