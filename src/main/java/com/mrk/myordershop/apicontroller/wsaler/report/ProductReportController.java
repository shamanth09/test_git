package com.mrk.myordershop.apicontroller.wsaler.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.resource.OrderReportResource;
import com.mrk.myordershop.security.oauth.resolver.Owner;
import com.mrk.myordershop.service.report.ProductReportService;

import io.swagger.annotations.Api;

@Controller
@RequestMapping("/api/v1/ws/reports")
@Api(value="Product", tags={"Product Report"}, description="Product Report")
public class ProductReportController {

	@Autowired
	private ProductReportService productReportService;

	@RequestMapping(value = "/categories", method = RequestMethod.GET)
	public ResponseEntity<OrderReportResource> getCategoryWiseOrderReports(
			@Owner Wholesaler wholesaler) {

		return new ResponseEntity<OrderReportResource>(
				productReportService.getCategoryWiseOrderReports(wholesaler),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/categories/{categoryId}/products", method = RequestMethod.GET)
	public ResponseEntity<OrderReportResource> getProductWiseOrderReports(
			@PathVariable("categoryId") int categoryId,
			@Owner Wholesaler wholesaler) {

		return new ResponseEntity<OrderReportResource>(
				productReportService.getProductWiseOrderReports(categoryId,
						wholesaler), HttpStatus.OK);
	}

}
