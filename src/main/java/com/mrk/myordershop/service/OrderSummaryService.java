package com.mrk.myordershop.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrk.myordershop.bean.Retailer;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.dto.BasicOrderStatusWiseSummary;
import com.mrk.myordershop.bean.dto.OrderSummaryByCategory;
import com.mrk.myordershop.bean.dto.OrderSummaryByUser;
import com.mrk.myordershop.bean.dto.OrderWeightMonthSummary;
import com.mrk.myordershop.bean.dto.RateCutSummaryFileter;
import com.mrk.myordershop.constant.OrderStatus;
import com.mrk.myordershop.resource.RateCutResource;

public interface OrderSummaryService {

	Page<RateCutResource> getRateCutSummary(Pageable pageable,
			RateCutSummaryFileter fileter, Wholesaler wholesaler);

	public List<Map<String, Object>> getLastNDaysTotalOrderSummary(
			Date fromDate, Date toDate, User user);

	int getTotalQuantity(User user, Boolean isCorrently);

	/**
	 * @param user
	 * @param statuses
	 * @return
	 * get orders count, weight, quantity by status wise
	 */
	List<BasicOrderStatusWiseSummary> getOrderStatusWiseSummary(
			User user, List<OrderStatus> statuses);

	OrderWeightMonthSummary getTotalWeightByMonth(String month,
			List<OrderStatus> orderStatuses, User user);

	Long getTotalWeight(User user, List<OrderStatus> orderStatuses);

	Map<String, Object> getAliveOrderSummaryByTillDate(Date lastDate, User user);

	List<Map<String, Object>> getLastNDaysOrderSummary(int days, User user);

	Page<OrderSummaryByUser> getOrderWholesalerWiseSummary(Pageable pageable,
			Retailer retailer);

	Page<OrderSummaryByUser> getOrderCustomerWiseSummary(Pageable pageable,
			Wholesaler wholesaler);

	Page<OrderSummaryByCategory> getOrderSummaryByCategory(Pageable pageable,
			User user);
}
