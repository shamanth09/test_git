package com.mrk.myordershop.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;

import com.mrk.myordershop.bean.Order;
import com.mrk.myordershop.bean.Retailer;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.WholesalerInstantOrder;
import com.mrk.myordershop.bean.dto.BasicOrderStatusWiseSummary;
import com.mrk.myordershop.bean.dto.CustomerFilter;
import com.mrk.myordershop.bean.dto.OrderStatusSummary;
import com.mrk.myordershop.bean.dto.OrderStatusWithDateSummary;
import com.mrk.myordershop.bean.dto.OrderStatusesSummary;
import com.mrk.myordershop.bean.dto.OrderSummaryByCategory;
import com.mrk.myordershop.bean.dto.OrderSummaryByUser;
import com.mrk.myordershop.bean.dto.OrderWeightMonthSummary;
import com.mrk.myordershop.bean.dto.RateCutSummaryFileter;
import com.mrk.myordershop.constant.ActiveFlag;
import com.mrk.myordershop.constant.OrderStatus;
import com.mrk.myordershop.dao.component.CoalesceOrder;
import com.mrk.myordershop.resource.RateCutResource;

@Repository
public class OrderSummaryDAOImpl implements OrderSummaryDAO {

	private static Logger log = Logger.getLogger(OrderSummaryDAOImpl.class);
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Page<RateCutResource> getRateCutSummary(Pageable pageable,
			RateCutSummaryFileter fileter, Wholesaler wholesaler) {
		Page<RateCutResource> rateCuts = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session
					.createCriteria(WholesalerInstantOrder.class);
			criteria.add(Restrictions.eq("user", wholesaler));
			criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			criteria.createAlias("referralUser", "refUser");
			criteria.createAlias("acceptance", "orderAcceptance");
			filter(criteria, fileter);
			int total = ((Number) criteria
					.setProjection(Projections.rowCount()).uniqueResult())
					.intValue();

			criteria.setProjection(null);
			criteria.setProjection(Projections
					.projectionList()
					.add(Projections.property("orderAcceptance.rateCut").as(
							"rateCut"))
					.add(Projections.property("id").as("orderId"))
					.add(Projections.property("orderNo").as("orderNo"))
					.add(Projections.property("refUser.name")
							.as("customerName"))
					.add(Projections.property("createTimestamp").as(
							"createTimestamp")));

			criteria.setResultTransformer(new AliasToBeanResultTransformer(
					RateCutResource.class));
			if (pageable != null) {
				buildSort(criteria, pageable);
				criteria.setMaxResults(pageable.getPageSize()).setFirstResult(
						pageable.getOffset());
			}
			rateCuts = new PageImpl<RateCutResource>(criteria.list(), pageable,
					total);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return rateCuts;
	}

	private void buildSort(Criteria criteria, Pageable pageable) {
		if (pageable.getSort() != null) {
			Iterator<org.springframework.data.domain.Sort.Order> i = pageable
					.getSort().iterator();
			while (i.hasNext()) {
				org.springframework.data.domain.Sort.Order order = i.next();
				switch (order.getProperty()) {
				case "createDate":
					if (order.isAscending())
						criteria.addOrder(org.hibernate.criterion.Order
								.asc("createTimestamp"));
					else
						criteria.addOrder(org.hibernate.criterion.Order
								.desc("createTimestamp"));
					break;
				case "updateDate":
					if (order.isAscending())
						criteria.addOrder(org.hibernate.criterion.Order
								.asc("updateTimestamp"));
					else
						criteria.addOrder(org.hibernate.criterion.Order
								.desc("updateTimestamp"));
					break;
				case "status":
					if (order.isAscending())
						criteria.addOrder(org.hibernate.criterion.Order
								.asc("orderStatus"));
					else
						criteria.addOrder(org.hibernate.criterion.Order
								.desc("orderStatus"));
					break;
				case "customer":
					criteria.createAlias("referralUser", "u");
					if (order.isAscending()) {
						criteria.addOrder(CoalesceOrder.asc("customerName",
								"u.name"));
					} else {
						criteria.addOrder(CoalesceOrder.desc("customerName",
								"u.name"));
					}
					break;
				default:
					criteria.addOrder(org.hibernate.criterion.Order
							.desc("createTimestamp"));
					break;
				}

			}
		} else
			criteria.addOrder(org.hibernate.criterion.Order
					.desc("createTimestamp"));
	}

