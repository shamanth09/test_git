package com.mrk.myordershop.security.oauth.util;

public interface OAuth2AccessTokenExtractor {

	public String getAccessTokenValue(byte[] response);
}
