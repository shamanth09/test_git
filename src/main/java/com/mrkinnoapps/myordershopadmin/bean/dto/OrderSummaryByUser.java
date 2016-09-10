package com.mrkinnoapps.myordershopadmin.bean.dto;


public class OrderSummaryByUser extends OrderSummary {

	private String name;

	private String mobile;

	private String userId;

	private long totalOrders;

	public OrderSummaryByUser() {

	}

	public OrderSummaryByUser(Class entity, String userId, long totalOrders) {
		super(entity);
		this.userId = userId;
		this.totalOrders = totalOrders;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public long getTotalOrders() {
		return totalOrders;
	}

	public void setTotalOrders(long totalOrders) {
		this.totalOrders = totalOrders;
	}

	public void setTotalOrders(String totalOrders) {
		this.totalOrders = Integer.valueOf(totalOrders);
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
