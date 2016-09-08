package com.mrk.myordershop.dao;

import java.util.List;

import org.springframework.data.domain.Page;

import com.mrk.myordershop.bean.Catalog;
import com.mrk.myordershop.bean.CatalogDesign;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;

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
