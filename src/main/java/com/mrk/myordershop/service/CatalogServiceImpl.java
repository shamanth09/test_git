package com.mrk.myordershop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.mrk.myordershop.bean.Catalog;
import com.mrk.myordershop.bean.CatalogDesign;
import com.mrk.myordershop.config.annotation.ReadTransactional;
import com.mrk.myordershop.dao.CatalogDAO;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

@Service
public class CatalogServiceImpl implements CatalogService {

	@Autowired
	private CatalogDAO catalogDAO;

	@Override
	@ReadTransactional
	public Page<Catalog> findCatalog() {
		return catalogDAO.findCatalog();
	}

	@Override
	@ReadTransactional
	public Page<CatalogDesign> findDesignByPage(int catalogId, int page) {
		return catalogDAO.findDesignByPage(catalogId, page);
	}

	@Override
	@ReadTransactional
	public List<Integer> groupByPageNo(Integer catalogId) {
		return catalogDAO.groupByPageNo(catalogId);
	}

	@Override
	@ReadTransactional
	public CatalogDesign getDesign(int id) throws EntityDoseNotExistException {
		return catalogDAO.getDesign(id);
	}

}
