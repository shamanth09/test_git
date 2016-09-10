package com.mrk.myordershop.security.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.UserRole;
import com.mrk.myordershop.config.annotation.ReadTransactional;
import com.mrk.myordershop.dao.UserDAO;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

public class CustomUserDetailsService implements UserDetailsService {
	private static final Logger log = Logger.getLogger(CustomUserDetailsService.class);

	private UserDAO userAuthDAO;

	@Override
	public UserDetails loadUserByUsername(String emailOrMobile) throws UsernameNotFoundException {
		boolean enabled = true;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;

		User domainUser;
		try {
			domainUser = getUserByUsernameOrMobile(emailOrMobile);
		} catch (EntityDoseNotExistException e) {
			throw new UsernameNotFoundException("User not found");
		}
		org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(
				domainUser.getId(), domainUser.getPassword(), enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, getAuthorities(domainUser));

		log.debug("===== " + user + " ====");
		return user;
	}

	@ReadTransactional
	public User getUserByUsernameOrMobile(String emailOrMobile) throws EntityDoseNotExistException {
		User user = userAuthDAO.getUserByMobieOrEmail(emailOrMobile);
		return user;
	}

	private Collection<SimpleGrantedAuthority> getAuthorities(User user) {
		List<SimpleGrantedAuthority> authList = getGrantedAuthorities(getRoles(user));
		return authList;
	}

	private Set<UserRole> getRoles(User user) {
		return user.getUserRoles();
	}

	private List<SimpleGrantedAuthority> getGrantedAuthorities(Set<UserRole> roles) {
		List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<SimpleGrantedAuthority>();
		for (UserRole role : roles) {
			grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole().toString()));
		}
		return grantedAuthorities;
	}
}
