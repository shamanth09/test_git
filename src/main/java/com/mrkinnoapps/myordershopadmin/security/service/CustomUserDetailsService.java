package com.mrkinnoapps.myordershopadmin.security.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class CustomUserDetailsService implements UserDetailsService {
	
	private String username;
	
	private String password;
	
	

	public CustomUserDetailsService(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}



	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {

		if (this.username.equals(username)) {
			List<GrantedAuthority> grant = new ArrayList<GrantedAuthority>();
			grant.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
			return new User(username, password, grant);
		} else {
			throw new UsernameNotFoundException("Username not matched");
		}
	}
}
