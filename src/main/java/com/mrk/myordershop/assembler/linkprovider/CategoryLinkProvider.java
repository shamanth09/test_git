package com.mrk.myordershop.assembler.linkprovider;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Link;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.mrk.myordershop.apicontroller.common.CategoryController;
import com.mrk.myordershop.constant.Role;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

public class CategoryLinkProvider {

	public static Link getImage(int categoryId)
			throws EntityDoseNotExistException {
		return linkTo(
				methodOn(CategoryController.class).getCategoryImage(categoryId))
				.withSelfRel();
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
