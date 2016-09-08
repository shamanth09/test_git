package com.mrk.myordershop.apicontroller.wsaler.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.dto.UserReport;
import com.mrk.myordershop.security.oauth.resolver.Owner;
import com.mrk.myordershop.service.report.UserReportService;

import io.swagger.annotations.Api;

@Controller
@RequestMapping("/api/v1/ws/reports/users")
@Api(value="User Report", tags={"User Report"}, description="User Report")
public class UserReportController {

	@Autowired
	private UserReportService userReportService;

	@RequestMapping(value="/retailers", method = RequestMethod.GET)
	public ResponseEntity<UserReport> getRetailerCount(@Owner Wholesaler wholesaler) {
		return new ResponseEntity<UserReport>(
				userReportService.getRetailerCount(wholesaler), HttpStatus.OK);
	}
	
	@RequestMapping(value="/suppliers", method = RequestMethod.GET)
	public ResponseEntity<UserReport> getSupplierCount(@Owner Wholesaler wholesaler) {
		return new ResponseEntity<UserReport>(
				userReportService.getSupplierCount(wholesaler), HttpStatus.OK);
	}

}
