package com.mrk.myordershop.bean.dto;

import java.util.Date;

import com.mrk.myordershop.constant.OrderStatus;

public class WholesalerOrderFilter {

	private OrderStatus orderStatus;

	private String productName;

	private String supplierId;
	
	private String wholesalerName;
	
	private String supplierrName;

	private Date fromDate;

	private Date toDate;

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	public String getWholesalerName() {
		return wholesalerName;
	}

	public void setWholesalerName(String wholesalerName) {
		this.wholesalerName = wholesalerName;
	}

	public String getSupplierrName() {
		return supplierrName;
	}

	public void setSupplierrName(String supplierrName) {
		this.supplierrName = supplierrName;
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

	public Date getCreateDate() {
		return this.fromDate;
	}

	public void setCreateDate(Date createDate) {
		this.fromDate = createDate;
		this.toDate = createDate;
	}
}
