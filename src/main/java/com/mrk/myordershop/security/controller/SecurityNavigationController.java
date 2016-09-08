package com.mrk.myordershop.security.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.UserRole;
import com.mrk.myordershop.constant.Role;

//@Controller
public class SecurityNavigationController {

	@Autowired
	private UserDetailsService userDetailsService;

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public void home(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.getRequestDispatcher(getHomeUrl(request, response))
					.forward(request, response);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/go/login", method = RequestMethod.GET)
	public String getLogin() {
		return "app.login";
	}

	@RequestMapping(value = "/go/login/{error}", method = RequestMethod.GET)
	public String getLogin(@PathVariable("error") String error, ModelMap map) {
		map.addAttribute("errors", "Invalid Username / Mobile No or Password");
		return "app.login";
	}

	@RequestMapping(value = {"/go/user/isavailable","/user/isavailable"}, method = RequestMethod.GET)
	public @ResponseBody
	String getUserAvailable(@RequestParam(value = "email") String email,
			HttpServletResponse response) {
		try {
			userDetailsService.loadUserByUsername(email);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return "{\"message\":\"Email not available\"}";
		} catch (UsernameNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_OK);
			return "{\"message\":\"Email available\"}";
		}
	}

	public String getHomeUrl(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String redirectUrl = "/v1/home";
		User user = (User) request.getSession().getAttribute("currentUser");
		if (user != null) {

			for (UserRole role : user.getUserRoles()) {
				if (role.getRole().equals(Role.ROLE_WSALER))
					redirectUrl = "/v1/ws/home";
				if (role.getRole().equals(Role.ROLE_SUPPLIER))
					redirectUrl = "/v1/sp/home";
			}
		}
		return redirectUrl;
	}
}
