package com.mrkinnoapps.myordershopadmin.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.mrkinnoapps.myordershopadmin.bean.constant.ActiveFlag;
import com.mrkinnoapps.myordershopadmin.bean.dto.ProductFilter;
import com.mrkinnoapps.myordershopadmin.bean.entity.Measurement;
import com.mrkinnoapps.myordershopadmin.bean.entity.Product;
import com.mrkinnoapps.myordershopadmin.exception.EntityCannotDeleteException;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;

@Repository
public class ProductDAOImpl implements ProductDAO {
	private static final Logger log = Logger.getLogger(ProductDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	// @SuppressWarnings("unchecked")
	// @Override
	// public Page<Product> getProductsByCategoryId(int categoryId,
	// PageMeta pageMeta) throws EntityDoseNotExistException {
	// Page<Product> page = null;
	// List<Product> products = null;
	// Session session = sessionFactory.openSession();
	// try {
	// Transaction transaction = session.beginTransaction();
	// Criteria criteria = session.createCriteria(Product.class, "prdct");
	// criteria.add(Restrictions.eq("prdct.category.id", categoryId));
	// int totalCount = criteria.list().size();
	// criteria.setMaxResults(pageMeta.getLimit());
	// criteria.setFirstResult(pageMeta.getOffset());
	// products = (List<Product>) criteria.list();
	// if (products.size() < 1)
	// throw new EntityDoseNotExistException();
	// page = new MyPage<Product>(products, totalCount,
	// pageMeta.getOffset(), pageMeta.getLimit());
	// transaction.commit();
	// } catch (HibernateException e) {
	// e.printStackTrace();
	// } finally {
	// session.close();
	// }
	// return page;
	// }

	@Override
	public Product get(int productId) throws EntityDoseNotExistException {
		Product product = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Query query = session
					.createQuery("from Product as p where p.id=:productId and p.activeFlag=:activeFlag");
			query.setParameter("productId", productId);
			query.setParameter("activeFlag", ActiveFlag.ACTIVE);
			if (query.list().size() < 1)
				throw new EntityDoseNotExistException(Product.class);
			product = (Product) query.list().get(0);
			product.getMeasurements().iterator();
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return product;
	}

	@Override
	public void save(Product product) throws EntityNotPersistedException {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.getTransaction();
		try {
			transaction.begin();
			session.save(product);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new EntityNotPersistedException(Product.class,
					e.getCause() != null ? e.getCause().getMessage()
							: e.getMessage());
		} finally {
			session.close();
		}
	}

	@Override
	public Product getByName(String name) throws EntityDoseNotExistException {
		Product product = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Query query = session
					.createQuery("from Product as p where p.name=:name and p.activeFlag=:activeFlag");
			query.setParameter("name", name);
			query.setParameter("activeFlag", ActiveFlag.ACTIVE);
			if (query.list().size() < 1)
				throw new EntityDoseNotExistException("Product", name);
			product = (Product) query.list().get(0);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return product;
	}

	@Override
	public Page<Product> get(ProductFilter productFilter, Pageable pageable) {
		Page<Product> page = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
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
			int totalCount = ((Number) criteria.setProjection(
					Projections.rowCount()).uniqueResult()).intValue();

			criteria.setProjection(null);
			criteria.setMaxResults(pageable.getPageSize());
			criteria.setFirstResult(pageable.getOffset());
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			List<Product> products = criteria.list();
			page = new PageImpl<Product>(products, pageable, totalCount);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return page;
	}

	private void buildFilter(Criteria criteria, ProductFilter productFilter) {
		criteria.createAlias("p.category", "category");
		if (productFilter.getCategoryId() != null)
			criteria.add(Restrictions.eq("category.id",
					productFilter.getCategoryId()));
		if (productFilter.getCategoryName() != null
				&& !productFilter.getCategoryName().equals(""))
			criteria.add(Restrictions.eq("category.name",
					productFilter.getCategoryName()));
		if (productFilter.getQuery() != null
				&& !productFilter.getQuery().trim().equals(""))
			criteria.add(Restrictions.like("name", productFilter.getQuery(),
					MatchMode.ANYWHERE));
		criteria.addOrder(Order.asc("name"));
	}

	@Override
	public Page<Product> getByCategoryId(int categoryId, Pageable pageable) {
		Page<Product> page = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Product.class, "prdct");
			criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			criteria.setFetchMode("measurements", FetchMode.SELECT);
			criteria.add(Restrictions.eq("prdct.category.id", categoryId));
			int totalCount = ((Number) criteria.setProjection(
					Projections.rowCount()).uniqueResult()).intValue();
			criteria.setProjection(null);
			if(pageable!=null){
			criteria.setMaxResults(pageable.getPageSize());
			criteria.setFirstResult(pageable.getOffset());
			}
//			criteria.setProjection( Projections.projectionList()
//			        .add( Projections.distinct(Projections.property("sku"))));
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			page = new PageImpl<Product>(criteria.list(), pageable,totalCount);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return page;
	}

	@Override
	public void update(Product product) throws EntityNotPersistedException {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.getTransaction();
		try {
			transaction.begin();
			session.update(product);
			transaction.commit();
		} catch (HibernateException e) {
			//e.printStackTrace();
			throw new EntityNotPersistedException(e.getCause().getMessage());
		} finally {
			session.close();
		}
	}

	@Override
	public List<Measurement> getMeasurement() {
		List<Measurement> meas = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Query query = session.createQuery("from Measurement as p");
			meas = query.list();
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return meas;
	}

	@Override
	public Page<Product> queryByNameWithCategoryName(String query,
			String categoryName, Pageable pageable) {
		Page<Product> page = null;
		Session session = sessionFactory.openSession();
		Transaction transaction = session.getTransaction();
		try {
			transaction.begin();
			Criteria criteria = session.createCriteria(Product.class)
					.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE))
					.createAlias("category", "c")
					.add(Restrictions.eq("c.name", categoryName))
					.add(Restrictions.like("name", query, MatchMode.ANYWHERE));
			int totalCount = ((Number) criteria.setProjection(
					Projections.rowCount()).uniqueResult()).intValue();

			criteria.setProjection(null);
			criteria.setMaxResults(pageable.getPageSize());
			criteria.setFirstResult(pageable.getOffset());
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			page = new PageImpl<Product>(criteria.list(), pageable, totalCount);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return page;
	}

	@Override
	public void deleteProduct(Product product) throws EntityCannotDeleteException {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.getTransaction();
		try {
			transaction.begin();
			session.delete(product);
			transaction.commit();
		} catch (HibernateException e) {
			throw new EntityCannotDeleteException(Product.class);
		} finally {
			session.close();
		}
	}
}
