package com.mrkinnoapps.myordershopadmin.bean.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "MOS_RETAILER")
@PrimaryKeyJoinColumn(name = "ID")
public class Retailer extends User implements Serializable {

	private static final long serialVersionUID = 1L;


}
