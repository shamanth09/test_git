/**
 * 
 */
package com.mrkinnoapps.myordershopadmin.controller.admin;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.servlet.ModelAndView;

import com.mrkinnoapps.myordershopadmin.bean.constant.ActiveFlag;
import com.mrkinnoapps.myordershopadmin.bean.constant.OrderStatus;
import com.mrkinnoapps.myordershopadmin.bean.constant.Role;
import com.mrkinnoapps.myordershopadmin.bean.dto.OrderFilter;
import com.mrkinnoapps.myordershopadmin.bean.dto.WholesalerOrderFilter;
import com.mrkinnoapps.myordershopadmin.bean.entity.InstantOrder;
import com.mrkinnoapps.myordershopadmin.bean.entity.Measurement;
import com.mrkinnoapps.myordershopadmin.bean.entity.Order;
import com.mrkinnoapps.myordershopadmin.bean.entity.Relation;
import com.mrkinnoapps.myordershopadmin.bean.entity.Supplier;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.bean.entity.Wholesaler;
import com.mrkinnoapps.myordershopadmin.bean.entity.WholesalerInstantOrder;
import com.mrkinnoapps.myordershopadmin.bean.entity.WholesalerOrder;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;
import com.mrkinnoapps.myordershopadmin.service.CategoryService;
import com.mrkinnoapps.myordershopadmin.service.MeltingAndSealService;
import com.mrkinnoapps.myordershopadmin.service.OrderService;
import com.mrkinnoapps.myordershopadmin.service.RelationService;
import com.mrkinnoapps.myordershopadmin.service.UserService;
import com.mrkinnoapps.myordershopadmin.service.WholeSalerOrderService;

/**
 * @author MBN
 *
 */
@Controller
@RequestMapping("/v1/admin/wholesalerorders")
public class WholesalerOrderController {

	@Autowired
	private WholeSalerOrderService wholeSalerOrderService;

	@Autowired
	private RelationService relationService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private UserService userService;

	@Autowired
	private MeltingAndSealService meltingAndSealingService;


	private static Logger log = LoggerFactory.getLogger(OrderController.class);

	@InitBinder
	private void initBinder(WebDataBinder binder) {
		binder.addCustomFormatter(new DateFormatter(
				"E MMM dd yyyy HH:mm:ss 'GMT'"));
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getWholsalerOrders(
			@RequestParam(required = false) String activeId, ModelMap map) {
		map.addAttribute("activeId", activeId);
		return new ModelAndView("admin/wholesalerorders/wholesalerorders");
	}

	@RequestMapping(value = "/xhr/list", method = RequestMethod.GET)
	public String getXHROrder(@RequestParam(required = false) String activeId,
			ModelMap map,WholesalerOrderFilter wholesalerOrderFilter,
			@PageableDefault(size = 10) Pageable pageable)
					throws EntityDoseNotExistException {
		Page<WholesalerOrder>  page=wholeSalerOrderService.get(pageable, activeId, wholesalerOrderFilter);
		map.addAttribute("page", page);
		map.addAttribute("pageable", pageable);
		map.addAttribute("totalcount", page.getContent().size());
		return "admin/wholesalerorders/xhr/list";
	}

	@RequestMapping(value = "/xhr/{worderId}", method = RequestMethod.GET)
	public ModelAndView getXHROrder(@PathVariable("worderId") Integer worderId,
			ModelMap map,
			@RequestParam(value = "activeId", required = false) String activeId)
					throws EntityDoseNotExistException {
		WholesalerOrder order=null;
		order=wholeSalerOrderService.getOrder(worderId, activeId);
		map.addAttribute("worder", order);
		map.addAttribute("order", order.getOrder());
		map.addAttribute("WholesalerInstantOrder", WholesalerInstantOrder.class);
		return new ModelAndView("admin/wholesalerorders/xhr/orderview");
	}

	@RequestMapping(value = "/xhr/{worderId}/edit", method = RequestMethod.GET)
	public ModelAndView getXHRORderEdit(
			@PathVariable("worderId") Integer worderId, ModelMap map,
			@RequestParam(value = "activeId", required = false) String activeId)
					throws EntityDoseNotExistException {
		WholesalerOrder order = null;
		List<Supplier> supp=new ArrayList<Supplier>();
		Page<Relation> p=null;
		order=wholeSalerOrderService.getOrder(worderId, activeId);
		map.addAttribute("worder",order);
		map.addAttribute("order", order.getOrder());
		map.addAttribute("orderstatus", OrderStatus.getWholesalerStatuses());
		map.addAttribute("categories", categoryService.getCategories(null));
		map.addAttribute("meltingandsealing",
				meltingAndSealingService.find((Wholesaler)order.getUser()));
		map.addAttribute("mesurment",Measurement.v.class);
		map.addAttribute("roles",Role.values());
		map.addAttribute("activeflag", ActiveFlag.values());
		p=relationService.findByPrimaryOrSecondaryUserId(order.getUser().getId(),null,null);
		for(Relation rel:p.getContent())
		{
			if(rel.getPrimaryUser() instanceof Supplier)
				supp.add((Supplier)rel.getPrimaryUser());
			else if(rel.getSecondaryUser() instanceof Supplier)
				supp.add((Supplier)rel.getSecondaryUser());
		}
		map.addAttribute("suppliers",supp);
		return new ModelAndView("admin/wholesalerorders/xhr/edit");
	}

	@RequestMapping(value = "/xhr/{worderId}", method = RequestMethod.POST)
	public ResponseEntity<WholesalerOrder> updateOrder(
			@PathVariable("worderId") Integer worderId,
			@ModelAttribute WholesalerOrder wholesalerOrder)
					throws EntityDoseNotExistException, EntityNotPersistedException {
		wholeSalerOrderService.update(wholesalerOrder);
		return new ResponseEntity<WholesalerOrder>(wholesalerOrder, HttpStatus.OK);
	}
}
