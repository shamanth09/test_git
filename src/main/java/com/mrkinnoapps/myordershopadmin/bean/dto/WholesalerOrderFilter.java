package com.mrkinnoapps.myordershopadmin.bean.dto;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import com.mrkinnoapps.myordershopadmin.bean.constant.OrderStatus;

public class WholesalerOrderFilter {

	private List<OrderStatus> orderStatus;

	private String productName;
	
	private String[] orderNo;

	private String supplierId;

	private String wholesalerName;

	private String[] supplierName;

	private Date fromDate;
	
	private Date toDate;

	private String userID;

	private String[] supplierMobileNo;
	
	public String[] getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String[] supplierName) {
		this.supplierName = supplierName;
	}

	public String[] getSupplierMobileNo() {
		return supplierMobileNo;
	}

	public void setSupplierMobileNo(String[] supplierMobileNo) {
		this.supplierMobileNo = supplierMobileNo;
	}

	public String[] getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String[] orderNo) {
		this.orderNo = orderNo;
	}

	public List<OrderStatus> getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(List<OrderStatus> orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	public String getWholesalerName() {
		return wholesalerName;
	}

	public void setWholesalerName(String wholesalerName) {
		this.wholesalerName = wholesalerName;
	}
	public Date getFromDate() {
		return fromDate;
	}

	public Date getFromDate(Date defaultDate) {
		return this.fromDate != null ? this.fromDate : defaultDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public Date getToDate(Date defaultDate) {
		return this.toDate != null ? this.toDate : defaultDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Date getCreateDate() {
		return this.fromDate;
	}


	public void setCreateDate(Date createDate) {
		this.fromDate = createDate;
		this.toDate = createDate;
	}

	public static void main(String[] args) {
		//		Date fromdate = new DateTime().minusYears(1).toDate();
		//		Date todate = new Date();
		//		long from = new DateTime(fromdate).getMillis();
		//		long to  = new DateTime(todate).getMillis();
		//		long checkSize = (to-from)/14;
		//		System.out.println(checkSize);
		//		System.out.println(new DateTime(checkSize));
		//		for(int i=1; i<=14;i++){
		//			todate = new Date(checkSize*i);
		//			System.out.println("fromdate == "+fromdate+"        "+"todate == "+todate);
		//			fromdate = todate;
		//			System.out.println();
		//		}
		long from = new DateTime().minusYears(3).getMillis();
		long to  = new DateTime().getMillis();
		//		System.out.println(new DateTime(from).toString("MMM d, y h:mm:ss a"));
		//		System.out.println(new DateTime(to).toString("MMM d, y h:mm:ss a"));
		long checkSize = (to-from)/14;
		Date date = new Date(from+checkSize);
		//		System.out.println(date);
		//		System.out.println();
		//		System.out.println(new DateTime(from+checkSize).toString("MMM d, y h:mm:ss a"));
		//		System.out.println();
		for(int i=1; i<=14;i++){
			System.out.println(new DateTime(from+(checkSize*i)).toString("MMM d, y h:mm:ss a"));
		}
	}

}
