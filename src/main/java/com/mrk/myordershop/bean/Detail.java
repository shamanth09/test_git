package com.mrk.myordershop.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mrk.myordershop.constant.SizeType;

@Entity
@Table(name = "MOS_DETAIL")
@Inheritance(strategy = InheritanceType.JOINED)
public class Detail implements Serializable {

	@JsonIgnore
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "ID")
	@JsonIgnore
	private int id;

	@Column(name = "QUANTITY")
	private Integer quantity;

	@Column(name = "SIZE")
	private String size;

	@Column(name = "LENGTH")
	private String length;

	@Column(name = "WEIGTH")
	private Double weight;

	@Column(name = "STONE_WEIGHT")
	private Double stoneWeight;

	@Column(name = "REMARKS")
	private String remarks;

	@Column(name = "SIZE_TYPE")
	@Enumerated(EnumType.STRING)
	private SizeType sizeType;

	@Column(name = "INITIAL")
	private String initial;

	@Column(name = "WITH_SCREW")
	private String withScrew;

	@Column(name = "LINE")
	private String lines;

	@Column(name = "BACK_CHAIN")
	private String backChain;

	@Column(name = "HUCK")
	private String huck;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getStoneWeight() {
		return stoneWeight;
	}

	public void setStoneWeight(Double stoneWeight) {
		this.stoneWeight = stoneWeight;
	}

	public SizeType getSizeType() {
		return sizeType;
	}

	public void setSizeType(SizeType sizeType) {
		this.sizeType = sizeType;
	}

	@JsonProperty
	public void setSizeType(String sizeType) {
		if (sizeType != null && !sizeType.isEmpty()) {
			if (sizeType.trim().equals("Plastic Size")
					|| sizeType.trim().equals("PLASTIC")
					|| sizeType.trim().equals("plastic")) {
				this.sizeType = SizeType.PLASTIC;
			} else if (sizeType.trim().equals("Steel Size")
					|| sizeType.trim().equals("STEEL")
					|| sizeType.trim().equals("steel")) {
				this.sizeType = SizeType.STEEL;
			}
		}
	}

	public String getInitial() {
		return initial;
	}

	public void setInitial(String initial) {
		this.initial = initial;
	}

	public String getWithScrew() {
		return withScrew;
	}

	public void setWithScrew(String withScrew) {
		this.withScrew = withScrew;
	}

	public String getLines() {
		return lines;
	}

	public void setLines(String lines) {
		this.lines = lines;
	}

	public String getBackChain() {
		return backChain;
	}

	public void setBackChain(String backChain) {
		this.backChain = backChain;
	}

	public String getHuck() {
		return huck;
	}

	public void setHuck(String huck) {
		this.huck = huck;
	}

	@Override
	public String toString() {
		return "Detail [id=" + id + ", quantity=" + quantity + ", size=" + size
				+ ", length=" + length + ", remarks=" + remarks + "]";
	}

}
