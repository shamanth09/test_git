package com.mrkinnoapps.myordershopadmin.bean.dto;

import java.util.Date;
import java.util.List;

import com.mrkinnoapps.myordershopadmin.bean.constant.ActiveFlag;
import com.mrkinnoapps.myordershopadmin.bean.constant.Role;

public class UserFilter {

	private String[] userName;

	private String[] email;

	private Role role;

	private String[] mobile;
	
	private List<ActiveFlag> activeFlags;
	
	private Date fromDate;
	
	private Date toDate;

	public List<ActiveFlag> getActiveFlags() {
		return activeFlags;
	}

	public void setActiveFlags(List<ActiveFlag> activeFlags) {
		this.activeFlags = activeFlags;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}


	public String[] getEmail() {
		return email;
	}

	public String[] getMobile() {
		return mobile;
	}


	public String[] getUserName() {
		return userName;
	}

	public void setUserName(String[] userName) {
		this.userName = userName;
	}

	public void setEmail(String[] email) {
		this.email = email;
	}

	public void setMobile(String[] mobile) {
		this.mobile = mobile;
	}
	


}
