package com.mrkinnoapps.myordershopadmin.bean.dto;

import java.util.regex.Pattern;

import com.mrkinnoapps.myordershopadmin.bean.constant.Role;

public class Register {

	private String email;
	private String name;
	private String password;
	private Role role;
	private String mobile;
	private String firmName;
	private String activationUrl;

	private String mobilePattern = "^[0-9]{10}$";
	private String emailPattern = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,3})$";

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if (Pattern.matches(mobilePattern, email)) {
			this.mobile = email;
		} else if (Pattern.matches(emailPattern, email)) {
			this.email = email;
		} else {
			this.email = email;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getFirmName() {
		return firmName;
	}

	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}

	public String getActivationUrl() {
		return activationUrl;
	}

	public void setActivationUrl(String activationUrl) {
		this.activationUrl = activationUrl;
	}
}
