package com.mrkinnoapps.myordershopadmin.controller.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.mrkinnoapps.myordershopadmin.bean.entity.Category;
import com.mrkinnoapps.myordershopadmin.bean.entity.Image;
import com.mrkinnoapps.myordershopadmin.bean.entity.Measurement;
import com.mrkinnoapps.myordershopadmin.bean.entity.Product;
import com.mrkinnoapps.myordershopadmin.exception.EntityCannotDeleteException;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;
import com.mrkinnoapps.myordershopadmin.service.CategoryService;
import com.mrkinnoapps.myordershopadmin.service.ProductService;

@Controller
@RequestMapping("/v1/admin/products")
public class ProductController {
	private static Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private ProductService productService;

	@Autowired
	private CategoryService categoryService;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(List.class, "measurements",
				new CustomCollectionEditor(ArrayList.class) {
			@Override
			protected Object convertElement(Object element) {
				Measurement mes = null;
				if (element instanceof String
						&& !((String) element).trim().equals("")) {
					mes = new Measurement();
					try {
						int id = Integer.parseInt(element.toString());
						mes.setId(id);
						element = mes;
					} catch (NumberFormatException e) {
					}
				}
				return mes != null ? mes : null;
			}
		});
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getProducts() {
		return new ModelAndView("admin/products/products");
	}

	@RequestMapping(value = "/xhr/categories", method = RequestMethod.GET)
	public String getProducts(ModelMap map,
			@PageableDefault(size = 10) Pageable pageable) {
		Page<Category> page = categoryService.getCategories(pageable);
		map.addAttribute("page", page);
		map.addAttribute("pageable", pageable);
		map.addAttribute("totalcount", page.getContent().size());
		return "admin/products/xhr/categoryList";
	}

	@RequestMapping(value = "/xhr/categories/{categoryId}", method = RequestMethod.GET)
	public String getXHRProducts(ModelMap map,
			@PathVariable Integer categoryId,
			@PageableDefault(size = 10) Pageable pageable)
					throws EntityDoseNotExistException {
		map.addAttribute("category", categoryService.getCategory(categoryId));
		Page<Product> page = productService.getByCategoryId(categoryId, pageable);
		map.addAttribute("page",page);
		map.addAttribute("pageable", pageable);
		map.addAttribute("totalcount",page.getContent().size());
		return "admin/products/xhr/list";
	}

	@RequestMapping(value = "/xhr/new", method = RequestMethod.GET)
	public String newXHRProduct(ModelMap map) {
		Product product = new Product();
		product.setCategory(new Category());
		map.addAttribute("product", product);
		Page<Category> categoryPage = categoryService.getCategories(null);
		map.addAttribute("categoryPage", categoryPage);
		map.addAttribute("measurements", productService.getMeasurements());
		return "admin/products/xhr/form";
	}

	@RequestMapping(value = "/xhr/{productId}", method = RequestMethod.GET)
	public String getXHRProduct(ModelMap map, @PathVariable Integer productId)
			throws EntityDoseNotExistException {
		map.addAttribute("product", productService.get(productId));
		return "admin/products/xhr/view";
	}

