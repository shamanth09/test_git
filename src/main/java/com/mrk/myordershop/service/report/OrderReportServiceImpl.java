package com.mrk.myordershop.service.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.dto.filter.OrderSummaryFilter;
import com.mrk.myordershop.config.annotation.ReadTransactional;
import com.mrk.myordershop.dao.report.OrderReportDAO;
import com.mrk.myordershop.resource.DateWiseOrderReportResource;
import com.mrk.myordershop.resource.MeltingAndSealSummaryResource;
import com.mrk.myordershop.resource.OrderReportResource;

@Service
@ReadTransactional
public class OrderReportServiceImpl implements OrderReportService {

	@Autowired
	private OrderReportDAO orderReportDAO;

	@Override
	public OrderReportResource getOrderCounts(User user) {
		OrderReportResource resource = new OrderReportResource();
		resource.setOrderStatusSummaries(orderReportDAO.getOrderCounts(user,
				null));
		return resource;
	}

	@Override
	public List<MeltingAndSealSummaryResource> getMeltingSummay(
			Wholesaler wholesaler) {
		return orderReportDAO.getMeltingSummay(wholesaler);
	}

	@Override
	public List<DateWiseOrderReportResource> getDateWiseOrderCounts(
			OrderSummaryFilter filter, Wholesaler wholesaler) {
		List<DateWiseOrderReportResource> summaries = new ArrayList<DateWiseOrderReportResource>();
		int chunkSize = filter.getChunkSize();
		if (filter.getFromDate() == null)
			filter.setFromDate(new LocalDate().minusDays(chunkSize).toDate());
		if (filter.getToDate() == null)
			filter.setToDate(new Date());
		long from = new DateTime(filter.getFromDate()).getMillis();
		long to = new DateTime(filter.getToDate()).getMillis();
		long checkSize = (to - from) / chunkSize;
		for (int i = 1; i <= chunkSize; i++) {
			filter.setToDate(new Date(from + (checkSize * i)));
			summaries.add(new DateWiseOrderReportResource(orderReportDAO
					.getOrderCounts(wholesaler, filter), filter.getFromDate(),
					filter.getToDate()));
			filter.setFromDate(filter.getToDate());
		}
		return summaries;
	}
}
