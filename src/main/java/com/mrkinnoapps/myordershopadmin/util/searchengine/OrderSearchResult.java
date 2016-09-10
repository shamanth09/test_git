package com.mrkinnoapps.myordershopadmin.util.searchengine;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import java.io.Serializable;
import com.mrkinnoapps.myordershopadmin.controller.admin.OrderController;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;

public class OrderSearchResult implements SearchResult {

	private String label;
	private String field;
	private Integer resultId;
	private String result;

	public String getLabel() {

		if (this.field.equalsIgnoreCase("orderNo")) {
			return "in orders";
		} else if (this.field.equalsIgnoreCase("customerName")) {
			return "in customers";
		} else if (this.field.equalsIgnoreCase("customerMobile")) {
			return "in customers";
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
		if (this.field.equalsIgnoreCase("orderNo")) {
			return linkTo(methodOn(OrderController.class).getOrders(null, null))
					.withSelfRel().getHref() + "#/orders/" + this.resultId;
		} else if (this.field.equalsIgnoreCase("customerName")) {
			return linkTo(
					methodOn(OrderController.class).getOrders(null, null))
					.withSelfRel().getHref() + "#/?customerName=" + this.result;
		} else if (this.field.equalsIgnoreCase("customerMobile")) {
			return linkTo(
					methodOn(OrderController.class).getOrders(null, null))
					.withSelfRel().getHref() + "#/?customerMobile=" + this.result;
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
