package com.mrkinnoapps.myordershopadmin.bean.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mrkinnoapps.myordershopadmin.bean.constant.ActiveFlag;

@Entity
@Table(name = "MOS_TOKEN")
public class Token {

	public enum Type {
		ACTIVATION, FORGOTPASSWORD
	}

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Integer id;

	@Column(name = "ACTIVE_FLAG")
	@Enumerated(EnumType.STRING)
	private ActiveFlag activeFlag;

	@Column(name = "TYPE")
	@Enumerated(EnumType.STRING)
	private Type type;

	@Column(name = "TOKEN")
	private String token;

	@Column(name = "TIMEOUT")
	private int timeout;

	@OneToOne
	@JoinColumn(name = "USER_ID")
	private User user;

	@JsonIgnore
	@Column(name = "CREATE_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTimestamp;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ActiveFlag getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(ActiveFlag activeFlag) {
		this.activeFlag = activeFlag;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public Date getCreateTimestamp() {
		return createTimestamp;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setCreateTimestamp(Date createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public boolean isExpire() {
		return this.createTimestamp.compareTo(new Date()) > this.timeout;
	}
}
