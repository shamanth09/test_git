package com.mrkinnoapps.myordershopadmin.dao;

import java.util.ArrayList;
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

import com.mrkinnoapps.myordershopadmin.bean.entity.Product;
import com.mrkinnoapps.myordershopadmin.util.searchengine.ProductSearchResult;
import com.mrkinnoapps.myordershopadmin.util.searchengine.SearchResult;
import com.mrkinnoapps.myordershopadmin.util.searchengine.Searcher;
import com.mrkinnoapps.myordershopadmin.util.searchengine.UserSearchResult;

@Repository
public class ProductSearcher implements Searcher{

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	private List<SearchResult> machaSearch(String query, String field) {
		List<SearchResult> list = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Product.class);

			criteria.setProjection(Projections.projectionList().add(Projections.property(field), "result")
					.add(Projections.property("id"), "resultId"))
					.add(Restrictions.like(field, query, MatchMode.ANYWHERE));

			criteria.setResultTransformer(Transformers.aliasToBean(ProductSearchResult.class));
			list = (List<SearchResult>) criteria.list();
			for (SearchResult searchResult : list) {
				ProductSearchResult productSearchResult  = (ProductSearchResult) searchResult;
				if(field.equals("name"))
					productSearchResult.setField("productName");
				else
					productSearchResult.setField("SKU");	
			}
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return list;
	}

	@Override
	public List<SearchResult> search(String query, String field) {
		List<SearchResult> productSearchList = new ArrayList<SearchResult>();
		switch (field) {
		case "productName":
			productSearchList.addAll(machaSearch(query, "name"));
			break;
		case "SKU":
			productSearchList.addAll(machaSearch(query, "sku"));
			break;
		}

		return productSearchList;
	}

	@Override
	public String[] getFields() {
		return new String[]{"productName","SKU"};
	}
}
