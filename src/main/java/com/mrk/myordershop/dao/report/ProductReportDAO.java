package com.mrk.myordershop.dao.report;

import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.resource.OrderReportResource;

public interface ProductReportDAO {

	OrderReportResource getProductWiseOrderReports(int categoryId, User user);

	OrderReportResource getCategoryWiseOrderReports(User user);

}
