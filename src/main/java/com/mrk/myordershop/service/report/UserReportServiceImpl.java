package com.mrk.myordershop.service.report;

import java.util.Date;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.dto.UserReport;
import com.mrk.myordershop.config.annotation.ReadTransactional;
import com.mrk.myordershop.dao.report.UserReportDAO;

@Service
@ReadTransactional
public class UserReportServiceImpl implements UserReportService {

	@Autowired
	private UserReportDAO userReportDAO;

	@Override
	public UserReport getRetailerCount(Wholesaler wholesaler) {
		Date fromDateMonth = new LocalDate().minusMonths(5).toDate();
		Date now = new Date();
		UserReport report = new UserReport();
		report.setInActiveUserCount(userReportDAO.getRetailerCountByOrderDate(
				null, fromDateMonth, wholesaler));
		report.setActiveUserCount(userReportDAO.getRetailerCountByOrderDate(
				fromDateMonth, now, wholesaler));
		return report;
	}

	@Override
	public UserReport getSupplierCount(Wholesaler wholesaler) {
		Date fromDateMonth = new LocalDate().minusMonths(5).toDate();
		Date now = new Date();
		UserReport report = new UserReport();
		report.setInActiveUserCount(userReportDAO.getSupplierCountByOrderDate(
				null, fromDateMonth, wholesaler));
		report.setActiveUserCount(userReportDAO.getSupplierCountByOrderDate(
				fromDateMonth, now, wholesaler));
		return report;
	}

}
