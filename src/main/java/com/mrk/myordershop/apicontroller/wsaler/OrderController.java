package com.mrk.myordershop.apicontroller.wsaler;

import java.io.IOException;
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
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonView;
import com.mrk.myordershop.assembler.ItemAssembler;
import com.mrk.myordershop.assembler.OrderAssembler;
import com.mrk.myordershop.assembler.OrderSearchAssembler;
import com.mrk.myordershop.assembler.OrderSummaryAssembler;
import com.mrk.myordershop.assembler.linkprovider.OrderLinkProvider;
import com.mrk.myordershop.bean.Cancellation;
import com.mrk.myordershop.bean.Image;
import com.mrk.myordershop.bean.InstantOrder;
import com.mrk.myordershop.bean.Order;
import com.mrk.myordershop.bean.OrderAcceptance;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.WholesalerInstantOrder;
import com.mrk.myordershop.bean.dto.OrderFilter;
import com.mrk.myordershop.bean.dto.OrderSummaryByCategory;
import com.mrk.myordershop.bean.dto.OrderSummaryByUser;
import com.mrk.myordershop.bean.dto.SearchResource;
import com.mrk.myordershop.bean.dto.UserSearchFilter;
import com.mrk.myordershop.bean.dto.View;
import com.mrk.myordershop.constant.OrderStatus;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;
import com.mrk.myordershop.resource.Resource;
import com.mrk.myordershop.security.oauth.resolver.Owner;
import com.mrk.myordershop.service.ItemService;
import com.mrk.myordershop.service.OrderService;
import com.mrk.myordershop.service.OrderSummaryService;
import com.mrk.myordershop.service.UserService;
import com.mrk.myordershop.service.WholeSalerOrderService;
import com.mrk.myordershop.validator.NewOrderValidator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * OrderController.java Naveen Apr 8, 2015
 */
@Controller("apiWsalerOrderController")
@RequestMapping("/api/v1/ws/orders")
@Api(value = "Order Details", tags = { "Order Details" })
public class OrderController {

	private static Logger log = Logger.getLogger(OrderController.class.getName());

	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderSummaryService orderSummaryService;
	@Autowired
	private WholeSalerOrderService wholeSalerOrderService;
	@Autowired
	private ItemService itemService;
	@Autowired
	private UserService userService;
	@Autowired
	@Qualifier("wsalerOrderAssembler")
	private OrderAssembler orderAssembler;
	@Autowired
	@Qualifier("wholesalerItemAssembler")
	private ItemAssembler wholesalerItemAssembler;
	@Autowired
	private OrderSearchAssembler orderSearchAssembler;
	@Autowired
	private OrderSummaryAssembler summaryAssembler;
	@Autowired
	private NewOrderValidator newOrderValidator;
	
