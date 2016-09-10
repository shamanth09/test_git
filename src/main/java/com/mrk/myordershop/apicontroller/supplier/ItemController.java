package com.mrk.myordershop.apicontroller.supplier;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mrk.myordershop.assembler.ItemAssembler;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.resource.Resource;
import com.mrk.myordershop.service.ItemService;

import io.swagger.annotations.Api;

/**
 * OrderController.java Naveen Apr 8, 2015
 */
@Controller("apiSupplierItemController")
@RequestMapping("/api/v1/sp/items")
@Api(value = "Order Details", tags = { "Order Details" })
public class ItemController {

	private static Logger log = Logger.getLogger(ItemController.class
			.getName());
	@Autowired
	private ItemService itemService;
	@Autowired
	@Qualifier("supplierItemAssembler")
	private ItemAssembler itemAssembler;

	@RequestMapping(value = "/{itemId}", method = RequestMethod.GET)
	public ResponseEntity<Resource> getItem(@PathVariable int itemId) throws EntityDoseNotExistException {
		return new ResponseEntity<Resource>(
				itemAssembler.toResource(itemService.getItem(itemId)),
				HttpStatus.OK);
	}
}
