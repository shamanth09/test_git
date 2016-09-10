package com.mrk.myordershop.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mrk.myordershop.bean.Contact;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.dto.CustomerFilter;
import com.mrk.myordershop.bean.dto.CustomerSummary;
import com.mrk.myordershop.bean.dto.CustomerSummaryContact;
import com.mrk.myordershop.bean.dto.CustomerSummaryUser;
import com.mrk.myordershop.bean.dto.OrderStatusSummary;
import com.mrk.myordershop.bean.dto.OrderSummaryByUser;
import com.mrk.myordershop.config.annotation.ReadTransactional;
import com.mrk.myordershop.dao.ContactDAO;
import com.mrk.myordershop.dao.OrderSummaryDAO;
import com.mrk.myordershop.dao.UserDAO;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

@Service
public class CustomerServiceImpl implements CustomerService {

	private static Logger log = Logger.getLogger(CustomerServiceImpl.class);
	@Autowired
	private OrderSummaryDAO orderSummaryDAO;

	@Autowired
	private ContactDAO contactDAO;

	@Autowired
	private UserDAO userDAO;

	@Override
	@ReadTransactional
	public Page<CustomerSummary> getSummary(CustomerFilter filter,
			Wholesaler wholesaler, Pageable pageable) {
		Page<OrderSummaryByUser> userPage = orderSummaryDAO
				.getOrderCustomerWiseSummary(pageable, filter, wholesaler);
		List<CustomerSummary> list = new ArrayList<CustomerSummary>();
		for (OrderSummaryByUser orderSummary : userPage.getContent()) {
			CustomerSummary customerSummary = null;
			User user = null;
			Contact contact = null;
			OrderStatusSummary orderStatusSummary = null;
			if (orderSummary.getUserId() != null) {
				try {
					user = userDAO.get(orderSummary.getUserId());
				} catch (EntityDoseNotExistException e) {
					e.printStackTrace();
				}
				orderStatusSummary = orderSummaryDAO.getOrderStatusSummary(
						orderSummary.getUserId(), wholesaler,
						filter.getOrderStatus());

			} else {
				try {
					contact = contactDAO.getByMobile(orderSummary.getMobile());
					orderStatusSummary = orderSummaryDAO
							.getOrderStatusSummaryByMobile(
									orderSummary.getMobile(), wholesaler,
									filter.getOrderStatus());
				} catch (EntityDoseNotExistException e) {
					// e.printStackTrace();
					contact = new Contact();
					contact.setName(orderSummary.getName());
					contact.setMobile(orderSummary.getMobile());
					orderStatusSummary = orderSummaryDAO
							.getOrderStatusSummaryByMobile(
									orderSummary.getMobile(), wholesaler,
									filter.getOrderStatus());
				}
			}
			if (user != null) {
				customerSummary = new CustomerSummaryUser(user);
			} else if (contact != null) {
				customerSummary = new CustomerSummaryContact(contact);
			}
			customerSummary.setSummary(orderStatusSummary);
			list.add(customerSummary);

		}
		Page<CustomerSummary> page = new PageImpl<CustomerSummary>(list,
				pageable, userPage.getTotalElements());
		return page;
	}
}
