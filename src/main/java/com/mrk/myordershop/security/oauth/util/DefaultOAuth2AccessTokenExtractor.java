package com.mrk.myordershop.security.oauth.util;

import java.io.IOException;

import org.springframework.security.oauth2.common.OAuth2AccessToken;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DefaultOAuth2AccessTokenExtractor implements
		OAuth2AccessTokenExtractor {

	private ObjectMapper mapper = new ObjectMapper();

	@Override
	public String getAccessTokenValue(byte[] response) {
		try {
			return mapper.readValue(response, OAuth2AccessToken.class)
					.getValue();
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