	private void filter(Criteria criteria, RateCutSummaryFileter filter) {
		if (filter.getFromRate() != null) {
			criteria.add(Restrictions.ge("orderAcceptance.rateCut",
					filter.getFromRate()));
		}
		if (filter.getToRate() != null) {
			if (filter.getToRate() < 1)
				criteria.add(Restrictions
						.disjunction()
						.add(Restrictions.isNull("orderAcceptance.rateCut"))
						.add(Restrictions.le("orderAcceptance.rateCut",
								filter.getToRate())));
			else
				criteria.add(Restrictions.le("orderAcceptance.rateCut",
						filter.getToRate()));

		}
		if (filter.getOrderStatus() != null
				&& !filter.getOrderStatus().isEmpty()) {
			criteria.add(Restrictions.in("orderStatus", filter.getOrderStatus()));
		}

	}

	@Override
	public int getTotalQuantity(User user, OrderStatus[] statuses) {
		Integer result = 0;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Order.class, "o")
					.createAlias("wholesaler", "u").createAlias("item", "i")
					.createAlias("i.detail", "de")
					.setProjection(Projections.sum("de.quantity"))
					.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			if (user instanceof Wholesaler)
				criteria.add(Restrictions.eq("wholesaler", user));
			else {
				criteria.add(Restrictions
						.disjunction()
						.add(Restrictions.eq("customerMobile", user.getMobile()))
						.add(Restrictions.eq("referralUser.id", user.getId())));
			}
			criteria.add(Restrictions.in("o.orderStatus", statuses));
			result = criteria.uniqueResult() != null ? ((Number) criteria
					.uniqueResult()).intValue() : 0;
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Long getTotalWeight(User user, OrderStatus[] statuses) {
		Long result = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Order.class, "o")
					.createAlias("wholesaler", "u").createAlias("item", "i")
					.createAlias("i.detail", "de")
					.setProjection(Projections.sum("de.weight"))
					.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			if (user instanceof Wholesaler)
				criteria.add(Restrictions.eq("wholesaler", user));
			else {
				criteria.add(Restrictions
						.disjunction()
						.add(Restrictions.eq("customerMobile", user.getMobile()))
						.add(Restrictions.eq("referralUser.id", user.getId())));
			}
			criteria.add(Restrictions.in("o.orderStatus", statuses));
			result = criteria.uniqueResult() != null ? ((Number) criteria
					.uniqueResult()).longValue() : 0;
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public OrderWeightMonthSummary getTotalWeightByMonth(Date fromDate,
			Date toDate, User user, OrderStatus[] statuses) {
		OrderWeightMonthSummary result = new OrderWeightMonthSummary();
		result.setFromDate(fromDate);
		result.setToDate(toDate);
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Order.class)
					.createAlias("wholesaler", "w").createAlias("item", "i")
					.createAlias("i.detail", "de");

