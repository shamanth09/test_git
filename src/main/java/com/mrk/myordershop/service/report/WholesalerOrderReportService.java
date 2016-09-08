package com.mrk.myordershop.service.report;

import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.resource.OrderReportResource;

public interface WholesalerOrderReportService {

	OrderReportResource getOrderCounts(User user);
}
