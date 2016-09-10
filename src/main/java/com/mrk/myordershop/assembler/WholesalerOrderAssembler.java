package com.mrk.myordershop.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.apache.log4j.Logger;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import com.mrk.myordershop.apicontroller.supplier.OrderController;
import com.mrk.myordershop.apicontroller.wsaler.WholesalerOrderController;
import com.mrk.myordershop.assembler.linkprovider.ItemLinkProvider;
import com.mrk.myordershop.assembler.linkprovider.OrderLinkProvider;
import com.mrk.myordershop.assembler.linkprovider.UserLinkProvider;
import com.mrk.myordershop.bean.InstantWholesalerOrder;
import com.mrk.myordershop.bean.WholesalerOrder;
import com.mrk.myordershop.constant.Role;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.resource.Resource;

public class WholesalerOrderAssembler extends
		ResourceAssemblerSupport<WholesalerOrder, Resource> {
	private final static Logger log = Logger
			.getLogger(WholesalerOrderAssembler.class);
	private Role role;

	public WholesalerOrderAssembler(Role role) {
		super(WholesalerOrder.class, Resource.class);
		this.role = role;
	}

	@Override
	public Resource toResource(WholesalerOrder order) {
		Resource<WholesalerOrder> orderResource = new Resource<WholesalerOrder>(
				order);
		addSelfLink(orderResource);
		addItemLink(orderResource);
		addImageLink(orderResource);
		addSupplirLink(orderResource);
		addWholesalerLink(orderResource);
		addRetailerOrderLink(orderResource);
		return orderResource;
	}

	private void addSelfLink(Resource<WholesalerOrder> resource) {
		WholesalerOrder order = resource.getResorce();
		try {
			Link selfLink = null;
			switch (role) {
			case ROLE_WSALER:
				selfLink = linkTo(
						methodOn(WholesalerOrderController.class).getOrder(
								order.getId(), null)).withSelfRel();
				resource.add(selfLink);
				break;
			case ROLE_SUPPLIER:
				selfLink = linkTo(
						methodOn(OrderController.class).getOrder(order.getId(),
								null)).withSelfRel();
				resource.add(selfLink);
				break;
			}
		} catch (EntityDoseNotExistException e) {
			e.printStackTrace();
		}
	}

	private void addRetailerOrderLink(Resource<WholesalerOrder> resource) {
		if (resource.getResorce().getOrder() != null)
			switch (role) {
			case ROLE_WSALER:
				resource.add(OrderLinkProvider.get(
						resource.getResorce().getOrder().getId()).withRel(
						"order"));
				break;
			}
	}

	private void addItemLink(Resource<WholesalerOrder> resource) {
		WholesalerOrder order = resource.getResorce();
		try {
			resource.add(ItemLinkProvider.get(order.getItem().getId()).withRel(
					"item"));
		} catch (EntityDoseNotExistException e) {
			e.printStackTrace();
		}
	}

	private void addImageLink(Resource<WholesalerOrder> resource) {
		WholesalerOrder order = resource.getResorce();
		switch (role) {
		case ROLE_WSALER:

			if (order instanceof InstantWholesalerOrder
					&& ((InstantWholesalerOrder) order).getImage() != null)
				try {
					Link imageLink = linkTo(
							methodOn(
									com.mrk.myordershop.apicontroller.wsaler.WholesalerOrderController.class)
									.getImage(order.getId(), null)).withRel(
							"image");
					resource.add(imageLink);
				} catch (EntityDoseNotExistException e) {
					e.printStackTrace();
				}
			break;
		case ROLE_SUPPLIER:
			if (order instanceof InstantWholesalerOrder
					&& ((InstantWholesalerOrder) order).getImage() != null) {
				try {
					Link imageLink = linkTo(
							methodOn(
									com.mrk.myordershop.apicontroller.supplier.OrderController.class)
									.getImage(order.getId(), null)).withRel(
							"image");
					resource.add(imageLink);
				} catch (EntityDoseNotExistException e) {
					e.printStackTrace();
				}
			}
			break;
		}

	}

	private void addSupplirLink(Resource<WholesalerOrder> resource) {
		WholesalerOrder order = resource.getResorce();
		try {
			switch (role) {
			case ROLE_WSALER:
				if (order.getSupplier() != null) {
					Link imageLink = linkTo(
							methodOn(
									com.mrk.myordershop.apicontroller.common.UserController.class)
									.getUser(order.getSupplier().getId()))
							.withRel("supplier");
					resource.add(imageLink);
				}
				break;
			}

		} catch (EntityDoseNotExistException e) {
			e.printStackTrace();
		}
	}

	private void addWholesalerLink(Resource<WholesalerOrder> resource) {
		WholesalerOrder order = resource.getResorce();
		resource.add(UserLinkProvider.get(order.getUser().getId()).withRel(
				"wholesaler"));
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
