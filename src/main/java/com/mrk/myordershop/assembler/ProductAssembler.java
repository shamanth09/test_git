package com.mrk.myordershop.assembler;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.mrk.myordershop.assembler.linkprovider.ProductLinkProvider;
import com.mrk.myordershop.bean.Product;
import com.mrk.myordershop.resource.Resource;

@Component
public class ProductAssembler extends
		ResourceAssemblerSupport<Product, Resource> {

	public ProductAssembler() {
		super(Product.class, Resource.class);
	}

	@Override
	public Resource<Product> toResource(Product product) {
		Resource<Product> resource = new Resource<Product>(product);
		resource.add(ProductLinkProvider.getImage(product.getId()).withRel(
				"image"));
		return resource;
	}

}
