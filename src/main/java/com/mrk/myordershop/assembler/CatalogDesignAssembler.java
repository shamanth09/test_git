package com.mrk.myordershop.assembler;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.mrk.myordershop.assembler.linkprovider.CatalogLinkProvider;
import com.mrk.myordershop.bean.CatalogDesign;
import com.mrk.myordershop.resource.Resource;

@Component
public class CatalogDesignAssembler extends
		ResourceAssemblerSupport<CatalogDesign, Resource> {

	public CatalogDesignAssembler() {
		super(CatalogDesign.class, Resource.class);
	}

	@Override
	public Resource<CatalogDesign> toResource(CatalogDesign catalogDesign) {
		Resource<CatalogDesign> resource = new Resource<CatalogDesign>(
				catalogDesign);
		resource.add(CatalogLinkProvider.getCatalogDesignImage(
				catalogDesign.getId()).withRel("image"));
		return resource;
	}

}
