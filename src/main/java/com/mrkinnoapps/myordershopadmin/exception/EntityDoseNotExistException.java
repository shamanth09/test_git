package com.mrkinnoapps.myordershopadmin.exception;

import java.io.Serializable;

public class EntityDoseNotExistException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String MESSAGE_FORMAT = "%s dose not exist.";
	private static final String MESSAGE_FORMAT_ID = "%s id %s dose not exist.";
	private static final String MESSAGE_FORMAT_S = "%s not exist";

	public EntityDoseNotExistException() {
	}

	public EntityDoseNotExistException(String entity) {
		super(String.format(MESSAGE_FORMAT, entity));
	}

	public EntityDoseNotExistException(String entity, String id) {
		super(String.format(MESSAGE_FORMAT_ID, entity, id));
	}

	public EntityDoseNotExistException(Class<?> t, Serializable id) {
		super(String
				.format(MESSAGE_FORMAT_ID, t.getSimpleName(), id.toString()));
	}

	public EntityDoseNotExistException(Class<?> t) {
		super(String.format(MESSAGE_FORMAT_S, t.getSimpleName().toLowerCase()));
	}
}
