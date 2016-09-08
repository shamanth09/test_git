package com.mrk.myordershop.constant;

public enum EnableDisable {

	Enable("Enable"), Disable("Disable");
	private String value;

	private EnableDisable(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
