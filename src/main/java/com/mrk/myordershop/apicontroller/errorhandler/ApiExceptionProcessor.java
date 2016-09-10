package com.mrk.myordershop.apicontroller.errorhandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

import com.mrk.myordershop.bean.dto.FieldErrorDTO;
import com.mrk.myordershop.exception.DuplicateContactsException;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;
import com.mrk.myordershop.exception.InvalidDataException;
import com.mrk.myordershop.exception.InvalidFlowException;
import com.mrk.myordershop.resource.DuplicateContactsErrorResources;
import com.mrk.myordershop.resource.ErrorResource;

@ControllerAdvice(basePackages = { "com.mrk.myordershop.apicontroller" })
public class ApiExceptionProcessor extends AbstractJsonpResponseBodyAdvice {

	@Autowired
	@Qualifier("messageSource")
	private MessageSource messageSource;

	// @InitBinder
	// public void initBinder(WebDataBinder binder) {
	// StringTrimmerEditor stringtrimmer = new StringTrimmerEditor(false);
	// binder.registerCustomEditor(String.class, stringtrimmer);
	//
	// DateFormat formate = new SimpleDateFormat("dd-MM-yyyy");
	// formate.setTimeZone(TimeZone.getDefault());
	// formate.setLenient(false);
	// binder.registerCustomEditor(Date.class, new ExpandableCustomDateEditor(
	// formate, JsonTimeStampSerializer.getFormates(), true));
	// }

	public ApiExceptionProcessor() {
		super("jsonp");
	}

	@ExceptionHandler(EntityDoseNotExistException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorResource getHandleEntityNotFound(HttpServletRequest req, EntityDoseNotExistException ex) {
		ErrorResource resource = new ErrorResource(ex.getMessage());
		resource.setCode("00404");
		return resource;
	}

	@ExceptionHandler(EntityNotPersistedException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorResource getHandleEntityNotPersisted(HttpServletRequest req, EntityNotPersistedException ex) {
		ErrorResource resource = new ErrorResource(ex.getMessage());
		resource.setCode("00405");
		return resource;
	}

	@ExceptionHandler(InvalidFlowException.class)
	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	@ResponseBody
	public ErrorResource getHandleInvalidFlowException(HttpServletRequest req, InvalidFlowException in) {
		ErrorResource resource = new ErrorResource(in.getMessage());
		resource.setCode("00403");
		return resource;
	}

	@ExceptionHandler(InvalidDataException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorResource getHandleInvalidDataException(HttpServletRequest req, InvalidDataException ex) {
		ErrorResource resource = new ErrorResource(ex.getMessage());
		resource.setCode("00406");
		return resource;
	}

	@ExceptionHandler(DuplicateContactsException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorResource getHandleDuplicateContactsException(HttpServletRequest req, DuplicateContactsException ex) {
		DuplicateContactsErrorResources resource = new DuplicateContactsErrorResources(ex.getMessage(),
				ex.getDuplicateContacts());
		resource.setCode("00411");
		return resource;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public List<FieldErrorDTO> processValidationError(MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();
		List<FieldError> errors = result.getFieldErrors();
		
		FieldErrorDTO fieldErrorDTO = null;
		List<FieldErrorDTO> errorList = new ArrayList<FieldErrorDTO>();
		for (FieldError fieldError : errors) {
			if(fieldError != null){
				Locale currentLocale = LocaleContextHolder.getLocale();
			      String msg = messageSource.getMessage(fieldError.getDefaultMessage(),fieldError.getArguments(), null, currentLocale);
			      fieldErrorDTO = new FieldErrorDTO(msg,fieldError.getField(),fieldError.getCode());
			      errorList.add(fieldErrorDTO);
			}
		}
		
//        StringBuffer customMessage = new StringBuffer();
//        
//        for (FieldError error : errors ) {
//            customMessage.append(error.getObjectName() +"." + error.getField() +" "+ error.getDefaultMessage()+"\n");
//        }        
        return errorList;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

}
