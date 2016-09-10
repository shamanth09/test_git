package com.mrk.myordershop.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrk.myordershop.bean.Supplier;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.dto.BasicOrderStatusWiseSummary;
import com.mrk.myordershop.bean.dto.CustomerFilter;
import com.mrk.myordershop.bean.dto.OrderStatusSummary;
import com.mrk.myordershop.bean.dto.OrderSummaryByCategory;
import com.mrk.myordershop.bean.dto.OrderSummaryByUser;
import com.mrk.myordershop.bean.dto.OrderWeightMonthSummary;
import com.mrk.myordershop.constant.OrderStatus;

public interface WholesalerOrderSummaryDAO {

	int getTotalQuantity(User user, OrderStatus[] statuses);

	Long getTotalWeight(User user, OrderStatus[] statuses);

	OrderWeightMonthSummary getTotalWeightByMonth(Date fromDate, Date toDate,
			User user, OrderStatus[] statuses);

	/**
	 * @param user
	 * @param statuses
	 * @return get orders count, weight, quantity by status wise
	 */
	List<BasicOrderStatusWiseSummary> getOrderStatusWiseSummary(
			User user, OrderStatus[] statuses);

	Map<String, Object> gerOrderCountByTillDate(Date toDate, String groupBy,
			User user);

	List<String[]> gerOrderCountByCreatedDate(Date fromdate, Date todate,
			String groupBy, User user);

	Page<OrderSummaryByUser> getOrderWholesalerWiseSummary(Pageable pageable,
			Supplier supplier);

	Page<OrderSummaryByCategory> getOrderSummaryByCategory(Pageable pageable,
			User user);

	Page<OrderSummaryByUser> getOrderSummaryGroupBySupplier(Pageable pageable,
			CustomerFilter filter, Wholesaler wholesaler);

	OrderStatusSummary getOrderStatusSummary(String userId,
			Wholesaler wholesaler, OrderStatus orderStatus);

}
