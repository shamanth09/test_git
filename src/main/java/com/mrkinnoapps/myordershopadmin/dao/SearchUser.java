package com.mrkinnoapps.myordershopadmin.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mrkinnoapps.myordershopadmin.bean.constant.ActiveFlag;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;

@Repository
public class SearchUser {

	@Autowired
	private SessionFactory sessionFactory;

	public List<User> search(String query) {
		List<User> page = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(User.class, "u");
			criteria.add(Restrictions.eq("u.activeFlag", ActiveFlag.ACTIVE));

			criteria.add(Restrictions.disjunction()
					.add(Restrictions.like("u.name", query, MatchMode.ANYWHERE))
					.add(Restrictions.like("u.email", query, MatchMode.ANYWHERE))
					.add(Restrictions.like("u.mobile", query, MatchMode.ANYWHERE)));

			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			criteria.setMaxResults(10);
			page = criteria.list();
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return page;
	}

}
