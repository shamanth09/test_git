package com.mrk.myordershop.dao;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mrk.myordershop.bean.Address;
import com.mrk.myordershop.constant.ActiveFlag;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;

/**
 * AddressDAOImpl.java Naveen Mar 30, 2015
 */
@Repository
public class AddressDAOImpl implements AddressDAO {

	@Autowired
	private SessionFactory sessionFactory;

	/*
	 * Naveen Mar 30, 2015
	 */
	@Override
	public void save(Address address) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.save(address);
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new EntityNotPersistedException(Address.class, e.getMessage());
		}
	}

	/*
	 * Naveen Mar 30, 2015
	 */
	@Override
	public void update(Address address) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.update(address);
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new EntityNotPersistedException(Address.class, e.getMessage());
		}
	}

	/*
	 * Naveen Mar 30, 2015
	 */
	@Override
	public Address getAddresByUserId(String userId) throws EntityDoseNotExistException {
		Address address = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Query query = session
					.createQuery("from Address as ad where ad.user.id=:userId and ad.activeFlag=:activeFlag");
			query.setParameter("userId", userId);
			query.setParameter("activeFlag", ActiveFlag.ACTIVE);
			if (query.list().size() < 1)
				throw new EntityDoseNotExistException();
			address = (Address) query.list().get(0);
		} catch (HibernateException e) {
			e.printStackTrace();

		}
		return address;
	}

	/*
	 * Naveen Mar 30, 2015
	 */
	@Override
	public Address getAddress(int id) throws EntityDoseNotExistException {
		Address address = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Query query = session.createQuery("from Address as ad where ad.id=:id");
			query.setParameter("id", id);
			if (query.list().size() < 1)
				throw new EntityDoseNotExistException();
			address = (Address) query.list().get(0);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return address;
	}

	@Override
	public void delete(Address address) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.delete(address);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}
}