	@InitBinder(value = { "instantOrder" })
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(newOrderValidator);
	}

	@ApiOperation(value = "get orders of retailers", notes = "get list of retailer orders")
	@ApiImplicitParams({ @ApiImplicitParam(name = "orderStatus", required = false), })
	@JsonView(View.OrderModerate.class)
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<PagedResources> getOrders(@Owner Wholesaler wholesaler,
			@PageableDefault(value = 10) Pageable pageable, @ModelAttribute OrderFilter filter,
			PagedResourcesAssembler pagedResourcesAssembler, @RequestParam(required = false) OrderStatus orderStatus,
			@RequestParam(required = false) String productName, @RequestParam(required = false) String customerName,
			@RequestParam(required = false) String customerMobile, @RequestParam(required = false) String retailerId,
			@RequestParam(required = false) Integer categoryId, @RequestParam(required = false) Date fromDate,
			@RequestParam(required = false) Date toDate, @RequestParam(required = false) Date fromExpectedDate,
			@RequestParam(required = false) Date toExpectedDate) throws EntityDoseNotExistException {
		log.debug(pageable.getPageSize());
		Page<Order> ordersPage = orderService.getOrders(pageable, wholesaler, filter);

		return new ResponseEntity<PagedResources>(
				pagedResourcesAssembler.toResource(ordersPage, orderAssembler, OrderLinkProvider.get(filter)),
				HttpStatus.OK);
	}

	@JsonView(View.OrderDetailed.class)
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value = "/{orderId}", method = RequestMethod.GET)
	public ResponseEntity<Resource> getOrder(@PathVariable int orderId, @Owner Wholesaler wholesaler)
			throws EntityDoseNotExistException {
		Order order = orderService.getOrder(orderId, wholesaler);
		return new ResponseEntity<Resource>(orderAssembler.toResource(order), HttpStatus.OK);
	}

	@JsonView(View.OrderModerate.class)
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value = "/deliversby", method = RequestMethod.GET)
	public ResponseEntity<PagedResources> deliversBy(@RequestParam(required = false) Date date,
			@Owner Wholesaler wholesaler, @PageableDefault(value = 10) Pageable pageable,
			PagedResourcesAssembler pagedResourcesAssembler) throws EntityDoseNotExistException {
		Page<Order> ordersPage = orderService.deliversBy(date, pageable, wholesaler);
		return new ResponseEntity<PagedResources>(pagedResourcesAssembler.toResource(ordersPage, orderAssembler),
				HttpStatus.OK);
	}

	/**
	 * @param instantOrder
	 * @param wholesaler
	 * @return
	 * @throws EntityNotPersistedException
	 * @throws EntityDoseNotExistException
	 */
	@JsonView(View.OrderBasic.class)
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<Resource> saveWholesalerInstanceOrder(
			@Validated @RequestBody WholesalerInstantOrder instantOrder,
			@Owner Wholesaler wholesaler) throws EntityNotPersistedException, EntityDoseNotExistException {
		instantOrder.setUser(wholesaler);
		orderService.saveInstantOrder(instantOrder, wholesaler);
		return new ResponseEntity<Resource>(orderAssembler.toResource(instantOrder), HttpStatus.OK);
	}

	/**
	 * @param wholesaler
	 * @param order
	 * @param map
	 * @return Update Order Status
	 * @throws EntityDoseNotExistException
	 */
	@RequestMapping(value = "/{orderId}/orderStatus", method = RequestMethod.PUT)
	public ResponseEntity<Object> updateOrderStatus(@Owner Wholesaler wholesaler, @PathVariable("orderId") int orderId,
			@RequestBody Order order, ModelMap map) throws EntityDoseNotExistException {
		orderService.updateOrderStatus(orderId, order.getOrderStatus(), wholesaler);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@RequestMapping(value = "/{orderId}/approve", method = RequestMethod.PUT)
	public ResponseEntity<Object> orderApprove(@Owner Wholesaler wholesaler, @PathVariable("orderId") int orderId,
			@RequestBody(required = false) OrderAcceptance orderAcceptance) throws EntityDoseNotExistException {
		orderService.orderApprove(orderId, orderAcceptance, wholesaler);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@RequestMapping(value = "/{orderId}/cancel", method = RequestMethod.PUT)
	public ResponseEntity<Object> CancelOrder(@Owner Wholesaler wholesaler, @PathVariable("orderId") int orderId,
			@RequestBody Cancellation cancellation, ModelMap map) throws EntityDoseNotExistException {
		orderService.cancelOrder(orderId, cancellation, wholesaler);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<PagedResources<SearchResource>> searchOnOrder(@Owner Wholesaler wholesaler,
			@ModelAttribute UserSearchFilter filter, @RequestParam("query") String query,
			PagedResourcesAssembler<SearchResource> pagedResourcesAssembler) {
		List<SearchResource> result = orderService.search(filter, wholesaler);
		return new ResponseEntity<PagedResources<SearchResource>>(
				pagedResourcesAssembler.toResource(new PageImpl<SearchResource>(result), orderSearchAssembler),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/{orderId}/images", method = RequestMethod.POST)
	public ResponseEntity<Object> saveWholesalerInstantOrderImage(@PathVariable("orderId") int orderId,
			MultipartFile source, @Owner Wholesaler wholesaler) {
		try {
			orderService.saveWholesalerInstantOrderImage(orderId, new Image(source.getBytes()), wholesaler);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (EntityDoseNotExistException e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@RequestMapping(value = "/{orderId}/images", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getImage(@PathVariable int orderId, @Owner Wholesaler wholesaler)
			throws EntityDoseNotExistException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);

		Order order = orderService.getOrder(orderId, wholesaler);
		if (order instanceof InstantOrder) {
			InstantOrder iOrder = (InstantOrder) order;
			if (iOrder.getImage() == null)
				throw new EntityDoseNotExistException("not found Image in this id");
			return new ResponseEntity<byte[]>(iOrder.getImage().getImageArray(), headers, HttpStatus.OK);
		} else {
			if (order instanceof WholesalerInstantOrder) {
				WholesalerInstantOrder iOrder = (WholesalerInstantOrder) order;
				if (iOrder.getImage() == null)
					throw new EntityDoseNotExistException("not found Image in this id");
				log.debug(iOrder.getImage().getImageArray());
				return new ResponseEntity<byte[]>(iOrder.getImage().getImageArray(), headers, HttpStatus.OK);
			}
		}
		throw new EntityDoseNotExistException("not found Image in this id");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/group/categories", method = RequestMethod.GET)
	public ResponseEntity<PagedResources> getOrderSummarybyCategory(@Owner Wholesaler wholesaler,
			@PageableDefault(value = 10) Pageable pageable, PagedResourcesAssembler pagedResourcesAssembler) {
		Page<OrderSummaryByCategory> ordersPage = orderSummaryService.getOrderSummaryByCategory(pageable, wholesaler);
		return new ResponseEntity<PagedResources>(pagedResourcesAssembler.toResource(ordersPage, summaryAssembler),
				HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/group", method = RequestMethod.GET)
	public ResponseEntity<PagedResources> getOrderSummary(@Owner Wholesaler wholesaler,
			@PageableDefault(value = 10) Pageable pageable, PagedResourcesAssembler pagedResourcesAssembler) {
		Page<OrderSummaryByUser> ordersPage = orderSummaryService.getOrderCustomerWiseSummary(pageable, wholesaler);
		return new ResponseEntity<PagedResources>(pagedResourcesAssembler.toResource(ordersPage, summaryAssembler),
				HttpStatus.OK);
	}

}
