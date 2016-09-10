package com.mrkinnoapps.myordershopadmin.dao;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mrkinnoapps.myordershopadmin.bean.constant.ActiveFlag;
import com.mrkinnoapps.myordershopadmin.bean.entity.Address;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;

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
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			session.save(address);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	/*
	 * Naveen Mar 30, 2015
	 */
	@Override
	public void update(Address address) {
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			session.update(address);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	/*
	 * Naveen Mar 30, 2015
	 */
	@Override
	public Address getAddresByUserId(String userId)
			throws EntityDoseNotExistException {
		Address address = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Query query = session
					.createQuery("from Address as ad where ad.user.id=:userId and ad.activeFlag=:activeFlag");
			query.setParameter("userId", userId);
			query.setParameter("activeFlag", ActiveFlag.ACTIVE);
			if (query.list().size() < 1)
				throw new EntityDoseNotExistException();
			address = (Address) query.list().get(0);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return address;
	}

	/*
	 * Naveen Mar 30, 2015
	 */
	@Override
	public Address getAddress(int id) throws EntityDoseNotExistException {
		Address address = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Query query = session
					.createQuery("from Address as ad where ad.id=:id");
			query.setParameter("id", id);
			if (query.list().size() < 1)
				throw new EntityDoseNotExistException();
			address = (Address) query.list().get(0);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return address;
	}

	@Override
	public void delete(Address address) {
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			session.delete(address);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

}
