package com.mrk.myordershop.bean.dto;

public class FieldErrorDTO {
	
	private String message;
	private String errorCode;
	private String field;
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public FieldErrorDTO() {
	    super();
	  }
	  
	  public FieldErrorDTO(String message, String field,String errorCode) {
	    super();
	    this.message = message;
	    this.field = field;
	    this.errorCode = errorCode;
	  }

	  public String getMessage() {
	    return message;
	  }
	  
	  public void setMessage(String message) {
	    this.message = message;
	  }
	  
}
