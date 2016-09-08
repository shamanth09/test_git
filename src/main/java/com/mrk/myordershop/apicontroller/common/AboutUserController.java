package com.mrk.myordershop.apicontroller.common;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mrk.myordershop.assembler.OrderSummaryAssembler;
import com.mrk.myordershop.bean.Retailer;
import com.mrk.myordershop.bean.Supplier;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.dto.BasicOrderStatusWiseSummary;
import com.mrk.myordershop.constant.OrderStatus;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.security.oauth.resolver.Owner;
import com.mrk.myordershop.service.OrderSummaryService;
import com.mrk.myordershop.service.RelationService;
import com.mrk.myordershop.service.UserService;
import com.mrk.myordershop.service.WholesalerOrderSummaryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller("commonAboutUserController")
@RequestMapping(value = "/api/v1/users/{userId}/about")
@Api(value="About user", tags={"About user"}, description="Summaries about user")
public class AboutUserController {

	@Autowired
	private OrderSummaryService orderSummaryService;
	@Autowired
	private OrderSummaryAssembler summaryAssembler;
	@Autowired
	private WholesalerOrderSummaryService wholesalerOrderSummaryService;
	@Autowired
	private RelationService relationService;
	@Autowired
	private UserService userService;

	/**
	 * @param userId
	 * @param currentUser
	 * @return get order summary with till date simple from
	 * @throws EntityDoseNotExistException
	 */
	@ApiOperation(value = "Get order summary of user by till date")
	@RequestMapping(value = "/order-summary", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getRtOrderSummary(
			@PathVariable("userId") String userId, @Owner User currentUser)
			throws EntityDoseNotExistException {
		Map<String, Object> result = null;
		User user = userService.get(userId);
		if ((user instanceof Wholesaler && currentUser instanceof Retailer)
				|| (user instanceof Retailer && currentUser instanceof Wholesaler)
				|| (user.equals(currentUser) && (currentUser instanceof Retailer))) {
			result = orderSummaryService.getAliveOrderSummaryByTillDate(
					new Date(), user);
		} else if ((user instanceof Wholesaler && currentUser instanceof Supplier)
				|| (user instanceof Supplier && currentUser instanceof Wholesaler)
				|| (user.equals(currentUser) && (currentUser instanceof Supplier))) {
			result = wholesalerOrderSummaryService
					.getAliveOrderSummaryByTillDate(new Date(), user);
		} else if (user.equals(currentUser)
				&& (currentUser instanceof Wholesaler)) {
			result = new HashMap<String, Object>();
			result.put("orderSummary", orderSummaryService
					.getAliveOrderSummaryByTillDate(new Date(), user));
			result.put("wholesalerOrderSummary", wholesalerOrderSummaryService
					.getAliveOrderSummaryByTillDate(new Date(), user));
		}

		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

	/**
	 * @param userId
	 * @param currentUser
	 * @param orderStatuses
	 * @return get basic order summary with count, weight, quantity
	 * @throws EntityDoseNotExistException
	 */
	@RequestMapping(value = "/basic-order-summary", method = RequestMethod.GET)
	public ResponseEntity<List<BasicOrderStatusWiseSummary>> getBasicOrderSummary(
			@PathVariable("userId") String userId, @Owner User currentUser,
			@RequestParam(required = false) List<OrderStatus> orderStatuses)
			throws EntityDoseNotExistException {
		List<BasicOrderStatusWiseSummary> count = null;
		User user = userService.get(userId);
		if ((user instanceof Wholesaler && currentUser instanceof Retailer)
				|| (user instanceof Retailer && currentUser instanceof Wholesaler)
				|| (user.equals(currentUser) && (currentUser instanceof Retailer))) {
			count = orderSummaryService.getOrderStatusWiseSummary(user,
					orderStatuses);
		} else if ((user instanceof Wholesaler && currentUser instanceof Supplier)
				|| (user instanceof Supplier && currentUser instanceof Wholesaler)
				|| (user.equals(currentUser) && (currentUser instanceof Supplier))) {
			count = wholesalerOrderSummaryService
					.getOrderStatusWiseSummary(user, orderStatuses);
		} else if (user.equals(currentUser)
				&& (currentUser instanceof Wholesaler)) {
			count = orderSummaryService.getOrderStatusWiseSummary(user,
					orderStatuses);
		}

		return new ResponseEntity<List<BasicOrderStatusWiseSummary>>(count,
				HttpStatus.OK);
	}
	
	@RequestMapping(value = "/relation-summary", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getRelatoinSummary(
			@PathVariable("userId") String userId, @Owner User currentUser)
			throws EntityDoseNotExistException {
		Map<String, Object> result = relationService.getActiveRelationCount(
				userId, currentUser);
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/weight", method = RequestMethod.GET)
	public ResponseEntity getTotalWeight(@PathVariable("userId") String userId,
			@Owner User currentUser,
			@RequestParam(required = false) List<OrderStatus> orderStatuses)
			throws EntityDoseNotExistException {
		long count = 0L;
		User user = userService.get(userId);
		if ((user instanceof Wholesaler && currentUser instanceof Retailer)
				|| (user instanceof Retailer && currentUser instanceof Wholesaler)
				|| (user.equals(currentUser) && (currentUser instanceof Retailer))) {
			count = orderSummaryService.getTotalWeight(user, orderStatuses);
		} else if ((user instanceof Wholesaler && currentUser instanceof Supplier)
				|| (user instanceof Supplier && currentUser instanceof Wholesaler)
				|| (user.equals(currentUser) && (currentUser instanceof Supplier))) {
			count = wholesalerOrderSummaryService.getTotalWeight(user,
					orderStatuses);
		} else if (user.equals(currentUser)
				&& (currentUser instanceof Wholesaler)) {
			count = orderSummaryService.getTotalWeight(user, orderStatuses);
		}
		Map<String, Object> relust = new HashMap<String, Object>();
		relust.put("count", count);
		return new ResponseEntity<Map<String, Object>>(relust, HttpStatus.OK);
	}

}
