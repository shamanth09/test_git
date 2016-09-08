package com.mrk.myordershop.apicontroller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mrk.myordershop.assembler.AddressAssembler;
import com.mrk.myordershop.bean.Address;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.security.oauth.resolver.Owner;
import com.mrk.myordershop.service.AddressService;
import com.mrk.myordershop.validator.AddressValidator;

import io.swagger.annotations.Api;

@Controller
@RequestMapping("/api/v1/addresses")
@Api(value="User Details", tags={"User Details"}, description="Address Details")
public class AddressController {

	@Autowired
	private AddressService addressService;

	@Autowired
	private AddressAssembler addressAssembler;
	
	@Autowired
	private AddressValidator addressValidator;
	
	@InitBinder(value = { "address" })
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(addressValidator);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/{addressId}", method = RequestMethod.GET)
	public ResponseEntity getAddress(@PathVariable int addressId,
			@Owner User user) throws EntityDoseNotExistException {
		Address address = addressService.getAddress(addressId, user);
		return new ResponseEntity(addressAssembler.toResource(address),
				HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/{addressId}", method = RequestMethod.PUT)
	public ResponseEntity updateAddress(@PathVariable int addressId,
			@Validated @RequestBody Address address, @Owner User user)
			throws EntityDoseNotExistException {
		addressService.updateAddress(addressId, address, user);
		return new ResponseEntity(HttpStatus.OK);
	}
}
