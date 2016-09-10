package com.mrkinnoapps.myordershopadmin.bean.constant;

public enum Attempt {
	SUCCESS("Success"), FAILUR("Failure");
	private String value;

	private Attempt(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
