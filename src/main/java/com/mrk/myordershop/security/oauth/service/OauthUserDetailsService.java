package com.mrk.myordershop.security.oauth.service;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.dto.UserCredential;
import com.mrk.myordershop.config.annotation.PersistTransactional;
import com.mrk.myordershop.config.annotation.ReadTransactional;
import com.mrk.myordershop.constant.ActiveFlag;
import com.mrk.myordershop.dao.UserDAO;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.InvalidDataException;
import com.mrk.myordershop.security.bean.SessionUser;

public class OauthUserDetailsService implements UserDetailsService {
	private static final Logger log = Logger.getLogger(OauthUserDetailsService.class);
	@Autowired
	private UserDAO userAuthDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	private MOSJdbcTokenStore tokenStore;
	@Autowired
	@Qualifier("tokenServices")
	private DefaultTokenServices tokenServices;

	@ReadTransactional
	@Override
	public UserDetails loadUserByUsername(String emailOrMobile) throws UsernameNotFoundException {
		User domainUser;
		try {
			domainUser = userDAO.getUserByMobieOrEmail(emailOrMobile);
			if (domainUser.getActiveFlag() != ActiveFlag.ACTIVE)
				throw new UsernameNotFoundException("user not in active");
			// this is going to persist in authentication table so to reduce
			// image
			// size set null
			if (domainUser.getImage() != null)
				domainUser.getImage().setImageArray(null);
			log.debug("user found [" + domainUser.getEmail() + "] enabled " + domainUser.getActiveFlag());
		} catch (EntityDoseNotExistException e) {
			log.debug("user not found [" + emailOrMobile + "]");
			throw new UsernameNotFoundException("User not found");
		}
		SessionUser user = SessionUser.get(domainUser);
		return user;
	}

	public boolean isUserLoggedInc(String username) {
		Collection<OAuth2AccessToken> accessTokens = tokenStore.findTokensByUserName(username);
		return accessTokens.size() > 0 ? true : false;
	}

	@PersistTransactional
	public void changePassword(UserCredential userCredential, User user) throws InvalidDataException {
		User userdb;
		try {
			userdb = userAuthDAO.get(user.getId());
			if (userdb.getPassword().equals(userCredential.getOldPassword())) {
				userdb.setPassword(userCredential.getNewPassword());
				userAuthDAO.update(userdb);
			} else
				throw new InvalidDataException("Password", "match with old password");
		} catch (EntityDoseNotExistException e) {
			e.printStackTrace();
		}

	}

	public void updateCurrentDomainUser(User user) {
		OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext()
				.getAuthentication();
		if (user.getImage() != null)
			user.getImage().setImageArray(null);
		((SessionUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).setDomainUser(user);
		tokenStore.updateAuthentication(authentication);
	}

	public static User getCurrentDomainUser() {
		User user = ((SessionUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getDomainUser();
		return user;
	}

	public void revokeAllToken(String emailOrMobile) {
		for (OAuth2AccessToken oauthToken : tokenStore.findTokensByUserName(emailOrMobile)) {
			tokenServices.revokeToken(oauthToken.getValue());
		}
	}
}
