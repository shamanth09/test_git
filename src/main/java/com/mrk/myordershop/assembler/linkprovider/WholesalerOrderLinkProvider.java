package com.mrk.myordershop.assembler.linkprovider;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.apache.log4j.Logger;
import org.springframework.hateoas.Link;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.mrk.myordershop.apicontroller.wsaler.WholesalerOrderController;
import com.mrk.myordershop.bean.dto.WholesalerOrderFilter;
import com.mrk.myordershop.constant.Role;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

public class WholesalerOrderLinkProvider {
	private static Logger log = Logger.getLogger(WholesalerOrderLinkProvider.class
			.getName());

	public static Link get(int orderId) {
		return get(orderId, getRole());
	}

	public static Link get(int orderId, Role role) {
		try {
			switch (role) {

			case ROLE_WSALER:
				return linkTo(
						methodOn(WholesalerOrderController.class).getOrder(
								orderId, null)).withSelfRel();

			case ROLE_SUPPLIER:
				return linkTo(
						methodOn(
								com.mrk.myordershop.apicontroller.supplier.OrderController.class)
								.getOrder(orderId, null)).withSelfRel();
			default:
				break;
			}
		} catch (EntityDoseNotExistException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Link get(WholesalerOrderFilter of) {
		switch (getRole()) {
		case ROLE_WSALER:
			return linkTo(
					methodOn(WholesalerOrderController.class).getOrders(null,
							null, null, of.getOrderStatus(),
							of.getProductName(), of.getSupplierId(),
							of.getFromDate(), of.getToDate(), null))
					.withSelfRel();
		case ROLE_SUPPLIER:
			return linkTo(
					methodOn(
							com.mrk.myordershop.apicontroller.supplier.OrderController.class)
							.getOrders(null, null, null, null,
									of.getOrderStatus(), of.getProductName(),
									of.getSupplierId(), of.getFromDate(),
									of.getToDate())).withSelfRel();
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
