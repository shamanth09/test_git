package com.mrk.myordershop.dao.querybuilder;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mrk.myordershop.bean.Relation;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.dto.SearchFilter.SearchIn;
import com.mrk.myordershop.bean.dto.UserSearchResource;
import com.mrk.myordershop.constant.RelationStatus;
import com.mrk.myordershop.constant.Role;

public class RelationSearchQueryBuilder {

	private Criteria criteria;
	private SearchIn field;
	private List<UserSearchResource> resultList;
	private Role role;

	public RelationSearchQueryBuilder(Criteria criteria, SearchIn field,
			Role role) {
		this.criteria = criteria;
		this.field = field;
		this.role = role;
	}

	public Criteria criteria() {
		return this.criteria;
	}

	public RelationSearchQueryBuilder setRestrictions(String query, User user,
			List<RelationStatus> status) {
		ProjectionList projectionList = Projections.projectionList();

		this.criteria.createAlias("primaryUser", "pUser").createAlias(
				"secondaryUser", "sUser");
		this.criteria.createAlias("pUser.userRoles", "pUserRoles").createAlias(
				"sUser.userRoles", "sUserRoles");
		if (status != null && status.size() > 0)
			this.criteria.add(Restrictions.in("status", status));
		switch (this.field) {
		case NAME:
			this.criteria.add(Restrictions
					.disjunction()
					.add(Restrictions
							.conjunction()
							.add(Restrictions.like("pUser.name", query,
									MatchMode.ANYWHERE))
							.add(Restrictions.eq("pUserRoles.role", role))
							.add(Restrictions.eq("sUser.id", user.getId())))
					.add(Restrictions
							.conjunction()
							.add(Restrictions.like("sUser.name", query,
									MatchMode.ANYWHERE))
							.add(Restrictions.eq("sUserRoles.role", role))
							.add(Restrictions.eq("pUser.id", user.getId()))));
			projectionList.add(Projections.property("pUser.id"))
					.add(Projections.groupProperty("pUser.name"))
					.add(Projections.property("sUser.id"))
					.add(Projections.groupProperty("sUser.name"));
			criteria.setMaxResults(10);
			break;
		case EMAIL:

			this.criteria.add(Restrictions
					.disjunction()
					.add(Restrictions
							.conjunction()
							.add(Restrictions.like("pUser.email", query,
									MatchMode.ANYWHERE))
							.add(Restrictions.eq("pUserRoles.role", role))
							.add(Restrictions.eq("sUser.id", user.getId())))
					.add(Restrictions
							.conjunction()
							.add(Restrictions.like("sUser.email", query,
									MatchMode.ANYWHERE))
							.add(Restrictions.eq("sUserRoles.role", role))
							.add(Restrictions.eq("pUser.id", user.getId()))));
			projectionList.add(Projections.property("pUser.id"))
					.add(Projections.groupProperty("pUser.email"))
					.add(Projections.property("sUser.id"))
					.add(Projections.groupProperty("sUser.email"));
			criteria.setMaxResults(10);
			break;
		case MOBILE:

			this.criteria.add(Restrictions
					.disjunction()
					.add(Restrictions
							.conjunction()
							.add(Restrictions.like("pUser.mobile", query,
									MatchMode.ANYWHERE))
							.add(Restrictions.eq("pUserRoles.role", role))
							.add(Restrictions.eq("sUser.id", user.getId())))
					.add(Restrictions
							.conjunction()
							.add(Restrictions.like("sUser.mobile", query,
									MatchMode.ANYWHERE))
							.add(Restrictions.eq("sUserRoles.role", role))
							.add(Restrictions.eq("pUser.id", user.getId()))));
			projectionList.add(Projections.property("pUser.id"))
					.add(Projections.groupProperty("pUser.mobile"))
					.add(Projections.property("sUser.id"))
					.add(Projections.groupProperty("sUser.mobile"));
			criteria.setMaxResults(10);
			break;
		}
		projectionList.add(Projections.property("id"));
		criteria.setProjection(projectionList);
		return this;
	}

	public List<UserSearchResource> list(User user) {
		if (resultList == null) {
			resultList = new ArrayList<UserSearchResource>();
			switch (this.field) {
			case NAME: {
				List<Object[]> list = criteria.list();
				for (Object[] objects : list) {
					String pUserId = objects[0].toString();
					String pUserName = objects[1].toString();
					String sUserId = objects[2].toString();
					String sUserName = objects[3].toString();
					String relationId = objects[4].toString();
					UserSearchResource osr = null;
					if (pUserId.equals(user.getId())) {
						osr = new UserSearchResource(sUserName, this.field,
								null, Relation.class);
						osr.setResultId(sUserId);
					} else if (sUserId.equals(user.getId())) {
						osr = new UserSearchResource(pUserName, this.field,
								null, Relation.class);
						osr.setResultId(pUserId);
					}

					osr.setRole(role);
					resultList.add(osr);
				}
			}
				break;
			case EMAIL: {
				List<Object[]> list = criteria.list();
				for (Object[] objects : list) {
					String pUserId = objects[0].toString();
					String pUserEmail = objects[1].toString();
					String sUserId = objects[2].toString();
					String sUserEmail = objects[3].toString();
					String relationId = objects[4].toString();
					UserSearchResource osr = null;
					if (pUserId.equals(user.getId())) {
						osr = new UserSearchResource(sUserEmail, this.field,
								null, Relation.class);
						osr.setResultId(sUserId);
					} else if (sUserId.equals(user.getId())) {
						osr = new UserSearchResource(pUserEmail, this.field,
								null, Relation.class);
						osr.setResultId(pUserId);
					}

					osr.setRole(role);
					resultList.add(osr);
				}
			}
				break;
			case MOBILE: {
				List<Object[]> list = criteria.list();
				for (Object[] objects : list) {
					String pUserId = objects[0].toString();
					String pUserMobile = objects[1].toString();
					String sUserId = objects[2].toString();
					String sUserMobile = objects[3].toString();
					String relationId = objects[4].toString();
					UserSearchResource osr = null;
					if (pUserId.equals(user.getId())) {
						osr = new UserSearchResource(sUserMobile, this.field,
								null, Relation.class);
						osr.setResultId(sUserId);
					} else if (sUserId.equals(user.getId())) {
						osr = new UserSearchResource(pUserMobile, this.field,
								null, Relation.class);
						osr.setResultId(pUserId);
					}
					osr.setRole(role);
					resultList.add(osr);
				}
			}
				break;

			}
		}

		return resultList;
	}
}
