package com.mrk.myordershop.assembler.linkprovider;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.apache.log4j.Logger;
import org.springframework.hateoas.Link;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.mrk.myordershop.apicontroller.wsaler.OrderController;
import com.mrk.myordershop.bean.dto.OrderFilter;
import com.mrk.myordershop.bean.dto.RateCutSummaryFileter;
import com.mrk.myordershop.constant.Role;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

public class OrderSummaryLinkProvider {
	private static Logger log = Logger.getLogger(OrderSummaryLinkProvider.class
			.getName());

	public static Link get(RateCutSummaryFileter filter) {
		return get(filter, getRole());
	}

	public static Link get(RateCutSummaryFileter filter, Role role) {
		switch (role) {
		case ROLE_WSALER:
			return linkTo(
					methodOn(
							com.mrk.myordershop.apicontroller.wsaler.OrderSummaryController.class)
							.getOrderRateCutSummary(null, null, null, filter,
									filter.getFromRate(), filter.getToRate(), filter.getOrderStatus()))
					.withSelfRel();
		default:
			break;
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

	public static Link get(OrderFilter of) throws EntityDoseNotExistException {
		switch (getRole()) {
		case ROLE_WSALER:
			return linkTo(
					methodOn(OrderController.class).getOrders(
							null,
							null,
							null,
							null,
							of.getOrderStatus(),
							of.getProductName(),
							of.getRetailerId() == null ? of.getCustomerName()
									: null,
							of.getRetailerId() == null ? of.getCustomerMobile()
									: null, of.getRetailerId(),
							of.getCategoryId(), of.getFromDate(),
							of.getToDate(), of.getFromExpectedDate(),
							of.getToExpectedDate())).withSelfRel();
		case ROLE_RETAIL:
			return linkTo(
					methodOn(
							com.mrk.myordershop.apicontroller.retail.OrderController.class)
							.getOrders(null, null, null, null,
									of.getOrderStatus(), of.getProductName(),
									of.getCustomerName(), of.getRetailerId(),
									of.getCategoryId(), of.getFromDate(),
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
