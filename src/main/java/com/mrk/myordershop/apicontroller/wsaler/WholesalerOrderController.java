package com.mrk.myordershop.apicontroller.wsaler;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mrk.myordershop.assembler.OrderSearchAssembler;
import com.mrk.myordershop.assembler.OrderSummaryAssembler;
import com.mrk.myordershop.assembler.WholesalerOrderAssembler;
import com.mrk.myordershop.assembler.linkprovider.WholesalerOrderLinkProvider;
import com.mrk.myordershop.bean.Cancellation;
import com.mrk.myordershop.bean.InstantWholesalerOrder;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.WholesalerOrder;
import com.mrk.myordershop.bean.dto.OrderSummaryByUser;
import com.mrk.myordershop.bean.dto.SearchFilter;
import com.mrk.myordershop.bean.dto.SearchResource;
import com.mrk.myordershop.bean.dto.WholesalerOrderFilter;
import com.mrk.myordershop.constant.OrderStatus;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.resource.Resource;
import com.mrk.myordershop.security.oauth.resolver.Owner;
import com.mrk.myordershop.service.ItemService;
import com.mrk.myordershop.service.WholeSalerOrderService;
import com.mrk.myordershop.service.WholesalerOrderSummaryService;
import com.mrk.myordershop.validator.NewOrderValidator;
import com.mrk.myordershop.validator.OrderCancellationValidation;

import io.swagger.annotations.Api;

/**
 * SupplierOrderController.java Naveen Apr 11, 2015
 */
@Controller("apiWSalerSupplierOrderController")
@RequestMapping("/api/v1/ws/sporders")
@Api(value = "Supplier Order Details", tags = { "Supplier Order Details" })
public class WholesalerOrderController {

	@Autowired
	private WholeSalerOrderService wholeSalerOrderService;
	@Autowired
	private WholesalerOrderSummaryService orderSummaryService;
	@Autowired
	@Qualifier("wholesalerOrderAssembler")
	private WholesalerOrderAssembler wholesalerOrderAssembler;
	@Autowired
	private ItemService itemService;
	@Autowired
	private OrderSearchAssembler orderSearchAssembler;
	@Autowired
	private OrderSummaryAssembler summaryAssembler;
	@Autowired
	private NewOrderValidator newOrderValidator;
	@Autowired
	private OrderCancellationValidation cancellationValidation;
	
