package com.mrk.myordershop.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.mrk.myordershop.bean.Catalog;
import com.mrk.myordershop.bean.CatalogDesign;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

public interface CatalogService {

	Page<Catalog> findCatalog();
	
	CatalogDesign getDesign(int id) throws EntityDoseNotExistException;
	
	Page<CatalogDesign> findDesignByPage(int catalogId, int page);
	
	List<Integer> groupByPageNo(Integer catalogId);
}
