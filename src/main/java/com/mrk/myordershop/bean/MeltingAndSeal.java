package com.mrk.myordershop.bean;

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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mrk.myordershop.constant.ActiveFlag;

@Entity
@Table(name = "MOS_MELTING_AND_SEAL")
public class MeltingAndSeal {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private int id;

	@JsonIgnore
	@Enumerated(EnumType.STRING)
	@Column(name = "ACTIVE_FLAG")
	private ActiveFlag activeFlag;

	@Column(name = "SEAL")
	private String seal;

	@Column(name = "MELTING")
	private Integer melting;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIMESTAMP")
	private Date createTimestamp;

	@ManyToOne
	@JoinColumn(name = "WHOLESALER_ID")
	private Wholesaler wholesaler;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ActiveFlag getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(ActiveFlag activeFlag) {
		this.activeFlag = activeFlag;
	}

	public String getSeal() {
		return seal != null ? seal.toUpperCase() : seal;
	}

	public void setSeal(String seal) {
		this.seal = seal;
	}

	public Integer getMelting() {
		return melting;
	}

	public void setMelting(Integer melting) {
		this.melting = melting;
	}

	public Date getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Date createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	@JsonIgnore
	public Wholesaler getWholesaler() {
		return wholesaler;
	}

	@JsonProperty
	public void setWholesaler(Wholesaler wholesaler) {
		this.wholesaler = wholesaler;
	}

}
