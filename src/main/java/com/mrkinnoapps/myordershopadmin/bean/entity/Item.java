package com.mrkinnoapps.myordershopadmin.bean.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mrkinnoapps.myordershopadmin.bean.constant.ActiveFlag;

@Entity
@Table(name = "MOS_ITEM")
public class Item implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@Transient
	private String uuid = UUID.randomUUID().toString();

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private int id;

	@JsonIgnore
	@Column(name = "ACTIVE_FLAG")
	@Enumerated(EnumType.STRING)
	private ActiveFlag activeFlag = ActiveFlag.ACTIVE;

	@OneToOne
	@JoinColumn(name = "DETAIL_ID")
	@Cascade({ CascadeType.ALL })
	private Detail detail;

	@OneToOne
	@JoinColumn(name = "PRODUCT_ID")
	private Product product;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "ORDER_ID")
	private Order order;

	@OneToOne
	@JoinColumn(name = "MELTING_AND_SEAL_ID")
	private MeltingAndSeal meltingAndSeal;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "WHOLESALER_ORDER_ID")
	private WholesalerOrder wholesalerOrder;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@JsonProperty
	public int getId() {
		return id;
	}

	@JsonIgnore
	public void setId(int id) {
		this.id = id;
	}

	public ActiveFlag getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(ActiveFlag activeFlag) {
		this.activeFlag = activeFlag;
	}

	public Detail getDetail() {
		return detail;
	}

	public void setDetail(Detail detail) {
		this.detail = detail;
	}

	@JsonIgnore
	public Product getProduct() {
		return product;
	}

	@JsonProperty
	public void setProduct(Product product) {
		this.product = product;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public WholesalerOrder getWholesalerOrder() {
		return wholesalerOrder;
	}

	public void setWholesalerOrder(WholesalerOrder wholesalerOrder) {
		this.wholesalerOrder = wholesalerOrder;
	}

	public MeltingAndSeal getMeltingAndSeal() {
		return meltingAndSeal;
	}

	public void setMeltingAndSeal(MeltingAndSeal meltingAndSeal) {
		this.meltingAndSeal = meltingAndSeal;
	}

}
