package com.mrk.myordershop.bean.dto.filter;

import java.util.Date;

public class OrderSummaryFilter {

	private String retailerId;

	private Date fromDate;

	private Date toDate;

	private Date fromExpectedDate;

	private Date toExpectedDate;

	private int categoryId;
	
	private int productId;

	private int chunkSize;

	public String getRetailerId() {
		return retailerId;
	}

	public void setRetailerId(String retailerId) {
		this.retailerId = retailerId;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public Date getFromDate(Date defaultDate) {
		return this.fromDate != null ? this.fromDate : defaultDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public Date getToDate(Date defaultDate) {
		return this.toDate != null ? this.toDate : defaultDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Date getFromExpectedDate() {
		return fromExpectedDate;
	}

	public void setFromExpectedDate(Date fromExpectedDate) {
		this.fromExpectedDate = fromExpectedDate;
	}

	public Date getToExpectedDate() {
		return toExpectedDate;
	}

	public void setToExpectedDate(Date toExpectedDate) {
		this.toExpectedDate = toExpectedDate;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getChunkSize() {
		return chunkSize;
	}

	public void setChunkSize(int chunkSize) {
		this.chunkSize = chunkSize;
	}

}
