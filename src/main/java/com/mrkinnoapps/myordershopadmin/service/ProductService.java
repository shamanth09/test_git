package com.mrkinnoapps.myordershopadmin.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrkinnoapps.myordershopadmin.bean.dto.ProductFilter;
import com.mrkinnoapps.myordershopadmin.bean.entity.Image;
import com.mrkinnoapps.myordershopadmin.bean.entity.Measurement;
import com.mrkinnoapps.myordershopadmin.bean.entity.Product;
import com.mrkinnoapps.myordershopadmin.exception.EntityCannotDeleteException;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;

public interface ProductService {

	void saveProduct(Product product) throws EntityNotPersistedException, EntityDoseNotExistException;

	Product update(int productId, Product product)
			throws EntityDoseNotExistException, EntityNotPersistedException;

	Product updateImage(int productId, Image image)
			throws EntityDoseNotExistException, EntityNotPersistedException;

	Product get(int productId) throws EntityDoseNotExistException;

	Page<Product> get(ProductFilter productFilter, Pageable pageable);

	Page<Product> getByCategoryId(int categoryId, Pageable pageable);

	Page<Product> queryByNameWithCategoryName(String query,
			String categoryName, Pageable pageable);

	List<Measurement> getMeasurements();

	void deleteProduct(Integer productId) throws EntityCannotDeleteException,EntityDoseNotExistException;
}
