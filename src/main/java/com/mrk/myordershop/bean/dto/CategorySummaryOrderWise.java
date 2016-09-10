package com.mrk.myordershop.bean.dto;

import java.util.Date;

import com.mrk.myordershop.constant.OrderStatus;

public class CategorySummaryOrderWise extends CategorySummary implements
		OrderStatusSummary {

	private Long count;

	private Double weight;

	private Long quantity;

	public long getCount() {
		return count != null ? count : 0;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public double getWeight() {
		return weight != null ? weight : 0;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public long getQuantity() {
		return quantity != null ? quantity : 0;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	@Override
	public void setValue(OrderStatus status, Integer count, Date date) {
	}

}
