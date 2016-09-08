package com.mrk.myordershop.security.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.mail.Session;

import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.mrk.myordershop.bean.UserRole;

public class SessionUser extends User {

	private static final Logger log = Logger.getLogger(SessionUser.class);

	private com.mrk.myordershop.bean.User domainUser;

	private static boolean enabled = true;
	private static boolean accountNonExpired = true;
	private static boolean credentialsNonExpired = true;
	private static boolean accountNonLocked = true;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected SessionUser(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}

	public static SessionUser get(com.mrk.myordershop.bean.User domainUser) {
		List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<SimpleGrantedAuthority>();
		for (UserRole role : domainUser.getUserRoles()) {
			grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole().toString()));
		}
		log.debug(grantedAuthorities);
		SessionUser user = new SessionUser(domainUser.getId(), domainUser.getPassword(), enabled, accountNonExpired,
				credentialsNonExpired, accountNonLocked, grantedAuthorities);
		user.setDomainUser(domainUser);
		return user;
	}

	public com.mrk.myordershop.bean.User getDomainUser() {
		return domainUser;
	}

	public void setDomainUser(com.mrk.myordershop.bean.User domainUser) {
		this.domainUser = domainUser;
	}

}