			criteria.setProjection(Projections
					.projectionList()
					.add(Projections
							.sqlGroupProjection(
									"date({alias}.CREATE_TIMESTAMP) as createTimestamp",
									"{alias}.CREATE_TIMESTAMP",
									new String[] { "createTimestamp" },
									new Type[] { StandardBasicTypes.DATE }))
					.add(Projections.sum("de.weight")));
			criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE)).add(
					Restrictions.between("createTimestamp", fromDate, toDate));
			if (user instanceof Wholesaler)
				criteria.add(Restrictions.eq("wholesaler", user));
			else {
				criteria.add(Restrictions
						.disjunction()
						.add(Restrictions.eq("customerMobile", user.getMobile()))
						.add(Restrictions.eq("referralUser.id", user.getId())));
			}
			criteria.add(Restrictions.in("orderStatus", statuses));
			List<Object[]> arrayList = criteria.list();

			for (Object[] entity : arrayList) {
				result.entry(entity[0].toString(),
						Double.valueOf(entity[1].toString()));
			}
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<BasicOrderStatusWiseSummary> getOrderStatusWiseSummary(
			User user, OrderStatus[] statuses) {
		List<BasicOrderStatusWiseSummary> result = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Order.class, "o")
					.createAlias("wholesaler", "w").createAlias("item", "i")
					.createAlias("i.detail", "de");

			criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			if (user instanceof Wholesaler)
				criteria.add(Restrictions.eq("wholesaler", user));
			else {
				criteria.add(Restrictions
						.disjunction()
						.add(Restrictions.eq("customerMobile", user.getMobile()))
						.add(Restrictions.eq("referralUser.id", user.getId())));
			}
			if (statuses != null && statuses.length > 0)
				criteria.add(Restrictions.in("orderStatus", statuses));
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.sum("de.weight"), "weight");
			projectionList.add(Projections.sum("de.quantity"), "quantity");
			projectionList.add(Projections.rowCount(), "count");
			projectionList.add(Projections.groupProperty("o.orderStatus"),
					"orderStatus");
			criteria.setProjection(projectionList);
			criteria.setResultTransformer(new AliasToBeanResultTransformer(
					BasicOrderStatusWiseSummary.class));
			result = criteria.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Map<String, Object> gerOrderCountByTillDate(Date toDate,
			String groupBy, User user) {
		Map<String, Object> statusEntity = new HashMap<String, Object>();
		List<String[]> result = null;
		Date lastDate = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Order.class, "o");
			criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			if (toDate != null)
				criteria.add(Restrictions.le("createTimestamp", toDate));
			if (user instanceof Wholesaler)
				criteria.add(Restrictions.eq("wholesaler", user));
			else {
				criteria.add(Restrictions
						.disjunction()
						.add(Restrictions.eq("customerMobile", user.getMobile()))
						.add(Restrictions.eq("referralUser.id", user.getId())));
			}

			criteria.setProjection(Projections.min("createTimestamp"));
			if (criteria.uniqueResult() != null)
				lastDate = (Date) criteria.uniqueResult();
			criteria.setProjection(Projections
					.projectionList()
					.add(Projections.alias(Projections.groupProperty(groupBy),
							"groupBy")).add(Projections.rowCount()));

			result = criteria.list();

			for (OrderStatus os : OrderStatus.getRetailerStatuses()) {
				for (Object[] strings : result) {
					statusEntity
							.put(OrderStatus.valueOf(strings[0].toString())
									.toString(), Integer.valueOf(strings[1]
									.toString()));
				}
				if (!statusEntity.containsKey(os.toString())) {
					statusEntity.put(os.toString(), 0);
				}
			}
			statusEntity.put("tillDate",
					new LocalDate(lastDate).toString("dd-MM-yyyy"));

		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return statusEntity;
	}

	@Override
	public Page<OrderSummaryByCategory> getOrderSummaryByCategory(
			Pageable pageable, User user) {
		Page<OrderSummaryByCategory> page = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Order.class, "o");
			criteria.add(Restrictions.eq("o.activeFlag", ActiveFlag.ACTIVE));
			if (user instanceof Wholesaler)
				criteria.add(Restrictions.eq("o.wholesaler", user));
			else {
				criteria.add(Restrictions
						.disjunction()
						.add(Restrictions.eq("o.customerMobile",
								user.getMobile()))
						.add(Restrictions.eq("o.referralUser.id", user.getId())));
			}
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			ProjectionList projectionList = Projections.projectionList();
			criteria.createAlias("o.item", "i").createAlias("i.product", "pro")
					.createAlias("i.detail", "d")
					.createAlias("pro.category", "cat");
			projectionList.add(Projections.groupProperty("cat.id"));
			projectionList.add(Projections.property("cat.name"));
			projectionList.add(Projections.rowCount(), "orderCount");
			projectionList.add(Projections.sum("d.weight"));
			projectionList.add(Projections.max("createTimestamp"), "maxCrts");

			int total = 0;
			List res = criteria.setProjection(
					Projections.projectionList().add(Projections.rowCount())
							.add(Projections.groupProperty("cat.id"))).list();
			if (res.size() > 0) {
				total = ((Number) ((Object[]) res.get(0))[0]).intValue();
			}

			criteria.setProjection(null);
			criteria.setProjection(projectionList);

			if (pageable.getSort() != null
					&& pageable.getSort().getOrderFor("orderCount") != null) {
				org.springframework.data.domain.Sort.Order orderBy = pageable
						.getSort().getOrderFor("orderCount");
				if (Direction.ASC.equals(orderBy.getDirection()))
					criteria.addOrder(org.hibernate.criterion.Order
							.asc("orderCount"));
				else
					criteria.addOrder(org.hibernate.criterion.Order
							.desc("orderCount"));
			} else {
				criteria.addOrder(org.hibernate.criterion.Order.desc("maxCrts"));
			}

			criteria.setMaxResults(pageable.getPageSize());
			criteria.setFirstResult(pageable.getOffset());

			List<Object[]> arrayList = criteria.list();

			List<OrderSummaryByCategory> result = new ArrayList<OrderSummaryByCategory>();
			for (Object[] objects : arrayList) {
				result.add(new OrderSummaryByCategory(Order.class,
						objects[0] != null ? Integer.valueOf(objects[0]
								.toString()) : null,
						objects[1] != null ? objects[1].toString() : null, Long
								.valueOf(objects[2].toString()), Double
								.valueOf(objects[3].toString())));
			}
			page = new PageImpl<OrderSummaryByCategory>(result, pageable, total);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return page;
	}

	@Override
	public List<String[]> gerOrderCountByCreatedDate(Date fromdate,
			Date todate, String groupBy, User user) {
		LocalDate fDate = new LocalDate(fromdate);
		LocalDate tDate = new LocalDate(todate);
		List<String[]> result = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session
					.createCriteria(Order.class, "o")
					.setProjection(
							Projections
									.projectionList()
									.add(Projections.alias(
											Projections.groupProperty(groupBy),
											"groupBy"))
									.add(Projections.rowCount()))
					.add(Restrictions.between("createTimestamp",
							fDate.toDate(), tDate.toDate()))
					.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));

			if (user instanceof Wholesaler)
				criteria.add(Restrictions.eq("wholesaler", user));
			else {
				criteria.add(Restrictions
						.disjunction()
						.add(Restrictions.eq("customerMobile", user.getMobile()))
						.add(Restrictions.eq("referralUser.id", user.getId())));
			}
			result = criteria.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Page<OrderSummaryByUser> getOrderCustomerWiseSummary(
			Pageable pageable, CustomerFilter filter, Wholesaler wholesaler) {
		Page<OrderSummaryByUser> page = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Order.class, "o");
			criteria.createAlias("o.referralUser", "u");
			criteria.add(Restrictions.eq("o.activeFlag", ActiveFlag.ACTIVE));
			criteria.add(Restrictions.eq("o.wholesaler", wholesaler));
			if (filter != null) {
				if (filter.getName() != null && !filter.getName().isEmpty()) {
					criteria.add(Restrictions
							.disjunction()
							.add(Restrictions.eq("o.customerName",
									filter.getName()))
							.add(Restrictions.eq("u.name", filter.getName())));
				}
				if (filter.getEmail() != null && !filter.getEmail().isEmpty()) {
					criteria.add(Restrictions.eq("u.email", filter.getEmail()));
				}
				if (filter.getMobile() != null && !filter.getMobile().isEmpty()) {
					criteria.add(Restrictions
							.disjunction()
							.add(Restrictions.eq("o.customerMobile",
									filter.getMobile()))
							.add(Restrictions.eq("u.mobile", filter.getMobile())));
				}
				if (filter.getOrderStatus() != null) {
					criteria.add(Restrictions.eq("o.orderStatus",
							filter.getOrderStatus()));
				}
			}
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.groupProperty("u.id"));
			projectionList.add(Projections.property("customerName"));
			projectionList.add(Projections.property("u.name"));
			projectionList.add(Projections.rowCount(), "orderCount");
			projectionList.add(Projections.max("createTimestamp"), "maxCrts");
			projectionList.add(Projections.groupProperty("customerMobile"));
			projectionList.add(Projections.property("u.mobile"));

			int total = ((Number) criteria
					.setProjection(Projections.rowCount()).uniqueResult())
					.intValue();
			criteria.setProjection(null);

			criteria.setProjection(projectionList);

			// DetachedCriteria subDC = DetachedCriteria
			// .forClass(Order.class, "o");
			// subDC.createAlias("o.user", "u");
			// subDC.setProjection(Projections.projectionList()
			// .add(Projections.rowCount())
			// .add(Projections.groupProperty("u.id"), "gpId")
			// .add(Projections.groupProperty("customerName")));
			//
			// DetachedCriteria dDC = DetachedCriteria.forClass(Order.class,
			// "o");
			// dDC.add(Subqueries.lt(Long.valueOf(0), subDC));
			// dDC.setProjection(Projections.rowCount());
			// log.debug(((Number) dDC.getExecutableCriteria(session).list())
			// .intValue());

			if (pageable.getSort() != null) {
				if (pageable.getSort().getOrderFor("orderCount") != null) {
					org.springframework.data.domain.Sort.Order orderBy = pageable
							.getSort().getOrderFor("orderCount");
					criteria.addOrder(Direction.ASC.equals(orderBy
							.getDirection()) ? org.hibernate.criterion.Order
							.asc("orderCount") : org.hibernate.criterion.Order
							.desc("orderCount"));
				}
				if (pageable.getSort().getOrderFor("createDate") != null) {
					org.springframework.data.domain.Sort.Order orderBy = pageable
							.getSort().getOrderFor("createDate");
					criteria.addOrder(Direction.ASC.equals(orderBy
							.getDirection()) ? org.hibernate.criterion.Order
							.asc("o.createTimestamp")
							: org.hibernate.criterion.Order
									.desc("o.createTimestamp"));
				}
			} else {
				criteria.addOrder(org.hibernate.criterion.Order.desc("maxCrts"));
			}

			log.error("total orders:" + total + " but actual :"
					+ (total = criteria.list().size()));// with
														// out
														// query
			criteria.setMaxResults(pageable.getPageSize());
			criteria.setFirstResult(pageable.getOffset());

			List<Object[]> arrayList = criteria.list();

			List<OrderSummaryByUser> result = new ArrayList<OrderSummaryByUser>();
			for (Object[] objects : arrayList) {
				String userId = (objects[0] != null ? objects[0].toString()
						: null);
				Long totalOrders = Long.valueOf(objects[3].toString());
				String customerName = (objects[1] != null ? objects[1]
						.toString() : "");
				String customerMobile = (objects[5] != null ? objects[5]
						.toString() : "");
				String userName = (objects[2] != null ? objects[2].toString()
						: "");
				String userMobile = (objects[6] != null ? objects[6].toString()
						: "");

				OrderSummaryByUser summary = new OrderSummaryByUser(
						Order.class, userId, totalOrders);

				if (userId != null && userId.equals(wholesaler.getId())) {
					summary.setName(customerName);
					summary.setUserId(null);
					summary.setMobile(customerMobile);
				} else {
					summary.setName(userName);
					summary.setMobile(userMobile);
				}
				result.add(summary);
			}
			page = new PageImpl<OrderSummaryByUser>(result, pageable, total);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return page;
	}

	@Override
	public Page<OrderSummaryByUser> getOrderWholesalerWiseSummary(
			Pageable pageable, Retailer retailer) {
		Page<OrderSummaryByUser> page = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Order.class, "o");
			criteria.add(Restrictions.eq("o.activeFlag", ActiveFlag.ACTIVE));
			criteria.add(Restrictions
					.disjunction()
					.add(Restrictions.eq("o.customerMobile",
							retailer.getMobile()))
					.add(Restrictions.eq("o.referralUser.id", retailer.getId())));
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			ProjectionList projectionList = Projections.projectionList();
			criteria.createAlias("o.wholesaler", "u");
			projectionList.add(Projections.groupProperty("u.id"));
			projectionList.add(Projections.property("u.name"));
			projectionList.add(Projections.rowCount(), "orderCount");
			projectionList.add(Projections.max("createTimestamp"), "maxCrts");

			int total = ((Number) criteria
					.setProjection(Projections.rowCount()).uniqueResult())
					.intValue();
			criteria.setProjection(null);

			criteria.setProjection(projectionList);

			if (pageable.getSort() != null
					&& pageable.getSort().getOrderFor("orderCount") != null) {
				org.springframework.data.domain.Sort.Order orderBy = pageable
						.getSort().getOrderFor("orderCount");
				if (Direction.ASC.equals(orderBy.getDirection()))
					criteria.addOrder(org.hibernate.criterion.Order
							.asc("orderCount"));
				else
					criteria.addOrder(org.hibernate.criterion.Order
							.desc("orderCount"));
			} else {
				criteria.addOrder(org.hibernate.criterion.Order.desc("maxCrts"));
			}

			criteria.setMaxResults(pageable.getPageSize());
			criteria.setFirstResult(pageable.getOffset());

			List<Object[]> arrayList = criteria.list();

			List<OrderSummaryByUser> result = new ArrayList<OrderSummaryByUser>();
			for (Object[] objects : arrayList) {
				OrderSummaryByUser summary = new OrderSummaryByUser(
						Order.class, objects[0] != null ? objects[0].toString()
								: null, Long.valueOf(objects[2].toString()));
				summary.setName(objects[1] != null ? objects[1].toString() : "");
				result.add(summary);
			}
			page = new PageImpl<OrderSummaryByUser>(result, pageable, total);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return page;
	}

	@Override
	@Deprecated
	public OrderStatusSummary getOrderStatusSummary(String userId,
			Wholesaler wholesaler, OrderStatus orderStatus) {
		OrderStatusSummary orderStatusSummary = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Order.class, "o");
			criteria.add(Restrictions.eq("o.activeFlag", ActiveFlag.ACTIVE));
			criteria.add(Restrictions.eq("o.wholesaler.id", wholesaler.getId()));
			criteria.add(Restrictions.eq("o.referralUser.id", userId));
			if (orderStatus != null)
				criteria.add(Restrictions.eq("o.orderStatus", orderStatus));
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.groupProperty("o.orderStatus"));
			projectionList.add(Projections.rowCount(), "orderCount");
			projectionList.add(Projections.max("o.createTimestamp"),
					"orderDate");
			criteria.setProjection(projectionList);

			List<Object[]> arrayList = criteria.list();

			if (orderStatus == null) {
				orderStatusSummary = new OrderStatusesSummary();
			} else {
				orderStatusSummary = new OrderStatusWithDateSummary();
			}
			for (Object[] objects : arrayList) {
				orderStatusSummary.setValue(
						OrderStatus.valueOf(objects[0].toString()),
						Integer.valueOf(objects[1].toString()),
						(Date) objects[2]);
			}
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return orderStatusSummary;
	}

	@Override
	@Deprecated
	public OrderStatusSummary getOrderStatusSummaryByMobile(String mobile,
			Wholesaler wholesaler, OrderStatus orderStatus) {
		OrderStatusSummary orderStatusSummary = new OrderStatusesSummary();
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Order.class, "o");
			criteria.createAlias("o.referralUser", "ref");
			criteria.add(Restrictions.eq("o.activeFlag", ActiveFlag.ACTIVE));
			criteria.add(Restrictions.eq("o.wholesaler.id", wholesaler.getId()));
			criteria.add(Restrictions.disjunction()
					.add(Restrictions.eq("o.customerMobile", mobile))
					.add(Restrictions.eq("ref.mobile", mobile)));
			if (orderStatus != null)
				criteria.add(Restrictions.eq("o.orderStatus", orderStatus));
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.groupProperty("o.orderStatus"));
			projectionList.add(Projections.rowCount(), "orderCount");
			projectionList.add(Projections.max("o.createTimestamp"),
					"orderDate");
			criteria.setProjection(projectionList);

			List<Object[]> arrayList = criteria.list();

			if (orderStatus == null) {
				orderStatusSummary = new OrderStatusesSummary();
			} else {
				orderStatusSummary = new OrderStatusWithDateSummary();
			}
			for (Object[] objects : arrayList) {
				orderStatusSummary.setValue(
						OrderStatus.valueOf(objects[0].toString()),
						Integer.valueOf(objects[1].toString()),
						(Date) objects[2]);
			}
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return orderStatusSummary;
	}

}
