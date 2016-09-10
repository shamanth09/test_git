package com.mrkinnoapps.myordershopadmin.dao;

import java.util.List;

import org.springframework.data.domain.Page;

import com.mrkinnoapps.myordershopadmin.bean.entity.Catalog;
import com.mrkinnoapps.myordershopadmin.bean.entity.CatalogDesign;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;

public interface CatalogDAO {

	void saveCatalog(Catalog catalog) throws EntityNotPersistedException;

	void saveDesign(CatalogDesign design) throws EntityNotPersistedException;

	Catalog getCatalog(int id) throws EntityDoseNotExistException;

	Catalog getCatalogByName(String name) throws EntityDoseNotExistException;

	CatalogDesign getDesign(int id) throws EntityDoseNotExistException;

	CatalogDesign getDesignByDesignNo(int page, String designNo)
			throws EntityDoseNotExistException;

	Page<Catalog> findCatalog();

	List<Integer> groupByPageNo(Integer catalogId);

	Page<CatalogDesign> findDesignByPage(int catalogId, int page);
}
