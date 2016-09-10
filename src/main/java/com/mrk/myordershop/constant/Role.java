package com.mrk.myordershop.constant;

import java.util.ArrayList;
import java.util.List;

public enum Role {
	ROLE_RETAIL("Retailer"), ROLE_WSALER("Wholesaler"), ROLE_SUPPLIER(
			"Supplier");

	private String value;

	private Role(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public static Role[] find(String query) {
		List<Role> oroles = new ArrayList<Role>();
		for (Role role : values()) {
			if (role.getValue().toLowerCase()
					.matches(query.toLowerCase() + ".*")) {
				oroles.add(role);
			}
		}
		return oroles.toArray(new Role[] {});
	}
}
