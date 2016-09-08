package com.mrk.myordershop.service.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.config.annotation.ReadTransactional;
import com.mrk.myordershop.dao.report.OrderReportDAO;
import com.mrk.myordershop.dao.report.ProductReportDAO;
import com.mrk.myordershop.resource.OrderReportResource;

@Service
@ReadTransactional
public class ProductReportServiceImpl implements ProductReportService {
	@Autowired
	private ProductReportDAO productReportDAO;
	@Autowired
	private OrderReportDAO orderReportDAO;

	@Override
	public OrderReportResource getCategoryWiseOrderReports(User user) {
		return productReportDAO.getCategoryWiseOrderReports(user);
	}
	@Override
	public OrderReportResource getProductWiseOrderReports(int categoryId, User user) {
		return productReportDAO.getProductWiseOrderReports(categoryId, user);
	}

}
