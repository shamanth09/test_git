package com.mrkinnoapps.myordershopadmin.dao;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mrkinnoapps.myordershopadmin.bean.entity.OauthAccessToken;

@Repository
public class OauthAccessTokenDAOImpl implements OauthAccessTokenDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public OauthAccessToken getOauthAccessToken(String userEmail) {
		OauthAccessToken accessToken = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Query query = session.createQuery("from OauthAccessToken as oau where oau.userEmail=:email");
			query.setParameter("email", userEmail);
			if (query.list().size() < 1)
				return accessToken;
			else {
				accessToken = (OauthAccessToken) query.list().get(0);
				transaction.commit();
			}
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return accessToken;
	}

	@Override
	public void delete(OauthAccessToken oauthAccessToken) {
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			session.delete(oauthAccessToken);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

	}

}
