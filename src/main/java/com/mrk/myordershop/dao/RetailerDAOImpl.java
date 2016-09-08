package com.mrk.myordershop.dao;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mrk.myordershop.bean.Retailer;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

@Repository
public class RetailerDAOImpl implements RetailerDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Retailer getRetailer(String retailerId) throws EntityDoseNotExistException {
		Retailer retailer = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Query query = session.createQuery("from Retailer as r where r.id=:retailerId");
			query.setParameter("retailerId", retailerId);
			if (query.list().size() < 1)
				throw new EntityDoseNotExistException(Retailer.class);
			retailer = (Retailer) query.list().get(0);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return retailer;

	}

	@Override
	public void updateRetailer(Retailer retailer) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.update(retailer);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}
}
