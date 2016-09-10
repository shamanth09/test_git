package com.mrk.myordershop.dao.querybuilder;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mrk.myordershop.bean.Contact;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.dto.SearchFilter.SearchIn;
import com.mrk.myordershop.bean.dto.SearchResource;

public class ContactSearchQueryBuilder {

	private Criteria criteria;
	private SearchIn field;
	private List<SearchResource> resultList;

	public ContactSearchQueryBuilder(Criteria criteria, SearchIn field) {
		this.criteria = criteria;
		this.field = field;
	}

	public Criteria criteria() {
		return this.criteria;
	}

	public ContactSearchQueryBuilder setRestrictions(String query, User user) {
		criteria.add(Restrictions.eq("c.user", user));
		ProjectionList projectionList = Projections.projectionList();
		switch (this.field) {
		case NAME:
			criteria.add(Restrictions.like("c.name", query, MatchMode.ANYWHERE));
			projectionList.add(Projections.groupProperty("c.name"));
			criteria.setMaxResults(10);
			break;
		case EMAIL:
			criteria.add(Restrictions
					.like("c.email", query, MatchMode.ANYWHERE));
			projectionList.add(Projections.groupProperty("c.email"));
			criteria.setMaxResults(10);
			break;
		case MOBILE:
			criteria.add(Restrictions.like("c.mobile", query,
					MatchMode.ANYWHERE));
			projectionList.add(Projections.groupProperty("c.mobile"));
			criteria.setMaxResults(10);
			break;
		case FIRM_NAME:
			criteria.add(Restrictions.like("c.firmName", query,
					MatchMode.ANYWHERE));
			projectionList.add(Projections.groupProperty("c.firmName"));
			break;
		}
		criteria.setProjection(projectionList);
		return this;
	}

	public List<SearchResource> list() {
		if (resultList == null) {
			resultList = new ArrayList<SearchResource>();
			switch (this.field) {
			case NAME:
			case EMAIL:
			case MOBILE:
			case FIRM_NAME:
				List<Object> list = criteria.list();
				for (Object objects : list) {
					String name = objects.toString();
					SearchResource osr = new SearchResource(name, this.field,
							this.field.getSuffix(), Contact.class);
					resultList.add(osr);
				}
				break;
			}
		}

		return resultList;
	}
}
