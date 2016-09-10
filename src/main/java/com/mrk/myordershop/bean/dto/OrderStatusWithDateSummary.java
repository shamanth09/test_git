package com.mrk.myordershop.bean.dto;

import java.util.Date;

import com.mrk.myordershop.constant.OrderStatus;

/**
 * @author Naveen
 * 
 */
public class OrderStatusWithDateSummary implements OrderStatusSummary {

	private String status;

	private OrderStatus orderStatus;

	private Long count;

	private Date date;

	private Long quantity;

	private Double weight;

	public String getStatus() {
		return status != null ? this.status
				: this.orderStatus != null ? orderStatus.getValue() : null;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getCount() {
		return count!=null?count:0;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public long getQuantity() {
		return quantity!=null?quantity:0;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public double getWeight() {
		return weight!=null?weight:0;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public void setValue(OrderStatus status, Integer value, Date date) {
		this.orderStatus = status;
		this.status = status.getValue();
		this.count = value.longValue();
		this.date = date;
	}

	public void setValue(OrderStatus status, Integer value, Date date,
			Long quantity, Double weight) {
		this.orderStatus = status;
		this.status = status.getValue();
		this.count = value.longValue();
		this.date = date;
		this.quantity = quantity;
		this.weight = weight;
	}
}
