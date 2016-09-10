package com.mrk.myordershop.apicontroller.retail;

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
import com.mrk.myordershop.assembler.OrderAssembler;
import com.mrk.myordershop.assembler.OrderSearchAssembler;
import com.mrk.myordershop.assembler.OrderSummaryAssembler;
import com.mrk.myordershop.assembler.linkprovider.OrderLinkProvider;
import com.mrk.myordershop.bean.Cancellation;
import com.mrk.myordershop.bean.Image;
import com.mrk.myordershop.bean.InstantOrder;
import com.mrk.myordershop.bean.Order;
import com.mrk.myordershop.bean.Retailer;
import com.mrk.myordershop.bean.dto.OrderFilter;
import com.mrk.myordershop.bean.dto.OrderSummaryByCategory;
import com.mrk.myordershop.bean.dto.OrderSummaryByUser;
import com.mrk.myordershop.bean.dto.SearchFilter;
import com.mrk.myordershop.bean.dto.SearchResource;
import com.mrk.myordershop.bean.dto.View;
import com.mrk.myordershop.constant.OrderStatus;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;
import com.mrk.myordershop.resource.Resource;
import com.mrk.myordershop.security.oauth.resolver.Owner;
import com.mrk.myordershop.service.OrderService;
import com.mrk.myordershop.service.OrderSummaryService;
import com.mrk.myordershop.service.RetailerService;
import com.mrk.myordershop.validator.NewOrderValidator;
import com.mrk.myordershop.validator.OrderCancellationValidation;

import io.swagger.annotations.Api;

@Controller("apiOrderController")
@RequestMapping("/api/v1/rt/orders")
@Api(value = "Order Details", tags = { "Order Details" })
public class OrderController {

	private final static Logger log = Logger.getLogger(OrderController.class);
	@Autowired
	private OrderService orderService;
	@Autowired
	private RetailerService retailerService;
	@Autowired
	@Qualifier("retailOrderAssembler")
	private OrderAssembler orderAssembler;
	@Autowired
	private OrderSummaryService orderSummaryService;
	@Autowired
	private OrderSearchAssembler orderSearchAssembler;
	@Autowired
	private OrderSummaryAssembler summaryAssembler;
	@Autowired
	private NewOrderValidator newOrderValidator;
	@Autowired
	private OrderCancellationValidation cancellationValidation;
	
	@InitBinder(value = { "instantOrder" })
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(newOrderValidator);
	}
	
	@InitBinder(value = { "cancellation" })
	private void cancelOrder (WebDataBinder binder) {
		binder.setValidator(cancellationValidation);
	}

	@JsonView(View.OrderBasic.class)
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<Resource> saveInstanceOrder(@Validated @RequestBody InstantOrder instantOrder, @Owner Retailer retailer,
			ModelMap map) throws EntityDoseNotExistException, EntityNotPersistedException {
		orderService.saveInstantOrder(instantOrder, retailer);
		return new ResponseEntity<Resource>(orderAssembler.toResource(instantOrder), HttpStatus.OK);
	}

	@JsonView(View.OrderModerate.class)
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<PagedResources<Object>> getOrders(@Owner Retailer retailer,
			@PageableDefault(value = 10) Pageable pageable, @ModelAttribute OrderFilter filter,
			PagedResourcesAssembler pagedResourcesAssembler, @RequestParam(required = false) OrderStatus orderStatus,
			@RequestParam(required = false) String productName, @RequestParam(required = false) String wholesalerId,
			@RequestParam(required = false) String retailerId, @RequestParam(required = false) Integer categoryId,
			@RequestParam(required = false) Date fromDate, @RequestParam(required = false) Date toDate)
			throws EntityDoseNotExistException {
		Page<Order> page = orderService.getOrders(pageable, retailer, filter);
		return new ResponseEntity<PagedResources<Object>>(
				pagedResourcesAssembler.toResource(page, orderAssembler, OrderLinkProvider.get(filter)), HttpStatus.OK);
	}

	@JsonView(View.OrderDetailed.class)
	@RequestMapping(value = "/{orderId}", method = RequestMethod.GET)
	public ResponseEntity<Resource> getOrder(@PathVariable int orderId, @Owner Retailer retailer)
			throws EntityDoseNotExistException {
		Order order = orderService.getOrder(orderId, retailer);
		return new ResponseEntity<Resource>(orderAssembler.toResource(order), HttpStatus.OK);
	}

	/**
	 * @param wholesaler
	 * @param orderId
	 * @param order
	 * @param map
	 * @return
	 * @throws EntityDoseNotExistException
	 */
	@RequestMapping(value = "/{orderId}/cancel", method = RequestMethod.PUT)
	public ResponseEntity<Object> updateOrderStatus(@Owner Retailer retailer, @PathVariable("orderId") int orderId,
			@Validated @RequestBody Cancellation cancellation) throws EntityDoseNotExistException {
		orderService.cancelOrder(orderId, cancellation, retailer);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<PagedResources<SearchResource>> searchOnOrder(@Owner Retailer retailer,
			@ModelAttribute SearchFilter filter, @RequestParam("query") String query,
			PagedResourcesAssembler<SearchResource> pagedResourcesAssembler) {
		List<SearchResource> result = orderService.search(filter, retailer);
		return new ResponseEntity<PagedResources<SearchResource>>(
				pagedResourcesAssembler.toResource(new PageImpl<SearchResource>(result), orderSearchAssembler),
				HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/group", method = RequestMethod.GET)
	public ResponseEntity<PagedResources> getOrderSummary(@Owner Retailer retailer,
			@PageableDefault(value = 10) Pageable pageable, PagedResourcesAssembler pagedResourcesAssembler) {
		Page<OrderSummaryByUser> ordersPage = orderSummaryService.getOrderWholesalerWiseSummary(pageable, retailer);
		return new ResponseEntity<PagedResources>(pagedResourcesAssembler.toResource(ordersPage, summaryAssembler),
				HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/group/categories", method = RequestMethod.GET)
	public ResponseEntity<PagedResources> getOrderSummarybyCategory(@Owner Retailer retailer,
			@PageableDefault(value = 10) Pageable pageable, PagedResourcesAssembler pagedResourcesAssembler) {
		Page<OrderSummaryByCategory> ordersPage = orderSummaryService.getOrderSummaryByCategory(pageable, retailer);
		return new ResponseEntity<PagedResources>(pagedResourcesAssembler.toResource(ordersPage, summaryAssembler),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/{orderId}/images", method = RequestMethod.POST)
	public ResponseEntity getUpdateInstanceOrderImage(@PathVariable("orderId") int orderId, MultipartFile source,
			@Owner Retailer retailer) {
		try {
			orderService.saveInstantOrderImage(orderId, new Image(source.getBytes()), retailer);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (EntityDoseNotExistException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(value = "/{orderId}/images", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getImage(@PathVariable int orderId, @Owner Retailer retailer)
			throws EntityDoseNotExistException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);

		Order order = orderService.getOrder(orderId, retailer);
		if (order instanceof InstantOrder) {
			InstantOrder instantOrder = (InstantOrder) order;
			if (instantOrder.getImage() == null)
				throw new EntityDoseNotExistException("not found Image in this id");
			return new ResponseEntity<byte[]>(instantOrder.getImage().getImageArray(), headers, HttpStatus.OK);
		}
		throw new EntityDoseNotExistException("not found InstanceOrder in this id");
	}
}
