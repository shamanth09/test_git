package com.mrk.myordershop.apicontroller.wsaler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mrk.myordershop.assembler.OrderSummaryAssembler;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.dto.OrderWeightMonthSummary;
import com.mrk.myordershop.bean.dto.RateCutSummaryFileter;
import com.mrk.myordershop.constant.OrderStatus;
import com.mrk.myordershop.resource.RateCutResource;
import com.mrk.myordershop.security.oauth.resolver.Owner;
import com.mrk.myordershop.service.OrderService;
import com.mrk.myordershop.service.OrderSummaryService;
import com.mrk.myordershop.util.ExpandableCustomDateEditor;
import com.mrk.myordershop.util.JsonTimeStampSerializer;

import io.swagger.annotations.Api;

@Controller("apiWsalerOrderSummaryController")
@RequestMapping("/api/v1/ws/orders/summaries")
@Api(value="Order Summaries", tags={"Order Summaries"})
public class OrderSummaryController {

	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderSummaryService orderSummaryService;
	@Autowired
	private OrderSummaryAssembler summaryAssembler;

	@InitBinder
	private void initBuinder(WebDataBinder binder) {
		DateFormat formate = new SimpleDateFormat("dd-MM-yyyy");
		formate.setTimeZone(TimeZone.getDefault());
		formate.setLenient(false);

		binder.registerCustomEditor(Date.class, new ExpandableCustomDateEditor(
				formate, JsonTimeStampSerializer.getFormates(), true));
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<List<Map<String, Object>>> getRtOrderSummary(
			@RequestParam(required = false, defaultValue = "10") int days,
			@RequestParam(required = false, defaultValue = "true") boolean isDayly,
			@RequestParam(required = false) Date fromDate,
			@RequestParam(required = false) Date toDate,
			@Owner Wholesaler wholesaler) {

		List<Map<String, Object>> result = null;
		if (isDayly) {
			result = orderSummaryService.getLastNDaysOrderSummary(days,
					wholesaler);
		} else {
			if (fromDate == null)
				fromDate = new LocalDate().minusDays(--days).toDate();
			if (toDate == null)
				toDate = new LocalDate().toDate();
			result = orderSummaryService.getLastNDaysTotalOrderSummary(
					fromDate, toDate, wholesaler);
		}

		return new ResponseEntity<List<Map<String, Object>>>(result,
				HttpStatus.OK);
	}

	@RequestMapping(value = "/alive", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getAliveWsOrderSummary(
			@RequestParam(required = false, defaultValue = "0") int minusLastDays,
			@RequestParam(required = false, defaultValue = "true") boolean isDayly,
			@Owner Wholesaler wholesaler) {

		Date tillDate = minusLastDays > 0 ? new LocalDate().minusDays(
				minusLastDays).toDate() : null;
		Map<String, Object> result = orderSummaryService
				.getAliveOrderSummaryByTillDate(tillDate, wholesaler);
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/quantity", method = RequestMethod.GET)
	public ResponseEntity<Map> getTotalQuantity(@Owner Wholesaler wholesaler, @RequestParam(required=false) Boolean isCorrently) {
		Map<String, Object> relust = new HashMap<String, Object>();
		relust.put("count", orderSummaryService.getTotalQuantity(wholesaler, isCorrently));
		return new ResponseEntity<Map>(relust, HttpStatus.OK);
	}

	@RequestMapping(value = "/weight", method = RequestMethod.GET)
	public ResponseEntity getTotalWeight(@Owner Wholesaler wholesaler,
			@RequestParam(required = false) String month,
			@RequestParam(required = false) List<OrderStatus> orderStatuses) {
		if (month != null && !month.trim().equals("")) {
			return new ResponseEntity<OrderWeightMonthSummary>(
					orderSummaryService.getTotalWeightByMonth(month,
							orderStatuses, wholesaler), HttpStatus.OK);

		} else {
			Map<String, Object> relust = new HashMap<String, Object>();
			relust.put("count", orderSummaryService.getTotalWeight(wholesaler, orderStatuses));
			return new ResponseEntity<Map>(relust, HttpStatus.OK);
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/ratecut", method = RequestMethod.GET)
	public ResponseEntity<PagedResources<RateCutResource>> getOrderRateCutSummary(
			@PageableDefault(value = 10) Pageable pageable,
			@Owner Wholesaler wholesaler,
			PagedResourcesAssembler pagedResourcesAssembler,
			@ModelAttribute RateCutSummaryFileter fileter,
			@RequestParam(required = false) Double fromRate,
			@RequestParam(required = false) Double toRate,
			@RequestParam(required = false) List<OrderStatus> orderStatus
			) {
		Page<RateCutResource> page = orderSummaryService.getRateCutSummary(
				pageable, fileter, wholesaler);
		return new ResponseEntity<PagedResources<RateCutResource>>(
				pagedResourcesAssembler.toResource(page, summaryAssembler),
				HttpStatus.OK);
	}
}
