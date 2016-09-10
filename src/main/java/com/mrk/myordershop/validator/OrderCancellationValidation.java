package com.mrk.myordershop.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.mrk.myordershop.bean.Cancellation;
import com.mrk.myordershop.exception.InvalidDataException;

@Component
public class OrderCancellationValidation implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Cancellation.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object obj, Errors err) {
		
		Cancellation cancellation = (Cancellation) obj;
		
		if(cancellation != null){
			if(cancellation.getReason() == null || cancellation.getReason().equals("")){
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "reason", "required", new Object[] { "cancellation reason " },
						"error.required.field");
			}
		}else
			throw new InvalidDataException("Cancellation");
	}

}
