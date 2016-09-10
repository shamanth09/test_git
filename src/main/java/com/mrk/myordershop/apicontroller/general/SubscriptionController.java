package com.mrk.myordershop.apicontroller.general;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mrk.myordershop.bean.Subscription;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;
import com.mrk.myordershop.exception.InvalidDataException;
import com.mrk.myordershop.service.SubscriptionService;

import io.swagger.annotations.Api;

@Controller("generalSubscriptionController")
@RequestMapping(value = "/api/subscription")
@Api(value="Subscription", tags={"User Subscription"})
public class SubscriptionController {

	@Autowired
	private SubscriptionService subscriptionService;

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity forgotPassword(@RequestParam String email)
			throws EntityNotPersistedException, InvalidDataException,
			EntityDoseNotExistException {
		Subscription subscription = new Subscription();
		subscription.setEmail(email);
		subscriptionService.subscrip(subscription);
		return new ResponseEntity(HttpStatus.OK);
	}

}
