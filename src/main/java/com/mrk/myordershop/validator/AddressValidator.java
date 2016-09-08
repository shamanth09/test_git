package com.mrk.myordershop.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.mrk.myordershop.bean.Address;

@Component
public class AddressValidator implements Validator{

	@Override
	public boolean supports(Class<?> claz) {
		return Address.class.isAssignableFrom(claz);
	}

	@Override
	public void validate(Object obj, Errors err) {
		
		Address address = (Address) obj;
		
		if(address.getTitle() == null || address.getTitle().equals(""))
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "title", "required", new Object[] { "firmName" },
					"error.required.field");
		if(address.getArea() == null || address.getArea().equals(""))
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "area", "required", new Object[] { "Area" },
					"error.required.field");
		if(address.getCity() == null || address.getCity().equals(""))
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "city", "required", new Object[] { "city" },
					"error.required.field");
		if(address.getCountry() == null || address.getCountry().equals(""))
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "country", "required", new Object[] { "country" },
					"error.required.field");
		if(address.getState() == null || address.getState().equals(""))
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "state", "required", new Object[] { "state" },
					"error.required.field");
		
		if(address.getPincode() == null || address.getPincode().equals(""))
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "pincode", "required", new Object[] { "pincode" },
					"error.required.field");
		if(address.getStreet() == null || address.getStreet().equals(""))
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "street", "required", new Object[] { "street" },
					"error.required.field");
		
	}

}
