package com.mrk.myordershop.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mrk.myordershop.bean.Contact;
import com.mrk.myordershop.bean.Supplier;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.dto.CustomerFilter;
import com.mrk.myordershop.bean.dto.CustomerSummaryUser;
import com.mrk.myordershop.bean.dto.OrderStatusSummary;
import com.mrk.myordershop.bean.dto.OrderSummaryByUser;
import com.mrk.myordershop.config.annotation.ReadTransactional;
import com.mrk.myordershop.dao.UserDAO;
import com.mrk.myordershop.dao.WholesalerOrderSummaryDAO;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

@Service
public class SupplierServiceImpl implements SupplierService {

	@Autowired
	private WholesalerOrderSummaryDAO orderSummaryDAO;

	@Autowired
	private UserDAO userDAO;

	@Override
	@ReadTransactional
	public Page<CustomerSummaryUser> getSummary(CustomerFilter filter,
			Wholesaler wholesaler, Pageable pageable) {
		Page<OrderSummaryByUser> userPage = orderSummaryDAO
				.getOrderSummaryGroupBySupplier(pageable, filter, wholesaler);
		List<CustomerSummaryUser> list = new ArrayList<CustomerSummaryUser>();
		for (OrderSummaryByUser orderSummary : userPage.getContent()) {
			CustomerSummaryUser customerSummary = null;
			Supplier user = null;
			Contact contact = null;
			OrderStatusSummary orderStatusSummary = null;
			try {
				user = (Supplier) userDAO.get(orderSummary.getUserId());
			} catch (EntityDoseNotExistException e) {
				e.printStackTrace();
			}
			orderStatusSummary = orderSummaryDAO.getOrderStatusSummary(
					orderSummary.getUserId(), wholesaler,
					filter.getOrderStatus());
			customerSummary = new CustomerSummaryUser(user);
			customerSummary.setSummary(orderStatusSummary);
			list.add(customerSummary);

		}
		Page<CustomerSummaryUser> page = new PageImpl<CustomerSummaryUser>(
				list, pageable, userPage.getTotalElements());
		return page;
	}

	@Override
	@ReadTransactional
	public CustomerSummaryUser getSummary(String userId, Wholesaler wholesaler) {
		CustomerSummaryUser customerSummary = null;
		Supplier user = null;
		Contact contact = null;
		OrderStatusSummary orderStatusSummary = null;
		try {
			user = (Supplier) userDAO.get(userId);
		} catch (EntityDoseNotExistException e) {
			e.printStackTrace();
		}
		orderStatusSummary = orderSummaryDAO.getOrderStatusSummary(userId,
				wholesaler, null);
		customerSummary = new CustomerSummaryUser(user);
		customerSummary.setSummary(orderStatusSummary);

		return customerSummary;
	}

}
