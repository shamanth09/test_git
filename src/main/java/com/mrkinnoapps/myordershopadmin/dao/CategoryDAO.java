package com.mrkinnoapps.myordershopadmin.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrkinnoapps.myordershopadmin.bean.entity.Category;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;

public interface CategoryDAO {

	void save(Category category);
	
	void update(Category category);

	Page<Category> getCategories( Pageable pageable);
	
	Category getCategory(int categoryId) throws EntityDoseNotExistException;

	Category getCategoryByName(String name) throws EntityDoseNotExistException;

	List<Category> queryByName(String query);
}
