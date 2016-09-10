package com.mrkinnoapps.myordershopadmin.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrkinnoapps.myordershopadmin.bean.dto.ProductFilter;
import com.mrkinnoapps.myordershopadmin.bean.entity.Measurement;
import com.mrkinnoapps.myordershopadmin.bean.entity.Product;
import com.mrkinnoapps.myordershopadmin.exception.EntityCannotDeleteException;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;

public interface ProductDAO {

	void save(Product product) throws EntityNotPersistedException;

	void update(Product product) throws EntityNotPersistedException;

	Product get(int productId) throws EntityDoseNotExistException;

	Product getByName(String name) throws EntityDoseNotExistException;

	Page<Product> get(ProductFilter productFilter, Pageable pageable);

	Page<Product> getByCategoryId(int categoryId, Pageable pageable);

	List<Measurement> getMeasurement();

	Page<Product> queryByNameWithCategoryName(String query,
			String categoryName, Pageable pageable);

	void deleteProduct(Product product) throws EntityCannotDeleteException;
}
