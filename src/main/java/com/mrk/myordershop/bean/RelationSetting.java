package com.mrk.myordershop.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MOS_RELATION_SETTING")
public class RelationSetting implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private int id;

	@Column(name = "ADVANCE")
	private Boolean advance;

	@Column(name = "RATE_CUT")
	private Boolean rateCut;

	@Column(name = "MIN_WEIGHT")
	private Double minWeight;
	
	@Column(name = "IS_BLOCKED")
	private Boolean block;

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Boolean isBlocked() {
		return block;
	}

	public void setBlocked(Boolean isBlocked) {
		this.block = isBlocked;
	}

	public Boolean getAdvance() {
		return advance;
	}

	public void setAdvance(Boolean advance) {
		this.advance = advance;
	}

	public Boolean getRateCut() {
		return rateCut;
	}

	public void setRateCut(Boolean rateCut) {
		this.rateCut = rateCut;
	}

	public double getMinWeight() {
		return minWeight!=null?minWeight:0;
	}

	public void setMinWeight(double minWeight) {
		this.minWeight = minWeight;
	}

}
