package com.mrk.myordershop.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

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

@Entity
@Table(name = "MOS_ORDER")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "ORDER_TYPE", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(value = "TYPE_ORDER")
public class Order implements Serializable, Paginated {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private int id;

	@Column(name = "ORDER_NO", unique = true)
	private String orderNo;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "REFERRAL_USER_ID")
	private Retailer referralUser;

	@Column(name = "ORDER_TYPE", insertable = false, updatable = false)
	@Enumerated(EnumType.STRING)
	private OrderType type;

	@JsonIgnore
	@Column(name = "ACTIVE_FLAG")
	@Enumerated(EnumType.STRING)
	private ActiveFlag activeFlag = ActiveFlag.ACTIVE;

	@OneToOne(fetch = FetchType.EAGER, mappedBy = "order")
	private Item item;

	@Column(name = "ORDER_STATUS")
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "USER_ID")
	private User user;

	@OneToOne
	@JoinColumn(name = "WHOLESALER")
	private Wholesaler wholesaler;

	@JsonIgnore
	@OneToMany(mappedBy = "order")
	private List<WholesalerOrder> wholesalerOrders;

	@JsonIgnore
	@Transient
	private WholesalerOrder currentWholesalerOrder;

	@JsonInclude(Include.NON_NULL)
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "CANCELLATION_ID")
	private Cancellation cancellation;

	@Column(name = "DESCRIPTION")
	private String description;

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

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ACCEPTANCE_ID")
	private OrderAcceptance acceptance;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<OrderHistory> histories;

	@JsonView(View.OrderBasic.class)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@JsonView(View.OrderBasic.class)
	public OrderType getType() {
		return type;
	}

	public void setType(OrderType type) {
		this.type = type;
	}

	@JsonView(View.OrderMeta.class)
	public ActiveFlag getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(ActiveFlag activeFlag) {
		this.activeFlag = activeFlag;
	}

	@JsonIgnore
	public Item getItem() {
		return item;
	}

	@JsonProperty
	public void setItem(Item item) {
		this.item = item;
	}

	@JsonView(View.OrderBasic.class)
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

	@JsonIgnore
	public Retailer getReferralUser() {
		return referralUser;
	}

	@JsonIgnore
	public void setReferralUser(Retailer referralUser) {
		this.referralUser = referralUser;
	}

	@JsonIgnore
	public Wholesaler getWholesaler() {
		return wholesaler;
	}

	@JsonProperty
	public void setWholesaler(Wholesaler wholesaler) {
		this.wholesaler = wholesaler;
	}

	public List<WholesalerOrder> getWholesalerOrders() {
		return wholesalerOrders;
	}

	public void setWholesalerOrders(List<WholesalerOrder> wholesalerOrders) {
		this.wholesalerOrders = wholesalerOrders;
	}

	public WholesalerOrder getCurrentWholesalerOrder() {
		return currentWholesalerOrder;
	}

	public void setCurrentWholesalerOrder(WholesalerOrder currentWholesalerOrder) {
		this.currentWholesalerOrder = currentWholesalerOrder;
	}

	@JsonView(View.OrderDetailed.class)
	public Integer getWholesalerOrderId() {
		return this.currentWholesalerOrder != null ? this.currentWholesalerOrder.getId() : null;
	}

	@JsonView(View.OrderDetailed.class)
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

	@JsonView(View.OrderBasic.class)
	public Date getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Date createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public void setCreateDate(Date createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	@JsonView(View.OrderBasic.class)
	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	@JsonIgnore
	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	@JsonView(View.OrderBasic.class)
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	@JsonView(View.OrderModerate.class)
	@JsonProperty("productName")
	public String getProductName() {
		if (this.item != null && this.item.getProduct() != null)
			return this.item.getProduct().getName();
		return null;
	}

	@JsonView(View.OrderDetailed.class)
	@JsonProperty("categoryName")
	public String getCategoryName() {
		if (this.item != null && this.item.getProduct() != null)
			return this.item.getProduct().getCategory().getName();
		return null;
	}

	@JsonView(View.OrderBasic.class)
	@JsonProperty("wholesalerName")
	public String getWholesalerName() {
		if (this.wholesaler != null)
			return this.wholesaler.getName();
		return null;
	}

	@JsonView(View.OrderBasic.class)
	@JsonSerialize(using = JsonTimeStampSerializer.class)
	public Date getExpectedDate() {
		return this.expectedDate != null ? this.expectedDate
				: new LocalDate(getCreateTimestamp()).plusDays(10).toDate();
	}

	public void setExpectedDate(Date expectedDate) {
		this.expectedDate = expectedDate;
	}

	@JsonView(View.OrderDetailed.class)
	@JsonProperty("acceptance")
	public OrderAcceptance getAcceptance() {
		return acceptance;
	}

	public void setAcceptance(OrderAcceptance acceptance) {
		this.acceptance = acceptance;
	}

	@JsonView(View.OrderDetailed.class)
	public Set<OrderHistory> getHistories() {
		return histories;
	}

	public void setHistories(Set<OrderHistory> histories) {
		this.histories = histories;
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
		Order other = (Order) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", type=" + type + ", activeFlag=" + activeFlag + ", orderStatus=" + orderStatus
				+ ", description=" + description + ", createTimestamp=" + createTimestamp + ", updateTimestamp="
				+ updateTimestamp + ", orderNo=" + orderNo + "]";
	}

}
