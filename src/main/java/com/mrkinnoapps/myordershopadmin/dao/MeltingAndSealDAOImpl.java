package com.mrkinnoapps.myordershopadmin.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mrkinnoapps.myordershopadmin.bean.entity.MeltingAndSeal;
import com.mrkinnoapps.myordershopadmin.bean.entity.Wholesaler;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;

@Repository
public class MeltingAndSealDAOImpl implements MeltingAndSealDAO {

	private static final Logger log = Logger
			.getLogger(MeltingAndSealDAOImpl.class);
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(MeltingAndSeal meltingAndSeal)
			throws EntityNotPersistedException {

		Session session = sessionFactory.openSession();
		session.beginTransaction();
		try {
			session.save(meltingAndSeal);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			throw new EntityNotPersistedException(MeltingAndSeal.class);
		} finally {
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public MeltingAndSeal get(int id) throws EntityDoseNotExistException {
		List<MeltingAndSeal> meltingAndSeals = null;
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		try {
			meltingAndSeals = session
					.createQuery("from MeltingAndSeal as m where m.id=:id")
					.setParameter("id", id).list();
			session.getTransaction().commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
		if (meltingAndSeals.size() < 1)
			throw new EntityDoseNotExistException(MeltingAndSeal.class, id);
		return meltingAndSeals.get(0);
	}
	
	

	@SuppressWarnings("unchecked")
	public MeltingAndSeal get(String seal, int melting, Wholesaler wholesaler)
			throws EntityDoseNotExistException {
		List<MeltingAndSeal> meltingAndSeals = null;
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		try {
			meltingAndSeals = session
					.createQuery(
							"from MeltingAndSeal as m where m.seal=:seal and  m.melting=:melting and m.wholesaler = :wholesaler")
					.setParameter("seal", seal)
					.setParameter("melting", melting)
					.setParameter("wholesaler", wholesaler).list();
			session.getTransaction().commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
		if (meltingAndSeals.size() < 1)
			throw new EntityDoseNotExistException(MeltingAndSeal.class);
		return meltingAndSeals.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MeltingAndSeal> find(Wholesaler wholesaler) {
		List<MeltingAndSeal> meltingAndSeals = null;
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		try {
			meltingAndSeals = session
					.createQuery(
							"from MeltingAndSeal as m where m.wholesaler=:wholesaler")
					.setParameter("wholesaler", wholesaler)
					.list();
			session.getTransaction().commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
		return meltingAndSeals;
	}

	@Override
	public void delete(MeltingAndSeal meltingAndSeal) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		try {
			session.delete(meltingAndSeal);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}
	
	@Override
	public void update(MeltingAndSeal seal) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.getTransaction();
		try {
			transaction.begin();
			session.update(seal);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
}
