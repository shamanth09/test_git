package com.mrk.myordershop.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mrk.myordershop.bean.Order;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.WholesalerInstantOrder;
import com.mrk.myordershop.bean.dto.SearchFilter.SearchIn;
import com.mrk.myordershop.bean.dto.SearchResource;
import com.mrk.myordershop.constant.ActiveFlag;
import com.mrk.myordershop.constant.OrderStatus;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;
import com.mrk.myordershop.util.OrderNoGenerator;

@Repository
public class WholesalerInstantOrderDAOImpl implements WholesalerInstantOrderDAO {

	@Autowired
	private SessionFactory sessionFactory;



	@Override
	public List<SearchResource> search(String query, SearchIn searchIn, User user) {
		List<SearchResource> page = new ArrayList<SearchResource>();
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(WholesalerInstantOrder.class, "o");
			ProjectionList projectionList = Projections.projectionList();
			criteria.add(Restrictions.eq("o.activeFlag", ActiveFlag.ACTIVE));
			criteria.add(Restrictions.eq("o.wholesaler.id", user.getId()));
			switch (searchIn) {
			case ORDER_STATUS:

				OrderStatus[] os = OrderStatus.find(query);
				if (os.length == 0) {
					return new ArrayList<SearchResource>();
				}
				criteria.add(Restrictions.in("o.orderStatus", os));
				projectionList.add(Projections.groupProperty("o.orderStatus").as("orderStatus"));

				break;
			case CUSTOMER_NAME:

				criteria.add(Restrictions.like("o.customerName", query, MatchMode.ANYWHERE));
				projectionList.add(Projections.groupProperty("o.customerName"));

				break;
			case MOBILE:

				criteria.add(Restrictions.like("o.customerMobile", query, MatchMode.ANYWHERE));
				projectionList.add(Projections.groupProperty("o.customerMobile"));

				break;
			}
			criteria.setProjection(projectionList);
			criteria.addOrder(org.hibernate.criterion.Order.desc("createTimestamp"));
			criteria.setMaxResults(10);
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			List<Object> list = criteria.list();
			for (Object order : list) {
				String str = order.toString();
				if (searchIn.equals(SearchIn.ORDER_STATUS))
					str = OrderStatus.valueOf(str).getValue();
				page.add(new SearchResource(str, searchIn, null, Order.class));
			}
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return page;
	}

}
