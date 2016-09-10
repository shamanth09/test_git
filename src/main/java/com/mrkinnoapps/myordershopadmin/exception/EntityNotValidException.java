package com.mrkinnoapps.myordershopadmin.exception;

import org.springframework.validation.BindingResult;

public class EntityNotValidException extends Exception {

	private static final long serialVersionUID = 1L;

	private final BindingResult bindingResult;

	public EntityNotValidException(BindingResult bindingResult) {
		this.bindingResult = bindingResult;
	}

	public BindingResult getBindingResult() {
		return this.bindingResult;
	}

}
