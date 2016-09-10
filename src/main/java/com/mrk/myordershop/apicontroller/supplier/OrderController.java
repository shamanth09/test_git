package com.mrk.myordershop.apicontroller.supplier;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
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
import com.mrk.myordershop.bean.InstantWholesalerOrder;
import com.mrk.myordershop.bean.Supplier;
import com.mrk.myordershop.bean.WholesalerOrder;
import com.mrk.myordershop.bean.dto.OrderSummaryByCategory;
import com.mrk.myordershop.bean.dto.OrderSummaryByUser;
import com.mrk.myordershop.bean.dto.SearchFilter;
import com.mrk.myordershop.bean.dto.SearchResource;
import com.mrk.myordershop.bean.dto.WholesalerOrderFilter;
import com.mrk.myordershop.constant.OrderStatus;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.resource.Resource;
import com.mrk.myordershop.security.oauth.resolver.Owner;
import com.mrk.myordershop.service.WholeSalerOrderService;
import com.mrk.myordershop.service.WholesalerOrderSummaryService;

import io.swagger.annotations.Api;

/**
 * OrderController.java Naveen Apr 8, 2015
 */
@Controller("apiSupplierOrderController")
@RequestMapping("/api/v1/sp/orders")
@Api(value="Supplier Order Details", tags={"Supplier Order Details"})
public class OrderController {

	private static Logger log = Logger.getLogger(OrderController.class
			.getName());

	@Autowired
	private WholeSalerOrderService wholeSalerOrderService;
	@Autowired
	@Qualifier("supplierWholesalerOrderAssembler")
	private WholesalerOrderAssembler orderAssembler;
	@Autowired
	private WholesalerOrderSummaryService orderSummaryService;
	@Autowired
	private OrderSummaryAssembler summaryAssembler;
	@Autowired
	private OrderSearchAssembler orderSearchAssembler;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<PagedResources> getOrders(@Owner Supplier supplier,
			@PageableDefault(value = 10) Pageable pageable,
			@ModelAttribute WholesalerOrderFilter filter,
			PagedResourcesAssembler pagedResourcesAssembler,
			@RequestParam(required = false) OrderStatus orderStatus,
			@RequestParam(required = false) String productName,
			@RequestParam(required = false) String supplierId,
			@RequestParam(required = false) Date fromDate,
			@RequestParam(required = false) Date toDate) {

		Page<WholesalerOrder> orderPage = wholeSalerOrderService.get(pageable,
				supplier, filter);

		return new ResponseEntity<PagedResources>(
				pagedResourcesAssembler.toResource(orderPage, orderAssembler, WholesalerOrderLinkProvider.get(filter)),
				HttpStatus.OK);
	}

	@RequestMapping(value = "{orderId}/orderStatus", method = RequestMethod.PUT)
	public ResponseEntity updateOrder(@PathVariable int orderId,
			@RequestBody WholesalerOrder orderStatus, @Owner Supplier supplier)
			throws EntityDoseNotExistException {
		WholesalerOrder wsOrderfdb = wholeSalerOrderService.updateOrderStatus(
				orderId, orderStatus.getOrderStatus(), supplier);
		return new ResponseEntity(HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/{orderId}", method = RequestMethod.GET)
	public ResponseEntity<Resource<WholesalerOrder>> getOrder(
			@PathVariable int orderId, @Owner Supplier supplier)
			throws EntityDoseNotExistException {
		WholesalerOrder order = wholeSalerOrderService.get(orderId, supplier);
		return new ResponseEntity<Resource<WholesalerOrder>>(
				orderAssembler.toResource(order), HttpStatus.OK);
	}

	@RequestMapping(value = "/{orderId}/images", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getImage(@PathVariable int orderId,
			@Owner Supplier supplier) throws EntityDoseNotExistException {
		WholesalerOrder order = wholeSalerOrderService.get(orderId, supplier);
		InstantWholesalerOrder instantOrder = null;
		if (order instanceof InstantWholesalerOrder)
			instantOrder = (InstantWholesalerOrder) order;
		else {
			throw new EntityDoseNotExistException(
					"not found InstanceOrder in this id");
		}
		if (instantOrder.getImage() == null)
			throw new EntityDoseNotExistException("not found Image in this id");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		return new ResponseEntity<byte[]>(instantOrder.getImage()
				.getImageArray(), headers, HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/group", method = RequestMethod.GET)
	public ResponseEntity<PagedResources> getOrderSummary(
			@Owner Supplier supplier,
			@PageableDefault(value = 10) Pageable pageable,
			PagedResourcesAssembler pagedResourcesAssembler) {
		Page<OrderSummaryByUser> ordersPage = orderSummaryService
				.getOrderWholesalerWiseSummary(pageable, supplier);
		return new ResponseEntity<PagedResources>(
				pagedResourcesAssembler
						.toResource(ordersPage, summaryAssembler),
				HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/group/categories", method = RequestMethod.GET)
	public ResponseEntity<PagedResources> getOrderSummarybyCategory(
			@Owner Supplier supplier,
			@PageableDefault(value = 10) Pageable pageable,
			PagedResourcesAssembler pagedResourcesAssembler) {
		Page<OrderSummaryByCategory> ordersPage = orderSummaryService
				.getOrderSummaryByCategory(pageable, supplier);
		return new ResponseEntity<PagedResources>(
				pagedResourcesAssembler
						.toResource(ordersPage, summaryAssembler),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<PagedResources<SearchResource>> searchOnOrder(
			@Owner Supplier supplier,@ModelAttribute SearchFilter filter, @RequestParam("query") String query,
			PagedResourcesAssembler<SearchResource> pagedResourcesAssembler) {
		List<SearchResource> result = wholeSalerOrderService.search(filter,
				supplier);
		return new ResponseEntity<PagedResources<SearchResource>>(
				pagedResourcesAssembler.toResource(
						new PageImpl<SearchResource>(result),
						orderSearchAssembler), HttpStatus.OK);
	}
}
