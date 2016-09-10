package com.mrkinnoapps.myordershopadmin.exception;

public class EntityCannotDeleteException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private static final String MESSAGE_FORMAT = "This %s is still in Orders.";
	private String deleteMessage = null;
	
	public String getDeleteMessage() {
		return deleteMessage;
	}

	public EntityCannotDeleteException() {
	}

	public EntityCannotDeleteException(Class<?> entity) {
		super(String.format(MESSAGE_FORMAT, entity));
	}

	public EntityCannotDeleteException(String entity) {
		super(String.format(MESSAGE_FORMAT, entity));
	}

	public EntityCannotDeleteException(String entity, String message) {
		super(String.format(entity, message));
		deleteMessage = message;
	}

	public EntityCannotDeleteException(Class<?> t, String message) {
		super(String.format(t.getSimpleName().toLowerCase(),
				message));
	}
}
