package com.mrk.myordershop.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.apache.log4j.Logger;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import com.mrk.myordershop.apicontroller.wsaler.OrderController;
import com.mrk.myordershop.apicontroller.wsaler.WholesalerOrderController;
import com.mrk.myordershop.assembler.linkprovider.ItemLinkProvider;
import com.mrk.myordershop.bean.Item;
import com.mrk.myordershop.constant.Role;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.resource.Resource;

public class ItemAssembler extends ResourceAssemblerSupport<Item, Resource> {
	private static final Logger log = Logger.getLogger(ItemAssembler.class);
	private Role role;

	public ItemAssembler(Role role) {
		super(Item.class, Resource.class);
		this.role = role;
	}

	@Override
	public Resource<Item> toResource(Item item) {
		Resource<Item> resource = new Resource<Item>(item);
		addSelfLink(resource);
		addOrderLink(resource);
		addWholesalerOrderLink(resource);
		return resource;
	}

	private void addSelfLink(Resource<Item> resource) {
		try {
			resource.add(ItemLinkProvider.get(resource.getResorce().getId()));
		} catch (EntityDoseNotExistException e) {
			e.printStackTrace();
		}
	}

	private void addOrderLink(Resource<Item> resource) {
		Item item = resource.getResorce();
		if (item.getOrder() != null) {
			try {
				Link orderLink = null;
				switch (this.role) {
				case ROLE_RETAIL:
					orderLink = linkTo(
							methodOn(
									com.mrk.myordershop.apicontroller.retail.OrderController.class)
									.getOrder(item.getOrder().getId(), null))
							.withRel("order");
					resource.add(orderLink);
					break;
				case ROLE_WSALER:
					orderLink = linkTo(
							methodOn(OrderController.class).getOrder(
									item.getOrder().getId(), null)).withRel(
							"order");
					resource.add(orderLink);
					break;
				case ROLE_SUPPLIER:
					orderLink = linkTo(
							methodOn(
									com.mrk.myordershop.apicontroller.supplier.OrderController.class)
									.getOrder(
											item.getWholesalerOrder().getId(),
											null)).withRel("order");
					resource.add(orderLink);
					break;
				default:
					break;
				}
			} catch (EntityDoseNotExistException e) {
				e.printStackTrace();
			}
		}
	}

	private void addWholesalerOrderLink(Resource<Item> resource) {
		Item item = resource.getResorce();
		switch (this.role) {
		case ROLE_WSALER:
			if (item.getWholesalerOrder() != null) {
				try {
					Link supplierOrderLink = linkTo(
							methodOn(WholesalerOrderController.class).getOrder(
									item.getWholesalerOrder().getId(), null))
							.withRel("wholesaler_order");
					resource.add(supplierOrderLink);
				} catch (EntityDoseNotExistException e) {
					e.printStackTrace();
				}
			}
			break;
		default:
			break;
		}
	}
}
