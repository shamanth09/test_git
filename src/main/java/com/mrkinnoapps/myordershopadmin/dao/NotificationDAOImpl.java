package com.mrkinnoapps.myordershopadmin.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.mrkinnoapps.myordershopadmin.bean.constant.ActiveFlag;
import com.mrkinnoapps.myordershopadmin.bean.entity.Notification;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;

@Repository
public class NotificationDAOImpl implements NotificationDAO {

	private static final Logger log = Logger.getLogger(NotificationDAOImpl.class);
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(Notification notificaion)
			throws EntityNotPersistedException {
		log.info("saving "+notificaion);
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.save(notificaion);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
			throw new EntityNotPersistedException(Notification.class);
		} finally {
			session.close();
		}
	}

	@Override
	public void update(Notification notificaion)
			throws EntityNotPersistedException {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.update(notificaion);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
			throw new EntityNotPersistedException(Notification.class);
		} finally {
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Notification get(int id) throws EntityDoseNotExistException {
		Notification notificaion = null;
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Notification.class);
			criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			criteria.add(Restrictions.eq("id", id));
			List<Notification> list = criteria.list();
			if (list.size() < 1)
				throw new EntityDoseNotExistException(Notification.class, id);
			notificaion = list.get(0);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			transaction.rollback();
		} finally {
			session.close();
		}
		return notificaion;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<Notification> findByUserId(String userId, Pageable pageable) {
		Page<Notification> page = null;
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Notification.class);
			criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			criteria.add(Restrictions.eq("userId", userId));
			criteria.addOrder(Order.desc("createTimestamp"));
			int total = ((Number) criteria
					.setProjection(Projections.rowCount()).uniqueResult())
					.intValue();
			criteria.setProjection(null);
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			page = new PageImpl<Notification>(criteria.list(), pageable, total);
		} catch (HibernateException e) {
			e.printStackTrace();
			transaction.rollback();
		} finally {
			session.close();
		}
		return page;
	}

	@Override
	public void delete(Notification notificaion) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.delete(notificaion);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			transaction.rollback();
		} finally {
			session.close();
		}
	}

	@Override
	public void deleteAll(User user) {
		Session session = sessionFactory.openSession();
		try {
			session.beginTransaction();
			session.createQuery(
					"delete from Notification n where n.userId = :userId")
					.setParameter("userId", user.getId()).executeUpdate();
			session.getTransaction().commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}
}
