package com.mrk.myordershop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mrk.myordershop.bean.Category;
import com.mrk.myordershop.bean.Product;
import com.mrk.myordershop.exception.EntityNotPersistedException;
import com.mrk.myordershop.service.ProductService;

public class ProductTest {
	private ProductService productService;

	public ProductTest() {
		ApplicationContext appContext = new ClassPathXmlApplicationContext(
				"META-INF/spring/application-context.xml");
		productService = appContext.getBean(ProductService.class);
	}

	private Product getProduct(String name, String sku, String type) {
		Product product = new Product();
		Category category = new Category();
		category.setId(1);
		product.setCategory(category);
		product.setName(name);
		product.setSku(sku);
		return product;
	}
	
	private void saveProducts() throws EntityNotPersistedException {
		this.productService.saveProduct(getProduct("saa", "ewe", "kar"));
		this.productService.saveProduct(getProduct("www", "sss", "ddd"));
	}
	private void displayProducts() {
		Category category = new Category();
		category.setId(1);
//		Page page = productService.getProducts(category,new PageMeta());
//		System.out.println(page.getContent().size());
	}
	public static void main(String[] args) {
			ProductTest test = new ProductTest();
//			test.saveProducts();
			test.displayProducts();
	}
}
