package com.mrk.myordershop.assembler;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.mrk.myordershop.assembler.linkprovider.UserLinkProvider;
import com.mrk.myordershop.bean.dto.CustomerSummary;
import com.mrk.myordershop.bean.dto.CustomerSummaryUser;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

@Component
public class CustomerSummaryAssembler extends
		ResourceAssemblerSupport<CustomerSummary, CustomerSummary> {

	public CustomerSummaryAssembler() {
		super(CustomerSummary.class, CustomerSummary.class);
	}

	@Override
	public CustomerSummary toResource(CustomerSummary customerSummary) {
		if (customerSummary instanceof CustomerSummaryUser) {
			CustomerSummaryUser customerSummaryUser = (CustomerSummaryUser) customerSummary;
			customerSummary.add(UserLinkProvider.get(
					customerSummaryUser.getUser().getId()).withRel("user"));
			try {
				customerSummary.add(UserLinkProvider.getImage(
						customerSummaryUser.getUser()).withRel("userImage"));
			} catch (EntityDoseNotExistException e) {
			}
		}
		return customerSummary;
	}

}
