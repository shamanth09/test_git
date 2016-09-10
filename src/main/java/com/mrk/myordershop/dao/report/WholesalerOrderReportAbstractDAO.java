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

import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.WholesalerOrder;
import com.mrk.myordershop.bean.dto.OrderStatusWithDateSummary;
import com.mrk.myordershop.constant.ActiveFlag;
import com.mrk.myordershop.resource.OrderReportResource;

public abstract class WholesalerOrderReportAbstractDAO implements
		WholesalerOrderReportDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public OrderReportResource getOrderCounts(User user) {
		OrderReportResource orderReportResource = new OrderReportResource();
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(WholesalerOrder.class,
					"o");
			if (user instanceof Wholesaler)
				criteria.add(Restrictions.eq("user", user));
			else
				criteria.add(Restrictions.eq("supplier", user));

			criteria.add(Restrictions.eq("o.activeFlag", ActiveFlag.ACTIVE));
			criteria.createAlias("o.item", "i").createAlias("i.detail", "d");
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.sum("d.quantity"), "quantity");
			projectionList.add(Projections.sum("d.weight"), "weight");
			projectionList.add(Projections.rowCount(), "count");
			projectionList.add(Projections.max("o.createTimestamp"), "date");
			projectionList.add(Projections.groupProperty("o.orderStatus"),
					"orderStatus");
			criteria.setProjection(projectionList);
			criteria.setResultTransformer(Transformers
					.aliasToBean(OrderStatusWithDateSummary.class));
			List<OrderStatusWithDateSummary> list = criteria.list();
			orderReportResource.setOrderStatusSummaries(list);
		} catch (HibernateException e) {
			e.printStackTrace();
		}  
		return orderReportResource;
	}
}
