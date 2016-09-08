package com.mrk.myordershop.bean.dto;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonView;

@Relation(collectionRelation = "content")
public class CustomerSummary extends ResourceSupport {

	@JsonView(View.Status.class)
	private OrderStatusSummary summary;

	public OrderStatusSummary getSummary() {
		return summary;
	}

	public void setSummary(OrderStatusSummary summary) {
		this.summary = summary;
	}

}
