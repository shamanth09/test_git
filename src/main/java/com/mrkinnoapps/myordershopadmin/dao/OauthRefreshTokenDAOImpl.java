package com.mrkinnoapps.myordershopadmin.dao;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mrkinnoapps.myordershopadmin.bean.entity.OauthRefreshToken;

@Repository
public class OauthRefreshTokenDAOImpl implements OauthRefreshTokenDAO{
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public OauthRefreshToken getOauthRefreshToken(String refreshTokenId) {
		OauthRefreshToken oauthRefreshToken = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Query query = session
					.createQuery("from OauthRefreshToken where tokenId=:tokenId");
			query.setParameter("tokenId", refreshTokenId);
			if (query.list().size() < 1)
				return oauthRefreshToken;
			else{
				oauthRefreshToken = (OauthRefreshToken) query.list().get(0);
				transaction.commit();
			}
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return oauthRefreshToken;
		
	}

	@Override
	public void delete(OauthRefreshToken oauthRefreshToken) {
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			session.delete(oauthRefreshToken);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		
	}

}
