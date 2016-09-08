package com.mrk.myordershop.bean;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Supplier.java Naveen Apr 9, 2015
 */
@Entity
@Table(name = "MOS_SUPPLIER")
@PrimaryKeyJoinColumn(name = "ID")
public class Supplier extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
