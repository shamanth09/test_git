package com.mrk.myordershop.client.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mrk.myordershop.bean.Device;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

@Repository
public class DeviceDAOImpl implements DeviceDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(Device device) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.save(device);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Device device) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.update(device);
		} catch (HibernateException e) {
			e.printStackTrace();
		}

	}

	@Override
	public Device get(int id) throws EntityDoseNotExistException {
		Device device = null;

		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Device.class);
			criteria.add(Restrictions.eq("id", id));
			if (criteria.list().size() < 1) {
				throw new EntityDoseNotExistException();
			}
			device = (Device) criteria.list().get(0);
		} catch (HibernateException e) {
			e.printStackTrace();
		}

		return device;
	}

	@Override
	public Device getByDeviceId(String deviceId)
			throws EntityDoseNotExistException {
		Device device = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Device.class);
			criteria.add(Restrictions.eq("deviceToken", deviceId));
			if (criteria.list().size() < 1) {
				throw new EntityDoseNotExistException();
			}
			device = (Device) criteria.list().get(0);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return device;
	}

	@Override
	public List<Device> findByUserId(String userId) {
		List<Device> devices = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Device.class);
			criteria.add(Restrictions.eq("userId", userId));
			devices = criteria.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}

		return devices;
	}

	@Override
	public List<Device> findByClientAndUserId(String clientId, String userId) {
		List<Device> devices = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Device.class);
			criteria.add(Restrictions.eq("clientId", clientId));
			criteria.add(Restrictions.eq("userId", userId));
			devices = criteria.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return devices;
	}

	@Override
	public void delete(Device device) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.delete(device);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}

}
