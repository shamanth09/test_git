package com.mrk.myordershop.security.oauth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.provider.error.OAuth2ExceptionRenderer;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;

public class HeaderOnlyOAuth2ExceptionRenderer implements
		OAuth2ExceptionRenderer {

	@Override
	public void handleHttpEntityResponse(HttpEntity<?> responseEntity,
			ServletWebRequest webRequest) throws Exception {

		if (responseEntity == null) {
			return;
		}

		HttpInputMessage inputMessage = createHttpInputMessage(webRequest);
		HttpOutputMessage outputMessage = createHttpOutputMessage(webRequest);

		if (responseEntity instanceof ResponseEntity
				&& outputMessage instanceof ServerHttpResponse) {
			((ServerHttpResponse) outputMessage)
					.setStatusCode(((ResponseEntity<?>) responseEntity)
							.getStatusCode());
		}
		HttpHeaders entityHeaders = responseEntity.getHeaders();
		if (!entityHeaders.isEmpty()) {
			outputMessage.getHeaders().putAll(entityHeaders);
		}
	}

	private HttpInputMessage createHttpInputMessage(NativeWebRequest webRequest)
			throws Exception {
		HttpServletRequest servletRequest = webRequest
				.getNativeRequest(HttpServletRequest.class);
		return new ServletServerHttpRequest(servletRequest);
	}

	private HttpOutputMessage createHttpOutputMessage(
			NativeWebRequest webRequest) throws Exception {
		HttpServletResponse servletResponse = (HttpServletResponse) webRequest
				.getNativeResponse();
		return new ServletServerHttpResponse(servletResponse);
	}
}
