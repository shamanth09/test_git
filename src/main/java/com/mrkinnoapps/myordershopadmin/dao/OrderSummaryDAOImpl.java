package com.mrkinnoapps.myordershopadmin.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;

import com.mrkinnoapps.myordershopadmin.bean.constant.ActiveFlag;
import com.mrkinnoapps.myordershopadmin.bean.constant.OrderStatus;
import com.mrkinnoapps.myordershopadmin.bean.dto.CustomerFilter;
import com.mrkinnoapps.myordershopadmin.bean.dto.OrderStatusSummary;
import com.mrkinnoapps.myordershopadmin.bean.dto.OrderStatusWithDateSummary;
import com.mrkinnoapps.myordershopadmin.bean.dto.OrderStatusesSummary;
import com.mrkinnoapps.myordershopadmin.bean.dto.OrderSummaryByCategory;
import com.mrkinnoapps.myordershopadmin.bean.dto.OrderSummaryByUser;
import com.mrkinnoapps.myordershopadmin.bean.dto.OrderWeightMonthSummary;
import com.mrkinnoapps.myordershopadmin.bean.dto.RateCutResource;
import com.mrkinnoapps.myordershopadmin.bean.dto.RateCutSummaryFileter;
import com.mrkinnoapps.myordershopadmin.bean.entity.Order;
import com.mrkinnoapps.myordershopadmin.bean.entity.Retailer;
import com.mrkinnoapps.myordershopadmin.bean.entity.Supplier;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.bean.entity.Wholesaler;
import com.mrkinnoapps.myordershopadmin.bean.entity.WholesalerInstantOrder;
import com.mrkinnoapps.myordershopadmin.dao.component.CoalesceOrder;

@Repository
public class OrderSummaryDAOImpl implements OrderSummaryDAO {

	private static Logger log = Logger.getLogger(OrderSummaryDAOImpl.class);
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Page<RateCutResource> getRateCutSummary(Pageable pageable,
			RateCutSummaryFileter fileter, Wholesaler wholesaler) {
		Page<RateCutResource> rateCuts = null;
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			Criteria criteria = session
					.createCriteria(WholesalerInstantOrder.class);
			criteria.add(Restrictions.eq("user", wholesaler));
			criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			filter(criteria, fileter);
			int total = ((Number) criteria
					.setProjection(Projections.rowCount()).uniqueResult())
					.intValue();

			criteria.setProjection(Projections
					.projectionList()
					.add(Projections.property("rateCut").as("rateCut"))
					.add(Projections.property("id").as("orderId"))
					.add(Projections.property("orderNo").as("orderNo"))
					.add(Projections.property("customerName")
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
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			transaction.rollback();
		} finally {
			session.close();
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
					criteria.createAlias("supplier", "u");
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
			criteria.add(Restrictions.ge("rateCut", filter.getFromRate()));
		}
		if (filter.getToRate() != null) {
			if (filter.getToRate() < 1)
				criteria.add(Restrictions.disjunction()
						.add(Restrictions.isNull("rateCut"))
						.add(Restrictions.le("rateCut", filter.getToRate())));
			else
				criteria.add(Restrictions.le("rateCut", filter.getToRate()));

		}

	}

	@Override
	public int getTotalQuantity(User user, OrderStatus[] statuses) {
		Integer result = 0;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Order.class, "o")
					.createAlias("wholesaler", "u").createAlias("item", "i")
					.createAlias("i.detail", "de")
					.setProjection(Projections.sum("de.quantity"))
					.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			if (user instanceof Wholesaler) {
				criteria.add(Restrictions.eq("wholesaler", user));
			} else {
				criteria.add(Restrictions
						.disjunction()
						.add(Restrictions.eq("user.id", user.getId()))
						.add(Restrictions.eq("customerMobile", user.getMobile())));
			}
			criteria.add(Restrictions.in("o.orderStatus", statuses));
			result = criteria.uniqueResult() != null ? ((Number) criteria
					.uniqueResult()).intValue() : 0;
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return result;
	}

