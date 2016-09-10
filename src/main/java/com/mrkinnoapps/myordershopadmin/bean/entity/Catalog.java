package com.mrkinnoapps.myordershopadmin.bean.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mrkinnoapps.myordershopadmin.bean.constant.ActiveFlag;

@Entity
@Table(name = "MOS_CATALOG")
public class Catalog {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private int id;

	@Enumerated(EnumType.STRING)
	@Column(name = "ACTIVE_FLAG")
	private ActiveFlag activeFlag;

	@Column(name = "NAME")
	private String name;

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

}
