package com.mrkinnoapps.myordershopadmin.controller.admin;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.service.UserService;
import com.mrkinnoapps.myordershopadmin.service.WholesalerOrderSummaryService;

@Controller
@RequestMapping(value = "/v1/admin/ws-order-summary")
public class WholesalerOrderSummaryController {

	@Autowired
	private WholesalerOrderSummaryService wholesalerOrderSummaryService;
	@Autowired
	private UserService userService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addCustomFormatter(new DateFormatter(
				"E MMM dd yyyy HH:mm:ss 'GMT'z"));
	}

	@RequestMapping(value = "/xhr/statuswise", method = RequestMethod.GET)
	public ResponseEntity wholeSalerGraph(@RequestParam String activeId,
			@RequestParam(required = false) Date from,@RequestParam(required = false) Date to,
			@RequestParam(required = false, defaultValue="14")Integer chunckSize) throws EntityDoseNotExistException {
		User user = userService.get(activeId);
		List list = wholesalerOrderSummaryService
				.getChunckOrderCountByStatusWise(from, to, user, chunckSize);
		return new ResponseEntity(list, HttpStatus.OK);

	}

}

