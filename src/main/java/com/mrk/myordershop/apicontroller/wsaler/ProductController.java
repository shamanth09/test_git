package com.mrk.myordershop.apicontroller.wsaler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mrk.myordershop.assembler.ProductAssembler;
import com.mrk.myordershop.bean.Measurement;
import com.mrk.myordershop.bean.Product;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;
import com.mrk.myordershop.security.oauth.resolver.Owner;
import com.mrk.myordershop.service.CategoryService;
import com.mrk.myordershop.service.ProductService;

import io.swagger.annotations.Api;

@Controller("apiWsalerProductController")
@RequestMapping(value = "/api/v1/ws/products")
@Api(value="Product Details", tags={"Product Details"})
public class ProductController {

	@Autowired
	private ProductService productService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ProductAssembler productAssembler;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity saveProduct(@RequestBody Product product,
			@Owner Wholesaler wholesaler) throws EntityNotPersistedException {
		productService.saveProduct(product);
		return new ResponseEntity(product, HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/{productId}", method = RequestMethod.PUT)
	public ResponseEntity updateProduct(
			@PathVariable("productId") int productId,
			@RequestBody Product product, @Owner Wholesaler wholesaler)
			throws EntityDoseNotExistException {
		productService.update(productId, product);
		return new ResponseEntity(HttpStatus.OK);
	}

	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value = "/measurements", method = RequestMethod.GET)
	public ResponseEntity<List> getMeasurements() {
		List<Measurement> measurements = productService.getMeasurements();
		return new ResponseEntity<List>(measurements, HttpStatus.OK);
	}
}
