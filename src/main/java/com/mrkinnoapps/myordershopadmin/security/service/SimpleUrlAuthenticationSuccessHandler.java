package com.mrkinnoapps.myordershopadmin.security.service;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


public class SimpleUrlAuthenticationSuccessHandler implements
		AuthenticationSuccessHandler {

	private Logger log = LoggerFactory
			.getLogger(SimpleUrlAuthenticationSuccessHandler.class);

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		String targetUrl = determineTargetUrl(authentication);
		if (response.isCommitted()) {
			log.debug("Response has already been committed. Unable to redirect to "
					+ targetUrl);
			return;
		}
		redirectStrategy.sendRedirect(request, response, targetUrl);
		clearAuthenticationAttributes(request);
	}

	protected String determineTargetUrl(Authentication authentication) {
		String url = null;
		Collection<? extends GrantedAuthority> grants = authentication
				.getAuthorities();
		for (GrantedAuthority grantedAuthority : grants) {
			if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
				url = "/v1/admin/dashboard";
				break;
			}
		}
		if (url == null)
			new IllegalStateException();
		return url;
	}

	protected void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null)
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}
}
