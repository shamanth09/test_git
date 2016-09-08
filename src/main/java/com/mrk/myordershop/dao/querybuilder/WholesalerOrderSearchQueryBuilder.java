package com.mrk.myordershop.dao.querybuilder;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import com.mrk.myordershop.bean.Order;
import com.mrk.myordershop.bean.WholesalerOrder;
import com.mrk.myordershop.bean.dto.SearchFilter.SearchIn;
import com.mrk.myordershop.bean.dto.SearchResource;
import com.mrk.myordershop.constant.OrderStatus;

public class WholesalerOrderSearchQueryBuilder {

	private Criteria criteria;
	private SearchIn field;
	private List<SearchResource> resultList;

	public WholesalerOrderSearchQueryBuilder(Criteria criteria, SearchIn field) {
		this.criteria = criteria;
		this.field = field;
	}

	public Criteria criteria() {
		return this.criteria;
	}

	public WholesalerOrderSearchQueryBuilder setRestrictions(String query) {
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
		case SUPPLIER_NAME:
			criteria.createAlias("o.supplier", "u", JoinType.LEFT_OUTER_JOIN,
					Restrictions.isNotNull("o.supplier"));

			criteria.add(Restrictions.like("u.name", query, MatchMode.ANYWHERE));

			projectionList.add(Projections.property("u.name"));
			projectionList.add(Projections.groupProperty("u.id"));
			criteria.setMaxResults(10);
			break;
		case MOBILE:
			criteria.createAlias("o.supplier", "u", JoinType.LEFT_OUTER_JOIN,
					Restrictions.isNotNull("o.supplier"));

			criteria.add(Restrictions.like("u.mobile", query,
					MatchMode.ANYWHERE));

			projectionList.add(Projections.property("u.mobile"));
			projectionList.add(Projections.groupProperty("u.id"));
			criteria.setMaxResults(10);
			break;
		case WHOLESALER_NAME:
			criteria.createAlias("o.user", "u", JoinType.LEFT_OUTER_JOIN,
					Restrictions.isNotNull("o.user"));

			criteria.add(Restrictions.like("u.name", query, MatchMode.ANYWHERE));

			projectionList.add(Projections.property("u.name"));
			projectionList.add(Projections.groupProperty("u.id"));
			criteria.setMaxResults(10);
			break;
		case ORDER_NO:
			criteria.createAlias("o.order", "rtorder");
			criteria.add(Restrictions.like("rtorder.orderNo", query,
					MatchMode.ANYWHERE));
			projectionList.add(Projections.property("rtorder.orderNo"));
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
			case SUPPLIER_NAME: {
				List<Object[]> list = criteria.list();
				for (Object[] objects : list) {
					String name = objects[0] != null ? objects[0].toString()
							+ " " : "";
					String userId = objects[1] != null ? objects[1].toString()
							: null;

					SearchResource osr = new SearchResource(name, this.field,
							null, WholesalerOrder.class);
					osr.setResultId(userId);
					resultList.add(osr);
				}
			}
				break;
			case MOBILE: {
				List<Object[]> list = criteria.list();
				for (Object[] objects : list) {
					String name = objects[0] != null ? objects[0].toString()
							+ " " : "";
					String userId = objects[1] != null ? objects[1].toString()
							: null;

					SearchResource osr = new SearchResource(name, this.field,
							null, WholesalerOrder.class);
					osr.setResultId(userId);
					resultList.add(osr);
				}
			}
				break;
			case WHOLESALER_NAME: {
				List<Object[]> list = criteria.list();
				for (Object[] objects : list) {
					String name = objects[0] != null ? objects[0].toString()
							+ " " : "";
					String userId = objects[1] != null ? objects[1].toString()
							: null;

					SearchResource osr = new SearchResource(name, this.field,
							null, WholesalerOrder.class);
					osr.setResultId(userId);
					resultList.add(osr);
				}
			}
				break;
			case ORDER_NO: {
				List<Object[]> list = criteria.list();
				for (Object[] order : list) {
					SearchResource osr = new SearchResource(
							order[0].toString(), this.field, null,
							WholesalerOrder.class);
					osr.setResultId(order[1].toString());
					resultList.add(osr);
				}
			}
				break;
			case PRODUCT_NAME: {
				List<Object> list = criteria.list();
				for (Object order : list) {
					String str = order.toString();
					resultList.add(new SearchResource(str, this.field, null,
							Order.class));
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
