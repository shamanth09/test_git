package com.mrk.myordershop.assembler;

import java.util.List;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.mrk.myordershop.bean.Category;
import com.mrk.myordershop.resource.Resource;

@Component
public class CategoryAssembler extends
		ResourceAssemblerSupport<Category, Resource> {

	public CategoryAssembler() {
		super(List.class, Resource.class);
	}

	@Override
	public Resource toResource(Category category) {

		Resource resourceCategory = new Resource(category);
		// Link imageLink = linkTo(
		// methodOn(CategoryController.class).getCategoryImage(
		// category.getName())).withRel("image");
		// resourceCategory.add(imageLink);
		return new Resource(resourceCategory);
	}

}
