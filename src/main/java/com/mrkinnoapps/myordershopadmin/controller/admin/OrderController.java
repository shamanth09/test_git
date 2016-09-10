package com.mrkinnoapps.myordershopadmin.controller.admin;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.mrkinnoapps.myordershopadmin.bean.constant.ActiveFlag;
import com.mrkinnoapps.myordershopadmin.bean.constant.OrderStatus;
import com.mrkinnoapps.myordershopadmin.bean.constant.Role;
import com.mrkinnoapps.myordershopadmin.bean.dto.OrderFilter;
import com.mrkinnoapps.myordershopadmin.bean.dto.WholesalerOrderFilter;
import com.mrkinnoapps.myordershopadmin.bean.entity.Image;
import com.mrkinnoapps.myordershopadmin.bean.entity.InstantOrder;
import com.mrkinnoapps.myordershopadmin.bean.entity.Measurement;
import com.mrkinnoapps.myordershopadmin.bean.entity.Order;
import com.mrkinnoapps.myordershopadmin.bean.entity.Supplier;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.bean.entity.Wholesaler;
import com.mrkinnoapps.myordershopadmin.bean.entity.WholesalerInstantOrder;
import com.mrkinnoapps.myordershopadmin.bean.entity.WholesalerOrder;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;
import com.mrkinnoapps.myordershopadmin.service.CategoryService;
import com.mrkinnoapps.myordershopadmin.service.ItemService;
import com.mrkinnoapps.myordershopadmin.service.MeltingAndSealService;
import com.mrkinnoapps.myordershopadmin.service.OrderService;
import com.mrkinnoapps.myordershopadmin.service.UserService;
import com.mrkinnoapps.myordershopadmin.service.WholeSalerOrderService;

@Controller
@RequestMapping("/v1/admin/orders")
public class OrderController {
	private static Logger log = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private OrderService orderservice;
	@Autowired
	private UserService userservice;
	@Autowired
	private MeltingAndSealService meltingAndSealingService;
	@Autowired
	private CategoryService categoryService;

	@Autowired
	ItemService itemservice;

	@Autowired
	private WholeSalerOrderService wholeSalerOrderService;

