package com.mrk.myordershop.service;

import java.util.List;

import com.mrk.myordershop.bean.Category;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

public interface CategoryService {

	List<Category> getCategories();

	Category getCategory(int categoryId) throws EntityDoseNotExistException;

	void save(Category category);

	/**
	 * Naveen
	 * Apr 11, 2015
	 * void
	 *
	 */
	Category getCategory(String name) throws EntityDoseNotExistException;
	
	List<Category> searchByName(String quiry);
}
