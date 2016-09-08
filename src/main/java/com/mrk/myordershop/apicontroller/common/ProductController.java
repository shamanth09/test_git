package com.mrk.myordershop.apicontroller.common;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mrk.myordershop.assembler.ProductAssembler;
import com.mrk.myordershop.assembler.linkprovider.ProductLinkProvider;
import com.mrk.myordershop.bean.Image;
import com.mrk.myordershop.bean.Product;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.dto.ProductFilter;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.resource.Resource;
import com.mrk.myordershop.security.oauth.resolver.Owner;
import com.mrk.myordershop.service.CategoryService;
import com.mrk.myordershop.service.ProductService;

import io.swagger.annotations.Api;

@Controller("apiProductController")
@RequestMapping(value = "/api/v1/products")
@Api(value="Product Details", tags={"Product Details"})
public class ProductController {
	private static final Logger log = Logger.getLogger(ProductController.class);

	@Autowired
	private ProductService productService;
	@Autowired
	private ProductAssembler productAssembler;
	@Autowired(required = false)
	private ServletContext servletContext;
	@Autowired
	private CategoryService categoryService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity getProdects(
			@ModelAttribute ProductFilter productFilter,
			@RequestParam(required = false) Integer categoryId,
			@RequestParam(required = false) String categoryName,
			@RequestParam(required = false) String query,
			@PageableDefault(size = 10) Pageable pageable,
			PagedResourcesAssembler pagedResourcesAssembler) {

		Page<Product> products = productService.get(productFilter, pageable);
		return new ResponseEntity(pagedResourcesAssembler.toResource(products,
				productAssembler, ProductLinkProvider.get(productFilter)),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/{productId}", method = RequestMethod.GET)
	public ResponseEntity<Resource> getProducts(
			@PathVariable("productId") int productId, @Owner User wholesaler)
			throws EntityDoseNotExistException {
		Product product = productService.get(productId);
		return new ResponseEntity<Resource>(
				productAssembler.toResource(product), HttpStatus.OK);
	}

	@RequestMapping(value = "/{productId:[\\d]+}/images", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getImage(@PathVariable int productId)
			throws EntityDoseNotExistException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		Image image = productService.get(productId).getImage();
		if (image != null) {
			return new ResponseEntity<byte[]>(image.getImageArray(), headers,
					HttpStatus.OK);
		}
		return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);

	}

}
