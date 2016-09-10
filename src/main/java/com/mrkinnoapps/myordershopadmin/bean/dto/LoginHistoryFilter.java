package com.mrkinnoapps.myordershopadmin.bean.dto;

import java.util.Date;

import com.mrkinnoapps.myordershopadmin.bean.constant.Attempt;

public class LoginHistoryFilter {

	private String userId;

	private String clientId;

	private Date fromTimestamp;

	private Date toTimestamp;

	private Attempt attempt;

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

	public Date getFromTimestamp() {
		return fromTimestamp;
	}

	public void setFromTimestamp(Date fromTimestamp) {
		this.fromTimestamp = fromTimestamp;
	}

	public Date getToTimestamp() {
		return toTimestamp;
	}

	public void setToTimestamp(Date toTimestamp) {
		this.toTimestamp = toTimestamp;
	}

	public Attempt getAttempt() {
		return attempt;
	}

	public void setAttempt(Attempt attempt) {
		this.attempt = attempt;
	}

}
