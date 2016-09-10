package com.mrkinnoapps.myordershopadmin.bean.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mrkinnoapps.myordershopadmin.bean.constant.Role;

@Entity
@Table(name = "MOS_USER_ROLE")
public class UserRole implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private int id;

	@Column(name = "USER_TYPES")
	@Enumerated(EnumType.STRING)
	private Role role;

	@Column(name = "NAME")
	private String name;

	@JsonIgnore
	@Column(name = "DISCRIPTION")
	private String discription;

	public UserRole() {
	}

	public UserRole(Role role) {
		this.role = role;
		this.name = role.getValue();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
		this.name = role.getValue();
	}

	public String getDiscription() {
		return discription;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