	@InitBinder(value = { "instantWholesalerOrder" })
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(newOrderValidator);
	}
	
	@InitBinder(value = { "cancellation" })
	private void cancelOrder (WebDataBinder binder) {
		binder.setValidator(cancellationValidation);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<PagedResources> getOrders(@Owner Wholesaler wholesaler,
			@PageableDefault(value = 10) Pageable pageable, @ModelAttribute WholesalerOrderFilter filter,
			@RequestParam(required = false) OrderStatus orderStatus, @RequestParam(required = false) String productName,
			@RequestParam(required = false) String supplierId, @RequestParam(required = false) Date fromDate,
			@RequestParam(required = false) Date toDate, PagedResourcesAssembler pagedResourcesAssembler) {
		Page<WholesalerOrder> order = wholeSalerOrderService.get(pageable, wholesaler, filter);
		return new ResponseEntity<PagedResources>(pagedResourcesAssembler.toResource(order, wholesalerOrderAssembler,
				WholesalerOrderLinkProvider.get(filter)), HttpStatus.OK);
	}

	/**
	 * @param wholesaler
	 * @param wsorder
	 * @return {"item":{"id":"1"}, "type":"TYPE_INSTANT", "wholesalerItem":{
	 *         "detail":{ "meltingAndSeal":"GP_KDM_80", "weigth":"2.0",
	 *         "quantity":"2" } },
	 *         "supplier":{"id":"402881864e10444f014e104462030000" },
	 *         "description":"kk" }
	 * @throws EntityDoseNotExistException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<Object> saveOrder(@Validated @RequestBody InstantWholesalerOrder wsInstantOrder,
			@Owner Wholesaler wholesaler) throws EntityDoseNotExistException {
		WholesalerOrder wholesalerOrder = wholeSalerOrderService.save(wsInstantOrder, wholesaler);
		return new ResponseEntity(wholesalerOrderAssembler.toResource(wholesalerOrder), HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/{orderId}", method = RequestMethod.GET)
	public ResponseEntity<Resource> getOrder(@PathVariable int orderId, @Owner Wholesaler wholesaler)
			throws EntityDoseNotExistException {
		WholesalerOrder order = null;
		order = wholeSalerOrderService.get(orderId, wholesaler);
		return new ResponseEntity<Resource>(wholesalerOrderAssembler.toResource(order), HttpStatus.OK);
	}

	/**
	 * @param orderId
	 * @param orderStatus
	 * @param supplier
	 * @return
	 * @throws EntityDoseNotExistException
	 *             Wholesaler can change status to InProgress and Dispatched for
	 *             supplier order by role of supplier
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "{orderId}/orderStatus", method = RequestMethod.PUT)
	public ResponseEntity updateOrder(@PathVariable int orderId, @RequestBody WholesalerOrder orderStatus,
			@Owner Wholesaler wholesaler) throws EntityDoseNotExistException {
		WholesalerOrder wsOrderfdb = wholeSalerOrderService.updateOrderStatus(orderId, orderStatus.getOrderStatus(),
				wholesaler);
		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(value = "/{orderId}/cancel", method = RequestMethod.PUT)
	public ResponseEntity<Object> updateOrderStatus(@Owner Wholesaler wholesaler, @PathVariable("orderId") int orderId,
			@Validated @RequestBody Cancellation cancellation) throws EntityDoseNotExistException {
		wholeSalerOrderService.cancelOrder(orderId, cancellation, wholesaler);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@RequestMapping(value = "/{orderId}/images", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getImage(@PathVariable int orderId, @Owner Wholesaler wholesaler)
			throws EntityDoseNotExistException {
		WholesalerOrder order = wholeSalerOrderService.get(orderId, wholesaler);
		InstantWholesalerOrder instantOrder = null;
		if (order instanceof InstantWholesalerOrder)
			instantOrder = (InstantWholesalerOrder) order;
		else {
			throw new EntityDoseNotExistException("not found InstanceOrder in this id");
		}
		if (instantOrder.getImage() == null)
			throw new EntityDoseNotExistException("not found Image in this id");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		return new ResponseEntity<byte[]>(instantOrder.getImage().getImageArray(), headers, HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/group", method = RequestMethod.GET)
	public ResponseEntity<PagedResources> getOrderSummary(@Owner Wholesaler wholesaler,
			@PageableDefault(value = 10) Pageable pageable, PagedResourcesAssembler pagedResourcesAssembler) {
		Page<OrderSummaryByUser> ordersPage = orderSummaryService.getOrderSummaryGroupBySupplier(pageable, wholesaler);
		return new ResponseEntity<PagedResources>(pagedResourcesAssembler.toResource(ordersPage, summaryAssembler),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<PagedResources<SearchResource>> searchOnOrder(@Owner Wholesaler wholesaler,
			@ModelAttribute SearchFilter filter, @RequestParam("query") String query,
			PagedResourcesAssembler<SearchResource> pagedResourcesAssembler) {
		List<SearchResource> result = wholeSalerOrderService.search(filter, wholesaler);
		return new ResponseEntity<PagedResources<SearchResource>>(
				pagedResourcesAssembler.toResource(new PageImpl<SearchResource>(result), orderSearchAssembler),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/summaries", method = RequestMethod.GET)
	public ResponseEntity<List<Map<String, Object>>> getWsOrderSummary(
			@RequestParam(required = false, defaultValue = "10") int days,
			@RequestParam(required = false, defaultValue = "true") boolean isDayly, @Owner Wholesaler wholesaler) {
		List<Map<String, Object>> result = null;
		if (isDayly) {
			result = orderSummaryService.getLastNDaysOrderSummary(days, wholesaler);
		} else {
			result = orderSummaryService.getLastNDaysTotalOrderSummary(days, wholesaler);
		}

		return new ResponseEntity<List<Map<String, Object>>>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/summaries/alive", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getAliveWsOrderSummary(
			@RequestParam(required = false, defaultValue = "0") int minusLastDays,
			@RequestParam(required = false, defaultValue = "true") boolean isDayly, @Owner Wholesaler wholesaler) {
		Date tillDate = null;
		if (minusLastDays > 0)
			tillDate = new LocalDate().minusDays(minusLastDays).toDate();

		Map<String, Object> result = orderSummaryService.getAliveOrderSummaryByTillDate(tillDate, wholesaler);
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}
}
