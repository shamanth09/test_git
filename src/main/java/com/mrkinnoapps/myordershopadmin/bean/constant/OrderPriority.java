package com.mrkinnoapps.myordershopadmin.bean.constant;

public enum OrderPriority {
	NORMAL("Normal"), URGENT("Urgent");

	private String value;

	private OrderPriority(String value) {
		this.value = value;
	}

	private String getValue() {
		return this.value;
	}
}
