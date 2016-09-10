package com.mrk.myordershop.security.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

/**
 * AccessDeniedHandler.java Naveen Apr 6, 2015
 */
public class CustomAccessDeniedHandler extends AccessDeniedHandlerImpl {

	private static Logger log = Logger.getLogger(CustomAccessDeniedHandler.class
			.getName());

	/*
	 * Naveen Apr 6, 2015
	 */
	@Override
	public void handle(HttpServletRequest request,
			HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException,
			ServletException {
		log.debug("Access Denied");
		log.debug("Redirecting to " + request.getContextPath());
		response.sendRedirect(request.getContextPath());
	}
}
