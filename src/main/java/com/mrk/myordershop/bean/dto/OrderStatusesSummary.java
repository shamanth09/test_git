package com.mrk.myordershop.bean.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.mrk.myordershop.constant.OrderStatus;

@Deprecated
public class OrderStatusesSummary implements OrderStatusSummary {

	@JsonView(View.Status.class)
	protected int active;

	@JsonView(View.Status.class)
	protected int rejected;

	@JsonView(View.Status.class)
	protected int canceled;

	@JsonView(View.Status.class)
	protected int inProgress;

	@JsonView(View.WSOStatus.class)
	protected int approved;

	@JsonView(View.WSOStatus.class)
	protected int available;

	@JsonView(View.WSOStatus.class)
	protected int delivered;

	@JsonView(View.SPOStatus.class)
	protected int dispatched;
	
	@JsonView(View.SPOStatus.class)
	protected int received;

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public int getRejected() {
		return rejected;
	}

	public void setRejected(int rejected) {
		this.rejected = rejected;
	}

	public int getCanceled() {
		return canceled;
	}

	public void setCanceled(int canceled) {
		this.canceled = canceled;
	}

	public int getApproved() {
		return approved;
	}

	public void setApproved(int approved) {
		this.approved = approved;
	}

	public int getInProgress() {
		return inProgress;
	}

	public void setInProgress(int inProgress) {
		this.inProgress = inProgress;
	}

	public int getAvailable() {
		return available;
	}

	public void setAvailable(int available) {
		this.available = available;
	}

	public int getDelivered() {
		return delivered;
	}

	public void setDelivered(int delivered) {
		this.delivered = delivered;
	}

	public int getDispatched() {
		return dispatched;
	}

	public void setDispatched(int dispatched) {
		this.dispatched = dispatched;
	}

	public int getReceived() {
		return received;
	}

	public void setReceived(int received) {
		this.received = dispatched;
	}
 
	public void setValue(OrderStatus status, Integer value, Date date) {
		if (status.equals(OrderStatus.ACTIVE)) {
			this.active = value;
		} else if (status.equals(OrderStatus.APPROVED)) {
			this.approved = value;
		} else if (status.equals(OrderStatus.AVAILABLE)) {
			this.available = value;
		} else if (status.equals(OrderStatus.DELIVERED)) {
			this.delivered = value;
		} else if (status.equals(OrderStatus.DISPATCHED)) {
			this.dispatched = value;
		}  else if (status.equals(OrderStatus.RECEIVED)) {
			this.received = value;
		} else if (status.equals(OrderStatus.IN_PROGRESS)) {
			this.inProgress = value;
		} else if (status.equals(OrderStatus.REJECTED)) {
			this.rejected = value;
		} else if (status.equals(OrderStatus.CANCELLED)) {
			this.canceled = value;
		}
	}

	public double getWeight() {
		return 0;
	}

	@JsonIgnore @Override
	public long getQuantity() {
		return 0;
	}

	public long getCount() {
		return 0;
	}

}
