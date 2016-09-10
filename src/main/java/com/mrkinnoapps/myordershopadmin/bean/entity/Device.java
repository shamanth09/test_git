package com.mrkinnoapps.myordershopadmin.bean.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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
	
}
