package com.mrkinnoapps.myordershopadmin.bean.entity;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mrkinnoapps.myordershopadmin.bean.constant.Attempt;
import com.mrkinnoapps.myordershopadmin.util.Locations;
import com.mrkinnoapps.myordershopadmin.util.Locations.Location;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

@Entity
@Table(name = "MOS_LOGIN_HISTORY")
public class LoginHistory {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private int id;

	@Column(name = "USER_ID")
	private String userId;

	@Enumerated(EnumType.STRING)
	@Column(name = "ATTEMPT")
	private Attempt attempt;

	@Column(name = "REFRESH_TOKEN")
	private String refreshToken;

	@Column(name = "CLIENT_ID")
	private String clientId;

	@Column(name = "IP")
	private String ip;

	@Column(name = "AGENT")
	private String agent;

	@Column(name = "CREATE_TIMESTAMP")
	private Date createTimestamp;

	@Column(name = "SESSION_DURATION")
	private int sessionDuration;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Attempt getAttempt() {
		return attempt;
	}

	public void setAttempt(Attempt attempt) {
		this.attempt = attempt;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public Date getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Date createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public int getSessionDuration() {
		return sessionDuration;
	}

	public void setSessionDuration(int sessionDuration) {
		this.sessionDuration = sessionDuration;
	}

	@JsonIgnore
	public Location getLocation() {
		return Locations.getLocations("122.171.24.133");
	}

	@JsonIgnore
	public String getOs() {
		if (!UserAgent.parseUserAgentString(this.agent).getOperatingSystem()
				.equals(OperatingSystem.UNKNOWN)) {
			return UserAgent.parseUserAgentString(this.agent)
					.getOperatingSystem().getName();
		} else {
			Matcher matcher = Pattern.compile(".*\\(").matcher(this.agent);
			matcher.find();
			String res = matcher.group();
			res = res.replace("(", "");
			return res;
		}
	}

	@JsonIgnore
	public String getBrowser() {
		if (!UserAgent.parseUserAgentString(this.agent).getBrowser()
				.equals(Browser.UNKNOWN)) {
			return UserAgent.parseUserAgentString(this.agent).getBrowser()
					.getName();
		} else {
			Matcher matcher = Pattern.compile("\\(.*\\)").matcher(this.agent);
			matcher.find();
			String res = matcher.group();
			res = res.replace("(", "").replace(")", "");
			return res;
		}
	}

	public static void main(String[] args) {
		// LoginHistory loginHistory = new LoginHistory();
		// loginHistory.agent = "order/1.1.2 (iphone; ios 9.2; scale/2.00)";
		Matcher matcher = Pattern.compile(".*\\(").matcher(
				"order/1.1.2 (iphone; ios 9.2; scale/2.00)ss");
		matcher.find();
		System.out.println(matcher.group());
		// System.out.println(loginHistory.getOs());
	}

	@Override
	public String toString() {
		return "LoginHistory [id=" + id + ", userId=" + userId + ", attempt="
				+ attempt + ", refreshToken=" + refreshToken + ", clientId="
				+ clientId + ", ip=" + ip + ", agent=" + agent
				+ ", createTimestamp=" + createTimestamp + ", sessionDuration="
				+ sessionDuration + "]";
	}
}
