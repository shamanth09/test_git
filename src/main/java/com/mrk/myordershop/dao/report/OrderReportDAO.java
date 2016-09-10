package com.mrk.myordershop.dao.report;

import java.util.List;

import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.dto.OrderStatusWithDateSummary;
import com.mrk.myordershop.bean.dto.filter.OrderSummaryFilter;
import com.mrk.myordershop.resource.MeltingAndSealSummaryResource;

public interface OrderReportDAO {

	/**
	 * @param user
	 * @return 
	 * get Orders Counts wise orderStatus with sum of weight, quantity
	 */
	List<OrderStatusWithDateSummary> getOrderCounts(User user, OrderSummaryFilter orderFilter);

	List<MeltingAndSealSummaryResource> getMeltingSummay(Wholesaler wholesaler);
}
