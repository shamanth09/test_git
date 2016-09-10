package com.mrk.myordershop.dao.supplier;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mrk.myordershop.bean.Supplier;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;

@Repository
public class SupplierDAOImpl implements SupplierDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(Supplier supplier) throws EntityNotPersistedException {

		Session session = sessionFactory.getCurrentSession();
		try {
			session.save(supplier);
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new EntityNotPersistedException(Supplier.class, e.getMessage());
		}

	}

	@Override
	public Supplier getSupplier(String supplierId) throws EntityDoseNotExistException {
		Supplier supplier = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Query query = session.createQuery("from Supplier as r where r.id=:supplierId");
			query.setParameter("supplierId", supplierId);
			if (query.list().size() < 1)
				throw new EntityDoseNotExistException(Supplier.class);
			supplier = (Supplier) query.list().get(0);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return supplier;

	}

	@Override
	public void updateSupplier(Supplier supplier) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.update(supplier);
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new EntityNotPersistedException(Supplier.class, e.getMessage());
		}
	}
}
