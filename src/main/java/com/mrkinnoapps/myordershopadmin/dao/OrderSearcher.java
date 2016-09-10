package com.mrkinnoapps.myordershopadmin.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mrkinnoapps.myordershopadmin.bean.entity.Order;
import com.mrkinnoapps.myordershopadmin.bean.entity.WholesalerInstantOrder;
import com.mrkinnoapps.myordershopadmin.util.searchengine.OrderSearchResult;
import com.mrkinnoapps.myordershopadmin.util.searchengine.SearchResult;
import com.mrkinnoapps.myordershopadmin.util.searchengine.Searcher;

@Repository
public class OrderSearcher implements Searcher {

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	public List<SearchResult> searchLoop(String query, String field) {
		List<SearchResult> searcher = null;
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Criteria criteria = null;
		try {

			if (field.equalsIgnoreCase("orderNo")) {
				criteria = session.createCriteria(Order.class, "order");
				criteria.add(Restrictions.like(field, query, MatchMode.ANYWHERE));
				criteria.setProjection(Projections.projectionList().add(Projections.property(field), "result")
						.add(Projections.property("id"), "resultId")
						.add(Projections.groupProperty(field)));
			} else {
				criteria = session.createCriteria(WholesalerInstantOrder.class);
				criteria.add(Restrictions.like(field, query, MatchMode.ANYWHERE));
				criteria.setProjection(Projections.projectionList().add(Projections.property(field), "result")
						.add(Projections.property("id"), "resultId")
						.add(Projections.groupProperty(field)));
			}
			criteria.setResultTransformer(Transformers.aliasToBean(OrderSearchResult.class));
			searcher = (List<SearchResult>) criteria.list();
			for (SearchResult searchResult : searcher) {
				OrderSearchResult orderSearchResult = (OrderSearchResult) searchResult;
				orderSearchResult.setField(field);
			}
			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return searcher;
	}

	@Override
	public List<SearchResult> search(String query, String field) {
		List<SearchResult> single = null;
		switch (field) {
		case "orderNo":
			single = searchLoop(query, "orderNo");
			break;

		case "customerMobile":
			single = searchLoop(query, "customerMobile");
			break;

		case "customerName":
			single = searchLoop(query, "customerName");
			break;
		}
		return single;
	}

	@Override
	public String[] getFields() {
		return new String[]{"orderNo","customerMobile","customerName"};
	}
}
