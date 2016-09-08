package com.mrk.myordershop.apicontroller.wsaler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.annotation.JsonView;
import com.mrk.myordershop.assembler.CustomerSummaryAssembler;
import com.mrk.myordershop.assembler.linkprovider.CustomerLinkProvider;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.dto.CustomerFilter;
import com.mrk.myordershop.bean.dto.CustomerSummary;
import com.mrk.myordershop.bean.dto.View;
import com.mrk.myordershop.security.oauth.resolver.Owner;
import com.mrk.myordershop.service.CustomerService;

import io.swagger.annotations.Api;

@Controller
@RequestMapping("/api/v1/ws/customers")
@Api(value="Customer Details", tags={"Customer Details"})
public class CustomerController {

	@Autowired
	private CustomerService customerService;
	@Autowired
	private CustomerSummaryAssembler customerSummaryAssembler;

	@JsonView(View.CustomerSummary.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<PagedResources> getSummary(
			@Owner Wholesaler wholesaler,
			@ModelAttribute CustomerFilter filter,
			@RequestParam(required = false) String email,
			@RequestParam(required = false) String mobile,
			@RequestParam(required = false) String name,
			@PageableDefault(size = 10) Pageable pageable,
			PagedResourcesAssembler<CustomerSummary> assembler) {
		Page<CustomerSummary> page = customerService.getSummary(filter,
				wholesaler, pageable);
		return new ResponseEntity<PagedResources>(assembler.toResource(page,
				customerSummaryAssembler, CustomerLinkProvider.find(filter)),
				HttpStatus.OK);
	}

}
