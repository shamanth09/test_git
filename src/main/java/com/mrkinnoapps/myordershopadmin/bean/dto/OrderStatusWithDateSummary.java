package com.mrkinnoapps.myordershopadmin.bean.dto;

import com.mrkinnoapps.myordershopadmin.bean.constant.OrderStatus;

import java.util.Date;

/**
 * @author Naveen
 * 
 */
public class OrderStatusWithDateSummary implements OrderStatusSummary {

	private String status;

	private OrderStatus orderStatus;

	private Long count;

	private Long quantity;

	private Double weight;

	private Date recentOrderDate;

	private Date pastOrderDate;
	

	public Date getPastOrderDate() {
		return pastOrderDate;
	}

	public void setPastOrderDate(Date pastOrderDate) {
		this.pastOrderDate = pastOrderDate;
	}

	public String getStatus() {
		return status != null ? this.status
				: this.orderStatus != null ? orderStatus.getValue() : null;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Date getRecentOrderDate() {
		return recentOrderDate;
	}

	public void setRecentOrderDate(Date recentOrderDate) {
		this.recentOrderDate = recentOrderDate;
	}

	public void setValue(OrderStatus status, Integer value,
			Date recentOrderDate, Long quantity, Double weight,
			Date pastOrderDate) {
		this.orderStatus = status;
		this.status = status.getValue();
		this.count = value.longValue();
		this.quantity = quantity;
		this.weight = weight;
		this.recentOrderDate = recentOrderDate;
		this.pastOrderDate = pastOrderDate;

	}

	@Override
	public void setValue(OrderStatus status, Integer count, Date recentOrderDate) {
		this.orderStatus = status;
		this.status = status.getValue();
		this.count = count.longValue();
		this.recentOrderDate = recentOrderDate;
	}

}
