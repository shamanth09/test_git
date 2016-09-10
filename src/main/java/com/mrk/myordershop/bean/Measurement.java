package com.mrk.myordershop.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "MOS_MEASUREMENT")
public class Measurement implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum v {
		LENGTH("Length"), SIZE("Size"), WEIGHT("Weight"), INITIAL("Initial"), SIZE_TYPE(
				"Size type"), WITH_SCREW("With Screw"), LINES("No Of Lines"), BACK_CHAIN(
				"Back Chain"), HUCK("Huck");
		private String value;

		v(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private int id;

	@Enumerated(EnumType.STRING)
	@Column(name = "MEASUREMENT")
	private v measurement;

	public Measurement(v measurement) {
		this.measurement = measurement;
	}

	public Measurement() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public v getMeasurement() {
		return measurement;
	}

	@JsonProperty(value = "lable")
	public String getMeasurementValue() {
		return measurement.getValue();
	}

	public void setMeasurement(v measurement) {
		this.measurement = measurement;
	}

}
