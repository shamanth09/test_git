package com.mrkinnoapps.myordershopadmin.dao;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mrkinnoapps.myordershopadmin.bean.constant.ActiveFlag;
import com.mrkinnoapps.myordershopadmin.bean.entity.Detail;
import com.mrkinnoapps.myordershopadmin.bean.entity.Item;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;

@Repository
public class ItemDAOImpl implements ItemDAO {

	private static Logger log = Logger.getLogger(ItemDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void saveItem(Item item) throws EntityNotPersistedException {
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			session.save(item);
			transaction.commit();
			session.refresh(item);
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new EntityNotPersistedException(Item.class);
		} finally {
			session.close();
		}
	}

	@Override
	public void update(Item item) {
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			session.update(item);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	@Override
	public Item getItem(int id) throws EntityDoseNotExistException {
		Item order = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Item.class);
			criteria.add(Restrictions.eq("id", id));
			if (criteria.list().size() < 1)
				throw new EntityDoseNotExistException(Item.class);
			order = (Item) criteria.list().get(0);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return order;
	}

	@Override
	public void deleteItem(Item item) {
		Detail detail = item.getDetail();
		item.setDetail(null);
		update(item);
		if (detail != null) {
			log.debug("delete detail = " + detail.getId());
			deleteDetail(detail);
		}
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			session.delete(item);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public void deleteDetail(Detail detail) {
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			session.delete(detail);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	@Override
	public Item getByOrderId(int orderId) {
		Item item = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Item.class, "it");
			criteria.add(Restrictions.eq("it.order.id", orderId));
			criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			item = (Item) criteria.list().get(0);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return item;
	}

}
