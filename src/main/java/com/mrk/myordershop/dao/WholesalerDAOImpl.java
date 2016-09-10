package com.mrk.myordershop.dao;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

@Repository
public class WholesalerDAOImpl implements WholesalerDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void saveWholesaler(Wholesaler wholesaler) {

		Session session = sessionFactory.getCurrentSession();
		try {
			session.save(wholesaler);
		} catch (HibernateException e) {
			e.printStackTrace();
		}  

	}

	@Override
	public Wholesaler getWholesaler(String wholesalerId)
			throws EntityDoseNotExistException {
		Wholesaler wholesaler = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Query query = session
					.createQuery("from Wholesaler as r where r.id=:wholesalerId");
			query.setParameter("wholesalerId", wholesalerId);
			if (query.list().size() < 1)
				throw new EntityDoseNotExistException(Wholesaler.class);
			wholesaler = (Wholesaler) query.list().get(0);
		} catch (HibernateException e) {
			e.printStackTrace();
		} 

		return wholesaler;

	}

	@Override
	public void updateWholesaler(Wholesaler wholesaler) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.update(wholesaler);
		} catch (HibernateException e) {
			e.printStackTrace();
		}  
	}
}
