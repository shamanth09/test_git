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

import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.util.searchengine.SearchResult;
import com.mrkinnoapps.myordershopadmin.util.searchengine.Searcher;
import com.mrkinnoapps.myordershopadmin.util.searchengine.UserSearchResult;

@Repository
public class UserSearch implements Searcher {

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	private List<SearchResult> searchLoop(String query, String field) {
		List<SearchResult> list = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(User.class);

			criteria.setProjection(Projections.projectionList()
					.add(Projections.property(field), "result")
					.add(Projections.property("id"), "resultId")
					.add(Projections.groupProperty(field)))
					.add(Restrictions.like(field, query, MatchMode.ANYWHERE));

			criteria.setResultTransformer(Transformers.aliasToBean(UserSearchResult.class));
			list = (List<SearchResult>) criteria.list();
			for (SearchResult searchResult : list) {
				UserSearchResult userSearchResult = (UserSearchResult) searchResult;
				if(field.equals("name"))
				userSearchResult.setField("userName");
				else if(field.equals("email"))
					userSearchResult.setField("email");
				else
					userSearchResult.setField("mobile");
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
		List<SearchResult> userSearchList = new ArrayList<SearchResult>();
		List<SearchResult> liResults = null;
		switch (field) {
		case "userName":
			liResults = searchLoop(query, "name");
			if (liResults != null && liResults.size() >= 1)
				userSearchList.addAll(liResults);
			break;
		case "userEmail":
			liResults = searchLoop(query, "email");
			if (liResults != null && liResults.size() >= 1)
				userSearchList.addAll(liResults);
			break;
		case "userMobile":
			liResults = searchLoop(query, "mobile");
			if (liResults != null && liResults.size() >= 1)
				userSearchList.addAll(liResults);
			break;

		}
		return userSearchList;
	}

	@Override
	public String[] getFields() {
		return new String[]{"userName","userEmail","userMobile"};
	}

}
