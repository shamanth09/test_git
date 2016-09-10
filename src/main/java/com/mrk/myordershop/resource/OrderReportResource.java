package com.mrk.myordershop.resource;

import java.text.DecimalFormat;
import java.util.List;

import com.mrk.myordershop.bean.dto.OrderStatusSummary;

public class OrderReportResource {

	private List<? extends OrderStatusSummary> orderStatusSummaries;

	public List<? extends OrderStatusSummary> getOrderStatusSummaries() {
		return orderStatusSummaries;
	}

	public void setOrderStatusSummaries(
			List<? extends OrderStatusSummary> orderStatusSummaries) {
		this.orderStatusSummaries = orderStatusSummaries;
	}

	public int getTotal() {
		int count = 0;
		for (OrderStatusSummary summary : this.orderStatusSummaries) {
			count += summary.getCount();
		}
		return count;
	}

	public int getTotalQuantity() {
		int count = 0;
		for (OrderStatusSummary summary : this.orderStatusSummaries) {
			count += summary.getQuantity();
		}
		return count;
	}

	public double getTotalWeight() {
		double count = 0;
		for (OrderStatusSummary summary : this.orderStatusSummaries) {
			count += summary.getWeight();
		}
		return Double.valueOf(new DecimalFormat("#.000").format(count));
	}
}
