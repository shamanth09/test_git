package com.mrkinnoapps.myordershopadmin.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrkinnoapps.myordershopadmin.bean.dto.CustomerFilter;
import com.mrkinnoapps.myordershopadmin.bean.dto.CustomerSummary;
import com.mrkinnoapps.myordershopadmin.bean.entity.Wholesaler;

public interface CustomerService {

	Page<CustomerSummary> getSummary(CustomerFilter filter,
			Wholesaler wholesaler, Pageable pageable);
}
