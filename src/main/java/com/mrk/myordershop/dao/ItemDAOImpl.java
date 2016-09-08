package com.mrk.myordershop.dao;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mrk.myordershop.bean.Item;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;

@Repository
public class ItemDAOImpl implements ItemDAO {

	private static Logger log = Logger.getLogger(ItemDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(Item item) throws EntityNotPersistedException {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.save(item);
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new EntityNotPersistedException(Item.class,e.getMessage());
		}  
	}

	@Override
	public void update(Item item) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.update(item);
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new EntityNotPersistedException(Item.class,e.getMessage());
		} 
	}

	@Override
	public Item get(int id) throws EntityDoseNotExistException {
		Item order = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Item.class);
			criteria.add(Restrictions.eq("id", id));
			if (criteria.list().size() < 1)
				throw new EntityDoseNotExistException(Item.class);
			order = (Item) criteria.list().get(0);
		} catch (HibernateException e) {
			e.printStackTrace();
		}  
		return order;
	}

}
