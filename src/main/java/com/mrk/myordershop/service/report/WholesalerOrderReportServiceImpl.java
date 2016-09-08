package com.mrk.myordershop.service.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.config.annotation.ReadTransactional;
import com.mrk.myordershop.dao.report.WholesalerOrderReportDAO;
import com.mrk.myordershop.resource.OrderReportResource;

@Service
@ReadTransactional
public class WholesalerOrderReportServiceImpl implements WholesalerOrderReportService {

	@Autowired
	private WholesalerOrderReportDAO orderReportDAO;

	@Override
	public OrderReportResource getOrderCounts(User user) {
		return orderReportDAO.getOrderCounts(user);
	}
}
