package com.mrk.myordershop.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "MOS_ORDER_ACCEPTANCE")
public class OrderAcceptance {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private int id;

	@Column(name = "ADVANCE")
	private Double advance;

	@Column(name = "RATE_CUT")
	private Double rateCut;

	@Column(name = "DESCRIPTION")
	private String description;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@JsonProperty
	public Double getAdvance() {
		return advance != null ? advance : 0;
	}

	public void setAdvance(Double advance) {
		this.advance = advance;
	}
	
	@JsonProperty
	public Double getRateCut() {
		return rateCut != null ? rateCut : 0;
	}

	public void setRateCut(Double rateCut) {
		this.rateCut = rateCut;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
