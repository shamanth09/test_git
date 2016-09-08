package com.mrk.myordershop.security.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

public class LoginAuthenticationEntryPoint extends
		LoginUrlAuthenticationEntryPoint {
	private static final Logger log = Logger
			.getLogger(LoginAuthenticationEntryPoint.class);

	@Autowired
	private RequestMappingHandlerMapping handlerMapping;

	public LoginAuthenticationEntryPoint(String loginUrl) {
		super(loginUrl);

	}

	@SuppressWarnings("deprecation")
	@Override
	public void commence(HttpServletRequest req, HttpServletResponse res,
			AuthenticationException auth) throws IOException, ServletException {

		String[] array = req.getRequestURI().split("/", 4);
		String url = array[2];
		boolean go = false;
		log.debug("entryPoint================");
		boolean login = url.contains("login.do");

		Map<RequestMappingInfo, HandlerMethod> map = handlerMapping
				.getHandlerMethods();
		Iterator<?> it = map.entrySet().iterator();
		Set<String> set = new HashSet<String>();
		while (it.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry pairs = (Map.Entry) it.next();
			String[] arr = pairs.getKey().toString().split("/", 3);

			set.add(arr[1]);

			if (set.contains(url)) {
				go = true;

			}
		}

		if (login || go) {
			log.debug("super.commence(req, res, auth);");
			super.commence(req, res, auth);

		} else {

			if ("XMLHttpRequest".equals(req.getHeader("X-Requested-With"))) {
				PrintWriter writer = res.getWriter();
				res.setStatus(200);
				writer.print("{\"success\":false,\"message\":\"Session Exprired\"}");
			} else {
				PrintWriter writer = res.getWriter();
				res.setStatus(200);
				res.sendRedirect("sessionExpired");
			}

		}
	}
}
