package com.mrk.myordershop.dao.report;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;

import com.mrk.myordershop.bean.Order;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.dto.OrderStatusWithDateSummary;
import com.mrk.myordershop.bean.dto.filter.OrderSummaryFilter;
import com.mrk.myordershop.constant.ActiveFlag;

public abstract class OrderReportAbstractDAO implements OrderReportDAO {

	@Autowired
	protected SessionFactory sessionFactory;

	@Override
	public List<OrderStatusWithDateSummary> getOrderCounts(User user, OrderSummaryFilter filter) {
		List<OrderStatusWithDateSummary> list = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Order.class, "o");
			if (user instanceof Wholesaler)
				criteria.add(Restrictions.eq("wholesaler", user));
			else {
				criteria.add(Restrictions.disjunction().add(Restrictions.eq("user.id", user.getId()))
						.add(Restrictions.eq("referralUser.id", user.getId())));
			}
			criteria.add(Restrictions.eq("o.activeFlag", ActiveFlag.ACTIVE));
			criteria.createAlias("o.item", "i").createAlias("i.detail", "d");
			if (filter != null) {
				buildCriteria(criteria, filter);
			}
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.sum("d.quantity"), "quantity");
			projectionList.add(Projections.sum("d.weight"), "weight");
			projectionList.add(Projections.rowCount(), "count");
			projectionList.add(Projections.max("o.createTimestamp"), "date");
			projectionList.add(Projections.groupProperty("o.orderStatus"), "orderStatus");
			criteria.setProjection(projectionList);
			criteria.setResultTransformer(Transformers.aliasToBean(OrderStatusWithDateSummary.class));
			list = criteria.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return list;
	}

	private void buildCriteria(Criteria criteria, OrderSummaryFilter filter) {
		if (filter != null && criteria != null) {
			if (filter.getFromDate() != null) {
				criteria.add(Restrictions.ge("createTimestamp", filter.getFromDate()));
			}
			if (filter.getToDate() != null) {
				criteria.add(Restrictions.le("createTimestamp", filter.getToDate()));
			}

			if (filter.getCategoryId() != 0) {
				criteria.createAlias("i.product", "prdt").createAlias("prdt.category", "cat");
				criteria.add(Restrictions.eq("cat.id", filter.getCategoryId()));
			}

		}
	}

	protected int getOrderCount(Criteria criteria) {
		int count = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		criteria.setProjection(null);
		return count;
	}
}
