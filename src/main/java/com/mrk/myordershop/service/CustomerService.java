package com.mrk.myordershop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.dto.CustomerFilter;
import com.mrk.myordershop.bean.dto.CustomerSummary;

public interface CustomerService {

	Page<CustomerSummary> getSummary(CustomerFilter filter,
			Wholesaler wholesaler, Pageable pageable);
}
