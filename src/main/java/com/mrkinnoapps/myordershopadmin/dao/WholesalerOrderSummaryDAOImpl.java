package com.mrkinnoapps.myordershopadmin.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
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
import com.mrkinnoapps.myordershopadmin.bean.dto.OrderStatusWithDateSummary;
import com.mrkinnoapps.myordershopadmin.bean.dto.OrderStatusesSummary;
import com.mrkinnoapps.myordershopadmin.bean.dto.OrderSummaryByCategory;
import com.mrkinnoapps.myordershopadmin.bean.dto.OrderSummaryByUser;
import com.mrkinnoapps.myordershopadmin.bean.dto.OrderWeightMonthSummary;
import com.mrkinnoapps.myordershopadmin.bean.entity.Supplier;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.bean.entity.Wholesaler;
import com.mrkinnoapps.myordershopadmin.bean.entity.WholesalerOrder;

@Repository
public class WholesalerOrderSummaryDAOImpl implements WholesalerOrderSummaryDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public int getTotalQuantity(User user, OrderStatus[] statuses) {
		Integer result = 0;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session
					.createCriteria(WholesalerOrder.class, "o")
					.createAlias("user", "u").createAlias("item", "i")
					.createAlias("i.detail", "de")
					.setProjection(Projections.sum("de.quantity"))
					.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			if (user instanceof Wholesaler)
				criteria.add(Restrictions.eq("user", user));
			else
				criteria.add(Restrictions.eq("supplier", user));
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
			Criteria criteria = session
					.createCriteria(WholesalerOrder.class, "o")
					.createAlias("user", "u").createAlias("item", "i")
					.createAlias("i.detail", "de")
					.setProjection(Projections.sum("de.weight"))
					.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			if (user instanceof Wholesaler)
				criteria.add(Restrictions.eq("user", user));
			else
				criteria.add(Restrictions.eq("supplier", user));
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
			Criteria criteria = session.createCriteria(WholesalerOrder.class)
					.createAlias("user", "w").createAlias("item", "i")
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
				criteria.add(Restrictions.eq("user", user));
			else
				criteria.add(Restrictions.eq("supplier", user));
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
		List<OrderStatusWithDateSummary> result = null;
		Date lastDate = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(WholesalerOrder.class,
					"o");
			if (toDate != null)
				criteria.add(Restrictions.le("createTimestamp", toDate));
			criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			if (user instanceof Wholesaler)
				criteria.add(Restrictions.eq("user", user));
			else
				criteria.add(Restrictions.eq("supplier", user));

