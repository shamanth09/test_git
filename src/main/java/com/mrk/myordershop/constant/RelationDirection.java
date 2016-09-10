package com.mrk.myordershop.constant;

public enum RelationDirection {
	FROM_YOU("From You"), TO_YOU("To You");

	private String value;

	private RelationDirection(String value) {
		this.value = value;
	}
}
