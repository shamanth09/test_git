package com.mrkinnoapps.myordershopadmin.bean.constant;

public enum RelationStatus {
	REQUESTED("Requested"), REJECTED("Rejected"), ACCEPTED("Accepted");
	private String value;

	private RelationStatus(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}
