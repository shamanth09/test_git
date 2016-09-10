package com.mrkinnoapps.myordershopadmin.bean.dto;


import org.springframework.data.domain.Page;

import com.mrkinnoapps.myordershopadmin.bean.entity.Relation;

public class RelationSummary {
	
	
	private Integer totalSupplierOrRequest;
	private Integer totalRetailerOrAccepted;
	private Integer totalWholesalerOrRejected;
	
	private Integer request;
	private Integer accepted;
	private Integer rejected;
	
	private Page<Relation> retailers;
	private Page<Relation> suppliers;
	private Page<Relation> wholesalers;
	
	public Page<Relation> getRetailers() {
		return retailers;
	}
	public Page<Relation> getSuppliers() {
		return suppliers;
	}
	public Page<Relation> getWholesalers() {
		return wholesalers;
	}
	public void setRetailers(Page<Relation> retailers) {
		this.retailers = retailers;
	}
	public void setSuppliers(Page<Relation> suppliers) {
		this.suppliers = suppliers;
	}
	public void setWholesalers(Page<Relation> page) {
		this.wholesalers = page;
	}
	public Integer getTotalSupplierOrRequest() {
		return totalSupplierOrRequest;
	}
	public Integer getTotalRetailerOrAccepted() {
		return totalRetailerOrAccepted;
	}
	public Integer getTotalWholesalerOrRejected() {
		return totalWholesalerOrRejected;
	}
	public void setTotalSupplierOrRequest(Integer totalSupplierOrRequest) {
		this.totalSupplierOrRequest = totalSupplierOrRequest;
	}
	public void setTotalRetailerOrAccepted(Integer totalRetailerOrAccepted) {
		this.totalRetailerOrAccepted = totalRetailerOrAccepted;
	}
	public void setTotalWholesalerOrRejected(Integer totalWholesalerOrRejected) {
		this.totalWholesalerOrRejected = totalWholesalerOrRejected;
	}
	public Integer getRequest() {
		return request;
	}
	public Integer getAccepted() {
		return accepted;
	}
	public Integer getRejected() {
		return rejected;
	}
	public void setRequest(Integer request) {
		this.request = request;
	}
	public void setAccepted(Integer accepted) {
		this.accepted = accepted;
	}
	public void setRejected(Integer rejected) {
		this.rejected = rejected;
	}
	
}
