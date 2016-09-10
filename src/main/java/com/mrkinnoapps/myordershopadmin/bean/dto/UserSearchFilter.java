package com.mrkinnoapps.myordershopadmin.bean.dto;

import com.mrkinnoapps.myordershopadmin.bean.constant.Role;

public class UserSearchFilter extends SearchFilter{

	private Role role;

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	public void fillSearchOnUser() {
		super.addSearchIn(SearchIn.NAME);
		super.addSearchIn(SearchIn.EMAIL);
		super.addSearchIn(SearchIn.MOBILE);
	}
}
