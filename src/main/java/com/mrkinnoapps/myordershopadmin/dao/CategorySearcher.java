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

import com.mrkinnoapps.myordershopadmin.bean.entity.Category;
import com.mrkinnoapps.myordershopadmin.util.searchengine.CatogorySearchResult;
import com.mrkinnoapps.myordershopadmin.util.searchengine.SearchResult;
import com.mrkinnoapps.myordershopadmin.util.searchengine.Searcher;

@Repository
public class CategorySearcher implements Searcher {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	private List<SearchResult> machaSearch(String query, String field) {
		List<SearchResult> list = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Category.class);

			criteria.setProjection(Projections.projectionList().add(Projections.property(field), "result")
					.add(Projections.property("id"), "resultId"))
					.add(Restrictions.like(field, query, MatchMode.ANYWHERE));

			criteria.setResultTransformer(Transformers.aliasToBean(CatogorySearchResult.class));
			list = (List<SearchResult>) criteria.list();
			for (SearchResult searchResult : list) {
				CatogorySearchResult catogorySearchResult  = (CatogorySearchResult) searchResult;
				catogorySearchResult.setField(field);
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
		List<SearchResult> categorySearchList = new ArrayList<SearchResult>();
		switch (field) {
		case "categoryName":
			categorySearchList.addAll(machaSearch(query, "name"));
			break;
		}
		return categorySearchList;
	}

	@Override
	public String[] getFields() {
		return new String[]{"categoryName"};
	}

}
