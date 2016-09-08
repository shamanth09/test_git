package com.mrk.myordershop.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mrk.myordershop.bean.dto.View;
import com.mrk.myordershop.constant.ActiveFlag;
import com.mrk.myordershop.constant.OrderStatus;
import com.mrk.myordershop.constant.OrderType;
import com.mrk.myordershop.util.JsonTimeStampSerializer;
import com.mrk.myordershop.util.Paginated;

/**
 * WholesalerOrder.java Naveen Apr 8, 2015
 */
@Entity
@Table(name = "MOS_WHOLESALER_ORDER")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(value = "TYPE_ORDER")
public class WholesalerOrder implements Serializable, Paginated {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private int id;

	@Column(name = "TYPE", insertable = false, updatable = false)
	@Enumerated(EnumType.STRING)
	private OrderType type;

	@JsonIgnore
	@Column(name = "ACTIVE_FLAG")
	@Enumerated(EnumType.STRING)
	private ActiveFlag activeFlag = ActiveFlag.ACTIVE;

	@ManyToOne
	@JoinColumn(name = "ORDER_ID", nullable = false)
	private Order order;

	@OneToOne(mappedBy = "wholesalerOrder")
	private Item item;

	@Column(name = "ORDER_STATUS")
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "USER_ID")
	private User user;

	@OneToOne
	@JoinColumn(name = "SUPPLIER_ID")
	private Supplier supplier;

	@Column(name = "DESCRIPTION")
	private String description;

	@JsonInclude(Include.NON_NULL)
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "CANCELLATION_ID")
	private Cancellation cancellation;

	@JsonSerialize(using = JsonTimeStampSerializer.class)
	@Column(name = "CREATE_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTimestamp;

	@Column(name = "UPDATE_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTimestamp;

	@Column(name = "EXPECTED_DATE")
	@Temporal(TemporalType.DATE)
	private Date expectedDate;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<WholesalerOrderHistory> histories;

	@JsonView(View.Basic.class)
	@JsonProperty("productName")
	public String getProductName() {
		if (this.item != null && this.item.getProduct() != null)
			return this.item.getProduct().getName();
		return null;
	}

	@JsonView(View.Detailed.class)
	@JsonProperty("categoryName")
	public String getCategoryName() {
		if (this.item != null && this.item.getProduct() != null)
			return this.item.getProduct().getCategory().getName();
		return null;
	}

	@JsonView(View.Basic.class)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@JsonView(View.Meta.class)
	public ActiveFlag getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(ActiveFlag activeFlag) {
		this.activeFlag = activeFlag;
	}

	@JsonIgnore
	public Order getOrder() {
		return order;
	}

	@JsonProperty
	public void setOrder(Order order) {
		this.order = order;
	}

	@JsonIgnore
	public Item getItem() {
		return item;
	}

	@JsonProperty
	public void setItem(Item item) {
		this.item = item;
	}

	@JsonView(View.Basic.class)
	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@JsonProperty
	public String getWholesalerName() {
		return user.getName();
	}

	@JsonIgnore
	public Supplier getSupplier() {
		return supplier;
	}

	@JsonView(View.Basic.class)
	@JsonProperty
	public String getSupplierName() {
		return supplier != null ? supplier.getName() : null;
	}

	@JsonProperty
	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	@JsonView(View.Detailed.class)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@JsonView(View.OrderDetailed.class)
	public Cancellation getCancellation() {
		return cancellation;
	}

	public void setCancellation(Cancellation cancellation) {
		this.cancellation = cancellation;
	}

	@JsonView(View.Basic.class)
	public Date getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Date createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	@JsonView(View.Detailed.class)
	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setCreateDate(Date createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	@JsonIgnore
	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public String getOrderNo() {
		return this.order != null ? this.order.getOrderNo() : null;
	}

	@JsonView(View.Basic.class)
	public OrderType getType() {
		return type;
	}

	public void setType(OrderType type) {
		this.type = type;
	}

	@JsonView(View.Detailed.class)
	public Set<WholesalerOrderHistory> getHistories() {
		return histories;
	}

	public void setHistories(Set<WholesalerOrderHistory> histories) {
		this.histories = histories;
	}

	@JsonView(View.Basic.class)
	@JsonSerialize(using = JsonTimeStampSerializer.class)
	public Date getExpectedDate() {
		return this.expectedDate != null ? this.expectedDate : new LocalDate(
				getCreateTimestamp()).plusDays(10).toDate();
	}

	@JsonIgnore
	public void setExpectedDate(Date expectedDate) {
		this.expectedDate = expectedDate;
	}

	@Override
	public String toString() {
		return "WholesalerOrder [id=" + id + ", activeFlag=" + activeFlag
				+ ", order=" + order + ", wholesalerItem=" + item
				+ ", orderStatus=" + orderStatus + ", user=" + user
				+ ", supplier=" + supplier + ", description=" + description
				+ ", createTimestamp=" + createTimestamp + ", updateTimestamp="
				+ updateTimestamp + ", orderNo=" + getOrderNo() + "]";
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
		WholesalerOrder other = (WholesalerOrder) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
