package com.mrk.myordershop.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mrk.myordershop.bean.Category;
import com.mrk.myordershop.constant.ActiveFlag;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;

@Repository
public class CategoryDAOImpl implements CategoryDAO {
	private static final Logger log = Logger.getLogger(CategoryDAOImpl.class);
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<Category> getCategories() {
		List<Category> categories = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Query query = session.createQuery("from Category as c");
			categories = query.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return categories;
	}

	@Override
	public void save(Category category) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.saveOrUpdate(category);
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new EntityNotPersistedException(Category.class, e.getMessage());
		}

	}

	/*
	 * Naveen Mar 31, 2015
	 */
	@Override
	public Category getCategory(int categoryId) throws EntityDoseNotExistException {
		Category category = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Query query = session.createQuery("from Category where id=:categoryId");
			query.setParameter("categoryId", categoryId);
			if (query.list().size() < 1)
				throw new EntityDoseNotExistException();
			category = (Category) query.list().get(0);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return category;
	}

	/*
	 * Naveen Apr 11, 2015
	 */
	@Override
	public Category getCategoryByName(String name) throws EntityDoseNotExistException {
		Category category = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Query query = session.createQuery("from Category where name=:name");
			query.setParameter("name", name);
			if (query.list().size() < 1)
				throw new EntityDoseNotExistException();
			category = (Category) query.list().get(0);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return category;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Category> queryByName(String quiry) {
		List<Category> productNames = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Category.class)
					.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE)).addOrder(Order.asc("name"));
			if (quiry != null)
				criteria.add(Restrictions.like("name", quiry, MatchMode.ANYWHERE));
			productNames = (List<Category>) criteria.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return productNames;
	}

}
