package com.mrk.myordershop.security.service;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.session.SessionManagementFilter;

public class CustomSessionManagementFilter extends SessionManagementFilter {
	private static final Logger log = Logger.getLogger(CustomSessionManagementFilter.class);
	public CustomSessionManagementFilter(
			SecurityContextRepository securityContextRepository) {
		super(securityContextRepository);
	}
	
	@Override
	public void afterPropertiesSet() throws ServletException {
		log.debug("=============after property set=================");
		super.afterPropertiesSet();
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		super.doFilter(req, res, chain);
		HttpSession sess = ((HttpServletRequest) req).getSession(false);
	}
}
