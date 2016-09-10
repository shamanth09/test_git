package com.mrkinnoapps.myordershopadmin.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrkinnoapps.myordershopadmin.bean.constant.OrderStatus;
import com.mrkinnoapps.myordershopadmin.bean.dto.CustomerFilter;
import com.mrkinnoapps.myordershopadmin.bean.dto.OrderStatusSummary;
import com.mrkinnoapps.myordershopadmin.bean.dto.OrderStatusesSummary;
import com.mrkinnoapps.myordershopadmin.bean.dto.OrderSummaryByCategory;
import com.mrkinnoapps.myordershopadmin.bean.dto.OrderSummaryByUser;
import com.mrkinnoapps.myordershopadmin.bean.dto.OrderWeightMonthSummary;
import com.mrkinnoapps.myordershopadmin.bean.dto.RateCutResource;
import com.mrkinnoapps.myordershopadmin.bean.dto.RateCutSummaryFileter;
import com.mrkinnoapps.myordershopadmin.bean.entity.Retailer;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.bean.entity.Wholesaler;

public interface OrderSummaryDAO {

	/**
	 * @param pageable
	 * @param fileter
	 * @param wholesaler
	 * @return
	 */
	Page<RateCutResource> getRateCutSummary(Pageable pageable,
			RateCutSummaryFileter fileter, Wholesaler wholesaler);

	/**
	 * @param user
	 * @param statuses
	 * @return
	 */
	int getTotalQuantity(User user, OrderStatus[] statuses);

	/**
	 * @param user
	 * @param statuses
	 * @return
	 */
	Long getTotalWeight(User user, OrderStatus[] statuses);

	/**
	 * @param fromDate
	 * @param toDate
	 * @param user
	 * @param statuses
	 * @return
	 */
	OrderWeightMonthSummary getTotalWeightByMonth(Date fromDate, Date toDate,
			User user, OrderStatus[] statuses);

	OrderStatusesSummary gerOrderCountByTillDate(Date toDate, String groupBy,
			User user);

	List<String[]> gerOrderCountByCreatedDate(Date fromdate, Date todate,
			String groupBy, User user);
	
	OrderStatusesSummary getOrderCountByStatusWise(Date fromdate, Date todate, User user);

	Page<OrderSummaryByUser> getOrderCustomerWiseSummary(Pageable pageable,
			CustomerFilter filter, Wholesaler wholesaler);

	Page<OrderSummaryByUser> getOrderWholesalerWiseSummary(Pageable pageable,
			Retailer retailer);

	Page<OrderSummaryByCategory> getOrderSummaryByCategory(Pageable pageable,
			User user);

	OrderStatusSummary getOrderStatusSummary(String userId,
			Wholesaler wholesaler, OrderStatus orderStatus);

	OrderStatusSummary getOrderStatusSummaryByMobile(String mobile,
			Wholesaler wholesaler, OrderStatus orderStatus);

}
