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
import com.mrkinnoapps.myordershopadmin.bean.entity.Supplier;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.bean.entity.Wholesaler;

public interface WholesalerOrderSummaryService {

	int getTotalQuantity(User user);

	OrderWeightMonthSummary getTotalWeightByMonth(String month, User user);

	Long getTotalWeight(User user);

	OrderStatusesSummary getAliveOrderSummaryByTillDate(Date lastDate, User user);

	public List<Map<String, Object>> getLastNDaysTotalOrderSummary(
			Date fromDate, Date toDate, User user);

	List<Map<String, Object>> getLastNDaysOrderSummary(int days, User user);

	Page<OrderSummaryByUser> getOrderWholesalerWiseSummary(Pageable pageable,
			Supplier supplier);

	List<Map<String, Object>> getLastNDaysTotalOrderSummary(int days,
			User wholesaler);

	Page<OrderSummaryByCategory> getOrderSummaryByCategory(Pageable pageable,
			User user);

	Page<OrderSummaryByUser> getOrderSummaryGroupBySupplier(Pageable pageable,
			Wholesaler currentUser);
	
	List<OrderStatusesSummary> getChunckOrderCountByStatusWise(Date fromdate,
			Date todate, User user,int chunkSize);

	OrderStatusesSummary getAliveOrderCount(Date fromdate, Date todate, User user);

}
