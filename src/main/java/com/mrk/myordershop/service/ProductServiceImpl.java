package com.mrk.myordershop.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mrk.myordershop.bean.Measurement;
import com.mrk.myordershop.bean.Product;
import com.mrk.myordershop.bean.dto.ProductFilter;
import com.mrk.myordershop.config.annotation.PersistTransactional;
import com.mrk.myordershop.config.annotation.ReadTransactional;
import com.mrk.myordershop.constant.ActiveFlag;
import com.mrk.myordershop.dao.ProductDAO;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDAO productDAO;

	@Override
	@ReadTransactional
	public Product get(int productId) throws EntityDoseNotExistException {
		return productDAO.get(productId);
	}

	@Override
	@PersistTransactional
	public void saveProduct(Product product) throws EntityNotPersistedException {
		product.setCreateTimestamp(new Date());
		product.setActiveFlag(ActiveFlag.ACTIVE);
		productDAO.save(product);
	}

	@Override
	@ReadTransactional
	public Page<Product> getByCategoryId(int categoryId, Pageable pageable) {
		return productDAO.getByCategoryId(categoryId, pageable);
	}

	@Override
	@PersistTransactional
	public Product update(int productId, Product product)
			throws EntityDoseNotExistException {
		Product productfdb = productDAO.get(productId);
		productfdb.setName(product.getName());
		productfdb.setSku(product.getSku());
		if (product.getCategory() != null)
			productfdb.setCategory(product.getCategory());
		productfdb.setMeasurements(product.getMeasurements());
		productDAO.update(productfdb);
		return productfdb;
	}

	@Override
	@ReadTransactional
	public Page<Product> get(ProductFilter productFilter, Pageable pageable) {
		return productDAO.get(productFilter, pageable);
	}

	@Override
	@ReadTransactional
	public Page<Product> queryByNameWithCategoryName(String query,
			String categoryName, Pageable pageable) {
		return productDAO.queryByNameWithCategoryName(query, categoryName,
				pageable);
	}

	@Override
	@ReadTransactional
	public List<Measurement> getMeasurements() {
		return productDAO.getMeasurement();
	}

}
