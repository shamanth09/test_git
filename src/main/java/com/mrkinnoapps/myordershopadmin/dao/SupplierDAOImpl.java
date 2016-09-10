package com.mrkinnoapps.myordershopadmin.dao;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mrkinnoapps.myordershopadmin.bean.entity.Supplier;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;

@Repository
public class SupplierDAOImpl implements SupplierDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(Supplier supplier) throws EntityNotPersistedException {

		Session session = sessionFactory.openSession();
		Transaction transaction = session.getTransaction();
		try {
			transaction.begin();
			session.save(supplier);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new EntityNotPersistedException();
		} finally {
			session.close();
		}

	}

	@Override
	public Supplier getSupplier(String supplierId)
			throws EntityDoseNotExistException {
		Supplier supplier = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Query query = session
					.createQuery("from Supplier as r where r.id=:supplierId");
			query.setParameter("supplierId", supplierId);
			if (query.list().size() < 1)
				throw new EntityDoseNotExistException(Supplier.class);
			supplier = (Supplier) query.list().get(0);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return supplier;

	}

	@Override
	public void updateSupplier(Supplier supplier) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.getTransaction();
		try {
			transaction.begin();
			session.update(supplier);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
}