	@RequestMapping(value = "/xhr/{productId}/product", method = RequestMethod.GET)
	public ResponseEntity<Product> getXHRProductAjax(ModelMap map, @PathVariable Integer productId)
			throws EntityDoseNotExistException {
		Product product=productService.get(productId);
		return  new ResponseEntity<Product>(product,HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/xhr/{productId}/edit", method = RequestMethod.GET)
	public String editXHRProduct(ModelMap map, @PathVariable Integer productId)
			throws EntityDoseNotExistException {
		Product product = productService.get(productId);
		map.addAttribute("product", product);

		Page<Category> categoryPage = categoryService.getCategories(null);
		map.addAttribute("categoryPage", categoryPage);
		map.addAttribute("measurements", productService.getMeasurements());
		return "admin/products/xhr/edit";
	}


	@RequestMapping(value = "/categories/{categoryId}/images", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getCategoryImage(
			@PathVariable Integer categoryId)
					throws EntityDoseNotExistException {
		Image image = categoryService.getCategory(categoryId).getImage();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		if (image != null) {
			return new ResponseEntity<byte[]>(image.getImageArray(), headers,
					HttpStatus.OK);
		}
		return new ResponseEntity<byte[]>(headers, HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/{productId}/images", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getProductImage(
			@PathVariable Integer productId) throws EntityDoseNotExistException {
		Image image = productService.get(productId).getImage();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		if (image != null) {
			return new ResponseEntity<byte[]>(image.getImageArray(), headers,
					HttpStatus.OK);
		}
		return new ResponseEntity<byte[]>(headers, HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/xhr", method = RequestMethod.POST)
	public ResponseEntity<Product> save(@ModelAttribute Product product)
			throws EntityNotPersistedException, EntityDoseNotExistException {
		productService.saveProduct(product);
		product=productService.get(product.getId());
		return new ResponseEntity<Product>(product, HttpStatus.OK);
	}

	@RequestMapping(value = "/xhr/{productId}", method = RequestMethod.POST)
	public ResponseEntity<Product> updateProducts(
			@PathVariable("productId") Integer productId,
			@ModelAttribute Product product)
					throws EntityDoseNotExistException, EntityNotPersistedException {
		productService.update(productId, product);
		return new ResponseEntity<Product>(HttpStatus.OK);
	}

	@RequestMapping(value = "/xhr/{productId}/images", method = RequestMethod.POST)
	public ResponseEntity<Product> updateProductsImage(
			@PathVariable("productId") Integer productId, MultipartFile file)
					throws EntityDoseNotExistException, EntityNotPersistedException {
		Product product = null;
		try {
			product = productService.updateImage(productId,
					new Image(file.getBytes()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Product>(product, HttpStatus.OK);
	}

	@RequestMapping(value = "/xhr/categories", method = RequestMethod.POST)
	public ResponseEntity<Category> saveCategory(
			@ModelAttribute Category category)
					throws EntityNotPersistedException {
		categoryService.save(category);
		return new ResponseEntity<Category>(category, HttpStatus.OK);
	}

	@RequestMapping(value = "/xhr/categories/{categoryId}", method = RequestMethod.POST)
	public ResponseEntity<Category> updateCategory(
			@PathVariable("categoryId") Integer categoryId,
			@ModelAttribute Category category)
					throws EntityDoseNotExistException {
		categoryService.update(categoryId, category);
		return new ResponseEntity<Category>(category, HttpStatus.OK);
	}

	@RequestMapping(value = "/xhr/categories/{categoryId}/images", method = RequestMethod.POST)
	public ResponseEntity<Category> updateCategoryImage(
			@PathVariable Integer categoryId, MultipartFile file)
					throws EntityDoseNotExistException, EntityNotPersistedException {
		Category category = null;
		try {
			category = categoryService.updateImage(categoryId,
					new Image(file.getBytes()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Category>(category, HttpStatus.OK);
	}
	@RequestMapping(value="/xhr/categories/{categoryId}/products",method=RequestMethod.GET)
	public ResponseEntity<List<Product>> getProductsByCategoryId(@PathVariable("categoryId") String categoryId){
		Page<Product> page=productService.getByCategoryId(Integer.parseInt(categoryId), null);
		return  new ResponseEntity<List<Product>>(page.getContent(),HttpStatus.OK);
	}

	@RequestMapping(value = "/xhr/{productId}/delete", method = RequestMethod.GET)
	public ResponseEntity deleteXHRProduct(ModelMap map, @PathVariable Integer productId)
			throws EntityCannotDeleteException,EntityDoseNotExistException {
		productService.deleteProduct(productId);
		return new ResponseEntity(HttpStatus.OK);
	}
}
