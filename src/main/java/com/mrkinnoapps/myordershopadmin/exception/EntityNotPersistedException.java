package com.mrkinnoapps.myordershopadmin.exception;

public class EntityNotPersistedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String MESSAGE_FORMAT = "%s dose not persist.";
	private static final String MESSAGE_FORMAT_S = "%s not saved case of %s";

	public EntityNotPersistedException() {
	}

	public EntityNotPersistedException(Class<?> entity) {
		super(String.format(MESSAGE_FORMAT, entity));
	}

	public EntityNotPersistedException(String entity) {
		super(String.format(MESSAGE_FORMAT, entity));
	}

	public EntityNotPersistedException(String entity, String message) {
		super(String.format(MESSAGE_FORMAT_S, entity, message));
	}

	public EntityNotPersistedException(Class<?> t, String message) {
		super(String.format(message));
	}
}
