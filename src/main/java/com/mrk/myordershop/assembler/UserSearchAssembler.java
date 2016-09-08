package com.mrk.myordershop.assembler;

import org.apache.log4j.Logger;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.mrk.myordershop.assembler.linkprovider.RelationLinkProvider;
import com.mrk.myordershop.assembler.linkprovider.UserLinkProvider;
import com.mrk.myordershop.bean.Relation;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.dto.RelationFilter;
import com.mrk.myordershop.bean.dto.UserFilter;
import com.mrk.myordershop.bean.dto.UserSearchResource;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

@Component
public class UserSearchAssembler extends
		ResourceAssemblerSupport<UserSearchResource, UserSearchResource> {

	private final static Logger log = Logger
			.getLogger(OrderSearchAssembler.class);

	public UserSearchAssembler() {
		super(UserSearchResource.class, UserSearchResource.class);
	}

	@Override
	public UserSearchResource toResource(UserSearchResource resource) {
		try {
			addUserLink(resource);
		} catch (EntityDoseNotExistException e) {
			e.printStackTrace();
		}
		return resource;
	}

	private void addUserLink(UserSearchResource resource)
			throws EntityDoseNotExistException {
		UserFilter of = new UserFilter();
		if (resource.getEntity().equals(User.class)) {
			switch (resource.getField()) {
			case NAME:
				of.setName(resource.getResult());
				break;
			case EMAIL:
				of.setEmail(resource.getResult());
				break;
			case MOBILE:
				of.setMobile(resource.getResult());
				break;
			default:
				break;
			}
			if (of != null) {
				System.out.println(resource.getRole());
				of.setRole(resource.getRole());
				resource.add(UserLinkProvider.get(of));
			}
		} else if (resource.getEntity().equals(Relation.class)) {
			RelationFilter filter = new RelationFilter();
			switch (resource.getField()) {
			case NAME:
				filter.setName(resource.getResult());
				break;
			case EMAIL:
				filter.setEmail(resource.getResult());
				break;
			case MOBILE:
				filter.setMobile(resource.getResult());
				break;
			}
			resource.add(RelationLinkProvider.get(filter));
		}
	}
}
