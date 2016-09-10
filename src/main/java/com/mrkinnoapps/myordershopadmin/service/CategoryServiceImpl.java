package com.mrkinnoapps.myordershopadmin.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mrkinnoapps.myordershopadmin.bean.entity.Category;
import com.mrkinnoapps.myordershopadmin.bean.entity.Image;
import com.mrkinnoapps.myordershopadmin.dao.CategoryDAO;
import com.mrkinnoapps.myordershopadmin.dao.CategorySearcher;
import com.mrkinnoapps.myordershopadmin.dao.ImageDAO;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;
import com.mrkinnoapps.myordershopadmin.util.searchengine.SearchEngine;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryDAO categoryDAO;
	@Autowired
	private ImageDAO imageDAO;
	
	@Autowired
	public CategoryServiceImpl(CategorySearcher categorySearcher,SearchEngine engine) {
		engine.searcherRegister(categorySearcher);
	}

	@Override
	public Page<Category> getCategories(Pageable pageable) {
		return categoryDAO.getCategories(pageable);
	}

	@Override
	public void save(Category category) {
		categoryDAO.save(category);
	}

	@Override
	public void update(int categoryId, Category category)
			throws EntityDoseNotExistException {
		Category categoryFdb = categoryDAO.getCategory(categoryId);
		categoryFdb.setName(category.getName());
		categoryFdb.setUpdateTimestamp(new Date());
		categoryDAO.update(categoryFdb);
	}

	@Override
	public Category updateImage(int categoryId, Image image)
			throws EntityDoseNotExistException, EntityNotPersistedException {
		Category categoryFdb = categoryDAO.getCategory(categoryId);
		Image oldImage = categoryFdb.getImage();
		imageDAO.save(image);
		categoryFdb.setImage(image);
		categoryDAO.update(categoryFdb);
		if (oldImage != null)
			imageDAO.delete(oldImage);
		return categoryFdb;
	}

	/*
	 * Naveen Mar 31, 2015
	 */
	@Override
	public Category getCategory(int categoryId)
			throws EntityDoseNotExistException {
		return categoryDAO.getCategory(categoryId);
	}

	/*
	 * Naveen Apr 11, 2015
	 */
	@Override
	public Category getCategory(String name) throws EntityDoseNotExistException {
		return categoryDAO.getCategoryByName(name);
	}

	@Override
	public List<Category> searchByName(String query) {
		return categoryDAO.queryByName(query);
	}

}
