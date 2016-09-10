package com.mrkinnoapps.myordershopadmin.controller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.service.UserService;

@Controller("CmnUserService")
@RequestMapping("/v1/cmn/users")
public class UserController {
	@Autowired
	private UserService userService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<String> getUser(
			@RequestParam(required = false, defaultValue = "") String email,
			@RequestParam(required = false, defaultValue = "") String mobile,
			@RequestParam(required = false) String activeId) {
		try {
			User user = null;
			if (!email.equals("")) {
				user = userService.getUserByMobieOrEmail(email);
				if (user.getId().equals(activeId))
					throw new EntityDoseNotExistException();
				return new ResponseEntity<String>(HttpStatus.OK);
			} else if (!mobile.equals("")) {
				user = userService.getUserByMobieOrEmail(mobile);
				if (user.getId().equals(activeId))
					throw new EntityDoseNotExistException();
				return new ResponseEntity<String>(HttpStatus.OK);
			}
		} catch (EntityDoseNotExistException e) {
		}
		return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
	}
}
