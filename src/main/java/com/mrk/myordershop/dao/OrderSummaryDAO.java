package com.mrk.myordershop.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrk.myordershop.bean.Retailer;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.dto.BasicOrderStatusWiseSummary;
import com.mrk.myordershop.bean.dto.CustomerFilter;
import com.mrk.myordershop.bean.dto.OrderStatusSummary;
import com.mrk.myordershop.bean.dto.OrderSummaryByCategory;
import com.mrk.myordershop.bean.dto.OrderSummaryByUser;
import com.mrk.myordershop.bean.dto.OrderWeightMonthSummary;
import com.mrk.myordershop.bean.dto.RateCutSummaryFileter;
import com.mrk.myordershop.constant.OrderStatus;
import com.mrk.myordershop.resource.RateCutResource;

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

	List<BasicOrderStatusWiseSummary> getOrderStatusWiseSummary(User user,
			OrderStatus[] statuses);

	Map<String, Object> gerOrderCountByTillDate(Date toDate, String groupBy,
			User user);

	List<String[]> gerOrderCountByCreatedDate(Date fromdate, Date todate,
			String groupBy, User user);

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
