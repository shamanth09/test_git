package com.mrk.myordershop.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrk.myordershop.bean.Measurement;
import com.mrk.myordershop.bean.Product;
import com.mrk.myordershop.bean.dto.ProductFilter;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;

public interface ProductDAO {

	void save(Product product) throws EntityNotPersistedException;

	void update(Product product);

	Product get(int productId) throws EntityDoseNotExistException;

	Product getByName(String name) throws EntityDoseNotExistException;

	Page<Product> get(ProductFilter productFilter, Pageable pageable);

	Page<Product> getByCategoryId(int categoryId, Pageable pageable);

	List<Measurement> getMeasurement();

	Page<Product> queryByNameWithCategoryName(String query,
			String categoryName, Pageable pageable);

}
