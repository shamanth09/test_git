package com.mrkinnoapps.myordershopadmin.bean.constant;

public enum ActiveFlag {
	ACTIVE("ACTIVE"), INACTIVE("INACTIVE"), INPROCESS("INPROCESS"), REJECT(
			"REJECT"), ACCEPT("ACCEPT");

	private final String name;

	private ActiveFlag(String name) {
		this.name = name;
	}

	public String getActiveFlag() {
		return this.name;
	}
}
