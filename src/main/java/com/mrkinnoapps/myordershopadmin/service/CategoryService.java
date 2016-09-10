package com.mrkinnoapps.myordershopadmin.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrkinnoapps.myordershopadmin.bean.entity.Category;
import com.mrkinnoapps.myordershopadmin.bean.entity.Image;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;

public interface CategoryService {

	Page<Category> getCategories(Pageable pageable);

	Category getCategory(int categoryId) throws EntityDoseNotExistException;

	void save(Category category) throws EntityNotPersistedException;

	void update(int categoryId, Category category)
			throws EntityDoseNotExistException;

	Category updateImage(int categoryId, Image image)
			throws EntityDoseNotExistException, EntityNotPersistedException;

	Category getCategory(String name) throws EntityDoseNotExistException;

	List<Category> searchByName(String quiry);
}
