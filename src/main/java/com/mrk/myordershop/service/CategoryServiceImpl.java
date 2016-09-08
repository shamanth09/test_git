package com.mrk.myordershop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrk.myordershop.bean.Category;
import com.mrk.myordershop.config.annotation.PersistTransactional;
import com.mrk.myordershop.config.annotation.ReadTransactional;
import com.mrk.myordershop.dao.CategoryDAO;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryDAO categoryDAO;
	
	@Override
	@ReadTransactional
	public List<Category> getCategories() {
		return categoryDAO.getCategories();
	}

	@Override
	@PersistTransactional
	public void save(Category category) {
		categoryDAO.save(category);
	}

	/* 
	 * Naveen
	 * Mar 31, 2015
	 */
	@Override
	@ReadTransactional
	public Category getCategory(int categoryId) throws EntityDoseNotExistException {
		return categoryDAO.getCategory(categoryId);
	}

	/* 
	 * Naveen
	 * Apr 11, 2015
	 */
	@Override
	@ReadTransactional
	public Category getCategory(String name) throws EntityDoseNotExistException {
		return categoryDAO.getCategoryByName(name);
	}

	@Override
	@ReadTransactional
	public List<Category> searchByName(String query) {
		return categoryDAO.queryByName(query);
	}

}
