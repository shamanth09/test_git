package com.mrk.myordershop.dao;

import java.util.List;

import com.mrk.myordershop.bean.Category;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

public interface CategoryDAO {

	void save(Category category);

	List<Category> getCategories();
	
	Category getCategory(int categoryId) throws EntityDoseNotExistException;

	Category getCategoryByName(String name) throws EntityDoseNotExistException;

	List<Category> queryByName(String query);
}