			criteria.setProjection(Projections
					.projectionList()
					.add(Projections.groupProperty(groupBy), "orderStatus")
					.add(Projections.rowCount(), "count")
					.add(Projections.min("createTimestamp"), "pastOrderDate"));
			criteria.setResultTransformer(Transformers
					.aliasToBean(OrderStatusWithDateSummary.class));
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
			Pageable pageable, User currentUser) {
		Page<OrderSummaryByCategory> page = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(WholesalerOrder.class,
					"o");
			criteria.add(Restrictions.eq("o.activeFlag", ActiveFlag.ACTIVE));
			if (currentUser instanceof Wholesaler)
				criteria.add(Restrictions.eq("o.user", currentUser));
			else
				criteria.add(Restrictions.eq("o.supplier", currentUser));
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			ProjectionList projectionList = Projections.projectionList();
			criteria.createAlias("o.item", "i").createAlias("i.product", "pro")
					.createAlias("i.detail", "d")
					.createAlias("pro.category", "cat");
			projectionList.add(Projections.groupProperty("cat.id"));
			projectionList.add(Projections.property("cat.name"));
			projectionList.add(Projections.rowCount(), "orderCount");
			projectionList.add(Projections.sum("d.weight"));
			projectionList.add(Projections.max("createTimestamp"), "crts");

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
				criteria.addOrder(org.hibernate.criterion.Order.desc("crts"));
			}

			criteria.setMaxResults(pageable.getPageSize());
			criteria.setFirstResult(pageable.getOffset());

			List<Object[]> arrayList = criteria.list();

			List<OrderSummaryByCategory> result = new ArrayList<OrderSummaryByCategory>();
			for (Object[] objects : arrayList) {
				result.add(new OrderSummaryByCategory(WholesalerOrder.class,
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
					.createCriteria(WholesalerOrder.class, "o")
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
				criteria.add(Restrictions.eq("user", user));
			else
				criteria.add(Restrictions.eq("supplier", user));
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
	public Page<OrderSummaryByUser> getOrderWholesalerWiseSummary(
			Pageable pageable, Supplier supplier) {
		Page<OrderSummaryByUser> page = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(WholesalerOrder.class,
					"o");
			criteria.add(Restrictions.eq("o.activeFlag", ActiveFlag.ACTIVE));
			criteria.add(Restrictions.eq("o.supplier", supplier));
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			ProjectionList projectionList = Projections.projectionList();
			criteria.createAlias("o.user", "u");
			projectionList.add(Projections.groupProperty("u.id"));
			projectionList.add(Projections.property("u.name"));
			projectionList.add(Projections.rowCount(), "orderCount");
			projectionList.add(Projections.max("createTimestamp"), "crts");

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
				criteria.addOrder(org.hibernate.criterion.Order.desc("crts"));
			}

			criteria.setMaxResults(pageable.getPageSize());
			criteria.setFirstResult(pageable.getOffset());

			List<Object[]> arrayList = criteria.list();

			List<OrderSummaryByUser> result = new ArrayList<OrderSummaryByUser>();
			for (Object[] objects : arrayList) {
				OrderSummaryByUser summary = new OrderSummaryByUser(
						WholesalerOrder.class,
						objects[0] != null ? objects[0].toString() : null,
						Long.valueOf(objects[2].toString()));
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
	public Page<OrderSummaryByUser> getOrderSummaryGroupBySupplier(
			Pageable pageable, Wholesaler wholesaler) {
		Page<OrderSummaryByUser> page = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(WholesalerOrder.class,
					"o");
			ProjectionList projectionList = Projections.projectionList();
			criteria.add(Restrictions.eq("o.activeFlag", ActiveFlag.ACTIVE));
			criteria.add(Restrictions.eq("o.user.id", wholesaler.getId()));

			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			criteria.createAlias("o.supplier", "u");
			projectionList.add(Projections.groupProperty("u.id"));
			projectionList.add(Projections.property("u.name"));
			projectionList.add(Projections.rowCount(), "orderCount");
			projectionList.add(Projections.max("createTimestamp"), "crts");

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
				criteria.addOrder(org.hibernate.criterion.Order.desc("crts"));
			}
			criteria.setMaxResults(pageable.getPageSize());
			criteria.setFirstResult(pageable.getOffset());

			List<Object[]> arrayList = criteria.list();

			List<OrderSummaryByUser> result = new ArrayList<OrderSummaryByUser>();
			for (Object[] objects : arrayList) {
				OrderSummaryByUser summary = new OrderSummaryByUser(
						WholesalerOrder.class,
						objects[0] != null ? objects[0].toString() : null,
						Long.valueOf(objects[2].toString()));
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
	// @Override
	// public Page<OrderSummaryByCategory> getOrderSummaryByCategory(
	// Pageable pageable, User user) {
	// Page<OrderSummaryByCategory> page = null;
	// Session session = sessionFactory.openSession();
	// try {
	// Transaction transaction = session.beginTransaction();
	// Criteria criteria = session.createCriteria(WholesalerOrder.class,
	// "o");
	// criteria.add(Restrictions.eq("o.activeFlag", ActiveFlag.ACTIVE));
	// if (user instanceof Wholesaler)
	// criteria.add(Restrictions.eq("o.user", user));
	// else
	// criteria.add(Restrictions.eq("o.supplier", user));
	// criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	//
	// ProjectionList projectionList = Projections.projectionList();
	// criteria.createAlias("o.item", "i").createAlias("i.product", "pro")
	// .createAlias("pro.category", "cat");
	// projectionList.add(Projections.groupProperty("cat.id"));
	// projectionList.add(Projections.property("cat.name"));
	// projectionList.add(Projections.rowCount(), "orderCount");
	// projectionList.add(Projections.max("createTimestamp"), "crts");
	//
	// int total = 0;
	// List res = criteria.setProjection(
	// Projections.projectionList().add(Projections.rowCount())
	// .add(Projections.groupProperty("cat.id"))).list();
	// if (res.size() > 0) {
	// total = ((Number) ((Object[]) res.get(0))[0]).intValue();
	// }
	//
	// criteria.setProjection(null);
	// criteria.setProjection(projectionList);
	//
	// if (pageable.getSort() != null
	// && pageable.getSort().getOrderFor("orderCount") != null) {
	// org.springframework.data.domain.Sort.Order orderBy = pageable
	// .getSort().getOrderFor("orderCount");
	// if (Direction.ASC.equals(orderBy.getDirection()))
	// criteria.addOrder(org.hibernate.criterion.Order
	// .asc("orderCount"));
	// else
	// criteria.addOrder(org.hibernate.criterion.Order
	// .desc("orderCount"));
	// } else {
	// criteria.addOrder(org.hibernate.criterion.Order.desc("crts"));
	// }
	//
	// criteria.setMaxResults(pageable.getPageSize());
	// criteria.setFirstResult(pageable.getOffset());
	//
	// List<Object[]> arrayList = criteria.list();
	//
	// List<OrderSummaryByCategory> result = new
	// ArrayList<OrderSummaryByCategory>();
	// for (Object[] objects : arrayList) {
	// result.add(new OrderSummaryByCategory(WholesalerOrder.class,
	// objects[0] != null ? Integer.valueOf(objects[0]
	// .toString()) : null,
	// objects[1] != null ? objects[1].toString() : null, Long
	// .valueOf(objects[2].toString())));
	// }
	// page = new PageImpl<OrderSummaryByCategory>(result, pageable, total);
	// transaction.commit();
	// } catch (HibernateException e) {
	// e.printStackTrace();
	// } finally {
	// session.close();
	// }
	// return page;
	// }

	@Override
	public OrderStatusesSummary getOrderCountByStatusWise(Date fromdate,
			Date todate, User user) {
		OrderStatusesSummary orderStatusesSummary = new OrderStatusesSummary();
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(WholesalerOrder.class, "o");
			criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			if (fromdate != null) {
				criteria.add(Restrictions.ge("createTimestamp", fromdate));
			}else {
				fromdate = (Date) criteria.setProjection(Projections.min("createTimestamp")).uniqueResult();
			}

			if (todate != null) {
				criteria.add(Restrictions.le("createTimestamp", todate));
			}else {
				todate = (Date) criteria.setProjection(Projections.max("createTimestamp")).uniqueResult();
			}
			ProjectionList projectionList = Projections
					.projectionList()
					.add(Projections.groupProperty("orderStatus"),"orderStatus")
					.add(Projections.rowCount(),"count")
					.add(Projections.min("createTimestamp"), "pastOrderDate");
			if (user instanceof Wholesaler)
				criteria.add(Restrictions.eq("user", user));
			else 
				criteria.add(Restrictions.eq("supplier", user));
			criteria.setProjection(projectionList);
			criteria.setResultTransformer(Transformers.aliasToBean(OrderStatusWithDateSummary.class));
			List<OrderStatusWithDateSummary> result =criteria.list(); 
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
