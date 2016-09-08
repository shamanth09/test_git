package com.mrk.myordershop.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mrk.myordershop.bean.MeltingAndSeal;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.constant.ActiveFlag;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;

@Repository
public class MeltingAndSealDAOImpl implements MeltingAndSealDAO {

	private static final Logger log = Logger
			.getLogger(MeltingAndSealDAOImpl.class);
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(MeltingAndSeal meltingAndSeal)
			throws EntityNotPersistedException {

		Session session = sessionFactory.getCurrentSession();
		try {
			session.save(meltingAndSeal);
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new EntityNotPersistedException(MeltingAndSeal.class,e.getMessage());
		}  
	}

	@SuppressWarnings("unchecked")
	@Override
	public MeltingAndSeal get(int id) throws EntityDoseNotExistException {
		List<MeltingAndSeal> meltingAndSeals = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			meltingAndSeals = session
					.createQuery("from MeltingAndSeal as m where m.id=:id")
					.setParameter("id", id).list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}  
		if (meltingAndSeals.size() < 1)
			throw new EntityDoseNotExistException(MeltingAndSeal.class, id);
		return meltingAndSeals.get(0);
	}

	@SuppressWarnings("unchecked")
	public MeltingAndSeal get(String seal, int melting, Wholesaler wholesaler)
			throws EntityDoseNotExistException {
		List<MeltingAndSeal> meltingAndSeals = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			meltingAndSeals = session
					.createQuery(
							"from MeltingAndSeal as m where m.seal=:seal and  m.melting=:melting and m.wholesaler = :wholesaler")
					.setParameter("seal", seal)
					.setParameter("melting", melting)
					.setParameter("wholesaler", wholesaler).list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}  
		if (meltingAndSeals.size() < 1)
			throw new EntityDoseNotExistException(MeltingAndSeal.class);
		return meltingAndSeals.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MeltingAndSeal> find(Wholesaler wholesaler) {
		List<MeltingAndSeal> meltingAndSeals = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			meltingAndSeals = session
					.createQuery(
							"from MeltingAndSeal as m where m.wholesaler=:wholesaler and m.activeFlag=:activeFlag order by m.melting desc")
					.setParameter("wholesaler", wholesaler)
					.setParameter("activeFlag", ActiveFlag.ACTIVE).list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}  
		return meltingAndSeals;
	}

	@Override
	public void delete(MeltingAndSeal meltingAndSeal) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.delete(meltingAndSeal);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}

}
