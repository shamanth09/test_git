package com.mrkinnoapps.myordershopadmin.dao;

import com.mrkinnoapps.myordershopadmin.bean.entity.OauthAccessToken;

public interface OauthAccessTokenDAO {
	
	OauthAccessToken getOauthAccessToken(String userEmail);
	
	void delete(OauthAccessToken oauthAccessToken);
	
}
