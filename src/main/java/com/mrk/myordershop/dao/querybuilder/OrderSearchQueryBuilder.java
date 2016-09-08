package com.mrk.myordershop.dao.querybuilder;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mrk.myordershop.bean.Order;
import com.mrk.myordershop.bean.Retailer;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.dto.SearchFilter.SearchIn;
import com.mrk.myordershop.bean.dto.SearchResource;
import com.mrk.myordershop.constant.OrderStatus;

public class OrderSearchQueryBuilder {

	private Criteria criteria;
	private SearchIn field;
	private List<SearchResource> resultList;

	public OrderSearchQueryBuilder(Criteria criteria, SearchIn field) {
		this.criteria = criteria;
		this.field = field;
	}

	public Criteria criteria() {
		return this.criteria;
	}

	public OrderSearchQueryBuilder setRestrictions(String query,
			Wholesaler wholesaler) {
		ProjectionList projectionList = Projections.projectionList();
		switch (this.field) {
		case ORDER_STATUS:
			OrderStatus[] os = OrderStatus.find(query);
			if (os.length == 0) {
				resultList = new ArrayList<SearchResource>();
			}
			criteria.add(Restrictions.in("o.orderStatus", os));
			projectionList.add(Projections.groupProperty("o.orderStatus"));
			criteria.setMaxResults(10);
			break;
		case PRODUCT_NAME:
			criteria.createAlias("o.item", "item");
			criteria.createAlias("item.product", "prodct");
			criteria.add(Restrictions.like("prodct.name", query,
					MatchMode.ANYWHERE));

			projectionList.add(Projections.groupProperty("prodct.name"));
			criteria.setMaxResults(10);
			break;
		case CUSTOMER_NAME:
			criteria.createAlias("o.user", "u");

			criteria.add(Restrictions.like("u.name", query, MatchMode.ANYWHERE))
					.add(Restrictions.not(Restrictions.eq("u.id",
							wholesaler.getId())));
			projectionList.add(Projections.property("u.name"));
			projectionList.add(Projections.groupProperty("u.id"));
			criteria.setMaxResults(10);
			break;
		case MOBILE:
			criteria.createAlias("o.user", "u");

			criteria.add(
					Restrictions.like("u.mobile", query, MatchMode.ANYWHERE))
					.add(Restrictions.not(Restrictions.eq("u.id",
							wholesaler.getId())));
			projectionList.add(Projections.groupProperty("u.mobile"));
			criteria.setMaxResults(10);
			break;
		case ORDER_NO:
			criteria.add(Restrictions.like("o.orderNo", query,
					MatchMode.ANYWHERE));
			projectionList.add(Projections.property("o.orderNo"));
			projectionList.add(Projections.property("o.id"));
			break;
		}
		criteria.setProjection(projectionList);
		return this;
	}

	public OrderSearchQueryBuilder setRestrictions(String query,
			Retailer retailer) {
		ProjectionList projectionList = Projections.projectionList();
		switch (this.field) {
		case ORDER_STATUS:
			OrderStatus[] os = OrderStatus.find(query);
			if (os.length == 0) {
				resultList = new ArrayList<SearchResource>();
			}
			criteria.add(Restrictions.in("o.orderStatus", os));
			projectionList.add(Projections.groupProperty("o.orderStatus"));
			criteria.setMaxResults(10);
			break;
		case PRODUCT_NAME:
			criteria.createAlias("o.item", "item");
			criteria.createAlias("item.product", "prodct");
			criteria.add(Restrictions.like("prodct.name", query,
					MatchMode.ANYWHERE));

			projectionList.add(Projections.groupProperty("prodct.name"));
			criteria.setMaxResults(10);
			break;
		case WHOLESALER_NAME:
			criteria.createAlias("o.wholesaler", "u");

			criteria.add(Restrictions.like("u.name", query, MatchMode.ANYWHERE))
					.add(Restrictions.not(Restrictions.eq("u.id",
							retailer.getId())));
			projectionList.add(Projections.property("u.name"));
			projectionList.add(Projections.groupProperty("u.id"));
			criteria.setMaxResults(10);
			break;
		case ORDER_NO:
			criteria.add(Restrictions.like("o.orderNo", query,
					MatchMode.ANYWHERE));
			projectionList.add(Projections.property("o.orderNo"));
			projectionList.add(Projections.property("o.id"));
			break;
		}
		criteria.setProjection(projectionList);
		return this;
	}

	public List<SearchResource> list() {
		if (resultList == null) {
			resultList = new ArrayList<SearchResource>();
			switch (this.field) {
			case CUSTOMER_NAME: {
				List<Object[]> list = criteria.list();
				for (Object[] objects : list) {
					String name = objects[0] != null ? objects[0].toString()
							: "";
					String userId = objects[1] != null ? objects[1].toString()
							: "";

					SearchResource osr = new SearchResource(name, this.field,
							null, Order.class);
					osr.setResultId(userId);
					resultList.add(osr);
				}
			}
				break;
			case WHOLESALER_NAME: {
				List<Object[]> list = criteria.list();
				for (Object[] objects : list) {
					String name = objects[0] != null ? objects[0].toString()
							: "";
					String userId = objects[1] != null ? objects[1].toString()
							: "";

					SearchResource osr = new SearchResource(name, this.field,
							null, Order.class);
					osr.setResultId(userId);
					resultList.add(osr);
				}
			}
				break;
			case ORDER_NO: {
				List<Object[]> list = criteria.list();
				for (Object[] order : list) {
					SearchResource osr = new SearchResource(
							order[0].toString(), this.field, null, Order.class);
					osr.setResultId(order[1].toString());
					resultList.add(osr);
				}
			}
				break;
			case ORDER_STATUS: {
				List<Object> list = criteria.list();
				for (Object order : list) {
					String str = order.toString();
					if (this.field.equals(SearchIn.ORDER_STATUS))
						str = OrderStatus.valueOf(str).toString();
					resultList.add(new SearchResource(str, this.field, null,
							Order.class));
				}
			}
				break;
			default:
				List<Object> list = criteria.list();
				for (Object order : list) {
					String str = order.toString();
					resultList.add(new SearchResource(str, this.field, null,
							Order.class));
				}
				break;
			}
		}

		return resultList;
	}
}
