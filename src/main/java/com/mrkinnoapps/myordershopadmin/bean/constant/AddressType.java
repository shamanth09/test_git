package com.mrkinnoapps.myordershopadmin.bean.constant;

public enum AddressType {
	CURRENT("Current"), OFFICE("Office"), PERMANENT("Permanent"), RESIDENTIAL(
			"Residential");
	private final String value;

	private AddressType(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
