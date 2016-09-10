package com.mrkinnoapps.myordershopadmin.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.datapipeline.model.Query;
import com.mrkinnoapps.myordershopadmin.bean.entity.Device;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;

@Repository
public class DeviceDAOImpl implements DeviceDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(Device device) {
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			session.save(device);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	@Override
	public void update(Device device) {
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			session.update(device);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

	}

	@Override
	public Device get(int id) throws EntityDoseNotExistException {
		Device device = null;

		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Device.class);
			criteria.add(Restrictions.eq("id", id));
			if (criteria.list().size() < 1) {
				throw new EntityDoseNotExistException();
			}
			device = (Device) criteria.list().get(0);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return device;
	}

	@Override
	public Device getByDeviceId(String deviceId)
			throws EntityDoseNotExistException {
		Device device = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Device.class);
			criteria.add(Restrictions.eq("deviceToken", deviceId));
			if (criteria.list().size() < 1) {
				throw new EntityDoseNotExistException();
			}
			device = (Device) criteria.list().get(0);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return device;
	}

	@Override
	public List<Device> findByUserId(String userId) {
		List<Device> devices = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Device.class);
			criteria.add(Restrictions.eq("userId", userId));
			devices = criteria.list();
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return devices;
	}

	@Override
	public List<Device> findByClientAndUserId(String clientId, String userId) {
		List<Device> devices = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Device.class);
			criteria.add(Restrictions.eq("clientId", clientId));
			criteria.add(Restrictions.eq("userId", userId));
			devices = criteria.list();
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return devices;
	}

	@Override
	public void delete(Device device) {
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			session.delete(device);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	@Override
	public int getTotalCount(String clientId) {
		int total = 0;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Device.class);
			criteria.add(Restrictions.eq("clientId", clientId));
			total = ((Number) criteria
					.setProjection(Projections.rowCount()).uniqueResult())
					.intValue();
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return total;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<Device> getList(Pageable pageable) {
		Page<Device> page = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Device.class);
			int total = ((Number) criteria
					.setProjection(Projections.rowCount()).uniqueResult())
					.intValue();
			criteria.setProjection(null);
			criteria.setMaxResults(pageable.getPageSize());
			criteria.setFirstResult(pageable.getOffset());
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			page = new PageImpl<>(criteria.list(), pageable, total);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return page;
	}

}
