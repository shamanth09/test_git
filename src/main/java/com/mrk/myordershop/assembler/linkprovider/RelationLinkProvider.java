package com.mrk.myordershop.assembler.linkprovider;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Link;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.mrk.myordershop.apicontroller.common.RelationController;
import com.mrk.myordershop.bean.dto.RelationFilter;
import com.mrk.myordershop.constant.Role;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

public class RelationLinkProvider {

	public static Link get(String userId) {
		try {
			return linkTo(methodOn(RelationController.class).get(userId, null))
					.withSelfRel();
		} catch (EntityDoseNotExistException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Link get(RelationFilter filter) {
		return linkTo(
				methodOn(RelationController.class).get(filter,
						filter.getName(), filter.getEmail(),
						filter.getMobile(), filter.getUserRole(),
						filter.getDirection(), null, null, null)).withSelfRel();
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
