package com.mrk.myordershop.apicontroller.wsaler.report;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.dto.filter.OrderSummaryFilter;
import com.mrk.myordershop.resource.DateWiseOrderReportResource;
import com.mrk.myordershop.resource.MeltingAndSealSummaryResource;
import com.mrk.myordershop.resource.OrderReportResource;
import com.mrk.myordershop.security.oauth.resolver.Owner;
import com.mrk.myordershop.service.report.OrderReportService;

import io.swagger.annotations.Api;

@Controller
@RequestMapping("/api/v1/ws/reports/orders")
@Api(value="Order Report", tags={"Order Report"}, description="Order Report")
public class OrderReportController {

	@Autowired
	private OrderReportService orderReportService;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<OrderReportResource> getOrderCounts(
			@Owner Wholesaler wholesaler) {
		return new ResponseEntity<OrderReportResource>(
				orderReportService.getOrderCounts(wholesaler), HttpStatus.OK);
	}

	@RequestMapping(value = "/time-line", method = RequestMethod.GET)
	public ResponseEntity<List<DateWiseOrderReportResource>> getDateWiseOrderCounts(
			@ModelAttribute OrderSummaryFilter filter,
			@RequestParam(required = false, defaultValue = "30") int chunkSize,
			@Owner Wholesaler wholesaler) {
		filter.setChunkSize(chunkSize);
		return new ResponseEntity<List<DateWiseOrderReportResource>>(
				orderReportService.getDateWiseOrderCounts(filter,
						wholesaler), HttpStatus.OK);
	}

	@RequestMapping(value = "/meltings", method = RequestMethod.GET)
	public ResponseEntity<List<MeltingAndSealSummaryResource>> getMeltingSummay(
			@Owner Wholesaler wholesaler) {

		return new ResponseEntity<List<MeltingAndSealSummaryResource>>(
				orderReportService.getMeltingSummay(wholesaler), HttpStatus.OK);
	}
}
