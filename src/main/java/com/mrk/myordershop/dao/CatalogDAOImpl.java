package com.mrk.myordershop.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;

import com.mrk.myordershop.bean.Catalog;
import com.mrk.myordershop.bean.CatalogDesign;
import com.mrk.myordershop.constant.ActiveFlag;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;

@Repository
public class CatalogDAOImpl implements CatalogDAO {

	@Autowired
	private SessionFactory factory;

	@Override
	public void saveCatalog(Catalog catalog) throws EntityNotPersistedException {
		Session session = factory.getCurrentSession();
		try {
			session.save(catalog);
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new EntityNotPersistedException(Catalog.class,e.getMessage());
		}  
	}

	@Override
	public void saveDesign(CatalogDesign design)
			throws EntityNotPersistedException {
		Session session = factory.getCurrentSession();
		try {
			session.save(design);
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new EntityNotPersistedException(Catalog.class,e.getMessage());
		}  
	}

	@Override
	public Catalog getCatalog(int id) throws EntityDoseNotExistException {
		Catalog catalog = null;
		Session session = factory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Catalog.class);
			criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			criteria.add(Restrictions.eq("id", id));
			List<Catalog> lis = criteria.list();
			if (lis.isEmpty())
				throw new EntityDoseNotExistException(Catalog.class);
			catalog = lis.get(0);
		} catch (HibernateException e) {
			e.printStackTrace();
		} 
		return catalog;
	}

	@Override
	public CatalogDesign getDesign(int id) throws EntityDoseNotExistException {
		CatalogDesign catalog = null;
		Session session = factory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(CatalogDesign.class);
			criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			criteria.add(Restrictions.eq("id", id));
			List<CatalogDesign> lis = criteria.list();
			if (lis.isEmpty())
				throw new EntityDoseNotExistException(Catalog.class);
			catalog = lis.get(0);
		} catch (HibernateException e) {
			e.printStackTrace();
		}  
		return catalog;
	}

	@Override
	public Catalog getCatalogByName(String name)
			throws EntityDoseNotExistException {
		Catalog catalog = null;
		Session session = factory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Catalog.class);
			criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			criteria.add(Restrictions.eq("name", name));
			List<Catalog> lis = criteria.list();
			if (lis.isEmpty())
				throw new EntityDoseNotExistException(Catalog.class);
			catalog = lis.get(0);
		} catch (HibernateException e) {
			e.printStackTrace();
		}  
		return catalog;
	}

	@Override
	public CatalogDesign getDesignByDesignNo(int page, String designNo)
			throws EntityDoseNotExistException {
		CatalogDesign catalog = null;
		Session session = factory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(CatalogDesign.class);
			criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			criteria.add(Restrictions.eq("page", page));
			criteria.add(Restrictions.eq("designNo", designNo));
			List<CatalogDesign> lis = criteria.list();
			if (lis.isEmpty())
				throw new EntityDoseNotExistException(Catalog.class);
			catalog = lis.get(0);
		} catch (HibernateException e) {
			e.printStackTrace();
		}  
		return catalog;
	}

	@Override
	public Page<Catalog> findCatalog() {
		Page<Catalog> catalogs = null;
		Session session = factory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Catalog.class);
			criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			catalogs = new PageImpl<Catalog>(criteria.list());
		} catch (HibernateException e) {
			e.printStackTrace();
		}  
		return catalogs;
	}

	@Override
	public Page<CatalogDesign> findDesignByPage(int catalogId, int page) {
		Page<CatalogDesign> catalogs = null;
		Session session = factory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(CatalogDesign.class);
			criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			criteria.add(Restrictions.eq("page", page));
			criteria.add(Restrictions.eq("catalog.id", catalogId));
			catalogs = new PageImpl<CatalogDesign>(criteria.list());
		} catch (HibernateException e) {
			e.printStackTrace();
		} 
		return catalogs;
	}

	@Override
	public List<Integer> groupByPageNo(Integer catalogId) {
		List<Integer> pages = null;
		Session session = factory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(CatalogDesign.class);
			criteria.setProjection(Projections.groupProperty("page"));
			criteria.add(Restrictions.eq("catalog.id", catalogId));
			pages = criteria.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}  
		return pages;
	}

}
