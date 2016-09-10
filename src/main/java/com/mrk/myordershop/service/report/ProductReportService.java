package com.mrk.myordershop.service.report;

import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.resource.OrderReportResource;

public interface ProductReportService {

	OrderReportResource getCategoryWiseOrderReports(User user);
	
	OrderReportResource getProductWiseOrderReports(int categoryId, User user);

}
