package com.mrk.myordershop.dao.report;

import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mrk.myordershop.bean.Order;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.WholesalerOrder;
import com.mrk.myordershop.constant.ActiveFlag;

@Repository
public class UserReportDAOImpl implements UserReportDAO {
	
	private static final Logger log = Logger.getLogger(UserReportDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public long getRetailerCountByOrderDate(Date fromDate, Date toDate,
			Wholesaler wholesaler) {
		long count = 0;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Order.class, "o");
			criteria.add(Restrictions.eq("o.activeFlag", ActiveFlag.ACTIVE));
			criteria.add(Restrictions.eq("o.wholesaler", wholesaler));
			if (fromDate != null) {
				log.debug(fromDate+"<");
				criteria.add(Restrictions.ge("o.createTimestamp", fromDate));
			}
			if (toDate != null) {
				log.debug("<"+toDate);
				criteria.add(Restrictions.le("o.createTimestamp", toDate));
			}
			criteria.createAlias("o.referralUser", "u");
			criteria.setProjection(Projections.groupProperty("u.id"));
			count = criteria.list().size();
		} catch (HibernateException e) {
			e.printStackTrace();
		}  
		return count;
	}

	@Override
	public long getSupplierCountByOrderDate(Date fromDate, Date toDate,
			Wholesaler wholesaler) {
		long count = 0;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(WholesalerOrder.class, "o");
			criteria.add(Restrictions.eq("o.activeFlag", ActiveFlag.ACTIVE));
			criteria.add(Restrictions.eq("o.user", wholesaler));
			
			if (fromDate != null) {
				log.debug(fromDate+"<");
				criteria.add(Restrictions.ge("o.createTimestamp", fromDate));
			}
			if (toDate != null) {
				log.debug("<"+toDate);
				criteria.add(Restrictions.le("o.createTimestamp", toDate));
			}
			criteria.createAlias("o.supplier", "u");
			criteria.setProjection(Projections.groupProperty("u.id"));
			count = criteria.list().size();
		} catch (HibernateException e) {
			e.printStackTrace();
		}  
		return count;
	}

}
