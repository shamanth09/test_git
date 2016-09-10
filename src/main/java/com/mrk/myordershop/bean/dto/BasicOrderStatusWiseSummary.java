package com.mrk.myordershop.bean.dto;

import java.io.Serializable;

import com.mrk.myordershop.constant.OrderStatus;

public class BasicOrderStatusWiseSummary implements Serializable{

	private Long count;
	
	private Double weight;
	
	private Long quantity;
	
	private OrderStatus orderStatus;

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}
	
}
