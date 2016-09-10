package com.mrkinnoapps.myordershopadmin.bean.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class OrderSummary {

	@JsonIgnore
	private Class entity;

	public OrderSummary(Class entity) {
		super();
		this.entity = entity;
	}

	public OrderSummary() {
	}

	public Class getEntity() {
		return entity;
	}

	public void setEntity(Class entity) {
		this.entity = entity;
	}

}
