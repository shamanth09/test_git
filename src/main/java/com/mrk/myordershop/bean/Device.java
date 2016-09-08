package com.mrk.myordershop.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "MOS_DEVICE")
public class Device {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private int id;

	@Column(name = "DEVICE_TOKEN", unique = true)
	private String deviceToken;

	@Column(name = "USER_ID")
	private String userId;
	
	@Column(name = "CLIENT_ID")
	private String clientId;
	
	@Column(name="CREATE_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTimestamp;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public Date getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Date createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

}
