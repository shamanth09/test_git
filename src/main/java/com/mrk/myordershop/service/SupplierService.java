package com.mrk.myordershop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.dto.CustomerFilter;
import com.mrk.myordershop.bean.dto.CustomerSummaryUser;

public interface SupplierService {

	Page<CustomerSummaryUser> getSummary(CustomerFilter filter,
			Wholesaler wholesaler, Pageable pageable);

	CustomerSummaryUser getSummary(String userId, Wholesaler wholesaler);

}
