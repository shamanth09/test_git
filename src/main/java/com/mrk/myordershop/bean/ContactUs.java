package com.mrk.myordershop.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.mrk.myordershop.constant.ActiveFlag;

/**
 * ContactUs.java Naveen Apr 4, 2015
 */
@Entity
@Table(name = "MOS_CONTACTUS")
public class ContactUs {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private int id;

	@Column(name = "ACTIVE_FLAG")
	@Enumerated(EnumType.STRING)
	private ActiveFlag activeFlag = ActiveFlag.ACTIVE;

	@Column(name = "NAME")
	private String name;

	@Column(name = "MOBILE")
	private String mobile;

	@Column(name = "ENQUIRY", length = 600)
	private String Enquiry;

	@Column(name = "CREATE_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTimeStamp = new Date();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ActiveFlag getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(ActiveFlag activeFlag) {
		this.activeFlag = activeFlag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEnquiry() {
		return Enquiry;
	}

	public void setEnquiry(String enquiry) {
		Enquiry = enquiry;
	}

	public Date getCreateTimeStamp() {
		return createTimeStamp;
	}

	public void setCreateTimeStamp(Date createTimeStamp) {
		this.createTimeStamp = createTimeStamp;
	}

}
