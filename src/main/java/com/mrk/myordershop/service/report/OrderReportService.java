package com.mrk.myordershop.service.report;

import java.util.List;

import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.dto.filter.OrderSummaryFilter;
import com.mrk.myordershop.resource.DateWiseOrderReportResource;
import com.mrk.myordershop.resource.MeltingAndSealSummaryResource;
import com.mrk.myordershop.resource.OrderReportResource;

public interface OrderReportService {

	OrderReportResource getOrderCounts(User user);

	List<MeltingAndSealSummaryResource> getMeltingSummay(Wholesaler wholesaler);

	List<DateWiseOrderReportResource> getDateWiseOrderCounts(
			OrderSummaryFilter filter, Wholesaler wholesaler);
}
