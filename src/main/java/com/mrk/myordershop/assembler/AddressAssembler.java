package com.mrk.myordershop.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.mrk.myordershop.apicontroller.common.AddressController;
import com.mrk.myordershop.apicontroller.common.UserController;
import com.mrk.myordershop.bean.Address;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.resource.Resource;

@Component
public class AddressAssembler extends
		ResourceAssemblerSupport<Address, Resource> {

	public AddressAssembler() {
		super(Address.class, Resource.class);
	}

	@Override
	public Resource toResource(Address address) {
		Resource resource = new Resource(address);
		try {
			Link self = linkTo(
					methodOn(AddressController.class).getAddress(
							address.getId(), null)).withSelfRel();
			resource.add(self);

			Link userLink = linkTo(
					methodOn(UserController.class).getUser(
							address.getUser().getId())).withRel("user");
			resource.add(userLink);
		} catch (EntityDoseNotExistException e) {
			e.printStackTrace();
		}

		return resource;
	}

}
