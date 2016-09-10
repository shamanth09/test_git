package com.mrkinnoapps.myordershopadmin.bean.dto;

import com.fasterxml.jackson.annotation.JsonView;

public class CustomerSummary {

	@JsonView(View.CustomerSummary.class)
	private OrderStatusSummary summary;

	public OrderStatusSummary getSummary() {
		return summary;
	}

	public void setSummary(OrderStatusSummary summary) {
		this.summary = summary;
	}

}
