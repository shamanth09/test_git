package com.mrkinnoapps.myordershopadmin.bean.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mrkinnoapps.myordershopadmin.bean.constant.ActiveFlag;

@Entity
@Table(name = "MOS_PRODUCT")
@org.hibernate.annotations.GenericGenerator(name = "uuid", strategy = "uuid.hex")
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "ID", nullable = false, unique = true)
	private Integer id;

	@JsonIgnore
	@Enumerated(EnumType.STRING)
	@Column(name = "ACTIVE_FLAG")
	private ActiveFlag activeFlag = ActiveFlag.ACTIVE;

	@Column(name = "SKU", unique = true)
	private String sku;

	@Column(name = "NAME")
	private String name;

	@OneToOne
	@JoinColumn(name = "CATEGORY_ID")
	private Category category;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "MOS_PRODUCT_MEASUREMENT", joinColumns = { @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID") }, inverseJoinColumns = { @JoinColumn(name = "MEASUREMENT_ID", referencedColumnName = "ID") })
	private List<Measurement> measurements = new ArrayList<Measurement>();

	@JsonIgnore
	@Column(name = "CREATE_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTimestamp;

	@JsonIgnore
	@Column(name = "UPDATE_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTimestamp;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "IMAGE_ID")
	@Cascade(value = { CascadeType.SAVE_UPDATE })
	private Image image;

	public static Product fillProduct(String name, Measurement measurement) {
		Product product = new Product();
		product.name = name;
		product.measurements.add(measurement);
		product.activeFlag = ActiveFlag.ACTIVE;
		product.createTimestamp = new Date();

		return product;
	}

	public static Product fillProduct(String name) {
		Product product = new Product();
		product.name = name;

		return product;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ActiveFlag getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(ActiveFlag activeFlag) {
		this.activeFlag = activeFlag;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore
	public Category getCategory() {
		return category;
	}

	@JsonProperty
	public void setCategory(Category category) {
		this.category = category;
	}

	public List<Measurement> getMeasurements() {
		return measurements;
	}

	public void setMeasurements(List<Measurement> measurements) {
		this.measurements = measurements;
	}

	public Date getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Date createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

}
