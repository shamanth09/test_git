package com.mrk.myordershop.apicontroller.wsaler.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.resource.OrderReportResource;
import com.mrk.myordershop.security.oauth.resolver.Owner;
import com.mrk.myordershop.service.report.WholesalerOrderReportService;

import io.swagger.annotations.Api;

@Controller
@RequestMapping("/api/v1/ws/reports/sporders")
@Api(value="Supplier Order Report", tags={"Supplier Order Report"}, description="Supplier Order Report")
public class SupplierOrderReportController {

	@Autowired
	private WholesalerOrderReportService orderReportService;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<OrderReportResource> getOrderCounts(
			@Owner Wholesaler wholesaler) {

		return new ResponseEntity<OrderReportResource>(
				orderReportService.getOrderCounts(wholesaler), HttpStatus.OK);
	}
}
