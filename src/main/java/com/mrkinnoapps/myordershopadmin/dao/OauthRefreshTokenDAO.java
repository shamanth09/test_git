package com.mrkinnoapps.myordershopadmin.dao;

import com.mrkinnoapps.myordershopadmin.bean.entity.OauthRefreshToken;

public interface OauthRefreshTokenDAO {
	
	OauthRefreshToken getOauthRefreshToken(String string);
	
	void delete(OauthRefreshToken oauthRefreshToken);

}
