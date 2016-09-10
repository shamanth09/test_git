package com.mrkinnoapps.myordershopadmin.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
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
import com.mrkinnoapps.myordershopadmin.bean.entity.Category;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;

@Repository
public class CategoryDAOImpl implements CategoryDAO {
	private static final Logger log = Logger.getLogger(CategoryDAOImpl.class);
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Page<Category> getCategories(Pageable pageable) {
		Page<Category> page = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Category.class);
			int total = ((Number) criteria
					.setProjection(Projections.rowCount()).uniqueResult())
					.intValue();
			criteria.setProjection(null);
			if (pageable != null) {
				criteria.setMaxResults(pageable.getPageSize());
				criteria.setFirstResult(pageable.getOffset());
			}
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			page = new PageImpl<Category>(criteria.list(), pageable, total);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return page;
	}

	@Override
	public void save(Category category) {
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			session.save(category);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

	}

	@Override
	public void update(Category category) {
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			session.update(category);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	/*
	 * Naveen Mar 31, 2015
	 */
	@Override
	public Category getCategory(int categoryId)
			throws EntityDoseNotExistException {
		Category category = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Query query = session
					.createQuery("from Category where id=:categoryId");
			query.setParameter("categoryId", categoryId);
			if (query.list().size() < 1)
				throw new EntityDoseNotExistException();
			category = (Category) query.list().get(0);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return category;
	}

	/*
	 * Naveen Apr 11, 2015
	 */
	@Override
	public Category getCategoryByName(String name)
			throws EntityDoseNotExistException {
		Category category = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Query query = session.createQuery("from Category where name=:name");
			query.setParameter("name", name);
			if (query.list().size() < 1)
				throw new EntityDoseNotExistException();
			category = (Category) query.list().get(0);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return category;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Category> queryByName(String quiry) {
		List<Category> productNames = null;
		Session session = sessionFactory.openSession();
		Transaction transaction = session.getTransaction();
		try {
			transaction.begin();
			Criteria criteria = session.createCriteria(Category.class)
					.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE))
					.addOrder(Order.asc("name"));
			if (quiry != null)
				criteria.add(Restrictions.like("name", quiry,
						MatchMode.ANYWHERE));
			productNames = (List<Category>) criteria.list();
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return productNames;
	}

}
