package com.mrkinnoapps.myordershopadmin.bean.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.LocalDate;

public class OrderWeightMonthSummary {

	private Date fromDate;

	private Date toDate;

	private Map<String, Double> data = new HashMap<String, Double>();

	public OrderWeightMonthSummary() {
	}

	public void entry(String date, Double count) {
		data.put(date, count);
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

	public Map<String, Double> getData() {
		return data;
	}

	public void setData(Map<String, Double> data) {
		this.data = data;
	}

	public String getMonth() {
		return new LocalDate(fromDate).toString("MMMM");
	}
}
