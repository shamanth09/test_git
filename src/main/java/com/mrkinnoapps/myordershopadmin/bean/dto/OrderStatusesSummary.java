package com.mrkinnoapps.myordershopadmin.bean.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import com.mrkinnoapps.myordershopadmin.bean.constant.OrderStatus;

public class OrderStatusesSummary implements OrderStatusSummary {

	private List<OrderStatusWithDateSummary> orderSummary = new ArrayList<OrderStatusWithDateSummary>();

	private Date fromDate;

	private Date toDate;

	public List<OrderStatusWithDateSummary> getOrderSummary() {
		return orderSummary;
	}

	public void setOrderSummary(List<OrderStatusWithDateSummary> orderSummary) {
		this.orderSummary = orderSummary;
	}

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

	@Override
	public void setValue(OrderStatus status, Integer count, Date date) {
	}
	
	public Long getTotalOrderCount() {
		Long totalCount = 0l;
		for (OrderStatusWithDateSummary orderStatusWithDateSummary : orderSummary) {
			totalCount += orderStatusWithDateSummary.getCount();
		}
		return totalCount;
	}

}
