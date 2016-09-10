package com.mrk.myordershop.bean.dto;

import java.util.Date;

import com.mrk.myordershop.constant.OrderStatus;

public interface OrderStatusSummary {

	void setValue(OrderStatus status, Integer count, Date date);
	
	double getWeight();
	
	long getQuantity();
	
	long getCount();
}
