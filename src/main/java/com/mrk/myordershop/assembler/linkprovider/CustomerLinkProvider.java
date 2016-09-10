package com.mrk.myordershop.assembler.linkprovider;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.mrk.myordershop.apicontroller.wsaler.CustomerController;
import com.mrk.myordershop.bean.dto.CustomerFilter;
import com.mrk.myordershop.constant.Role;

public class CustomerLinkProvider {

	public static Link find(CustomerFilter filter) {
		switch (getRole()) {
		case ROLE_WSALER:
			return ControllerLinkBuilder.linkTo(
					ControllerLinkBuilder.methodOn(CustomerController.class)
							.getSummary(null, filter, filter.getEmail(),
									filter.getMobile(), filter.getName(), null,
									null)).withSelfRel();
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
