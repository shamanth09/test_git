package com.mrk.myordershop.exception;

public class InvalidDataException extends RuntimeException {

	private static final String MESSAGE_FORMAT = "%s has invalid data.";
	private static final String MESSAGE_FORMAT_S = "%s dose not %s";

	public InvalidDataException() {
	}

	public InvalidDataException(String entity) {
		super(String.format(MESSAGE_FORMAT, entity));
	}

	public InvalidDataException(String entity, String message) {
		super(String.format(MESSAGE_FORMAT_S, entity, message));
	}

	public InvalidDataException(Class<?> t, String message) {
		super(String.format(MESSAGE_FORMAT_S, t.getSimpleName().toLowerCase(),
				message));
	}
}
