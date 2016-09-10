package com.mrkinnoapps.myordershopadmin.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.mrkinnoapps.myordershopadmin.bean.entity.Catalog;
import com.mrkinnoapps.myordershopadmin.bean.entity.CatalogDesign;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;

public interface CatalogService {

	Page<Catalog> findCatalog();
	
	CatalogDesign getDesign(int id) throws EntityDoseNotExistException;
	
	Page<CatalogDesign> findDesignByPage(int catalogId, int page);
	
	List<Integer> groupByPageNo(Integer catalogId);
}
