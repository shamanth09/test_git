package com.mrk.myordershop.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.apache.log4j.Logger;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import com.mrk.myordershop.apicontroller.common.ProductController;
import com.mrk.myordershop.apicontroller.retail.OrderController;
import com.mrk.myordershop.apicontroller.wsaler.WholesalerOrderController;
import com.mrk.myordershop.assembler.linkprovider.ItemLinkProvider;
import com.mrk.myordershop.assembler.linkprovider.OrderLinkProvider;
import com.mrk.myordershop.assembler.linkprovider.UserLinkProvider;
import com.mrk.myordershop.bean.InstantOrder;
import com.mrk.myordershop.bean.Order;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.WholesalerInstantOrder;
import com.mrk.myordershop.constant.Role;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.resource.Resource;

public class OrderAssembler extends ResourceAssemblerSupport<Order, Resource> {

	private final static Logger log = Logger.getLogger(OrderAssembler.class);
	private Role role;

	public OrderAssembler(Role role) {
		super(Order.class, Resource.class);
		this.role = role;
	}

	@Override
	public Resource toResource(Order order) {
		Resource<Order> orderResource = new Resource<Order>(order);
		try {
			addSelfLink(orderResource);
			addRetailerLink(orderResource);
			addWholesalerLink(orderResource);
			addItemLink(orderResource);
			addproductLink(orderResource);
			addImageLink(orderResource);
			addSupplierOrderLink(orderResource);

		} catch (EntityDoseNotExistException e) {
			e.printStackTrace();
		}

		return orderResource;
	}

	private void addWholesalerLink(Resource<Order> resource) {
		Order order = resource.getResorce();
		resource.add(UserLinkProvider.get(order.getWholesaler().getId()).withRel("wholesaler"));

	}

	private void addSelfLink(Resource<Order> resource) {
		resource.add(OrderLinkProvider.get(resource.getResorce().getId()));
	}

	private void addRetailerLink(Resource<Order> resource) throws EntityDoseNotExistException {
		Order order = resource.getResorce();
		User user = order.getReferralUser() != null ? order.getReferralUser() : order.getUser();
		resource.add(UserLinkProvider.get(user.getId()).withRel("retailer"));
	}

	private void addItemLink(Resource<Order> resource) throws EntityDoseNotExistException {
		Order order = resource.getResorce();
		resource.add(ItemLinkProvider.get(order.getItem().getId()).withRel("item"));
	}

	private void addproductLink(Resource<Order> resource) throws EntityDoseNotExistException {
		Order order = resource.getResorce();
		Link imageLink = linkTo(
				methodOn(ProductController.class).getProducts(order.getItem().getProduct().getId(), null))
						.withRel("product");
		resource.add(imageLink);
	}

	private void addImageLink(Resource<Order> resource) throws EntityDoseNotExistException {
		Order order = resource.getResorce();
		switch (this.role) {
		case ROLE_RETAIL:
			if (order instanceof InstantOrder) {
				InstantOrder io = (InstantOrder) order;
				if (io.getImage() != null) {
					Link imageLink = linkTo(methodOn(OrderController.class).getImage(order.getId(), null))
							.withRel("image");
					resource.add(imageLink);
				}
			}
			break;
		case ROLE_WSALER:
			if (order instanceof InstantOrder) {
				InstantOrder io = (InstantOrder) order;
				if (io.getImage() != null) {
					Link imageLink = linkTo(methodOn(com.mrk.myordershop.apicontroller.wsaler.OrderController.class)
							.getImage(order.getId(), null)).withRel("image");
					resource.add(imageLink);
				}
			}
			if (order instanceof WholesalerInstantOrder) {
				WholesalerInstantOrder io = (WholesalerInstantOrder) order;
				if (io.getImage() != null) {
					Link imageLink = linkTo(methodOn(com.mrk.myordershop.apicontroller.wsaler.OrderController.class)
							.getImage(order.getId(), null)).withRel("image");
					resource.add(imageLink);
				}
			}
			break;
		default:
			break;
		}
	}

	private void addSupplierOrderLink(Resource<Order> resource) throws EntityDoseNotExistException {
		Order order = resource.getResorce();
		switch (this.role) {
		case ROLE_WSALER:
			if (order.getCurrentWholesalerOrder() != null) {
				Link supplierOrderLink = linkTo(methodOn(WholesalerOrderController.class)
						.getOrder(order.getCurrentWholesalerOrder().getId(), null)).withRel("wholesaler_order");
				resource.add(supplierOrderLink);
			}
			break;
		default:
			break;
		}
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
