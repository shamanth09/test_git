package com.mrkinnoapps.myordershopadmin.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrkinnoapps.myordershopadmin.bean.constant.OrderStatus;
import com.mrkinnoapps.myordershopadmin.bean.dto.OrderStatusesSummary;
import com.mrkinnoapps.myordershopadmin.bean.dto.OrderSummaryByCategory;
import com.mrkinnoapps.myordershopadmin.bean.dto.OrderSummaryByUser;
import com.mrkinnoapps.myordershopadmin.bean.dto.OrderWeightMonthSummary;
import com.mrkinnoapps.myordershopadmin.bean.entity.Supplier;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.bean.entity.Wholesaler;

public interface WholesalerOrderSummaryDAO {

	int getTotalQuantity(User user, OrderStatus[] statuses);

	Long getTotalWeight(User user, OrderStatus[] statuses);

	OrderWeightMonthSummary getTotalWeightByMonth(Date fromDate, Date toDate,
			User user, OrderStatus[] statuses);

	OrderStatusesSummary gerOrderCountByTillDate(Date toDate, String groupBy,
			User user);

	List<String[]> gerOrderCountByCreatedDate(Date fromdate, Date todate,
			String groupBy, User user);

	Page<OrderSummaryByUser> getOrderWholesalerWiseSummary(Pageable pageable,
			Supplier supplier);

	Page<OrderSummaryByCategory> getOrderSummaryByCategory(Pageable pageable,
			User user);

	Page<OrderSummaryByUser> getOrderSummaryGroupBySupplier(Pageable pageable,
			Wholesaler wholesaler);

	OrderStatusesSummary getOrderCountByStatusWise(Date fromdate, Date todate,
			User user);


}
