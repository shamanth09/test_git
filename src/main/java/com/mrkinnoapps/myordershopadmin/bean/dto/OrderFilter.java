package com.mrkinnoapps.myordershopadmin.bean.dto;

import java.util.Date;
import java.util.List;

import com.mrkinnoapps.myordershopadmin.bean.constant.OrderStatus;

public class OrderFilter {

	private List<OrderStatus> orderStatus;
	
	private String[] userName;
	
	private String[] productName;

	private String[] customerName;

	private String[] customerMobile;

	private String wholesalerName;

	private String retailerId;

	private Date fromDate;


	private Date toDate;

	private Date fromExpectedDate;

	private Date toExpectedDate;

	private Integer categoryId;

	private Integer productId;
	
	public String[] getUserName() {
		return userName;
	}
	
	public void setUserName(String[] userName) {
		this.userName = userName;
	}
	
	public String[] getProductName() {
		return productName;
	}
	
	public void setProductName(String[] productName) {
		this.productName = productName;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public List<OrderStatus> getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(List<OrderStatus> orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String[] getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String[] customerName) {
		this.customerName = customerName;
	}

	public String[] getCustomerMobile() {
		return customerMobile;
	}

	public void setCustomerMobile(String[] customerMobile) {
		this.customerMobile = customerMobile;
	}

	public String getWholesalerName() {
		return wholesalerName;
	}

	public void setWholesalerName(String wholesalerName) {
		this.wholesalerName = wholesalerName;
	}

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

	public Date getFromExpectedDate() {
		return fromExpectedDate;
	}

	public Date getFromExpectedDate(Date defaultDate) {
		return this.fromExpectedDate != null ? this.fromExpectedDate
				: defaultDate;
	}

	public void setFromExpectedDate(Date fromExpectedDate) {
		this.fromExpectedDate = fromExpectedDate;
	}

	public Date getToExpectedDate() {
		return toExpectedDate;
	}

	public Date getToExpectedDate(Date defaultDate) {
		return this.toExpectedDate != null ? this.toExpectedDate : defaultDate;
	}

	public void setToExpectedDate(Date toExpectedDate) {
		this.toExpectedDate = toExpectedDate;
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

	public void setExpectedDate(Date expectedDate) {
		this.fromExpectedDate = expectedDate;
		this.toExpectedDate = expectedDate;
	}

	public void setCreateDate(Date createDate) {
		this.fromDate = createDate;
		this.toDate = createDate;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

}
