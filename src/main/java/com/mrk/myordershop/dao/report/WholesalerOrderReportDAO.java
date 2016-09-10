package com.mrk.myordershop.dao.report;

import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.resource.OrderReportResource;

public interface WholesalerOrderReportDAO {

	OrderReportResource getOrderCounts(User user);
}
