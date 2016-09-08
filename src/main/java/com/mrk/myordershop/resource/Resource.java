package com.mrk.myordershop.resource;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

@JsonIgnoreProperties({ "id" })
@Relation(collectionRelation = "content")
public class Resource<T> extends ResourceSupport {

	@JsonUnwrapped
	private T resorce;

	public Resource(T resorce) {
		this.resorce = resorce;
	}

	public T getResorce() {
		return resorce;
	}
}
