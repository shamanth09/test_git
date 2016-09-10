package com.mrk.myordershop.assembler.linkprovider;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.apache.log4j.Logger;
import org.springframework.hateoas.Link;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.mrk.myordershop.apicontroller.common.ProductController;
import com.mrk.myordershop.bean.dto.ProductFilter;
import com.mrk.myordershop.constant.Role;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

public class ProductLinkProvider {
	private static Logger log = Logger.getLogger(ProductLinkProvider.class
			.getName());

	public static Link get(int productId) {
		try {
			return linkTo(
					methodOn(ProductController.class).getProducts(productId,
							null)).withSelfRel();
		} catch (EntityDoseNotExistException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Link getImage(int productId) {
		try {
			return linkTo(
					methodOn(ProductController.class).getImage(
							productId)).withSelfRel();
		} catch (EntityDoseNotExistException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Link get(ProductFilter of) {
		switch (getRole()) {
		case ROLE_WSALER:
			return linkTo(
					methodOn(ProductController.class).getProdects(of,
							of.getCategoryId(), of.getCategoryName(),
							of.getQuery(), null, null)).withSelfRel();
		case ROLE_RETAIL:
			return linkTo(
					methodOn(ProductController.class).getProdects(of,
							of.getCategoryId(), of.getCategoryName(),
							of.getQuery(), null, null)).withSelfRel();
		case ROLE_SUPPLIER:
			return linkTo(
					methodOn(ProductController.class).getProdects(of,
							of.getCategoryId(), of.getCategoryName(),
							of.getQuery(), null, null)).withSelfRel();
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
