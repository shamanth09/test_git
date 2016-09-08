package com.mrk.myordershop.bean;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.mrk.myordershop.bean.dto.View;
import com.mrk.myordershop.constant.OrderPriority;

/**
 * @author Naveen
 * Which will forwared to supplier for instantOrder or retailer
 */
@Entity
@Table(name = "MOS_INSTANT_WHOLESALER_ORDER")
@DiscriminatorValue(value = "TYPE_INSTANT")
public class InstantWholesalerOrder extends WholesalerOrder {

	private static final long serialVersionUID = 1L;

	@OneToOne
	@JoinColumn(name = "IMAGE_ID")
	private Image image;

	@Column(name = "PRIORITY")
	@Enumerated(EnumType.STRING)
	private OrderPriority priority;
	
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

	@JsonView(View.OrderBasic.class)
	public OrderPriority getPriority() {
		return priority;
	}

	public void setPriority(OrderPriority priority) {
		this.priority = priority;
	}

	@JsonView(View.OrderDetailed.class)
	public String getSampleDescription() {
		return sampleDescription;
	}

	public void setSampleDescription(String sampleDescription) {
		this.sampleDescription = sampleDescription;
	}

}
