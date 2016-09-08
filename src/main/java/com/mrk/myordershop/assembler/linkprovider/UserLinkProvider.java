package com.mrk.myordershop.assembler.linkprovider;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Link;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.mrk.myordershop.apicontroller.common.AddressController;
import com.mrk.myordershop.apicontroller.wsaler.UserController;
import com.mrk.myordershop.bean.Image;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.dto.UserFilter;
import com.mrk.myordershop.constant.Role;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

public class UserLinkProvider {

	public static Link get(String userId) {
		try {
			return linkTo(
					methodOn(
							com.mrk.myordershop.apicontroller.common.UserController.class)
							.getUser(userId)).withSelfRel();
		} catch (EntityDoseNotExistException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Link getImage(User user) throws EntityDoseNotExistException {
		Link imageLink = null;
		if (user.getImage() != null) {
			imageLink = linkTo(
					methodOn(
							com.mrk.myordershop.apicontroller.common.UserController.class)
							.getUserImage(user.getId())).withSelfRel();
		} else {
			throw new EntityDoseNotExistException(Image.class);
		}
		return imageLink;
	}

	public static Link getAddress(int addressId) {
		Link addressLink = null;
		try {
			addressLink = linkTo(
					methodOn(AddressController.class).getAddress(addressId,
							null)).withSelfRel();
		} catch (EntityDoseNotExistException e) {
			e.printStackTrace();
		}
		return addressLink;
	}

	public static Link get(UserFilter filter)
			throws EntityDoseNotExistException {
		switch (getRole()) {
		case ROLE_WSALER:
			return linkTo(
					methodOn(UserController.class).findUser(filter, null, null,
							null, filter.getName(), filter.getEmail(),
							filter.getMobile(), filter.getRole()))
					.withSelfRel();
		case ROLE_RETAIL:
			return linkTo(
					methodOn(
							com.mrk.myordershop.apicontroller.retail.UserController.class)
							.findUser(filter, null, null, null,
									filter.getName(), filter.getEmail(),
									filter.getMobile())).withSelfRel();
		case ROLE_SUPPLIER:
			return linkTo(
					methodOn(
							com.mrk.myordershop.apicontroller.supplier.UserController.class)
							.findUser(filter, null, null, null,
									filter.getName(), filter.getEmail(),
									filter.getMobile())).withSelfRel();

		default:
			break;
		}
		return null;
	}

	private static Role getRole() {
		Role role = null;
		for (GrantedAuthority au : SecurityContextHolder.getContext()
				.getAuthentication().getAuthorities()) {
			role = Role.valueOf(au.getAuthority());
		}
		return role;
	}
}
