package com.mrkinnoapps.myordershopadmin.bean.dto;

public class FieldError {

	private String defaultMessage;

	private String objectName;

	private String field;

	private String code;

	public FieldError(String defaultMessage, String objectName, String field,
			String code) {
		super();
		this.defaultMessage = defaultMessage;
		this.objectName = objectName;
		this.field = field;
		this.code = code;
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}

	public void setDefaultMessage(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
