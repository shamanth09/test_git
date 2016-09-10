package com.mrk.myordershop.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mrk.myordershop.bean.Subscription;
import com.mrk.myordershop.exception.EntityNotPersistedException;

@Repository
public class SubscriptionDAOImpl implements SubscriptionDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void subscrip(Subscription subscription)
			throws EntityNotPersistedException {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.save(subscription);
		} catch (HibernateException e) {
			throw new EntityNotPersistedException(Subscription.class,e.getMessage());
		}  
	}
}
