package com.mrk.myordershop.log.filter;

import java.util.Date;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

import com.mrk.myordershop.log.bean.LoginHistory;
import com.mrk.myordershop.log.service.LoginHistoryService;
import com.mrk.myordershop.security.bean.SessionUser;
import com.mrk.myordershop.security.oauth.service.OAuth2ServerAuthenticationFilter;

public class OAuth2ServerAuthenticationFilterImpl extends OAuth2ServerAuthenticationFilter {

	private static final Logger log = Logger.getLogger(OAuth2ServerAuthenticationFilterImpl.class);

	@Autowired
	private LoginHistoryService loginHistroryService;

	public OAuth2ServerAuthenticationFilterImpl(DefaultTokenServices tokenServices) {
		super(tokenServices);
	}

	@Override
	protected void onSuccessfulUserAuthentication(ServletRequest request, ServletResponse response,
			Authentication clientAuthentication, OAuth2Authentication userOAuth2Authentication,
			OAuth2AccessToken token) {

		// To skip refresh_token request
		if (userOAuth2Authentication.getOAuth2Request().getRefreshTokenRequest() == null) {
			SessionUser user = (SessionUser) userOAuth2Authentication.getPrincipal();

			LoginHistory history = new LoginHistory();
			history.setAgent(extractUserAgent(request));
			history.setClientId(clientAuthentication.getName());
			history.setRefreshToken(token.getRefreshToken().getValue());
			history.setSessionDuration(token.getExpiresIn());
			history.setIp(extractIp(request));
			history.setCreateTimestamp(new Date());
			new LoginSuccessPersist(user.getUsername(), history).persist();
			log.debug("on Successful User Authentication- " + user.getUsername() + " history- " + history);
		}
	}

	@Override
	protected void onFailureUserAuthentication(ServletRequest request, ServletResponse response,
			Authentication clientAuthentication, String username) {
		LoginHistory history = new LoginHistory();
		history.setAgent(extractUserAgent(request));
		history.setClientId(clientAuthentication.getName());
		history.setIp(extractIp(request));
		history.setCreateTimestamp(new Date());

		new LoginFailurePersist(username, history).persist();
		log.debug("on fail user authenticaion -" + username);
	}

	@Override
	protected void onFailClientAuthentication(ServletRequest request, ServletResponse response, String clientId) {
		log.debug("on fail Client Authentication -" + clientId);
	}

	@Override
	protected void onSuccessfulClientAuthentication(ServletRequest request, ServletResponse response,
			Authentication authentication) {
		log.debug("on Successful Client Authentication -" + authentication);
	}

	private class LoginSuccessPersist implements Runnable {
		private String username;
		private LoginHistory history;

		public LoginSuccessPersist(String userId, LoginHistory history) {
			this.username = userId;
			this.history = history;
		}

		@Override
		public void run() {
			loginHistroryService.loginSuccessAttempt(username, history);
		}

		public void persist() {
			this.run();
		}

	}

	private class LoginFailurePersist implements Runnable {
		private String username;
		private LoginHistory history;

		public LoginFailurePersist(String username, LoginHistory history) {
			this.username = username;
			this.history = history;
		}

		@Override
		public void run() {
			loginHistroryService.loginFailureAttempt(username, history);
		}

		public void persist() {
			this.run();
		}

	}

	private String extractIp(ServletRequest request) {
		String ipAddress = null;
		if (request instanceof HttpServletRequest) {
			HttpServletRequest req = (HttpServletRequest) request;
			ipAddress = req.getHeader("X-FORWARDED-FOR");
			if (ipAddress == null) {
				ipAddress = request.getRemoteAddr();
			}
		}
		return ipAddress;
	}

	private String extractUserAgent(ServletRequest request) {
		String userAgent = null;
		if (request instanceof HttpServletRequest) {
			userAgent = ((HttpServletRequest) request).getHeader("User-Agent").toLowerCase();
		}
		return userAgent;
	}

	// private void showResponse(ServletResponse response) {
	// if (response instanceof HttpServletResponse) {
	// HttpServletResponse res = (HttpServletResponse) response;
	// log.debug("Response Status " + res.getStatus());
	// log.debug("Response Locale " + res.getLocale());
	// log.debug("Response Committed " + res.isCommitted());
	// for (String header : res.getHeaderNames()) {
	// log.debug(">>> header " + header + " : "
	// + res.getHeader(header));
	// }
	// log.debug("Response ");
	// }
	// }

	// private void showRequest(ServletRequest request) {
	// if (request instanceof HttpServletRequest) {
	// HttpServletRequest req = (HttpServletRequest) request;
	// log.debug("Request AuthType " + req.getAuthType());
	// log.debug("Request LocalAddr " + req.getLocalAddr());
	// log.debug("Request LocalName " + req.getLocalName());
	// log.debug("Request LocalPort " + req.getLocalPort());
	// log.debug("Request PathInfo " + req.getPathInfo());
	// log.debug("Request PathTranslated " + req.getPathTranslated());
	// log.debug("Request Protocol " + req.getProtocol());
	// log.debug("Request QueryString " + req.getQueryString());
	// log.debug("Request RemoteAddr " + req.getRemoteAddr());
	// log.debug("Request X-FORWARDED-FOR "
	// + req.getHeader("X-FORWARDED-FOR"));
	// log.debug("Request RemoteHost " + req.getRemoteHost());
	// log.debug("Request RemotePort " + req.getRemotePort());
	// log.debug("Request RemoteUser " + req.getRemoteUser());
	// log.debug("Request Scheme " + req.getScheme());
	// log.debug("Request ServerName " + req.getServerName());
	// log.debug("Request ServerPort " + req.getServerPort());
	// log.debug("Request ServletPath " + req.getServletPath());
	// log.debug("Request Locale " + req.getLocale());
	// for (Locale key : Collections.list(req.getLocales())) {
	// log.debug("Request Locales " + key);
	// }
	// log.debug("Request UserPrincipal " + req.getUserPrincipal());
	// for (String key : Collections.list(req.getAttributeNames())) {
	// log.debug("Request AttributeNames " + key + ":"
	// + req.getAttribute(key));
	// }
	// try {
	// for (Part part : req.getParts()) {
	// log.debug("Request >>> part " + part);
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (ServletException e) {
	// e.printStackTrace();
	// }
	// log.debug("Request header " + req.getHeaderNames());
	// }
	// }
}
