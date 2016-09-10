package com.mrk.myordershop.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import com.mrk.myordershop.bean.Supplier;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.WholesalerOrder;
import com.mrk.myordershop.bean.dto.BasicOrderStatusWiseSummary;
import com.mrk.myordershop.bean.dto.CustomerFilter;
import com.mrk.myordershop.bean.dto.OrderStatusSummary;
import com.mrk.myordershop.bean.dto.OrderStatusWithDateSummary;
import com.mrk.myordershop.bean.dto.OrderStatusesSummary;
import com.mrk.myordershop.bean.dto.OrderSummaryByCategory;
import com.mrk.myordershop.bean.dto.OrderSummaryByUser;
import com.mrk.myordershop.bean.dto.OrderWeightMonthSummary;
import com.mrk.myordershop.constant.ActiveFlag;
import com.mrk.myordershop.constant.OrderStatus;

@Repository
public class WholesalerOrderSummaryDAOImpl implements WholesalerOrderSummaryDAO {

	private static Logger log = Logger.getLogger(WholesalerOrderSummaryDAOImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public int getTotalQuantity(User user, OrderStatus[] statuses) {
		Integer result = 0;
		Session session = sessionFactory.getCurrentSession();
		try {
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
		Criteria criteria = session.createCriteria(WholesalerOrder.class, "o")
				.createAlias("supplier", "s").createAlias("item", "i")
				.createAlias("i.detail", "de");

		criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
		if (user instanceof Wholesaler)
			criteria.add(Restrictions.eq("user", user));
		else
			criteria.add(Restrictions.eq("supplier", user));
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
	public OrderWeightMonthSummary getTotalWeightByMonth(Date fromDate,
			Date toDate, User user, OrderStatus[] statuses) {
		OrderWeightMonthSummary result = new OrderWeightMonthSummary();
		result.setFromDate(fromDate);
		result.setToDate(toDate);
		Session session = sessionFactory.getCurrentSession();
		try {
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
			Criteria criteria = session.createCriteria(WholesalerOrder.class,
					"o");
			if (toDate != null)
				criteria.add(Restrictions.le("createTimestamp", toDate));
			criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			if (user instanceof Wholesaler)
				criteria.add(Restrictions.eq("user", user));
			else
				criteria.add(Restrictions.eq("supplier", user));

			criteria.setProjection(Projections.min("createTimestamp"));
			if (criteria.uniqueResult() != null)
				lastDate = (Date) criteria.uniqueResult();
			criteria.setProjection(Projections
					.projectionList()
					.add(Projections.alias(Projections.groupProperty(groupBy),
							"groupBy")).add(Projections.rowCount()));

			result = criteria.list();

			for (OrderStatus os : OrderStatus.getWholesalerStatuses()) {
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
			Pageable pageable, User currentUser) {
		Page<OrderSummaryByCategory> page = null;
		Session session = sessionFactory.getCurrentSession();
		try {
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
		} catch (HibernateException e) {
			e.printStackTrace();
		}  
		return result;
	}

	@Override
	public Page<OrderSummaryByUser> getOrderWholesalerWiseSummary(
			Pageable pageable, Supplier supplier) {
		Page<OrderSummaryByUser> page = null;
		Session session = sessionFactory.getCurrentSession();
		try {
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
			log.error("total orders:" + total + " but actual :"
					+ (total = criteria.list().size()));// with out query
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
		} catch (HibernateException e) {
			e.printStackTrace();
		} 
		return page;
	}

	@Override
	public Page<OrderSummaryByUser> getOrderSummaryGroupBySupplier(
			Pageable pageable, CustomerFilter filter, Wholesaler wholesaler) {
		Page<OrderSummaryByUser> page = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(WholesalerOrder.class,
					"o");
			ProjectionList projectionList = Projections.projectionList();
			criteria.add(Restrictions.eq("o.activeFlag", ActiveFlag.ACTIVE));
			criteria.add(Restrictions.eq("o.user.id", wholesaler.getId()));
			criteria.createAlias("o.supplier", "s");
			if (filter != null) {
				if (filter.getName() != null && !filter.getName().isEmpty()) {
					criteria.add(Restrictions.eq("s.name", filter.getName()));
				}
				if (filter.getEmail() != null && !filter.getEmail().isEmpty()) {
					criteria.add(Restrictions.eq("s.email", filter.getEmail()));
				}
				if (filter.getMobile() != null && !filter.getMobile().isEmpty()) {
					criteria.add(Restrictions.eq("s.mobile", filter.getMobile()));
				}
				if (filter.getOrderStatus() != null) {
					criteria.add(Restrictions.eq("o.orderStatus",
							filter.getOrderStatus()));
				}
			}
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			projectionList.add(Projections.groupProperty("s.id"));
			projectionList.add(Projections.property("s.name"));
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
			} else if (pageable.getSort() != null && pageable.getSort().getOrderFor("createDate") != null) {
				org.springframework.data.domain.Sort.Order orderBy = pageable
						.getSort().getOrderFor("createDate");
				criteria.addOrder(Direction.ASC.equals(orderBy.getDirection()) ? org.hibernate.criterion.Order
						.asc("o.createTimestamp")
						: org.hibernate.criterion.Order
								.desc("o.createTimestamp"));
			} else {
				criteria.addOrder(org.hibernate.criterion.Order.desc("crts"));
			}
			log.error("total orders:" + total + " but actual :"
					+ (total = criteria.list().size()));// with out query
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
		} catch (HibernateException e) {
			e.printStackTrace();
		} 
		return page;
	}

	@Override
	public OrderStatusSummary getOrderStatusSummary(String userId,
			Wholesaler wholesaler, OrderStatus orderStatus) {
		OrderStatusSummary orderStatusSummary = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(WholesalerOrder.class,
					"o");
			criteria.add(Restrictions.eq("o.activeFlag", ActiveFlag.ACTIVE));
			criteria.add(Restrictions.eq("o.user.id", wholesaler.getId()));
			criteria.add(Restrictions.eq("o.supplier.id", userId));
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
