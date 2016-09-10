package com.mrkinnoapps.myordershopadmin.bean.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "oauth_refresh_token")
public class OauthRefreshToken implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="token_id")
	private String tokenId;
	
	public String getTokenId() {
		return tokenId;
	}

}
