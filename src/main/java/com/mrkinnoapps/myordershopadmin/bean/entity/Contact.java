package com.mrkinnoapps.myordershopadmin.bean.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.mrkinnoapps.myordershopadmin.bean.constant.ContactGroup;
import com.mrkinnoapps.myordershopadmin.bean.dto.View;

@Entity
@Table(name = "MOS_CONTACT")
public class Contact {

	@JsonView(View.ContactBasic.class)
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private int id;

	@JsonView(View.ContactBasic.class)
	@Column(name = "NAME")
	private String name;

	@JsonView(View.ContactDetail.class)
	@Enumerated(EnumType.STRING)
	@Column(name = "CONTACT_GROUP")
	private ContactGroup group;

	@JsonView(View.ContactBasic.class)
	@Column(name = "MOBILE")
	private String mobile;

	@JsonView(View.ContactBasic.class)
	@Column(name = "FIRM_NAME")
	private String firmName;

	@JsonView(View.ContactBasic.class)
	@Column(name = "EMAIL")
	private String email;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private User user;

	@JsonIgnore
	@Column(name = "CREATE_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTimestamp = new Date();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ContactGroup getGroup() {
		return group;
	}

	public void setGroup(ContactGroup group) {
		this.group = group;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getFirmName() {
		return firmName;
	}

	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Date createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

}
