package com.mrk.myordershop.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.service.UserService;

@Component
public class UserValidator implements Validator {

	@Autowired
	private UserService userService;

	@Override
	public boolean supports(Class<?> claz) {
		return User.class.isAssignableFrom(claz);
	}

	@Override
	public void validate(Object obj, Errors err) {

		User user = (User) obj;
		if (user.getId() == null) {
			if (user.getMobile() != null && !user.getMobile().equals("")) {
				try {
					User xyz = userService.getUserByMobieOrEmail(user.getMobile());
					if (xyz != null)
						err.rejectValue("mobile", "Not Acceptable", new Object[] { "mobile " }, "error.user.exists");
				} catch (EntityDoseNotExistException e1) {
				}
			} else
				err.rejectValue("mobile", "required", new Object[] { "mobile " }, "error.required.field");
		} else {
			try {
				User existedUser = userService.getUserByMobieOrEmail(user.getMobile());
				if (existedUser != null) {
					if (!existedUser.getId().equals(user.getId()))
						err.rejectValue("mobile", "Not Acceptable", new Object[] { "mobile " }, "error.user.exists");
				}
			} catch (EntityDoseNotExistException e) {
			}
		}
		if (user.getName() == null || user.getName().equals(""))
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "name", "required", new Object[] { "name " },
					"error.required.field");
		if (user.getEmail() == null || user.getEmail().equals(""))
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "email", "required", new Object[] { "email " },
					"error.required.field");

	}

}
