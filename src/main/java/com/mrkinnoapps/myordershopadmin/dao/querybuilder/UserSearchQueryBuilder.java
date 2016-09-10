package com.mrkinnoapps.myordershopadmin.dao.querybuilder;

import java.util.ArrayList;
import java.util.List;

import javax.management.relation.Relation;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import com.mrkinnoapps.myordershopadmin.bean.constant.Role;
import com.mrkinnoapps.myordershopadmin.bean.dto.SearchFilter.SearchIn;
import com.mrkinnoapps.myordershopadmin.bean.dto.UserSearchResource;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;

public class UserSearchQueryBuilder {

	private Criteria criteria;
	private SearchIn field;
	private List<UserSearchResource> resultList;
	private Role role;

	public UserSearchQueryBuilder(Criteria criteria, SearchIn field, Role role) {
		this.criteria = criteria;
		this.field = field;
		this.role = role;
	}

	public Criteria criteria() {
		return this.criteria;
	}

	public UserSearchQueryBuilder setRestrictions(String query, User user,
			String[] excludeUsers) {
		ProjectionList projectionList = Projections.projectionList();

		DetachedCriteria dcpu = DetachedCriteria.forClass(Relation.class);
		dcpu.createAlias("primaryUser", "pUser").createAlias("secondaryUser",
				"sUser");
		DetachedCriteria dcsu = DetachedCriteria.forClass(Relation.class);
		dcsu.createAlias("primaryUser", "pUser").createAlias("secondaryUser",
				"sUser");
		if (excludeUsers.length > 0) {
			dcpu.add(Restrictions.conjunction()
					.add(Restrictions.in("pUser.id", excludeUsers))
					.add(Restrictions.eq("sUser.id", user.getId())));

			dcsu.add(Restrictions.conjunction()
					.add(Restrictions.in("sUser.id", excludeUsers))
					.add(Restrictions.eq("pUser.id", user.getId())));

		} else {
			dcpu.add(Restrictions.eq("sUser.id", user.getId()));

			dcsu.add(Restrictions.eq("pUser.id", user.getId()));
		}
		switch (this.field) {
		case NAME:
			criteria.add(Restrictions.like("u.name", query, MatchMode.ANYWHERE));

			projectionList.add(Projections.groupProperty("u.name"));
			projectionList.add(Projections.property("u.id"));
			criteria.setMaxResults(10);
			break;
		case EMAIL:
			criteria.add(Restrictions
					.like("u.email", query, MatchMode.ANYWHERE));

			// dcpu.add(Restrictions
			// .conjunction()
			// .add(Restrictions.like("pUser.email", query,
			// MatchMode.ANYWHERE))
			// .add(Restrictions.eq("sUser.id", user.getId())));
			//
			// dcsu.add(Restrictions
			// .conjunction()
			// .add(Restrictions.eq("pUser.id", user.getId()))
			// .add(Restrictions.like("sUser.email", query,
			// MatchMode.ANYWHERE)));

			projectionList.add(Projections.groupProperty("u.email"));
			projectionList.add(Projections.property("u.id"));
			criteria.setMaxResults(10);
			break;
		case MOBILE:
			criteria.add(Restrictions.like("u.mobile", query,
					MatchMode.ANYWHERE));

			// dcpu.add(Restrictions
			// .conjunction()
			// .add(Restrictions.like("pUser.mobile", query,
			// MatchMode.ANYWHERE))
			// .add(Restrictions.eq("sUser.id", user.getId())));
			//
			// dcsu.add(Restrictions
			// .conjunction()
			// .add(Restrictions.eq("pUser.id", user.getId()))
			// .add(Restrictions.like("sUser.mobile", query,
			// MatchMode.ANYWHERE)));

			projectionList.add(Projections.groupProperty("u.mobile"));
			projectionList.add(Projections.property("u.id"));
			break;
		}
		dcpu.setProjection(Projections.property("pUser.id"));
		criteria.add(Subqueries.propertyNotIn("u.id", dcpu));
		dcsu.setProjection(Projections.property("sUser.id"));
		criteria.add(Subqueries.propertyNotIn("u.id", dcsu));
		criteria.setProjection(projectionList);
		return this;
	}

	public List<UserSearchResource> list() {
		if (resultList == null) {
			resultList = new ArrayList<UserSearchResource>();
			switch (this.field) {
			case NAME: {
				List<Object[]> list = criteria.list();
				for (Object[] objects : list) {
					String name = objects[0] != null ? objects[0].toString()
							: "";
					String userId = objects[1] != null ? objects[1].toString()
							: "";

					UserSearchResource osr = new UserSearchResource(name,
							this.field, null, User.class);
					osr.setRole(role);
					osr.setResultId(userId);
					resultList.add(osr);
				}
			}
				break;
			case EMAIL: {
				List<Object[]> list = criteria.list();
				for (Object[] objects : list) {
					String name = objects[0] != null ? objects[0].toString()
							: "";
					String userId = objects[1] != null ? objects[1].toString()
							: "";

					UserSearchResource osr = new UserSearchResource(name,
							this.field, null, User.class);
					osr.setRole(role);
					osr.setResultId(userId);
					resultList.add(osr);
				}
			}
				break;
			case MOBILE: {
				List<Object[]> list = criteria.list();
				for (Object[] objects : list) {
					String name = objects[0] != null ? objects[0].toString()
							: "";
					String userId = objects[1] != null ? objects[1].toString()
							: "";

					UserSearchResource osr = new UserSearchResource(name,
							this.field, null, User.class);
					osr.setRole(role);
					osr.setResultId(userId);
					resultList.add(osr);
				}
			}
				break;

//			default:
//				List<Object> list = criteria.list();
//				for (Object order : list) {
//					String str = order.toString();
//					if (this.field.equals("orderStatus"))
//						str = OrderStatus.valueOf(str).getValue();
//					UserSearchResource osr2 = new UserSearchResource(str,
//							this.field, null, User.class);
//					osr2.setRole(role);
//					resultList.add(osr2);
//				}
//				break;
			}
		}
		return resultList;
	}
}
