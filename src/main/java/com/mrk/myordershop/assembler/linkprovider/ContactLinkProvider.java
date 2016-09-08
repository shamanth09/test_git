package com.mrk.myordershop.assembler.linkprovider;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.mrk.myordershop.apicontroller.wsaler.ContactController;
import com.mrk.myordershop.bean.dto.filter.ContactFilter;
import com.mrk.myordershop.constant.Role;

public class ContactLinkProvider {

	public static Link get(ContactFilter filter) {
		switch (getRole()) {
		case ROLE_WSALER:
			return ControllerLinkBuilder.linkTo(
					ControllerLinkBuilder.methodOn(ContactController.class)
							.get(filter.getName(), filter.getEmail(),
									filter.getMobile(), filter.getFirmName(),
									filter, null, null, null)).withSelfRel();
		default:
			return null;

		}
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
