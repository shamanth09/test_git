package com.mrk.myordershop.assembler.linkprovider;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.apache.log4j.Logger;
import org.springframework.hateoas.Link;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.mrk.myordershop.apicontroller.wsaler.OrderController;
import com.mrk.myordershop.bean.dto.OrderFilter;
import com.mrk.myordershop.constant.Role;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

public class OrderLinkProvider {
	private static Logger log = Logger.getLogger(OrderLinkProvider.class
			.getName());

	public static Link get(int orderId) {
		return get(orderId, getRole());
	}

	public static Link get(int orderId, Role role) {
		try {
			switch (role) {
			case ROLE_RETAIL:
				return linkTo(
						methodOn(
								com.mrk.myordershop.apicontroller.retail.OrderController.class)
								.getOrder(orderId, null)).withSelfRel();
			case ROLE_WSALER:
				return linkTo(
						methodOn(
								com.mrk.myordershop.apicontroller.wsaler.OrderController.class)
								.getOrder(orderId, null)).withSelfRel();
			default:
				break;
			}
		} catch (EntityDoseNotExistException e) {
			e.printStackTrace();
		}
		return null;
	}

	// public static Link getByOrderNo(String orderNo) {
	// log.debug("Current User Role=" + getRole());
	// try {
	// switch (getRole()) {
	// case ROLE_RETAIL:
	// return linkTo(
	// methodOn(
	// com.mrk.myordershop.apicontroller.retail.OrderController.class)
	// .getOrder(orderNo)).withSelfRel();
	// case ROLE_WSALER:
	// return linkTo(methodOn(OrderController.class).getOrder(orderNo))
	// .withSelfRel();
	// default:
	// break;
	// }
	// } catch (EntityDoseNotExistException e) {
	// e.printStackTrace();
	// }
	// return null;
	// }

	public static Link get(OrderFilter f) throws EntityDoseNotExistException {
		switch (getRole()) {
		case ROLE_WSALER:
			return linkTo(
					methodOn(OrderController.class).getOrders(
							null,
							null,
							null,
							null,
							f.getOrderStatus(),
							f.getProductName(),
							f.getRetailerId() == null ? f.getCustomerName()
									: null,
							f.getRetailerId() == null ? f.getCustomerMobile()
									: null, f.getRetailerId(),
							f.getCategoryId(), f.getFromDate(),
							f.getToDate(), f.getFromExpectedDate(),
							f.getToExpectedDate())).withSelfRel();
		case ROLE_RETAIL:
			return linkTo(
					methodOn(
							com.mrk.myordershop.apicontroller.retail.OrderController.class)
							.getOrders(null, null, null, null,
									f.getOrderStatus(), f.getProductName(),
									f.getCustomerName(), f.getRetailerId(),
									f.getCategoryId(), f.getFromDate(),
									f.getToDate())).withSelfRel();
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
