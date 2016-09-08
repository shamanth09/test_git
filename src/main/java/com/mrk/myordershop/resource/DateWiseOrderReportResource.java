package com.mrk.myordershop.resource;

import java.util.Date;
import java.util.List;

import com.mrk.myordershop.bean.dto.OrderStatusSummary;

public class DateWiseOrderReportResource extends OrderReportResource {

	private Date fromDate;

	private Date toDate;

	public DateWiseOrderReportResource(
			List<? extends OrderStatusSummary> orderStatusSummaries,
			Date fromDate, Date toDate) {
		this.setOrderStatusSummaries(orderStatusSummaries);
		this.fromDate = fromDate;
		this.toDate = toDate;
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

}
