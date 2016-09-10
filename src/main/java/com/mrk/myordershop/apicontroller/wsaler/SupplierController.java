package com.mrk.myordershop.apicontroller.wsaler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.annotation.JsonView;
import com.mrk.myordershop.assembler.SupplierSummaryAssembler;
import com.mrk.myordershop.assembler.linkprovider.SupplierLinkProvider;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.dto.CustomerFilter;
import com.mrk.myordershop.bean.dto.CustomerSummaryUser;
import com.mrk.myordershop.bean.dto.View;
import com.mrk.myordershop.security.oauth.resolver.Owner;
import com.mrk.myordershop.service.SupplierService;

import io.swagger.annotations.Api;

@Controller
@RequestMapping("/api/v1/ws/suppliers")
@Api(value="User Details", tags={"User Details"})
public class SupplierController {

	@Autowired
	private SupplierService supplierService;
	@Autowired
	private SupplierSummaryAssembler summaryAssembler;

	@JsonView(View.SupplierSummary.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<PagedResources> getSummary(
			@Owner Wholesaler wholesaler,
			@ModelAttribute CustomerFilter filter,
			@RequestParam(required = false) String email,
			@RequestParam(required = false) String mobile,
			@RequestParam(required = false) String name,
			@PageableDefault(size = 10) Pageable pageable,
			PagedResourcesAssembler<CustomerSummaryUser> assembler) {
		Page<CustomerSummaryUser> page = supplierService.getSummary(filter,
				wholesaler, pageable);
		return new ResponseEntity<PagedResources>(assembler.toResource(page,
				this.summaryAssembler, SupplierLinkProvider.find(filter)),
				HttpStatus.OK);
	}

	@JsonView(View.SupplierSummary.class)
	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	public ResponseEntity<ResourceSupport> getSummary(
			@Owner Wholesaler wholesaler, @PathVariable("userId") String userId) {
		CustomerSummaryUser summaryUser = supplierService.getSummary(userId,
				wholesaler);
		return new ResponseEntity<ResourceSupport>(
				this.summaryAssembler.toResource(summaryUser), HttpStatus.OK);
	}

}
