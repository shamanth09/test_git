package com.mrk.myordershop.log.constant;

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
