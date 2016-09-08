package com.mrk.myordershop.apicontroller.general;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mrk.myordershop.bean.Register;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.exception.EntityNotPersistedException;
import com.mrk.myordershop.service.UserService;
import com.mrk.myordershop.validator.RegisterValidator;

import io.swagger.annotations.Api;

@Controller("apiRegistrationController")
@RequestMapping("/api")
@Api(value = "Registration", tags = { "User Registration" })
public class RegistrationController {

	@Autowired
	private UserService userService;
	@Autowired
	private RegisterValidator registerValidator;
	
	@InitBinder(value = { "register" })
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(registerValidator);
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity register(@Validated @RequestBody Register register)
			throws EntityNotPersistedException {
		User user = userService.register(register);
		Map<String, String> userMap = new HashMap<String, String>();
		userMap.put("userId", user.getId());
		return new ResponseEntity(userMap, HttpStatus.OK);
	}

}
