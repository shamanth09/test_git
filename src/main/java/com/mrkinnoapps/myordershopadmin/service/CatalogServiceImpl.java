package com.mrkinnoapps.myordershopadmin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.mrkinnoapps.myordershopadmin.bean.entity.Catalog;
import com.mrkinnoapps.myordershopadmin.bean.entity.CatalogDesign;
import com.mrkinnoapps.myordershopadmin.dao.CatalogDAO;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;

@Service
public class CatalogServiceImpl implements CatalogService {

	@Autowired
	private CatalogDAO catalogDAO;

	@Override
	public Page<Catalog> findCatalog() {
		return catalogDAO.findCatalog();
	}

	@Override
	public Page<CatalogDesign> findDesignByPage(int catalogId, int page) {
		return catalogDAO.findDesignByPage(catalogId, page);
	}

	@Override
	public List<Integer> groupByPageNo(Integer catalogId) {
		return catalogDAO.groupByPageNo(catalogId);
	}

	@Override
	public CatalogDesign getDesign(int id) throws EntityDoseNotExistException {
		return catalogDAO.getDesign(id);
	}

}
