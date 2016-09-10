package com.mrkinnoapps.myordershopadmin.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrkinnoapps.myordershopadmin.bean.dto.OrderStatusesSummary;
import com.mrkinnoapps.myordershopadmin.bean.dto.OrderSummaryByCategory;
import com.mrkinnoapps.myordershopadmin.bean.dto.OrderSummaryByUser;
import com.mrkinnoapps.myordershopadmin.bean.dto.OrderWeightMonthSummary;
import com.mrkinnoapps.myordershopadmin.bean.dto.RateCutResource;
import com.mrkinnoapps.myordershopadmin.bean.dto.RateCutSummaryFileter;
import com.mrkinnoapps.myordershopadmin.bean.entity.Retailer;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.bean.entity.Wholesaler;

public interface OrderSummaryService {

	Page<RateCutResource> getRateCutSummary(Pageable pageable,
			RateCutSummaryFileter fileter, Wholesaler wholesaler);

	public List<Map<String, Object>> getLastNDaysTotalOrderSummary(
			Date fromDate, Date toDate, User user);

	int getTotalQuantity(User user);

	OrderWeightMonthSummary getTotalWeightByMonth(String month, User user);

	Long getTotalWeight(User user);

	OrderStatusesSummary getAliveOrderSummaryByTillDate(Date lastDate, User user);

	List<Map<String, Object>> getLastNDaysOrderSummary(int days, User user);

	Page<OrderSummaryByUser> getOrderWholesalerWiseSummary(Pageable pageable,
			Retailer retailer);

	Page<OrderSummaryByUser> getOrderCustomerWiseSummary(Pageable pageable,
			Wholesaler wholesaler);

	Page<OrderSummaryByCategory> getOrderSummaryByCategory(Pageable pageable,
			User user);

	List<OrderStatusesSummary> getChunckOrderCountByStatusWise(Date fromdate,
			Date todate, User user, int chunkSize);

	OrderStatusesSummary getAliveOrderCount(Date fromDate, Date toDate, User user);
}
