package com.mrk.myordershop.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "MOS_NOTIFICATION_SETTING")
public class NotificationSettings {
	
	public transient final static Integer NON = 0X0;

	public transient final static Integer SMS = 0X01;
	
	public transient final static Integer API = 0X02;

	public transient final static Integer EMAIL = 0X04;

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private int id;

	@Column(name = "NEW_ORDER")
	private int newOrder;

	@Column(name = "ORDER_STATUS_CHANGE")
	private int orderStatusChange;

	@Column(name = "WS_ORDER_STATUS_CHANGE")
	private int wsOrderStatusChange;

	@Column(name = "RELATION")
	private int relation;

	@Column(name = "EMAIL")
	private int email;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "USER_ID")
	private User user;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNewOrder() {
		return newOrder;
	}

	public void setNewOrder(int newOrder) {
		this.newOrder = newOrder;
	}

	public int getOrderStatusChange() {
		return orderStatusChange;
	}

	public void setOrderStatusChange(int orderStatusChange) {
		this.orderStatusChange = orderStatusChange;
	}

	public int getWsOrderStatusChange() {
		return wsOrderStatusChange;
	}

	public void setWsOrderStatusChange(int wsOrderStatusChange) {
		this.wsOrderStatusChange = wsOrderStatusChange;
	}

	public int getRelation() {
		return relation;
	}

	public void setRelation(int relation) {
		this.relation = relation;
	}

	public int getEmail() {
		return email;
	}

	public void setEmail(int email) {
		this.email = email;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean checkNewOrderAvailability(Integer type) {
		return ((this.newOrder & type) != NON);
	}

	public boolean checkOrderStatusChangeAvailability(Integer type) {
		return ((this.orderStatusChange & type) != NON);
	}

	public boolean checkWSOrderStatusChangeAvailability(Integer type) {
		return ((this.wsOrderStatusChange & type) != NON);
	}

	public boolean checkRelationAvailability(Integer type) {
		return ((this.relation & type) != NON);
	}

	public boolean checkEmailAvailability(Integer type) {
		return ((this.email & type) != NON);
	}

	public static Integer getIntValue(String[] values) {
		int value = NON;
		for (String string : values) {
			value |= string.equals("EMAIL") ? EMAIL
					: string.equals("SMS") ? SMS
							: string.equals("API") ? API : NON;
		}
		return value;
	}
}
