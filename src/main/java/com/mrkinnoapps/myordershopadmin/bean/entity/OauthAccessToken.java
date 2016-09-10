package com.mrkinnoapps.myordershopadmin.bean.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "oauth_access_token")
public class OauthAccessToken implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "token_id")
	private String tokenId;
	
	@Column(name= "authentication_id",unique = true)
	private String authenticationId;
	
	@Column(name= "user_name")
	private String userEmail;
	
	@Column(name= "client_id")
	private String clientId;
	
	@Column(name = "refresh_token")
	private String refreshToken;

	public String getTokenId() {
		return tokenId;
	}

	public String getAuthenticationId() {
		return authenticationId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public String getClientId() {
		return clientId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
}
