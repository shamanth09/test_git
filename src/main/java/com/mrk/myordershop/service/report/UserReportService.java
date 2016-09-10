package com.mrk.myordershop.service.report;

import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.dto.UserReport;

public interface UserReportService {

	UserReport getRetailerCount(Wholesaler wholesaler);

	UserReport getSupplierCount(Wholesaler wholesaler);
}
