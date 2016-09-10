package com.mrk.myordershop.constant.view;

public class Alert {

	public static String TYPE_SUCCESS = "success", TYPE_ERROR = "error";
	private String type;
	private String message;


	/**
	 * 
	 */
	public Alert() { }
	public Alert(String type, String message) {
		this.type = type;
		this.message = message;
	}

	public static String getTYPE_SUCCESS() {
		return TYPE_SUCCESS;
	}

	public static void setTYPE_SUCCESS(String tYPE_SUCCESS) {
		TYPE_SUCCESS = tYPE_SUCCESS;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
