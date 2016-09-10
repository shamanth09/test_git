package com.mrkinnoapps.myordershopadmin.util.searchengine;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.io.Serializable;

import com.mrkinnoapps.myordershopadmin.controller.admin.ProductController;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;

public class CatogorySearchResult implements SearchResult{

	private String label;
	private String field;
	private Integer resultId;
	private String result;

	public String getLabel() {

		if (this.field.equalsIgnoreCase("name")) {
			return "in Categories";
		} else
			return " ";
	}

	public void setLabel(String lebel) {
		this.label = lebel;
	}

	@Override
	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	@Override
	public String getUrl() throws EntityDoseNotExistException {
		if (this.field.equalsIgnoreCase("name")) {
			return linkTo(methodOn(ProductController.class).getProducts())
					.withSelfRel().getHref() + "#/" + this.resultId;
		} else
			return " ";
	}

	@Override
	public Serializable getResultId() {
		return resultId;
	}

	public void setResultId(Integer resultId) {
		this.resultId = resultId;
	}

	@Override
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
