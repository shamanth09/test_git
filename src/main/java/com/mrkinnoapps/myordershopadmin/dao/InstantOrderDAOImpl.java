package com.mrkinnoapps.myordershopadmin.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mrkinnoapps.myordershopadmin.bean.constant.ActiveFlag;
import com.mrkinnoapps.myordershopadmin.bean.constant.OrderStatus;
import com.mrkinnoapps.myordershopadmin.bean.dto.SearchFilter.SearchIn;
import com.mrkinnoapps.myordershopadmin.bean.dto.SearchResource;
import com.mrkinnoapps.myordershopadmin.bean.entity.InstantOrder;
import com.mrkinnoapps.myordershopadmin.bean.entity.Order;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;

@Repository
public class InstantOrderDAOImpl implements InstantOrderDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(InstantOrder instantOrder)
			throws EntityNotPersistedException {
//		Session session = sessionFactory.openSession();
//		try {
//			Transaction transaction = session.beginTransaction();
//			instantOrder.setOrderNo(OrderNoGenerator.generate(
//					InstantOrder.class, session));
//			session.save(instantOrder);
//			transaction.commit();
//		} catch (HibernateException e) {
//			e.printStackTrace();
//			new EntityDoseNotExistException(Order.class);
//		} finally {
//			session.close();
//		}
	}

	@Override
	public InstantOrder getInstantOrder(int orderId)
			throws EntityDoseNotExistException {
		InstantOrder order = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(InstantOrder.class);
			criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			criteria.add(Restrictions.eq("id", orderId));
			if (criteria.list().size() < 1)
				throw new EntityDoseNotExistException();
			order = (InstantOrder) criteria.list().get(0);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return order;
	}

	@Override
	public void update(InstantOrder instantOrder) {
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			session.update(instantOrder);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	@Override
	public List<SearchResource> search(String query, SearchIn filterField,
			User user) {
		List<SearchResource> page = new ArrayList<SearchResource>();
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(InstantOrder.class, "o");

			criteria.add(Restrictions.eq("o.activeFlag", ActiveFlag.ACTIVE));
			criteria.add(Restrictions.eq("o.wholesaler.id", user.getId()));
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			ProjectionList projectionList = Projections.projectionList();
			switch (filterField) {
			case ORDER_STATUS:

				OrderStatus[] os = OrderStatus.find(query);
				if (os.length == 0) {
					transaction.commit();
					return new ArrayList<SearchResource>();
				}
				criteria.add(Restrictions.in("o." + filterField, os));
				projectionList.add(Projections.groupProperty("o.orderStatus")
						.as("orderStatus"));

				break;
			// case "productName":
			//
			// criteria.add(Restrictions.like("o.productName", query,
			// MatchMode.ANYWHERE));
			// projectionList.add(Projections.groupProperty("o.productName"));
			//
			// break;
			case CUSTOMER_NAME:

				// criteria.add(Restrictions.like("o.customerName", query,
				// MatchMode.ANYWHERE));
				// projectionList.add(Projections.groupProperty("o.customerName"));

				break;
			}
			criteria.setProjection(projectionList);
			criteria.addOrder(org.hibernate.criterion.Order
					.desc("createTimestamp"));
			criteria.setMaxResults(10);
			List<Object> orderNo = criteria.list();
			for (Object order : orderNo) {
				String str = order.toString();
				if (filterField.equals(SearchIn.ORDER_STATUS))
					str = OrderStatus.valueOf(str).getValue();
				page.add(new SearchResource(str, filterField, null, Order.class));
			}
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return page;
	}

}
