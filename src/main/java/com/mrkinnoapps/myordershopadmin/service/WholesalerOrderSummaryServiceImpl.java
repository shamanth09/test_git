package com.mrkinnoapps.myordershopadmin.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mrkinnoapps.myordershopadmin.bean.constant.OrderStatus;
import com.mrkinnoapps.myordershopadmin.bean.dto.OrderStatusesSummary;
import com.mrkinnoapps.myordershopadmin.bean.dto.OrderSummaryByCategory;
import com.mrkinnoapps.myordershopadmin.bean.dto.OrderSummaryByUser;
import com.mrkinnoapps.myordershopadmin.bean.dto.OrderWeightMonthSummary;
import com.mrkinnoapps.myordershopadmin.bean.entity.Supplier;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.bean.entity.Wholesaler;
import com.mrkinnoapps.myordershopadmin.dao.WholesalerOrderSummaryDAO;

@Service
public class WholesalerOrderSummaryServiceImpl implements
		WholesalerOrderSummaryService {

	@Autowired
	private WholesalerOrderSummaryDAO orderSummaryDAO;

	@Override
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

			for (OrderStatus os : OrderStatus.getWholesalerStatuses()) {
				for (Object[] strings : result) {
					if (os.equals(OrderStatus.valueOf(strings[0].toString()))) {
						statusEntity.put(os.getValue(),
								Integer.valueOf(strings[1].toString()));
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
	public int getTotalQuantity(User user) {
		return orderSummaryDAO.getTotalQuantity(user, new OrderStatus[] {
				OrderStatus.ACTIVE, OrderStatus.APPROVED,
				OrderStatus.AVAILABLE, OrderStatus.DELIVERED,
				OrderStatus.DISPATCHED, OrderStatus.IN_PROGRESS });
	}

	@Override
	public Long getTotalWeight(User user) {
		return orderSummaryDAO.getTotalWeight(user, new OrderStatus[] {
				OrderStatus.ACTIVE, OrderStatus.APPROVED,
				OrderStatus.AVAILABLE, OrderStatus.DELIVERED,
				OrderStatus.DISPATCHED, OrderStatus.IN_PROGRESS });
	}

	@Override
	public OrderWeightMonthSummary getTotalWeightByMonth(String month, User user) {
		LocalDate date = LocalDate.parse(
				month + "-" + new LocalDate().getYear(),
				DateTimeFormat.forPattern("MMMM-yyyy"));
		java.util.Date fromDate = date.dayOfMonth().withMinimumValue().toDate();
		java.util.Date toDate = date.dayOfMonth().withMaximumValue().toDate();

		return orderSummaryDAO.getTotalWeightByMonth(fromDate, toDate, user, new OrderStatus[] {
				OrderStatus.ACTIVE, OrderStatus.APPROVED,
				OrderStatus.AVAILABLE, OrderStatus.DELIVERED,
				OrderStatus.DISPATCHED, OrderStatus.IN_PROGRESS });
	}

	@Override
	public OrderStatusesSummary getAliveOrderSummaryByTillDate(Date lastDate,
			User user) {
		return orderSummaryDAO.gerOrderCountByTillDate(lastDate, "orderStatus",
				user);
	}

	@Override
	public Page<OrderSummaryByUser> getOrderWholesalerWiseSummary(
			Pageable pageable, Supplier supplier) {
		return orderSummaryDAO
				.getOrderWholesalerWiseSummary(pageable, supplier);
	}

	@Override
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
		for (OrderStatus os : OrderStatus.getWholesalerStatuses()) {
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

	@Override
	public Page<OrderSummaryByUser> getOrderSummaryGroupBySupplier(
			Pageable pageable, Wholesaler currentUser) {
		return orderSummaryDAO.getOrderSummaryGroupBySupplier(pageable, currentUser);
	}

	@Override
	public List<Map<String, Object>> getLastNDaysTotalOrderSummary(int days,
			User currentUser) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		LocalDate fromDate = new LocalDate().minusDays(--days);
		LocalDate toDate = new LocalDate();
		Map<String, Object> statusEntity = new HashMap<String, Object>();
		statusEntity.put("fromDate", fromDate.toString("dd-MM-yyyy"));
		statusEntity.put("toDate", toDate.toString("dd-MM-yyyy"));

		List<String[]> result = orderSummaryDAO
				.gerOrderCountByCreatedDate(fromDate.toDate(),
						toDate.toDate(), "orderStatus", currentUser);

		for (OrderStatus os : OrderStatus.getWholesalerStatuses()) {
			for (Object[] strings : result) {
				if (os.equals(OrderStatus.valueOf(strings[0].toString()))) {
					statusEntity.put(os.getValue(),
							Integer.valueOf(strings[1].toString()));
				}
			}
			if (!statusEntity.containsKey(os.getValue())) {
				statusEntity.put(os.getValue(), 0);
			}
		}
		resultList.add(statusEntity);
		return resultList;
	}

	@Override
	public Page<OrderSummaryByCategory> getOrderSummaryByCategory(
			Pageable pageable, User user) {
		return orderSummaryDAO.getOrderSummaryByCategory(pageable, user);
	}

	@Override
	public List<OrderStatusesSummary> getChunckOrderCountByStatusWise(
			Date fromdate, Date todate, User user, int chunkSize) {
		if(fromdate == null)
			fromdate = new LocalDate().minusDays(14).toDate();
		if(todate == null)
			todate = new Date();
		List<OrderStatusesSummary> summaries = new ArrayList<OrderStatusesSummary>();
		long from = new DateTime(fromdate).getMillis();
		long to  = new DateTime(todate).getMillis();
		long checkSize = (to-from)/chunkSize;
		for(int i=1; i<=chunkSize;i++){
			todate = new Date(from+(checkSize*i));
			summaries.add(orderSummaryDAO.getOrderCountByStatusWise(fromdate,todate, user));
			fromdate = todate;
		}
		return summaries;
	}

	@Override
	public OrderStatusesSummary getAliveOrderCount(Date fromdate, Date todate,
			User user) {
		return orderSummaryDAO.getOrderCountByStatusWise(fromdate, todate, user);
	}

}
