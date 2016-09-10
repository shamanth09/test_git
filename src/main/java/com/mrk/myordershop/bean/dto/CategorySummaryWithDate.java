package com.mrk.myordershop.bean.dto;

import java.util.Date;

import com.mrk.myordershop.constant.OrderStatus;

public class CategorySummaryWithDate extends CategorySummary implements
		OrderStatusSummary {

	private Date fromDate;

	private Date toDate;

	private long count;

	private Date date;

	private Long quantity;

	private Double weight;

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public long getQuantity() {
		return quantity != null ? quantity : 0;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public double getWeight() {
		return weight != null ? weight : 0;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	@Override
	public void setValue(OrderStatus status, Integer count, Date date) {
	}

}
