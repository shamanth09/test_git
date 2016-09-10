package com.mrkinnoapps.myordershopadmin.bean.dto;

import com.mrkinnoapps.myordershopadmin.bean.constant.Role;
import com.mrkinnoapps.myordershopadmin.bean.dto.SearchFilter.SearchIn;

public class UserSearchResource extends SearchResource {

	private Role role;

	public UserSearchResource(String result, SearchIn field, String lable,
			Class entity) {
		super(result, field, lable, entity);
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}
