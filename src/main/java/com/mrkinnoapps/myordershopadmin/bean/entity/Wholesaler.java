package com.mrkinnoapps.myordershopadmin.bean.entity;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Wholesaler.java Naveen Apr 6, 2015
 */
@Entity
@Table(name = "MOS_WHOLESALER")
@PrimaryKeyJoinColumn(name = "ID")
public class Wholesaler extends User {

	private static final long serialVersionUID = 1L;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
