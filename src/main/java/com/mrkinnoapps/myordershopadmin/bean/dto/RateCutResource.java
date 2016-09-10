package com.mrkinnoapps.myordershopadmin.bean.dto;

import java.util.Date;

import com.mrkinnoapps.myordershopadmin.bean.entity.WholesalerInstantOrder;

public class RateCutResource extends OrderSummary {

	private Integer orderId;

	private String orderNo;

	private Integer rateCut;

	private String customerName;
	
	private Date createTimestamp;

	public RateCutResource() {
		super(WholesalerInstantOrder.class);
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getRateCut() {
		return rateCut;
	}

	public void setRateCut(Integer rateCut) {
		this.rateCut = rateCut;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Date getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Date createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

}
