package com.mrkinnoapps.myordershopadmin.controller.advicer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mrkinnoapps.myordershopadmin.bean.dto.FieldError;
import com.mrkinnoapps.myordershopadmin.exception.EntityCannotDeleteException;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotValidException;
import com.mrkinnoapps.myordershopadmin.resource.ErrorResource;

@ControllerAdvice(basePackages = { "com.mrkinnoapps.myordershopadmin.controller.admin" })
public class AdminExceptionHandler {

	@ExceptionHandler(EntityNotPersistedException.class)
	public ResponseEntity<ErrorResource> entityNotPersisterExceptionHandler(
			EntityNotPersistedException e) {
		return new ResponseEntity<ErrorResource>(new ErrorResource("5000",
				e.getMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(EntityDoseNotExistException.class)
	public ResponseEntity<ErrorResource> handleEntityDoseNotExistException(
			EntityDoseNotExistException e) {
		return new ResponseEntity<ErrorResource>(new ErrorResource("4000",
				e.getMessage()), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(EntityCannotDeleteException.class)
	public ResponseEntity<ErrorResource> entityCannotDeleteException(
			EntityCannotDeleteException e) {
		return new ResponseEntity<ErrorResource>(new ErrorResource("1234",e.getDeleteMessage()
				), HttpStatus.BAD_REQUEST);
	}


	@ExceptionHandler(EntityNotValidException.class)
	@ResponseBody
	public ResponseEntity<List<FieldError>> processValidationError(
			EntityNotValidException ex) {
		List<FieldError> fieldErrors = new ArrayList<FieldError>();
		for (org.springframework.validation.FieldError fieldError : ex
				.getBindingResult().getFieldErrors()) {
			fieldErrors.add(new FieldError(fieldError.getDefaultMessage(),
					fieldError.getObjectName(), fieldError.getField(),
					fieldError.getCode()));
		}
		return new ResponseEntity<List<FieldError>>(fieldErrors,
				HttpStatus.BAD_REQUEST);
	}
}
