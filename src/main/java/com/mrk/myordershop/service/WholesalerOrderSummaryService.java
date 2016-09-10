package com.mrk.myordershop.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrk.myordershop.bean.Supplier;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.dto.BasicOrderStatusWiseSummary;
import com.mrk.myordershop.bean.dto.OrderSummaryByCategory;
import com.mrk.myordershop.bean.dto.OrderSummaryByUser;
import com.mrk.myordershop.bean.dto.OrderWeightMonthSummary;
import com.mrk.myordershop.constant.OrderStatus;

public interface WholesalerOrderSummaryService {

	int getTotalQuantity(User user);

	OrderWeightMonthSummary getTotalWeightByMonth(String month, User user);

	Long getTotalWeight(User user, List<OrderStatus> orderStatuses);

	/**
	 * @param user
	 * @param statuses
	 * @return
	 * get orders count, weight, quantity by status wise
	 */
	List<BasicOrderStatusWiseSummary> getOrderStatusWiseSummary(
			User user, List<OrderStatus> statuses);
			
	Map<String, Object> getAliveOrderSummaryByTillDate(Date lastDate, User user);

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

}
