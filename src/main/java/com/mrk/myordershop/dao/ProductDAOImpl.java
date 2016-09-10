package com.mrk.myordershop.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.mrk.myordershop.bean.Measurement;
import com.mrk.myordershop.bean.Product;
import com.mrk.myordershop.bean.dto.ProductFilter;
import com.mrk.myordershop.constant.ActiveFlag;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;

@Repository
public class ProductDAOImpl implements ProductDAO {
	private static final Logger log = Logger.getLogger(ProductDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Product get(int productId) throws EntityDoseNotExistException {
		Product product = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Query query = session.createQuery("from Product as p where p.id=:productId and p.activeFlag=:activeFlag");
			query.setParameter("productId", productId);
			query.setParameter("activeFlag", ActiveFlag.ACTIVE);
			if (query.list().size() < 1)
				throw new EntityDoseNotExistException(Product.class);
			product = (Product) query.list().get(0);
			product.getMeasurements().iterator();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return product;
	}

	@Override
	public void save(Product product) throws EntityNotPersistedException {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.save(product);
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new EntityNotPersistedException(Product.class,
					e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
		}
	}

	@Override
	public Product getByName(String name) throws EntityDoseNotExistException {
		Product product = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Query query = session.createQuery("from Product as p where p.name=:name and p.activeFlag=:activeFlag");
			query.setParameter("name", name);
			query.setParameter("activeFlag", ActiveFlag.ACTIVE);
			if (query.list().size() < 1)
				throw new EntityDoseNotExistException("Product", name);
			product = (Product) query.list().get(0);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return product;
	}

	@Override
	public Page<Product> get(ProductFilter productFilter, Pageable pageable) {
		Page<Product> page = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Product.class, "p");
			criteria.add(Restrictions.eq("p.activeFlag", ActiveFlag.ACTIVE));
			buildFilter(criteria, productFilter);
			criteria.setFetchMode("measurements", FetchMode.SELECT);
			// Iterator<org.springframework.data.domain.Sort.Order> in =
			// pageable
			// .getSort().iterator();
			// while (in.hasNext()) {
			// org.springframework.data.domain.Sort.Order order = in.next();
			// if (order.isAscending())
			// criteria.addOrder(Order.asc(order.getProperty()));
			// if (!order.isAscending())
			// criteria.addOrder(Order.desc(order.getProperty()));
			// }
			int totalCount = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();

			criteria.setProjection(null);
			criteria.setMaxResults(pageable.getPageSize());
			criteria.setFirstResult(pageable.getOffset());
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			List<Product> products = criteria.list();
			page = new PageImpl<Product>(products, pageable, totalCount);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return page;
	}

	private void buildFilter(Criteria criteria, ProductFilter productFilter) {
		criteria.createAlias("p.category", "category");
		if (productFilter.getCategoryId() != null)
			criteria.add(Restrictions.eq("category.id", productFilter.getCategoryId()));
		if (productFilter.getProductId() != null)
			criteria.add(Restrictions.eq("id", productFilter.getProductId()));
		if (productFilter.getCategoryName() != null && !productFilter.getCategoryName().equals(""))
			criteria.add(Restrictions.eq("category.name", productFilter.getCategoryName()));
		if (productFilter.getQuery() != null && !productFilter.getQuery().trim().equals(""))
			criteria.add(Restrictions.like("name", productFilter.getQuery(), MatchMode.ANYWHERE));
		criteria.addOrder(Order.asc("name"));
	}

	@Override
	public Page<Product> getByCategoryId(int categoryId, Pageable pageable) {
		Page<Product> page = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Product.class, "prdct");
			criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			criteria.add(Restrictions.eq("prdct.category.id", categoryId));
			int totalCount = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();

			criteria.setProjection(null).setMaxResults(pageable.getPageSize());
			criteria.setFirstResult(pageable.getOffset());
			page = new PageImpl<Product>(criteria.list(), pageable, totalCount);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return page;
	}

	@Override
	public void update(Product product) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.update(product);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Measurement> getMeasurement() {
		List<Measurement> meas = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Query query = session.createQuery("from Measurement as p");
			meas = query.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return meas;
	}

	@Override
	public Page<Product> queryByNameWithCategoryName(String query, String categoryName, Pageable pageable) {
		Page<Product> page = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Product.class)
					.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE)).createAlias("category", "c")
					.add(Restrictions.eq("c.name", categoryName))
					.add(Restrictions.like("name", query, MatchMode.ANYWHERE));
			int totalCount = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();

			criteria.setProjection(null);
			criteria.setMaxResults(pageable.getPageSize());
			criteria.setFirstResult(pageable.getOffset());
			page = new PageImpl<Product>(criteria.list(), pageable, totalCount);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return page;
	}

}
