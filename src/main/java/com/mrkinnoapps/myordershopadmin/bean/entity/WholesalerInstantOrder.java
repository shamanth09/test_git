package com.mrkinnoapps.myordershopadmin.bean.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.mrkinnoapps.myordershopadmin.bean.constant.OrderPriority;
import com.mrkinnoapps.myordershopadmin.bean.constant.Role;
import com.mrkinnoapps.myordershopadmin.bean.dto.View;

@Entity
@Table(name = "MOS_WHOLESALER_INSTANT_ORDER")
@PrimaryKeyJoinColumn(name = "ID")
@DiscriminatorValue(value = "TYPE_WHOLESALER_INSTANT")
public class WholesalerInstantOrder extends Order {

	private static final long serialVersionUID = 1L;

	@OneToOne
	@JoinColumn(name = "IMAGE_ID")
	private Image image;

	@Column(name = "PRIORITY")
	@Enumerated(EnumType.STRING)
	private OrderPriority priority;

	@Column(name = "CUSTOMER_NAME")
	private String customerName;

	@Column(name = "CUSTOMER_MOBILE")
	private String customerMobile;

	@Column(name = "CUSTOMER_FIRM_NAME")
	private String customerFirmName;

	@Column(name = "RATECUT")
	private Integer rateCut;

	@Column(name = "ADVANCE")
	private String advance;

	@Column(name = "SAMPLE_FROM")
	@Enumerated(EnumType.STRING)
	private Role sampleFrom;

	@Column(name = "SAMPLE_DESCRIPTION")
	private String sampleDescription;
	
	@JsonIgnore
	public Image getImage() {
		return image;
	}

	@JsonProperty
	public void setImage(Image image) {
		this.image = image;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@JsonView(View.OrderBasic.class)
	public OrderPriority getPriority() {
		return priority;
	}

	public void setPriority(OrderPriority priority) {
		this.priority = priority;
	}

	@JsonView(View.OrderBasic.class)
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	@JsonView(View.OrderBasic.class)
	public String getCustomerMobile() {
		return customerMobile;
	}

	public void setCustomerMobile(String customerMobile) {
		this.customerMobile = customerMobile;
	}

	@JsonView(View.OrderBasic.class)
	public String getCustomerFirmName() {
		return customerFirmName;
	}

	public void setCustomerFirmName(String customerFirmName) {
		this.customerFirmName = customerFirmName;
	}

	@JsonView(View.OrderDetailed.class)
	public Integer getRateCut() {
		return rateCut;
	}

	public void setRateCut(Integer rateCut) {
		this.rateCut = rateCut;
	}

	@JsonView(View.OrderDetailed.class)
	public String getAdvance() {
		return advance;
	}

	public void setAdvance(String advance) {
		this.advance = advance;
	}

	@JsonView(View.OrderDetailed.class)
	public Role getSampleFrom() {
		return sampleFrom;
	}

	public void setSampleFrom(Role sampleFrom) {
		this.sampleFrom = sampleFrom;
	}

	@JsonView(View.OrderDetailed.class)
	public String getSampleDescription() {
		return sampleDescription;
	}

	public void setSampleDescription(String sampleDescription) {
		this.sampleDescription = sampleDescription;
	}
}
