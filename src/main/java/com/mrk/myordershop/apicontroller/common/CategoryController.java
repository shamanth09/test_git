package com.mrk.myordershop.apicontroller.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mrk.myordershop.assembler.CategoryAssembler;
import com.mrk.myordershop.assembler.ProductAssembler;
import com.mrk.myordershop.bean.Category;
import com.mrk.myordershop.bean.Image;
import com.mrk.myordershop.bean.Product;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.service.CategoryService;
import com.mrk.myordershop.service.ProductService;

import io.swagger.annotations.Api;

@Controller("apiCollectionsController")
@RequestMapping("/api/v1/categories")
@Api(value="Category", tags={"Product Details"}, description="Product Category Details")
public class CategoryController {

	@Autowired
	private ProductService productService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private CategoryAssembler categoryAssembler;

	@Autowired
	private ProductAssembler productAssembler;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<List<Category>> getProdectTypes(
			@RequestParam(value = "query", defaultValue = "") String q) {
		List<Category> categories = categoryService.searchByName(q);
		return new ResponseEntity<List<Category>>(categories, HttpStatus.OK);
	}

	@RequestMapping(value = "/{categoryId:[\\d]+}/products", method = RequestMethod.GET)
	public ResponseEntity<PagedResources> getProducts(
			@PathVariable int categoryId,
			@PageableDefault(size = 10) Pageable pageable,
			PagedResourcesAssembler<Product> pagedResourcesAssembler)
			throws EntityDoseNotExistException {
		Page<Product> page = productService.getByCategoryId(categoryId,
				pageable);
		return new ResponseEntity<PagedResources>(
				pagedResourcesAssembler.toResource(page, productAssembler),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/{categoryId:[\\d]+}/images", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getCategoryImage(@PathVariable int categoryId)
			throws EntityDoseNotExistException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		Image image = categoryService.getCategory(categoryId).getImage();
		if (image != null) {
			return new ResponseEntity<byte[]>(image.getImageArray(), headers,
					HttpStatus.OK);
		}
		return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);

	}

}
