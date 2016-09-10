package com.mrk.myordershop.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mrk.myordershop.bean.Measurement;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;

@Repository
public class MeasurementDAOImpl implements MeasurementDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(Measurement measurement) throws EntityNotPersistedException {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.save(measurement);
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new EntityNotPersistedException(e.getMessage());
		}
	}

	@Override
	public Measurement get(int id) throws EntityDoseNotExistException {
		Measurement mess = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			List<Measurement> ms = session.createCriteria(Measurement.class).add(Restrictions.eq("id", id))
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
			if (ms.size() < 1)
				throw new EntityDoseNotExistException();
			else
				mess = ms.get(0);
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new EntityDoseNotExistException();
		}
		return mess;
	}

	@Override
	public Measurement get(Measurement.v name) throws EntityDoseNotExistException {
		Measurement mess = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			List<Measurement> ms = session.createCriteria(Measurement.class).add(Restrictions.eq("measurement", name))
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
			if (ms.size() < 1)
				throw new EntityDoseNotExistException();
			else
				mess = ms.get(0);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return mess;
	}

	@Override
	public void delete(Measurement measurement) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.delete(measurement);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Measurement> fine() {
		List<Measurement> mess = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			mess = session.createCriteria(Measurement.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return mess;
	}

}
