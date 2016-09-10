package com.mrkinnoapps.myordershopadmin.bean.dto;

import com.mrkinnoapps.myordershopadmin.bean.constant.RelationDirection;
import com.mrkinnoapps.myordershopadmin.bean.constant.RelationStatus;
import com.mrkinnoapps.myordershopadmin.bean.constant.Role;

public class RelationFilter {

	private RelationStatus status;

	private String name;

	private String email;

	private String mobile;

	private Role userRole;

	private RelationDirection direction;

	public RelationStatus getStatus() {
		return status;
	}

	public void setStatus(RelationStatus status) {
		this.status = status;
	}

	public Role getUserRole() {
		return userRole;
	}

	public void setUserRole(Role userRole) {
		this.userRole = userRole;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RelationDirection getDirection() {
		return direction;
	}

	public void setDirection(RelationDirection direction) {
		this.direction = direction;
	}

}
