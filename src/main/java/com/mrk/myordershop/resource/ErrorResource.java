package com.mrk.myordershop.resource;

public class ErrorResource {

	private String code;
	private String property;
	private String message;
	private String developerMessage;
	private String moreInfo;

	public ErrorResource(String message) {
		this.message = message;
	}

	public ErrorResource(String message, String property) {
		this(message);
		this.property = property;
	}

	public ErrorResource(String message, String property,
			String developerMessage) {
		this(message, property);
		this.developerMessage = developerMessage;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDeveloperMessage() {
		return developerMessage;
	}

	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}

	public String getMoreInfo() {
		return moreInfo;
	}

	public void setMoreInfo(String moreInfo) {
		this.moreInfo = moreInfo;
	}

}
