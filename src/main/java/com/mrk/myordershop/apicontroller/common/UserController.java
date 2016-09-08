package com.mrk.myordershop.apicontroller.common;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.mrk.myordershop.assembler.UserAssembler;
import com.mrk.myordershop.bean.Image;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.dto.UserCredential;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.InvalidDataException;
import com.mrk.myordershop.resource.Resource;
import com.mrk.myordershop.security.oauth.resolver.Owner;
import com.mrk.myordershop.security.oauth.service.OauthUserDetailsService;
import com.mrk.myordershop.service.UserService;
import com.mrk.myordershop.validator.UserValidator;

import io.swagger.annotations.Api;

@Controller
@RequestMapping(value = "/api/v1/")
@Api(value = "User Details", tags = { "User Details" }, description = "User Details")
public class UserController {
	private static final Logger log = Logger.getLogger(UserController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private OauthUserDetailsService userDetailsService;
	@Autowired
	private UserAssembler userAssembler;
	@Autowired
	private UserValidator userValidator;
	
	@InitBinder(value = { "user" })
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(userValidator);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/me", method = RequestMethod.GET)
	public ResponseEntity<Resource> getMe(@Owner User user) {
		log.debug("get current user data" + user);
		return new ResponseEntity<Resource>(userAssembler.toResource(user), HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
	public ResponseEntity<Resource> getUser(@PathVariable String userId) throws EntityDoseNotExistException {
		User user = userService.get(userId);
		return new ResponseEntity<Resource>(userAssembler.toResource(user), HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/users", method = RequestMethod.PUT)
	public ResponseEntity<Resource> updateUser(@Owner User currentuser,
			@Validated @RequestBody User user)
			throws EntityDoseNotExistException {
		user = userService.update(currentuser.getId(), user);
		userDetailsService.updateCurrentDomainUser(user);
		return new ResponseEntity<Resource>(userAssembler.toResource(user), HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/users/images", method = RequestMethod.POST)
	public ResponseEntity<Resource> updateUserImage(@Owner User user, MultipartFile file)
			throws EntityDoseNotExistException {
		try {
			Image image = new Image(file.getBytes());
			user = userService.updateUserImage(user.getId(), image);
			userDetailsService.updateCurrentDomainUser(user);
		} catch (IOException e) {
			return new ResponseEntity<Resource>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Resource>(userAssembler.toResource(user), HttpStatus.OK);
	}

	@RequestMapping(value = "/users/{userId}/images", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getUserImage(@PathVariable String userId) throws EntityDoseNotExistException {
		User user = userService.get(userId);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		if (user.getImage() != null)
			return new ResponseEntity<byte[]>(user.getImage().getImageArray(), headers, HttpStatus.OK);
		return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/passwords", method = RequestMethod.PUT)
	public ResponseEntity updatePassword(@RequestBody UserCredential credential, @Owner User user)
			throws InvalidDataException {
		userService.changePassword(credential, user);
		return new ResponseEntity(HttpStatus.OK);
	}
}
