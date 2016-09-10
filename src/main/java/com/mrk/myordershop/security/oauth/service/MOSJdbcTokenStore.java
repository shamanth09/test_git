package com.mrk.myordershop.security.oauth.service;

import java.sql.Types;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

public class MOSJdbcTokenStore extends JdbcTokenStore {

	private static final String DEFAULT_REFRESH_TOKEN_UPDATE_STATEMENT = "update oauth_refresh_token set authentication = ? where token_id=?";

	private static final String DEFAULT_ACCESS_TOKEN_UPDATE_STATEMENT = "update oauth_access_token set authentication = ? where user_name=?";

	private String updateAccessTokenSql = DEFAULT_ACCESS_TOKEN_UPDATE_STATEMENT;

	private String updateRefreshTokenSql = DEFAULT_REFRESH_TOKEN_UPDATE_STATEMENT;

	private JdbcTemplate jdbcTemplate;

	public MOSJdbcTokenStore(DataSource dataSource) {
		super(dataSource);
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public void updateAuthentication(OAuth2Authentication authentication) {
		jdbcTemplate.update(updateAccessTokenSql,
				new Object[] { new SqlLobValue(serializeAuthentication(authentication)), authentication.getName() },
				new int[] { Types.BLOB, Types.VARCHAR });
		for (OAuth2AccessToken accessToken : findTokensByUserName(authentication.getName())) {
			jdbcTemplate.update(updateRefreshTokenSql,
					new Object[] { new SqlLobValue(serializeAuthentication(authentication)),
							extractTokenKey(accessToken.getRefreshToken().getValue()) },
					new int[] { Types.BLOB, Types.VARCHAR });
		}

	}
}
