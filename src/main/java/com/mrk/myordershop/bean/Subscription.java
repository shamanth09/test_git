package com.mrk.myordershop.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MOS_SUBSCRIPTION")
public class Subscription {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private int id;

	@Column(name = "EMAIL", unique = true)
	private String email;

	@Column(name = "MOBILE", unique = true)
	private String mobile;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

}
