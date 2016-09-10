package com.mrk.myordershop.log.dao;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.mrk.myordershop.exception.EntityNotPersistedException;
import com.mrk.myordershop.log.bean.LoginHistory;
import com.mrk.myordershop.log.bean.dto.LoginHistoryFilter;

@Repository
public class LoginHistroryDAOImpl implements LoginHistroryDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(LoginHistory loginHistory) throws EntityNotPersistedException {
		Session session = sessionFactory.getCurrentSession();

		try {
			session.save(loginHistory);
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new EntityNotPersistedException(LoginHistory.class);
		}
	}

	@Override
	public void update(LoginHistory loginHistory) {
		Session session = sessionFactory.getCurrentSession();

		try {
			session.update(loginHistory);
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new EntityNotPersistedException(LoginHistory.class);
		}
	}

	@Override
	public void delete(LoginHistory loginHistory) {
		Session session = sessionFactory.getCurrentSession();

		try {
			session.delete(loginHistory);
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new EntityNotPersistedException(LoginHistory.class);
		}
	}

	@Override
	public Page<LoginHistory> get(Pageable pageable, LoginHistoryFilter filter) {
		Page<LoginHistory> page = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(LoginHistory.class, "h");
			buildFilter(criteria, filter, "h");
			criteria.addOrder(Order.desc("h.createTimestamp"));
			int total = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
			criteria.setProjection(null);
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			if (pageable != null) {
				criteria.setFirstResult(pageable.getOffset());
				criteria.setMaxResults(pageable.getPageSize());
			}

			page = new PageImpl<LoginHistory>(criteria.list(), pageable, total);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return page;
	}

	private void buildFilter(Criteria criteria, LoginHistoryFilter filter, String alias) {
		if (filter.getUserId() != null && !filter.getUserId().isEmpty()) {
			criteria.add(Restrictions.eq(alias + ".userId", filter.getUserId()));
		}

		if (filter.getAttempt() != null) {
			criteria.add(Restrictions.eq(alias + ".attempt", filter.getAttempt()));
		}

		if (filter.getClientId() != null && !filter.getClientId().isEmpty()) {
			criteria.add(Restrictions.eq(alias + ".clientId", filter.getClientId()));
		}

		if (filter.getFromTimestamp() != null || filter.getToTimestamp() != null) {
			criteria.add(Restrictions.between(alias + ".createTimestamp", filter.getFromTimestamp(),
					filter.getToTimestamp()));
		}
	}
}
