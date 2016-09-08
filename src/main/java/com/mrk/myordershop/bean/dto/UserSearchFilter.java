package com.mrk.myordershop.bean.dto;

import java.util.List;

import com.mrk.myordershop.constant.RelationStatus;
import com.mrk.myordershop.constant.Role;

public class UserSearchFilter extends SearchFilter{

	private Role role;
	
	private List<RelationStatus> status;

	
	public List<RelationStatus> getStatus() {
		return status;
	}

	public void setStatus(List<RelationStatus> status) {
		this.status = status;
	}

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
