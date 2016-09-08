package com.mrk.myordershop.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mrk.myordershop.constant.ActiveFlag;

@Entity
@Table(name = "MOS_CATEGORY")
public class Category implements Serializable, Comparable<Category> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private int id;

	@JsonIgnore
	@Column(name = "ACTIVE_FLAG")
	@Enumerated(EnumType.STRING)
	private ActiveFlag activeFlag;

	@Column(name = "NAME", unique = true)
	private String name;

	@JsonIgnore
	@Column(name = "CREATE_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTimestamp = new Date();

	@JsonIgnore
	@Column(name = "UPDATE_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTimestamp;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "IMAGE_ID")
	@Cascade(value = { CascadeType.SAVE_UPDATE })
	private Image image;

	public static Category fillCategory(String name) {
		Category category = new Category();
		category.name = name;
		category.activeFlag = ActiveFlag.ACTIVE;
		category.createTimestamp = new Date();
		return category;
	}

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", activeFlag=" + activeFlag + ", name="
				+ name + "]";
	}

	@Override
	public int compareTo(Category o) {
		return o.getName().compareTo(this.name);
	}
}
