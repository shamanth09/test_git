package com.mrk.myordershop.bean;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "MOS_INSTANT_ORDER")
@PrimaryKeyJoinColumn(name = "ID")
@DiscriminatorValue(value = "TYPE_INSTANT")
public class InstantOrder extends Order {

	private static final long serialVersionUID = 1L;

	@OneToOne
	@JoinColumn(name = "IMAGE_ID")
	private Image image;

	@JsonIgnore
	public Image getImage() {
		return image;
	}

	@JsonProperty
	public void setImage(Image image) {
		this.image = image;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
