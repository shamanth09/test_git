package com.mrk.myordershop.constant;

import java.util.ArrayList;
import java.util.List;

public enum OrderStatus {

	ACTIVE("Active"), REJECTED("Rejected"), APPROVED("Approved"), IN_PROGRESS(
			"In Progress"), AVAILABLE("Available"), DELIVERED("Delivered"), DISPATCHED(
			"Dispatched"),RECEIVED(
					"Received"), CANCELLED("Cancelled");
	private String value;

	private OrderStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public boolean isRetailerOrderStatus() {
		for (OrderStatus status : getRetailerStatuses()) {
			if (this.equals(status)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isWholesalerOrderStatus() {
		for (OrderStatus status : getWholesalerStatuses()) {
			if (this.equals(status)) {
				return true;
			}
		}
		return false;
	}

	public static OrderStatus[] getRetailerStatuses() {
		return new OrderStatus[] { ACTIVE, REJECTED, APPROVED, IN_PROGRESS,
				AVAILABLE, DELIVERED, CANCELLED };
	}

	public static OrderStatus[] getWholesalerStatuses() {
		return new OrderStatus[] { ACTIVE, REJECTED, IN_PROGRESS, DISPATCHED,
				CANCELLED, RECEIVED };
	}

	public static OrderStatus[] find(String query) {
		List<OrderStatus> ostatus = new ArrayList<OrderStatus>();
		for (OrderStatus status : values()) {
			if (status.getValue().toLowerCase()
					.matches(query.toLowerCase() + ".*")) {
				ostatus.add(status);
			}
		}
		return ostatus.toArray(new OrderStatus[] {});
	}
}
