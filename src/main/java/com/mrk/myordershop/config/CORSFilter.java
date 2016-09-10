package com.mrk.myordershop.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

public class CORSFilter extends OncePerRequestFilter {

	private static final String HEADER_ALLOW_ORIGIN = "Access-Control-Allow-Origin";

	private static final String HEADER_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";

	private static final String HEADER_ALLOW_METHODS = "Access-Control-Allow-Methods";

	private static final String HEADER_ALLOW_HEADERS = "Access-Control-Allow-Headers";

	private static final String HEADER_REQUEST_METHOD = "Access-Control-Request-Method";

	private static final String HEADER_MAX_AGE = "Access-Control-Max-Age";

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		response.addHeader(HEADER_ALLOW_ORIGIN, "*");
		if (request.getHeader(HEADER_REQUEST_METHOD) != null
				&& "OPTIONS".equals(request.getMethod())) {
			response.addHeader(HEADER_ALLOW_CREDENTIALS, "true");

			response.addHeader(HEADER_ALLOW_METHODS,
					"GET, POST, PUT, DELETE, OPTIONS");

			response.addHeader(
					HEADER_ALLOW_HEADERS,
					"Origin, Accept, X-Requested-With, Content-Type, Authorization,"
							+ " Access-Control-Request-Method, Access-Control-Request-Headers");

			response.addHeader(HEADER_MAX_AGE, "1");
		} else {
			filterChain.doFilter(request, response);
		}

	}

}
