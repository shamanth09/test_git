package com.mrk.myordershop.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mrk.myordershop.bean.Retailer;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.dto.BasicOrderStatusWiseSummary;
import com.mrk.myordershop.bean.dto.OrderSummaryByCategory;
import com.mrk.myordershop.bean.dto.OrderSummaryByUser;
import com.mrk.myordershop.bean.dto.OrderWeightMonthSummary;
import com.mrk.myordershop.bean.dto.RateCutSummaryFileter;
import com.mrk.myordershop.config.annotation.ReadTransactional;
import com.mrk.myordershop.constant.OrderStatus;
import com.mrk.myordershop.dao.OrderSummaryDAO;
import com.mrk.myordershop.resource.RateCutResource;

@Service
public class OrderSummaryServiceImpl implements OrderSummaryService {

	private static final Logger log = Logger
			.getLogger(OrderSummaryServiceImpl.class);
	@Autowired
	private OrderSummaryDAO orderSummaryDAO;
	@Autowired
	private UserService userService;

	@Override
	@ReadTransactional
	public List<Map<String, Object>> getLastNDaysOrderSummary(int days,
			User user) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		LocalDate date = new LocalDate().minusDays(--days);
		while (days >= 0) {
			Map<String, Object> statusEntity = new LinkedHashMap<String, Object>();
			statusEntity.put("date", date.toString("dd-MM-yyyy"));

			List<String[]> result = orderSummaryDAO.gerOrderCountByCreatedDate(
					date.toDate(), date.plusDays(1).toDate(), "orderStatus",
					user);
			for (OrderStatus os : OrderStatus.getRetailerStatuses()) {
				for (Object[] strings : result) {
					if (os.equals(OrderStatus.valueOf(strings[0].toString()))) {
						statusEntity.put(
								OrderStatus.valueOf(strings[0].toString())
										.getValue(), Integer.valueOf(strings[1]
										.toString()));
					}
				}
				if (!statusEntity.containsKey(os.getValue())) {
					statusEntity.put(os.getValue(), 0);
				}
			}
			resultList.add(statusEntity);
			days--;
			date = date.plusDays(1);
		}
		return resultList;
	}

	@Override
	@ReadTransactional
	public Page<RateCutResource> getRateCutSummary(Pageable pageable,
			RateCutSummaryFileter fileter, Wholesaler wholesaler) {

		return orderSummaryDAO.getRateCutSummary(pageable, fileter, wholesaler);
	}

	@Override
	@ReadTransactional
	public int getTotalQuantity(User user, Boolean isCorrently) {
		OrderStatus[] orderStatus = null;
		if (isCorrently != null && isCorrently) {
			orderStatus = new OrderStatus[] { OrderStatus.ACTIVE,
					OrderStatus.APPROVED, OrderStatus.AVAILABLE,
					OrderStatus.DISPATCHED, OrderStatus.IN_PROGRESS };
		} else {
			orderStatus = new OrderStatus[] { OrderStatus.ACTIVE,
					OrderStatus.APPROVED, OrderStatus.AVAILABLE,
					OrderStatus.DELIVERED, OrderStatus.DISPATCHED,
					OrderStatus.IN_PROGRESS };
		}
		return orderSummaryDAO.getTotalQuantity(user, orderStatus);
	}

	@Override
	@ReadTransactional
	public Long getTotalWeight(User user, List<OrderStatus> orderStatuses) {
		if (orderStatuses == null || orderStatuses.size() == 0) {
			orderStatuses = Arrays.asList(new OrderStatus[] {
					OrderStatus.ACTIVE, OrderStatus.APPROVED,
					OrderStatus.AVAILABLE, OrderStatus.DELIVERED,
					OrderStatus.DISPATCHED, OrderStatus.IN_PROGRESS });
		}
		return orderSummaryDAO.getTotalWeight(user,
				orderStatuses.toArray(new OrderStatus[orderStatuses.size()]));
	}

	@Override
	@ReadTransactional
	public List<BasicOrderStatusWiseSummary> getOrderStatusWiseSummary(
			User user, List<OrderStatus> statuses) {
		if (statuses == null)
			statuses = new ArrayList<OrderStatus>();
		return orderSummaryDAO.getOrderStatusWiseSummary(user,
				statuses.toArray(new OrderStatus[statuses.size()]));
	}

	@Override
	@ReadTransactional
	public OrderWeightMonthSummary getTotalWeightByMonth(String month,
			List<OrderStatus> orderStatuses, User user) {
		LocalDate date = LocalDate.parse(
				month + "-" + new LocalDate().getYear(),
				DateTimeFormat.forPattern("MMMM-yyyy"));
		java.util.Date fromDate = date.dayOfMonth().withMinimumValue().toDate();
		java.util.Date toDate = date.dayOfMonth().withMaximumValue().toDate();

		if (orderStatuses == null || orderStatuses.size() == 0) {
			orderStatuses = Arrays.asList(new OrderStatus[] {
					OrderStatus.ACTIVE, OrderStatus.APPROVED,
					OrderStatus.AVAILABLE, OrderStatus.DELIVERED,
					OrderStatus.DISPATCHED, OrderStatus.IN_PROGRESS });
		}
		return orderSummaryDAO.getTotalWeightByMonth(fromDate, toDate, user,
				orderStatuses.toArray(new OrderStatus[orderStatuses.size()]));
	}

	@Override
	@ReadTransactional
	public Map<String, Object> getAliveOrderSummaryByTillDate(Date lastDate,
			User user) {
		return orderSummaryDAO.gerOrderCountByTillDate(lastDate, "orderStatus",
				user);
	}

	@Override
	@ReadTransactional
	public Page<OrderSummaryByUser> getOrderCustomerWiseSummary(
			Pageable pageable, Wholesaler wholesaler) {
		return orderSummaryDAO.getOrderCustomerWiseSummary(pageable, null,
				wholesaler);
	}

	@Override
	@ReadTransactional
	public Page<OrderSummaryByUser> getOrderWholesalerWiseSummary(
			Pageable pageable, Retailer retailer) {
		return orderSummaryDAO
				.getOrderWholesalerWiseSummary(pageable, retailer);
	}

	@Override
	@ReadTransactional
	public Page<OrderSummaryByCategory> getOrderSummaryByCategory(
			Pageable pageable, User user) {
		return orderSummaryDAO.getOrderSummaryByCategory(pageable, user);
	}

	@Override
	@ReadTransactional
	public List<Map<String, Object>> getLastNDaysTotalOrderSummary(
			Date fromDate, Date toDate, User user) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		LocalDate lfromDate = new LocalDate(fromDate);
		LocalDate ltoDate = new LocalDate(toDate);
		Map<String, Object> statusEntity = new HashMap<String, Object>();
		statusEntity.put("fromDate", lfromDate.toString("dd-MM-yyyy"));
		statusEntity.put("toDate", ltoDate.toString("dd-MM-yyyy"));
		List<String[]> result = orderSummaryDAO.gerOrderCountByCreatedDate(
				lfromDate.toDate(), ltoDate.toDate(), "orderStatus", user);
		for (OrderStatus os : OrderStatus.getRetailerStatuses()) {
			for (Object[] strings : result) {
				if (os.equals(OrderStatus.valueOf(strings[0].toString()))) {
					statusEntity
							.put(OrderStatus.valueOf(strings[0].toString())
									.toString(), Integer.valueOf(strings[1]
									.toString()));
				}
			}
			if (!statusEntity.containsKey(os.toString())) {
				statusEntity.put(os.toString(), 0);
			}
		}
		resultList.add(statusEntity);
		return resultList;
	}
}
