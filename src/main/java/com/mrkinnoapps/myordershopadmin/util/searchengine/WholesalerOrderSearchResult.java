/**
 * 
 */
package com.mrkinnoapps.myordershopadmin.util.searchengine;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.io.Serializable;

import com.mrkinnoapps.myordershopadmin.controller.admin.OrderController;
import com.mrkinnoapps.myordershopadmin.controller.admin.WholesalerOrderController;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;

/**
 * @author Mallinath May 31, 2016  
 */
public class WholesalerOrderSearchResult implements SearchResult{
	private String label;
	private String field;
	private Serializable resultId;
	private String result;

	public String getLabel() {

		if (this.field.equalsIgnoreCase("orderNo")) {
			return "in wholesalers";
		} else if (this.field.equalsIgnoreCase("supplierName")) {
			return "in wholesalers";
		} else if (this.field.equalsIgnoreCase("supplierMobileNo")) {
			return "in wholesalers";
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
			return linkTo(methodOn(WholesalerOrderController.class).getWholsalerOrders(null, null))
					.withSelfRel().getHref() + "#/?orderNo=" + this.result;
		} else if (this.field.equalsIgnoreCase("supplierName")) {
			return linkTo(
					methodOn(WholesalerOrderController.class).getWholsalerOrders(null, null))
					.withSelfRel().getHref() + "#/?supplierName=" + this.result;
		} else if (this.field.equalsIgnoreCase("supplierMobileNo")) {
			return linkTo(
					methodOn(WholesalerOrderController.class).getWholsalerOrders(null, null))
					.withSelfRel().getHref() + "#/?supplierMobileNo=" + this.result;
		} else
			return " ";
	}

	@Override
	public Serializable getResultId() {
		return resultId;
	}

	public void setResultId(Serializable resultId) {
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
