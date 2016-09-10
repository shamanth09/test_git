package com.mrk.myordershop.exception;

public class InvalidFlowException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static String messageFormat = "%s %s";

	public InvalidFlowException() {
		// TODO Auto-generated constructor stub
	}

	public InvalidFlowException(String msg) {
		super(String.format(msg));
	}

	public InvalidFlowException(Class<?> t, String message) {
		super(String.format(messageFormat, t.getSimpleName().toLowerCase(), message));
	}

}