	@InitBinder
	private void initBinder(WebDataBinder binder) {
		binder.addCustomFormatter(new DateFormatter("E MMM dd yyyy HH:mm:ss 'GMT'"));
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getOrders(@RequestParam(required = false) String activeId, ModelMap map) {
		map.addAttribute("activeId", activeId);
		return new ModelAndView("admin/orders/orders");
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView saveOrders(@ModelAttribute Order order) {
		orderservice.saveOrder(order);
		return new ModelAndView("admin/orders/orders");
	}

	@RequestMapping(value = "/xhr/list", method = RequestMethod.GET)
	public String getXHROrder(@RequestParam(required = false) String activeId, ModelMap map, OrderFilter filter,
			WholesalerOrderFilter wholesalerOrderFilter, @PageableDefault(size = 10) Pageable pageable)
					throws EntityDoseNotExistException {
		Page<Order> page = orderservice.getOrders(pageable, activeId, filter);
		map.addAttribute("page", page);
		map.addAttribute("pageable", pageable);
		map.addAttribute("totalcount", page.getContent().size());
		return "admin/orders/xhr/list";
	}

	@RequestMapping(value = "/xhr/{orderId}", method = RequestMethod.GET)
	public ModelAndView getXHROrder(@PathVariable("orderId") Integer orderId, ModelMap map,
			@RequestParam(value = "activeId", required = false) String activeId) throws EntityDoseNotExistException {
		Order order = null;
		if (activeId == null || activeId.isEmpty()) {
			order = orderservice.getOrder(orderId);
		} else {
			order = orderservice.getOrder(orderId, activeId);
		}
		map.addAttribute("order", order);
		map.addAttribute("WholesalerInstantOrder", WholesalerInstantOrder.class);
		return new ModelAndView("admin/orders/xhr/orderview");
	}

	@RequestMapping(value = "/xhr/{orderId}/edit", method = RequestMethod.GET)
	public ModelAndView getXHRORderEdit(@PathVariable("orderId") Integer orderId, ModelMap map,
			@RequestParam(value = "activeId", required = false) String activeId) throws EntityDoseNotExistException {
		Order order = null;
		if (activeId == null || activeId.isEmpty()) {
			order = orderservice.getOrder(orderId);
		} else {
			order = orderservice.getOrder(orderId, activeId);
		}
		map.addAttribute("order", order);
		map.addAttribute("orderstatus", OrderStatus.getRetailerStatuses());
		map.addAttribute("categories", categoryService.getCategories(null));
		map.addAttribute("meltingandsealing", meltingAndSealingService.find(order.getWholesaler()));
		map.addAttribute("mesurment", Measurement.v.class);
		map.addAttribute("roles", Role.values());
		map.addAttribute("activeflag", ActiveFlag.values());
		map.addAttribute("WholesalerInstantOrder", WholesalerInstantOrder.class);
		return new ModelAndView("admin/orders/xhr/edit");
	}

	@RequestMapping(value = "/xhr/{orderId}", method = RequestMethod.POST)
	public ResponseEntity<Order> updateOrder(@PathVariable("orderId") Integer orderId,
			@ModelAttribute InstantOrder order, @ModelAttribute WholesalerInstantOrder wholesalerInstantOrder)
					throws EntityDoseNotExistException, EntityNotPersistedException {
		System.out.println("am coming inside--------------------!!!!");
		Order order2 = orderservice.getOrder(orderId);
		if (order2 instanceof WholesalerInstantOrder)
			orderservice.update(wholesalerInstantOrder);
		else
			orderservice.update(order);

		Order order3 = new Order();
		order3.setId(orderId);
		return new ResponseEntity<Order>(order3, HttpStatus.OK);
	}

	@RequestMapping(value = "/{orderId}/images", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getOrderImage(@PathVariable("orderId") Integer orderId,
			@RequestParam(required = false) String activeId) throws EntityDoseNotExistException {
		Image image = null;
		Order order = null;
		if (activeId == null || activeId.isEmpty()) {
			order = orderservice.getOrder(orderId);
		} else {
			order = orderservice.getOrder(orderId, activeId);
		}
		if (order instanceof InstantOrder)
			image = ((InstantOrder) order).getImage();
		else
			image = ((WholesalerInstantOrder) order).getImage();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		if (image != null) {
			return new ResponseEntity<byte[]>(image.getImageArray(), headers, HttpStatus.OK);
		}
		return new ResponseEntity<byte[]>(headers, HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/xhr/{orderId}/images", method = RequestMethod.POST)
	public ResponseEntity<Order> updateOrderImpage(@PathVariable("orderId") Integer orderId, MultipartFile file)
			throws EntityDoseNotExistException, EntityNotPersistedException {
		Order order = null;
		try {
			order = orderservice.updateImage(orderId, new Image(file.getBytes()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Order>(order, HttpStatus.OK);
	}

	// @RequestMapping(value = "xhr/supplier-orders/{userId}", method =
	// RequestMethod.GET)
	// public String getXHRSupplierOrder(@PathVariable String activeId,
	// ModelMap map, OrderFilter filter,WholesalerOrderFilter
	// wholesalerOrderFilter,
	// @PageableDefault(size = 10) Pageable pageable)
	// throws EntityDoseNotExistException {
	// User user = userservice.get(activeId);
	// Wholesaler wholesaler = (Wholesaler) user;
	// wholesalerOrderFilter.setUserID(activeId);
	// Page<WholesalerOrder> page = wholeSalerOrderService.get(pageable,
	// wholesaler, wholesalerOrderFilter);
	// map.addAttribute("page", page);
	// map.addAttribute("pageable", pageable);
	// map.addAttribute("totalcount",page.getContent().size());
	// //map.addAttribute("supplier", supplier);
	// return "admin/orders/xhr/list";
	// }
}