	@Override
	public Long getTotalWeight(User user, OrderStatus[] statuses) {
		Long result = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
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
						.add(Restrictions.eq("user.id", user.getId()))
						.add(Restrictions.eq("customerMobile", user.getMobile())));
			}
			criteria.add(Restrictions.in("o.orderStatus", statuses));
			result = criteria.uniqueResult() != null ? ((Number) criteria
					.uniqueResult()).longValue() : 0;
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return result;
	}

	@Override
	public OrderWeightMonthSummary getTotalWeightByMonth(Date fromDate,
			Date toDate, User user, OrderStatus[] statuses) {
		OrderWeightMonthSummary result = new OrderWeightMonthSummary();
		result.setFromDate(fromDate);
		result.setToDate(toDate);
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
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
						.add(Restrictions.eq("user.id", user.getId()))
						.add(Restrictions.eq("customerMobile", user.getMobile())));
			}
			criteria.add(Restrictions.in("orderStatus", statuses));
			List<Object[]> arrayList = criteria.list();

			for (Object[] entity : arrayList) {
				result.entry(entity[0].toString(),
						Double.valueOf(entity[1].toString()));
			}
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return result;
	}

	@Override
	public OrderStatusesSummary gerOrderCountByTillDate(Date toDate,
			String groupBy, User user) {
		OrderStatusesSummary statusEntity = new OrderStatusesSummary();
		Date lastDate = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Order.class, "o");
			criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			if (toDate != null)
				 criteria.add(Restrictions.le("createTimestamp", toDate));
			if (user instanceof Wholesaler)
				criteria.add(Restrictions.eq("wholesaler", user));
			else {
				criteria.add(Restrictions
						.disjunction()
						.add(Restrictions.eq("user.id", user.getId()))
						.add(Restrictions.eq("customerMobile", user.getMobile())));
				// criteria.add(Restrictions.eq("user", user));
			}

			criteria.setProjection(Projections.projectionList()
					.add(Projections.groupProperty(groupBy), "orderStatus")
					.add(Projections.min("createTimestamp"), "pastOrderDate")
					.add(Projections.rowCount(), "count"));
			criteria.setResultTransformer(Transformers
					.aliasToBean(OrderStatusWithDateSummary.class));
			List<OrderStatusWithDateSummary> result = null;
			result = criteria.list();
			statusEntity.setOrderSummary(result);
			transaction.commit();

		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return statusEntity;
	}

	@Override
	public Page<OrderSummaryByCategory> getOrderSummaryByCategory(
			Pageable pageable, User user) {
		Page<OrderSummaryByCategory> page = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Order.class, "o");
			criteria.add(Restrictions.eq("o.activeFlag", ActiveFlag.ACTIVE));
			if (user instanceof Wholesaler)
				criteria.add(Restrictions.eq("o.wholesaler", user));
			else {
				criteria.add(Restrictions
						.disjunction()
						.add(Restrictions.eq("o.user.id", user.getId()))
						.add(Restrictions.eq("o.customerMobile",
								user.getMobile())));
				// criteria.add(Restrictions.eq("o.user", user));
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
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return page;
	}

	@Override
	public List<String[]> gerOrderCountByCreatedDate(Date fromdate,
			Date todate, String groupBy, User user) {
		LocalDate fDate = new LocalDate(fromdate);
		LocalDate tDate = new LocalDate(todate);
		List<String[]> result = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
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
				// criteria.add(Restrictions.eq("user", user));
				criteria.add(Restrictions
						.disjunction()
						.add(Restrictions.eq("user.id", user.getId()))
						.add(Restrictions.eq("customerMobile", user.getMobile())));
			}
			result = criteria.list();
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return result;
	}

	@Override
	public Page<OrderSummaryByUser> getOrderCustomerWiseSummary(
			Pageable pageable, CustomerFilter filter, Wholesaler wholesaler) {
		Page<OrderSummaryByUser> page = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Order.class, "o");
			criteria.createAlias("o.user", "u");
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
					+ (total = criteria.list().size()));// with out query
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
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return page;
	}

	@Override
	public Page<OrderSummaryByUser> getOrderWholesalerWiseSummary(
			Pageable pageable, Retailer retailer) {
		Page<OrderSummaryByUser> page = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Order.class, "o");
			criteria.add(Restrictions.eq("o.activeFlag", ActiveFlag.ACTIVE));
			criteria.add(Restrictions
					.disjunction()
					.add(Restrictions.eq("o.user.id", retailer.getId()))
					.add(Restrictions.eq("o.customerMobile",
							retailer.getMobile())));
			// criteria.add(Restrictions.eq("o.user", retailer));
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
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return page;
	}

	@Override
	public OrderStatusSummary getOrderStatusSummary(String userId,
			Wholesaler wholesaler, OrderStatus orderStatus) {
		OrderStatusSummary orderStatusSummary = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Order.class, "o");
			criteria.add(Restrictions.eq("o.activeFlag", ActiveFlag.ACTIVE));
			criteria.add(Restrictions.eq("o.wholesaler.id", wholesaler.getId()));
			criteria.add(Restrictions.eq("o.user.id", userId));
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
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return orderStatusSummary;
	}

	@Override
	public OrderStatusSummary getOrderStatusSummaryByMobile(String mobile,
			Wholesaler wholesaler, OrderStatus orderStatus) {
		OrderStatusSummary OrderStatusesSummary = new OrderStatusesSummary();
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Order.class, "o");
			criteria.add(Restrictions.eq("o.activeFlag", ActiveFlag.ACTIVE));
			criteria.add(Restrictions.eq("o.wholesaler.id", wholesaler.getId()));
			criteria.add(Restrictions.eq("o.customerMobile", mobile));
			if (orderStatus != null)
				criteria.add(Restrictions.eq("o.orderStatus", orderStatus));

			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.groupProperty("o.orderStatus"),
					"orderStatus");
			projectionList.add(Projections.rowCount(), "count");
			projectionList.add(Projections.max("o.createTimestamp"),
					"recentOrderDate");
			criteria.setProjection(projectionList);

			if (orderStatus == null) {
				OrderStatusesSummary statusesSummary = new OrderStatusesSummary();
				criteria.setResultTransformer(Transformers
						.aliasToBean(OrderStatusWithDateSummary.class));

				List<OrderStatusWithDateSummary> arrayList = criteria.list();
				statusesSummary.setOrderSummary(arrayList);
				OrderStatusesSummary = statusesSummary;
			} else {
				OrderStatusesSummary = new OrderStatusWithDateSummary();
			}
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return OrderStatusesSummary;
	}

	@Override
	public OrderStatusesSummary getOrderCountByStatusWise(Date fromdate,
			Date todate, User user) {
		OrderStatusesSummary orderStatusesSummary = new OrderStatusesSummary();
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Order.class, "o");
			criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			if (fromdate != null) {
				criteria.add(Restrictions.ge("createTimestamp", fromdate));
			} else {
				fromdate = (Date) criteria.setProjection(
						Projections.min("createTimestamp")).uniqueResult();
			}

			if (todate != null) {
				criteria.add(Restrictions.le("createTimestamp", todate));
			} else {
				todate = (Date) criteria.setProjection(
						Projections.max("createTimestamp")).uniqueResult();
			}
			if (user instanceof Wholesaler)
				criteria.add(Restrictions.eq("wholesaler", user));
			else {
				criteria.add(Restrictions
						.disjunction()
						.add(Restrictions.eq("user.id", user.getId()))
						.add(Restrictions.eq("customerMobile", user.getMobile())));
			}
			ProjectionList projectionList = Projections
					.projectionList()
					.add(Projections.groupProperty("orderStatus"),
							"orderStatus").add(Projections.rowCount(), "count");
			criteria.setProjection(projectionList);
			criteria.setResultTransformer(Transformers
					.aliasToBean(OrderStatusWithDateSummary.class));
			List<OrderStatusWithDateSummary> result = criteria.list();
			orderStatusesSummary.setFromDate(fromdate);
			orderStatusesSummary.setToDate(todate);
			orderStatusesSummary.setOrderSummary(result);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return orderStatusesSummary;
	}
}
