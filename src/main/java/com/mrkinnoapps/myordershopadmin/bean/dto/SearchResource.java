package com.mrkinnoapps.myordershopadmin.bean.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mrkinnoapps.myordershopadmin.bean.dto.SearchFilter.SearchIn;

public class SearchResource {

	@JsonIgnore
	private Class entity;
	private String resultId;
	private String result;
	private SearchIn field;
	private String lable;

	public SearchResource(String result, SearchIn field, String lable,
			Class entity) {
		this.result = result;
		this.field = field;
		this.lable = lable;
		this.entity = entity;
	}

	public String getResultId() {
		return resultId;
	}

	public void setResultId(String resultId) {
		this.resultId = resultId;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public SearchIn getField() {
		return field;
	}

	public void setField(SearchIn field) {
		this.field = field;
	}

	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	public Class getEntity() {
		return entity;
	}

	public void setEntity(Class entity) {
		this.entity = entity;
	}

}
