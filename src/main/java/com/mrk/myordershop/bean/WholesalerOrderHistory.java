package com.mrk.myordershop.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mrk.myordershop.constant.OrderStatus;

@Entity
@Table(name = "MOS_WHOLESALER_ORDER_HISTORY")
public class WholesalerOrderHistory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private int id;

	@Column(name = "FROM_STATUS")
	@Enumerated(EnumType.STRING)
	private OrderStatus fromStatus;

	@Column(name = "TO_STATUS")
	@Enumerated(EnumType.STRING)
	private OrderStatus tosSatus;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIMESTAMP")
	private Date createTimestamp;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "ORDER_ID")
	private WholesalerOrder order;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public OrderStatus getFromStatus() {
		return fromStatus;
	}

	public void setFromStatus(OrderStatus fromStatus) {
		this.fromStatus = fromStatus;
	}

	public OrderStatus getTosSatus() {
		return tosSatus;
	}

	public void setTosSatus(OrderStatus tosSatus) {
		this.tosSatus = tosSatus;
	}

	public Date getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Date createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public WholesalerOrder getOrder() {
		return order;
	}

	public void setOrder(WholesalerOrder order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "WholesalerOrderHistory [id=" + id + ", fromStatus="
				+ fromStatus + ", tosSatus=" + tosSatus + ", createTimestamp="
				+ createTimestamp + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WholesalerOrderHistory other = (WholesalerOrderHistory) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
