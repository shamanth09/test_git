package com.mrk.myordershop.assembler;

import org.apache.log4j.Logger;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.mrk.myordershop.assembler.linkprovider.ContactLinkProvider;
import com.mrk.myordershop.assembler.linkprovider.OrderLinkProvider;
import com.mrk.myordershop.assembler.linkprovider.WholesalerOrderLinkProvider;
import com.mrk.myordershop.bean.Contact;
import com.mrk.myordershop.bean.Order;
import com.mrk.myordershop.bean.WholesalerOrder;
import com.mrk.myordershop.bean.dto.OrderFilter;
import com.mrk.myordershop.bean.dto.SearchResource;
import com.mrk.myordershop.bean.dto.WholesalerOrderFilter;
import com.mrk.myordershop.bean.dto.filter.ContactFilter;
import com.mrk.myordershop.constant.OrderStatus;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

@Component
public class OrderSearchAssembler extends
		ResourceAssemblerSupport<SearchResource, SearchResource> {

	private final static Logger log = Logger
			.getLogger(OrderSearchAssembler.class);

	public OrderSearchAssembler() {
		super(SearchResource.class, SearchResource.class);
	}

	@Override
	public SearchResource toResource(SearchResource resource) {
		if (resource.getEntity().equals(Order.class))
			try {
				addOrderLink(resource);
			} catch (EntityDoseNotExistException e) {
				e.printStackTrace();
			}
		else if (resource.getEntity().equals(WholesalerOrder.class))
			addWholesalerOrderLink(resource);
		else if (resource.getEntity().equals(Contact.class))
			addContactSearchLink(resource);
		return resource;
	}

	private void addContactSearchLink(SearchResource resource) {
		ContactFilter cf = null;
		switch (resource.getField()) {
		case NAME:
			cf = new ContactFilter();
			cf.setName(resource.getResult());
			resource.add(ContactLinkProvider.get(cf));
			break;
		case MOBILE:
			cf = new ContactFilter();
			cf.setMobile(resource.getResult());
			resource.add(ContactLinkProvider.get(cf));
			break;
		case EMAIL:
			cf = new ContactFilter();
			cf.setEmail(resource.getResult());
			resource.add(ContactLinkProvider.get(cf));
			break;
		case FIRM_NAME:
			cf = new ContactFilter();
			cf.setFirmName(resource.getResult());
			resource.add(ContactLinkProvider.get(cf));
			break;
		}
	}

	private void addOrderLink(SearchResource resource)
			throws EntityDoseNotExistException {

		switch (resource.getField()) {
		case ORDER_NO:
			resource.add(OrderLinkProvider.get(Integer.parseInt(resource
					.getResultId())));
			break;
		case ORDER_STATUS:
			OrderFilter of = new OrderFilter();
			of.setOrderStatus(OrderStatus.valueOf(resource.getResult()
					.toUpperCase()));
			resource.add(OrderLinkProvider.get(of));
			break;
		case PRODUCT_NAME:
			OrderFilter of2 = new OrderFilter();
			of2.setProductName(resource.getResult());
			resource.add(OrderLinkProvider.get(of2));
			break;
		case CUSTOMER_NAME:
			OrderFilter of3 = new OrderFilter();
			of3.setCustomerName(resource.getResult());
			of3.setRetailerId(resource.getResultId());
			resource.add(OrderLinkProvider.get(of3));
			break;
		case WHOLESALER_NAME:
			OrderFilter of4 = new OrderFilter();
			of4.setWholesalerName(resource.getResult());
			of4.setRetailerId(resource.getResultId());
			resource.add(OrderLinkProvider.get(of4));
			break;
		default:
			break;
		}
	}

	private void addWholesalerOrderLink(SearchResource resource) {
		switch (resource.getField()) {
		case ORDER_NO:
			resource.add(WholesalerOrderLinkProvider.get(Integer
					.valueOf(resource.getResultId())));
			break;
		case ORDER_STATUS:
			WholesalerOrderFilter of = new WholesalerOrderFilter();
			of.setOrderStatus(OrderStatus.valueOf(resource.getResult()
					.toUpperCase()));
			resource.add(WholesalerOrderLinkProvider.get(of));
			break;
		case PRODUCT_NAME:
			WholesalerOrderFilter of2 = new WholesalerOrderFilter();
			of2.setProductName(resource.getResult());
			resource.add(WholesalerOrderLinkProvider.get(of2));
			break;
		case SUPPLIER_NAME:
			WholesalerOrderFilter of3 = new WholesalerOrderFilter();
			of3.setSupplierId(resource.getResultId());
			resource.add(WholesalerOrderLinkProvider.get(of3));
			break;
		default:
			break;
		}
	}
}
