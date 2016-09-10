package com.mrkinnoapps.myordershopadmin.util.searchengine;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.io.Serializable;

import com.mrkinnoapps.myordershopadmin.controller.admin.UserController;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;

public class UserSearchResult implements SearchResult {

	private String label;
	private String field;
	private Serializable resultId;
	private String result;

	public UserSearchResult() {
		// TODO Auto-generated constructor stub
	}
	
	public void setLabel(String label) {
		this.label = label;
	}

	public void setField(String field) {
		this.field = field;
	}

	public void setResultId(String resultId) {
		this.resultId = resultId;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public String getField() {
		return field;
	}

	@Override
	public String getUrl() throws EntityDoseNotExistException {
		if (this.field.equalsIgnoreCase("email") || this.field.equalsIgnoreCase("mobile")) {
			return linkTo(methodOn(UserController.class).getUser()).withSelfRel().getHref()+"#/users/"+this.resultId;
		}
		else if(this.field.equalsIgnoreCase("userName")){
			return linkTo(methodOn(UserController.class).getUser()).withSelfRel().getHref()+"#/?userName="+this.result;
		}
		else
			return " ";
	}

	@Override
	public Serializable getResultId() {
		return resultId;
	}

	@Override
	public String getLabel() {
		if (this.field.equalsIgnoreCase("userName")) {
			return "in User Name";
		} else if (this.field.equalsIgnoreCase("mobile")) {
			return "in User Mobile";
		} else if (this.field.equalsIgnoreCase("email")) {
			return "in User Email";
		} else
			return " ";
	}

	@Override
	public String getResult() {
		return result;
	}

}
