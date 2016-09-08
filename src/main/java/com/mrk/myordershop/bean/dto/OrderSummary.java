package com.mrk.myordershop.bean.dto;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Relation(collectionRelation = "content")
public class OrderSummary extends ResourceSupport {

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
