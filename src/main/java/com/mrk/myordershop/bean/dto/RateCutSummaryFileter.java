package com.mrk.myordershop.bean.dto;

import java.util.List;

import com.mrk.myordershop.constant.OrderStatus;

public class RateCutSummaryFileter {

	private Double fromRate;

	private Double toRate;
	
	private List<OrderStatus> orderStatus;

	public Double getFromRate() {
		return fromRate;
	}

	public void setFromRate(Double fromRate) {
		this.fromRate = fromRate;
	}

	public Double getToRate() {
		return toRate;
	}

	public void setToRate(Double toRate) {
		this.toRate = toRate;
	}

	public List<OrderStatus> getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(List<OrderStatus> orderStatus) {
		this.orderStatus = orderStatus;
	}

}
