package com.mrkinnoapps.myordershopadmin.validtors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.mrkinnoapps.myordershopadmin.bean.entity.Address;

@Component
public class AddressValidator implements Validator {
	
	 private Pattern pattern;  
	 private Matcher matcher;  
	 String pin = "[0-9]{6}";  

	@Override
	public boolean supports(Class<?> cls) {
		return Address.class.isAssignableFrom(cls);
	}

	@Override
	public void validate(Object obj, Errors err) {

		Address address=(Address)obj; 
		
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "title",
				"required", "Required field");
		
		/*ValidationUtils.rejectIfEmptyOrWhitespace(err, "street",
				"required", "Required field");*/
		
		/*ValidationUtils.rejectIfEmptyOrWhitespace(err, "area",
				"required", "Required field");*/
		
		/*ValidationUtils.rejectIfEmptyOrWhitespace(err, "city",
				"required", "Required field");*/
		
		/*ValidationUtils.rejectIfEmptyOrWhitespace(err, "state",
				"required", "Required field");*/
		
		/*ValidationUtils.rejectIfEmptyOrWhitespace(err, "country",
				"required", "Required field");*/
		
		/*ValidationUtils.rejectIfEmptyOrWhitespace(err, "pincode",
				"required", "Required field");*/
		
		
		/*if(address.getPincode()!=null && !(address.getPincode().isEmpty()))
		{
			pattern=pattern.compile(pin);
			matcher=pattern.matcher(address.getPincode());
			if(!matcher.matches())
			{
				err.rejectValue("pincode", "incorrect", " 6 digit pin number is required");
			}
		}*/
		
	}

}
