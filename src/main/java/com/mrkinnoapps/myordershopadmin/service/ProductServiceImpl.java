package com.mrkinnoapps.myordershopadmin.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mrkinnoapps.myordershopadmin.bean.constant.ActiveFlag;
import com.mrkinnoapps.myordershopadmin.bean.dto.OrderFilter;
import com.mrkinnoapps.myordershopadmin.bean.dto.ProductFilter;
import com.mrkinnoapps.myordershopadmin.bean.entity.Image;
import com.mrkinnoapps.myordershopadmin.bean.entity.Measurement;
import com.mrkinnoapps.myordershopadmin.bean.entity.Order;
import com.mrkinnoapps.myordershopadmin.bean.entity.Product;
import com.mrkinnoapps.myordershopadmin.dao.ImageDAO;
import com.mrkinnoapps.myordershopadmin.dao.OrderDAO;
import com.mrkinnoapps.myordershopadmin.dao.ProductDAO;
import com.mrkinnoapps.myordershopadmin.dao.ProductSearcher;
import com.mrkinnoapps.myordershopadmin.exception.EntityCannotDeleteException;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;
import com.mrkinnoapps.myordershopadmin.util.searchengine.SearchEngine;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDAO productDAO;
	@Autowired
	private OrderDAO orderDAO;
	@Autowired
	private ImageDAO imageDAO;
	private SearchEngine engine;
	
	@Autowired
	public ProductServiceImpl(ProductSearcher productSearcher,SearchEngine engine  ) {
		this.engine= engine;
		engine.searcherRegister(productSearcher);
	}

	@Override
	public Product get(int productId) throws EntityDoseNotExistException {
		return productDAO.get(productId);
	}

	@Override
	public void saveProduct(Product product) throws EntityNotPersistedException {
		product.setCreateTimestamp(new Date());
		product.setActiveFlag(ActiveFlag.ACTIVE);
		productDAO.save(product);
	}

	@Override
	public Page<Product> getByCategoryId(int categoryId, Pageable pageable) {
		return productDAO.getByCategoryId(categoryId, pageable);
	}

	@Override
	public Product update(int productId, Product product)
			throws EntityDoseNotExistException, EntityNotPersistedException {
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
	public Product updateImage(int productId, Image image)
			throws EntityDoseNotExistException, EntityNotPersistedException {
		Product productfdb = productDAO.get(productId);
		Image oldImage = productfdb.getImage();
		imageDAO.save(image);
		productfdb.setImage(image);
		productDAO.update(productfdb);
		if (oldImage != null)
			imageDAO.delete(oldImage);
		return productfdb;
	}

	@Override
	public Page<Product> get(ProductFilter productFilter, Pageable pageable) {
		return productDAO.get(productFilter, pageable);
	}

	@Override
	public Page<Product> queryByNameWithCategoryName(String query,
			String categoryName, Pageable pageable) {
		return productDAO.queryByNameWithCategoryName(query, categoryName,
				pageable);
	}

	@Override
	public List<Measurement> getMeasurements() {
		return productDAO.getMeasurement();
	}

	@Override
	public void deleteProduct(Integer productId) throws EntityCannotDeleteException, EntityDoseNotExistException{
		Product product = productDAO.get(productId);
		OrderFilter filter = new OrderFilter();
		filter.setProductId(productId);
		Page<Order> page = orderDAO.getOrders(null, filter);
		if(page.getContent().size()==0)
		productDAO.deleteProduct(product);
		else
			throw new EntityCannotDeleteException("Product","This product have orders");
		
	}

}
