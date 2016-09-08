package com.mrk.myordershop.apicontroller.retail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mrk.myordershop.assembler.ItemAssembler;
import com.mrk.myordershop.bean.Item;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.resource.Resource;
import com.mrk.myordershop.service.ItemService;

import io.swagger.annotations.Api;

@Controller("apiRetailerItemControler")
@RequestMapping("/api/v1/rt/items")
@Api(value = "Order Details", tags = { "Order Details" })
public class ItemController {

	@Autowired
	private ItemService itemService;
	@Autowired
	@Qualifier("retailerItemAssembler")
	private ItemAssembler itemAssembler;

	@RequestMapping(value = "/{itemId}", method = RequestMethod.GET)
	public ResponseEntity<Resource> getItem(@PathVariable("itemId") int itemId)
			throws EntityDoseNotExistException {
		Item item = itemService.getItem(itemId);
		return new ResponseEntity<Resource>(itemAssembler.toResource(item),
				HttpStatus.OK);
	}
}
