package com.mrk.myordershop.assembler;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.mrk.myordershop.assembler.linkprovider.UserLinkProvider;
import com.mrk.myordershop.bean.Relation;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.resource.Resource;
import com.mrk.myordershop.security.bean.SessionUser;

@Component
public class RelationAssembler extends ResourceAssemblerSupport<Relation, Resource> {

	public RelationAssembler() {
		super(Relation.class, Resource.class);
	}

	@Override
	public Resource toResource(Relation relation) {
		Resource<Relation> resource = new Resource<Relation>(relation);
		relation.setCurrentUserEmail(getCurrentUserEmail());
		try {
			resource.add(UserLinkProvider.getImage(relation.getUser()).withRel("image"));
		} catch (EntityDoseNotExistException e) {
		}
		if (relation.getUser().getAddress() != null)
			resource.add(UserLinkProvider.getAddress((relation.getUser().getAddress().getId())).withRel("address"));
		return resource;
	}

	private String getCurrentUserEmail() {
		return ((SessionUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getDomainUser()
				.getEmail();
	}
}
