package com.mrk.myordershop.assembler.linkprovider;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Link;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.mrk.myordershop.constant.Role;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

public class ItemLinkProvider {

	public static Link get(int itemId) throws EntityDoseNotExistException {
		switch (getRole()) {
		case ROLE_RETAIL:
			return linkTo(
					methodOn(
							com.mrk.myordershop.apicontroller.retail.ItemController.class)
							.getItem(itemId)).withSelfRel();
		case ROLE_WSALER:
			return linkTo(
					methodOn(
							com.mrk.myordershop.apicontroller.wsaler.ItemController.class)
							.getItem(itemId)).withSelfRel();
		case ROLE_SUPPLIER:
			return linkTo(
					methodOn(
							com.mrk.myordershop.apicontroller.supplier.ItemController.class)
							.getItem(itemId)).withSelfRel();
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
