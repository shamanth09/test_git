package com.mrkinnoapps.myordershopadmin.bean.dto;

import java.util.Date;

import com.mrkinnoapps.myordershopadmin.bean.constant.OrderStatus;

public interface OrderStatusSummary {

	void setValue(OrderStatus status, Integer count, Date date);
}
