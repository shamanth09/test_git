package com.mrkinnoapps.myordershopadmin.resource;

import java.util.HashMap;
import java.util.Map;

public class ErrorResource {

	private String code;

	private String reason;

	private Map<String, Object> property;

	public ErrorResource(String code, String reason) {
		this.code = code;
		this.reason = reason;
	}

	public ErrorResource() {
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Map<String, Object> getProperty() {
		return property;
	}

	public void setProperty(Map<String, Object> property) {
		this.property = this.property != null ? this.property
				: new HashMap<String, Object>();
		this.property = property;
	}
}
