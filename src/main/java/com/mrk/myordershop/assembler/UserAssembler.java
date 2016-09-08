package com.mrk.myordershop.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.mrk.myordershop.apicontroller.common.AddressController;
import com.mrk.myordershop.apicontroller.common.UserController;
import com.mrk.myordershop.assembler.linkprovider.UserLinkProvider;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.resource.Resource;

@Component
public class UserAssembler extends ResourceAssemblerSupport<User, Resource> {

	public UserAssembler() {
		super(User.class, Resource.class);
	}

	@Override
	public Resource toResource(User user) {
		Resource userResource = new Resource(user);
		try {
			Link selfLink = linkTo(
					methodOn(UserController.class).getUser(user.getId()))
					.withSelfRel();
			userResource.add(selfLink);
			if (user.getAddress() != null) {
				Link addressLink = linkTo(
						methodOn(AddressController.class).getAddress(
								user.getAddress().getId(), null)).withRel(
						"address");
				userResource.add(addressLink);
			}
		} catch (EntityDoseNotExistException e) {
			e.printStackTrace();
		}
		addImageUrl(userResource);
		return userResource;
	}

	private void addImageUrl(Resource<User> userResource) {
		User user = userResource.getResorce();
		try {
			userResource.add(UserLinkProvider.getImage(user).withRel("image"));
		} catch (EntityDoseNotExistException e) {
		}
	}

}
