package com.mrk.myordershop.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrk.myordershop.bean.Measurement;
import com.mrk.myordershop.bean.Product;
import com.mrk.myordershop.bean.dto.ProductFilter;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;

public interface ProductService {

	void saveProduct(Product product) throws EntityNotPersistedException;

	Product update(int productId, Product product) throws EntityDoseNotExistException;

	Product get(int productId) throws EntityDoseNotExistException;

	Page<Product> get(ProductFilter productFilter, Pageable pageable);

	Page<Product> getByCategoryId(int categoryId, Pageable pageable);

	Page<Product> queryByNameWithCategoryName(String query,
			String categoryName, Pageable pageable);

	List<Measurement> getMeasurements();

}
