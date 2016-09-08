package com.mrk.myordershop.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.mrk.myordershop.bean.Register;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.service.UserService;

@Component
public class RegisterValidator implements Validator {

	@Autowired
	private UserService userService;

	@Override
	public boolean supports(Class<?> claz) {
		return Register.class.isAssignableFrom(claz);
	}

	@Override
	public void validate(Object obj, Errors err) {

		Register reg = (Register) obj;
		User gotUser = null;
		if (reg != null) {
			try {
				gotUser = userService.getUserByMobieOrEmail(reg.getMobile());
			} catch (EntityDoseNotExistException e) {
//				err.rejectValue("mobile", "required", new Object[] { "mobile " }, "error.required.field");
			}

			if (gotUser != null) {
				err.rejectValue("mobile", "Not Acceptable", new Object[] { "mobile " }, "error.user.exists");
			}

			if (reg.getName() == null || reg.getName().equals(""))
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "name", "required", new Object[] { "name " },
						"error.required.field");
			if (reg.getEmail() == null || reg.getEmail().equals(""))
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "email", "required", new Object[] { "email " },
						"error.required.field");
			if (reg.getPassword() == null || reg.getPassword().equals(""))
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "password", "required", new Object[] { "password " },
						"error.required.field");
			if(reg.getFirmName() == null || reg.getFirmName().equals(""))
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "firmName", "required", new Object[] { "Firm Name " },
						"error.required.field");

		}
	}

}
