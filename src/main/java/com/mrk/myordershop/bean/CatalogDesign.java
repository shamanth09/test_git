package com.mrk.myordershop.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mrk.myordershop.constant.ActiveFlag;

@Entity
@Table(name = "MOS_CATALOG_DESIGN")
public class CatalogDesign {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private int id;

	@JsonIgnore
	@Enumerated(EnumType.STRING)
	@Column(name = "ACTIVE_FLAG")
	private ActiveFlag activeFlag;

	@Column(name = "DESIGN_NO")
	private String designNo;

	@Column(name = "PAGE")
	private int page;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "IMAGE_ID")
	@Cascade(value = { CascadeType.SAVE_UPDATE })
	private Image image;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "CATALOG_ID")
	private Catalog catalog;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getImageId() {
		return image.getId();
	}

	public ActiveFlag getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(ActiveFlag activeFlag) {
		this.activeFlag = activeFlag;
	}

	public String getDesignNo() {
		return designNo;
	}

	public void setDesignNo(String designNo) {
		this.designNo = designNo;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public Catalog getCatalog() {
		return catalog;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}

}
