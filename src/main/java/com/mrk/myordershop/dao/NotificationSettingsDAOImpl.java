package com.mrk.myordershop.dao;


import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mrk.myordershop.bean.NotificationSettings;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;

@Repository
public class NotificationSettingsDAOImpl implements NotificationSettingsDAO {
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(NotificationSettings notificationSettings) throws EntityNotPersistedException {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.save(notificationSettings);
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new EntityNotPersistedException(NotificationSettings.class, e.getCause()
					.getMessage());
		}
	}

	@Override
	public void update(NotificationSettings notificationSettings) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.update(notificationSettings);
		} catch (HibernateException e) {
			e.printStackTrace();
		}  
		
	}

	@Override
	public NotificationSettings getNotificationByUserID(String userID) throws EntityDoseNotExistException {
		NotificationSettings notificationSettings = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(NotificationSettings.class,"n");
			criteria.createAlias("n.user", "u");
			criteria.add(Restrictions.eq("u.id",userID));
			if(criteria.list().size() < 1){
				throw new EntityDoseNotExistException(NotificationSettings.class);
			}
			notificationSettings = (NotificationSettings) criteria.list().get(0);
		} catch (HibernateException e) {
			e.printStackTrace();
		}  
		return notificationSettings;
	}

	@Override
	public NotificationSettings getNotification(int id) throws EntityDoseNotExistException {
		NotificationSettings notificationSettings = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Query query = session.createQuery("from NotificationSettings where id=:id");
			query.setParameter("id", id);
			if(query.list().size() < 1){
				throw new EntityDoseNotExistException(NotificationSettings.class);
			}
			notificationSettings = (NotificationSettings) query.list().get(0);
		} catch (HibernateException e) {
			e.printStackTrace();
		}  
		return notificationSettings;
	}

}
