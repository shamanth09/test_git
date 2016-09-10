package com.mrk.myordershop.resource;

import java.util.Date;

import org.springframework.hateoas.core.Relation;

import com.mrk.myordershop.bean.WholesalerInstantOrder;
import com.mrk.myordershop.bean.dto.OrderSummary;

@Relation(collectionRelation = "content")
public class RateCutResource extends OrderSummary {

	private Integer orderId;

	private String orderNo;

	private Double rateCut;

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

	public Double getRateCut() {
		return rateCut;
	}

	public void setRateCut(Double rateCut) {
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
