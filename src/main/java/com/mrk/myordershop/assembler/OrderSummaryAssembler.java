package com.mrk.myordershop.assembler;

import org.apache.log4j.Logger;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.mrk.myordershop.assembler.linkprovider.OrderLinkProvider;
import com.mrk.myordershop.assembler.linkprovider.WholesalerOrderLinkProvider;
import com.mrk.myordershop.bean.Order;
import com.mrk.myordershop.bean.WholesalerInstantOrder;
import com.mrk.myordershop.bean.WholesalerOrder;
import com.mrk.myordershop.bean.dto.OrderFilter;
import com.mrk.myordershop.bean.dto.OrderSummary;
import com.mrk.myordershop.bean.dto.OrderSummaryByCategory;
import com.mrk.myordershop.bean.dto.OrderSummaryByUser;
import com.mrk.myordershop.bean.dto.WholesalerOrderFilter;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.resource.RateCutResource;

@Component
public class OrderSummaryAssembler extends ResourceAssemblerSupport<OrderSummary, OrderSummary> {

	private final static Logger log = Logger.getLogger(OrderSearchAssembler.class);

	public OrderSummaryAssembler() {
		super(OrderSummary.class, OrderSummary.class);
	}

	@Override
	public OrderSummary toResource(OrderSummary resource) {
		if (resource.getEntity().equals(Order.class)) {
			if (resource instanceof OrderSummaryByUser) {
				OrderFilter filter = new OrderFilter();
				filter.setCustomerName(((OrderSummaryByUser) resource).getName());
				try {
					resource.add(OrderLinkProvider.get(filter).withRel("self"));
				} catch (EntityDoseNotExistException e) {
					e.printStackTrace();
				}
			} else if (resource instanceof OrderSummaryByCategory) {
				OrderFilter filter = new OrderFilter();
				filter.setCategoryId(((OrderSummaryByCategory) resource).getCategoryId());
				try {
					resource.add(OrderLinkProvider.get(filter).withRel("self"));
				} catch (EntityDoseNotExistException e) {
					e.printStackTrace();
				}
			}
		}

		if (resource.getEntity().equals(WholesalerOrder.class)) {
			if (resource instanceof OrderSummaryByUser) {
				WholesalerOrderFilter filter = new WholesalerOrderFilter();
				filter.setSupplierId(((OrderSummaryByUser) resource).getUserId());
				resource.add(WholesalerOrderLinkProvider.get(filter).withRel("self"));
			}
		}
		if (resource.getEntity().equals(WholesalerInstantOrder.class)) {
			if (resource instanceof RateCutResource) {
				resource.add(OrderLinkProvider.get(((RateCutResource) resource).getOrderId()));
			}
		}
		return resource;
	}

}
