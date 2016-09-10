package com.mrk.myordershop.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "MOS_CANCELLATION")
public class Cancellation {

	@Id
	@Column(name = "ID")
	@GeneratedValue
	private int id;

	@Column(name = "REASON")
	private String reason;
	
	@Column(name="CREATE_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTimstamp;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getCreateTimstamp() {
		return createTimstamp;
	}

	public void setCreateTimstamp(Date createTimstamp) {
		this.createTimstamp = createTimstamp;
	}
	
}
