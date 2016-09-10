package com.mrkinnoapps.myordershopadmin.dao;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mrkinnoapps.myordershopadmin.bean.entity.Retailer;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;

@Repository
public class RetailerDAOImpl implements RetailerDAO {

	@Autowired
	private SessionFactory sessionFactory;


	@Override
	public Retailer getRetailer(String retailerId)
			throws EntityDoseNotExistException {
		Retailer retailer = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Query query = session
					.createQuery("from Retailer as r where r.id=:retailerId");
			query.setParameter("retailerId", retailerId);
			if (query.list().size() < 1)
				throw new EntityDoseNotExistException(Retailer.class);
			retailer = (Retailer) query.list().get(0);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return retailer;

	}

	@Override
	public void updateRetailer(Retailer retailer) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.getTransaction();
		try {
			transaction.begin();
			session.update(retailer);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
}
