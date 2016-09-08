package com.mrk.myordershop.dao.report;

import java.util.Date;

import com.mrk.myordershop.bean.Wholesaler;

public interface UserReportDAO {

	long getRetailerCountByOrderDate(Date fromDate, Date toDate,
			Wholesaler wholesaler);

	long getSupplierCountByOrderDate(Date fromDate, Date toDate,
			Wholesaler wholesaler);
}
