package com.mrk.myordershop.apicontroller.general;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mrk.myordershop.bean.Token;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;
import com.mrk.myordershop.exception.InvalidDataException;
import com.mrk.myordershop.service.UserService;

import io.swagger.annotations.Api;

@Controller("generalUserController")
@RequestMapping(value = "/api/users")
@Api(value="User Details", tags={"User Details"}, description="User Details public")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<String> getUser(
			@RequestParam(required = false, defaultValue = "") String email,
			@RequestParam(required = false, defaultValue = "") String mobile)
			throws EntityDoseNotExistException {
		User user = null;
		if (!email.equals("")) {
			user = userService.getUserByMobieOrEmail(email);
			return new ResponseEntity<String>(HttpStatus.OK);
		} else if (!mobile.equals("")) {
			user = userService.getUserByMobieOrEmail(mobile);
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/activate/{token}", method = RequestMethod.GET)
	public ResponseEntity activate(@PathVariable String token)
			throws EntityNotPersistedException, InvalidDataException,
			EntityDoseNotExistException {
		userService.activateUser(token);
		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(value = "/frgtpswd", method = RequestMethod.POST)
	public ResponseEntity forgotPassword(@RequestParam String email,
			@RequestParam String actionUrl) throws EntityNotPersistedException,
			InvalidDataException, EntityDoseNotExistException {

		User user = userService.forgotPassword(email, actionUrl);
		Map<String, String> userMap = new HashMap<String, String>();
		userMap.put("userId", user.getId());
		return new ResponseEntity(userMap, HttpStatus.OK);
	}

	@RequestMapping(value = "/chngpswd", method = RequestMethod.POST)
	public ResponseEntity changePassword(@RequestParam String password,
			@RequestParam String token) throws EntityNotPersistedException,
			InvalidDataException, EntityDoseNotExistException {

		User user = userService.changePassword(password, token);
		Map<String, String> userMap = new HashMap<String, String>();
		userMap.put("userId", user.getId());
		return new ResponseEntity(userMap, HttpStatus.OK);
	}

	@RequestMapping(value = "/token/resend", method = RequestMethod.POST)
	public ResponseEntity resentToken(@RequestParam String userId,
			@RequestParam String actionUrl, @RequestParam String type)
			throws EntityNotPersistedException, InvalidDataException,
			EntityDoseNotExistException {

		userService.resentToken(userId, Token.Type.valueOf(type), actionUrl);
		return new ResponseEntity(HttpStatus.OK);
	}

}
