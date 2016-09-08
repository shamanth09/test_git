package com.mrk.myordershop.dao;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.mrk.myordershop.bean.Item;
import com.mrk.myordershop.bean.Order;
import com.mrk.myordershop.bean.Retailer;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.WholesalerInstantOrder;
import com.mrk.myordershop.bean.WholesalerOrder;
import com.mrk.myordershop.bean.dto.OrderFilter;
import com.mrk.myordershop.bean.dto.SearchFilter.SearchIn;
import com.mrk.myordershop.bean.dto.SearchResource;
import com.mrk.myordershop.constant.ActiveFlag;
import com.mrk.myordershop.constant.OrderStatus;
import com.mrk.myordershop.dao.component.CoalesceOrder;
import com.mrk.myordershop.dao.querybuilder.OrderSearchQueryBuilder;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;
import com.mrk.myordershop.util.OrderNoGenerator;

/**
 * @author Naveen
 * 
 */
@Repository
public class OrderDAOImpl implements OrderDAO {

	private static Logger log = Logger.getLogger(OrderDAOImpl.class.getName());
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private WholesalerOrderDAO wholesalerOrderDAO;

	/*
	 * Naveen Mar 27, 2015
	 */
	@Override
	public void save(Order order) throws EntityNotPersistedException {
		Session session = sessionFactory.getCurrentSession();
		try {
			order.setOrderNo(OrderNoGenerator.generate(order.getClass(), session));
			session.save(order);
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new EntityNotPersistedException(Order.class, e.getMessage());
		}

	}

	@Override
	public void update(Order order) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.update(order);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Naveen Mar 27, 2015
	 */
	@Override
	public Order getOrder(int orderId, User user) throws EntityDoseNotExistException {
		Order order = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Order.class);
			criteria.add(Restrictions.eq("id", orderId));
			if (user instanceof Wholesaler)
				criteria.add(Restrictions.eq("wholesaler.id", user.getId()));
			else if (user instanceof Retailer) {
				criteria.add(Restrictions.disjunction().add(Restrictions.eq("user.id", user.getId()))
						.add(Restrictions.eq("referralUser.id", user.getId())));
			}
			if (criteria.list().size() < 1)
				throw new EntityDoseNotExistException(Order.class);
			order = (Order) criteria.list().get(0);
			if (user instanceof Wholesaler)
				try {
					WholesalerOrder o = wholesalerOrderDAO.getCurrentWholesalerOrderByOrderId(order.getId(),
							(Wholesaler) user);
					order.setCurrentWholesalerOrder(o);
				} catch (EntityDoseNotExistException e) {
				}
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return order;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mrk.myordershop.dao.OrderDAO#getOrder(int,
	 * com.mrk.myordershop.bean.User)
	 */
	@Override
	public Order getOrder(int orderId) throws EntityDoseNotExistException {
		Order order = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Order.class);
			criteria.add(Restrictions.eq("id", orderId));
			if (criteria.list().size() < 1)
				throw new EntityDoseNotExistException(Order.class);
			order = (Order) criteria.list().get(0);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return order;
	}

	@Override
	public Page<Order> getOrders(Pageable pageable, User retailer, OrderFilter filter) {
		Page<Order> page = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Order.class);
			criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			if (retailer instanceof Retailer) {
				criteria.add(Restrictions.disjunction().add(Restrictions.eq("user.id", retailer.getId()))
						.add(Restrictions.eq("referralUser.id", retailer.getId())));
			} else if (retailer instanceof Wholesaler) {
				criteria.add(Restrictions.eq("wholesaler", retailer));
			}
			if (pageable != null) {
				buildSort(criteria, pageable);
			}
			buildCriteria(criteria, filter, retailer);

			int total = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();

			criteria.setProjection(null);
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			if (pageable != null) {
				criteria.setFirstResult(pageable.getOffset());
				criteria.setMaxResults(pageable.getPageSize());
			}

			List<Order> orders = criteria.list();
			if (retailer instanceof Wholesaler) {
				Wholesaler wholesaler = (Wholesaler) retailer;
				for (Order order : orders) {
					try {
						WholesalerOrder o = wholesalerOrderDAO.getCurrentWholesalerOrderByOrderId(order.getId(),
								wholesaler);
						order.setCurrentWholesalerOrder(o);
					} catch (EntityDoseNotExistException e) {
					}
				}
			}
			page = new PageImpl<Order>(orders, pageable, total);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return page;
	}

	@Override
	public Page<Order> deliversBy(Date date, Pageable pageable, Wholesaler wholesaler) {
		Page<Order> page = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Order.class, "o");
			criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			criteria.add(Restrictions.eq("wholesaler", wholesaler));

			Criterion beforeDate = Restrictions.conjunction().add(Restrictions.le("expectedDate", date))
					.add(Restrictions.not(Restrictions.in("orderStatus",
							new OrderStatus[] { OrderStatus.DELIVERED, OrderStatus.CANCELLED, OrderStatus.REJECTED })));

			Criterion afterDate = Restrictions.conjunction().add(Restrictions.gt("expectedDate", date))
					.add(Restrictions
							.not(Restrictions.in("orderStatus",
									new OrderStatus[] { OrderStatus.DELIVERED, OrderStatus.CANCELLED,
											OrderStatus.REJECTED })))
					.add(Restrictions.eq("orderStatus", OrderStatus.AVAILABLE));

			Criterion beforeDateOfCreate = Restrictions.conjunction().add(Restrictions.isNull("expectedDate"))
					.add(Restrictions.le("createTimestamp", new LocalDate(date).minusDays(10).toDate()))
					.add(Restrictions.not(Restrictions.in("orderStatus",
							new OrderStatus[] { OrderStatus.DELIVERED, OrderStatus.CANCELLED, OrderStatus.REJECTED })));

			Criterion afterDateOfCreate = Restrictions.conjunction().add(Restrictions.isNull("expectedDate"))
					.add(Restrictions.gt("createTimestamp", new LocalDate(date).minusDays(10).toDate()))
					.add(Restrictions
							.not(Restrictions.in("orderStatus",
									new OrderStatus[] { OrderStatus.DELIVERED, OrderStatus.CANCELLED,
											OrderStatus.REJECTED })))
					.add(Restrictions.eq("orderStatus", OrderStatus.AVAILABLE));

			criteria.add(Restrictions.disjunction().add(beforeDate).add(beforeDateOfCreate).add(afterDate)
					.add(afterDateOfCreate));
			criteria.addOrder(org.hibernate.criterion.Order.desc("createTimestamp"));
			int total = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
			criteria.setProjection(null);

			if (pageable != null)
				criteria.setMaxResults(pageable.getPageSize()).setFirstResult(pageable.getOffset());
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			page = new PageImpl<Order>(criteria.list(), pageable, total);

		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return page;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mrk.myordershop.dao.OrderDAO#getOrderSummary(org.springframework.
	 * data. domain.Pageable, com.mrk.myordershop.bean.Wholesaler)
	 */

	private void buildSort(Criteria criteria, Pageable pageable) {
		if (pageable.getSort() != null) {
			Iterator<org.springframework.data.domain.Sort.Order> i = pageable.getSort().iterator();
			while (i.hasNext()) {
				org.springframework.data.domain.Sort.Order order = i.next();
				log.debug("order page sort==" + order.getProperty() + " dir.as=" + order.getDirection());
				switch (order.getProperty()) {
				case "createDate":
					if (order.isAscending())
						criteria.addOrder(org.hibernate.criterion.Order.asc("createTimestamp"));
					else
						criteria.addOrder(org.hibernate.criterion.Order.desc("createTimestamp"));
					break;
				case "updateDate":
					if (order.isAscending())
						criteria.addOrder(org.hibernate.criterion.Order.asc("updateTimestamp"));
					else
						criteria.addOrder(org.hibernate.criterion.Order.desc("updateTimestamp"));
					break;
				case "status":
					if (order.isAscending())
						criteria.addOrder(org.hibernate.criterion.Order.asc("orderStatus"));
					else
						criteria.addOrder(org.hibernate.criterion.Order.desc("orderStatus"));
					break;
				case "customer":
					criteria.createAlias("referralUser", "u");
					if (order.isAscending()) {
						criteria.addOrder(CoalesceOrder.asc("customerName", "u.name"));
					} else {
						criteria.addOrder(CoalesceOrder.desc("customerName", "u.name"));
					}
					break;
				default:
					criteria.addOrder(org.hibernate.criterion.Order.desc("createTimestamp"));
					break;
				}

			}
		} else
			criteria.addOrder(org.hibernate.criterion.Order.desc("createTimestamp"));
	}

	private void buildCriteria(Criteria criteria, OrderFilter filter, User wholesaler) {
		if (filter != null && criteria != null) {

			if (filter.getOrderStatus() != null)
				criteria.add(Restrictions.eqOrIsNull("orderStatus", filter.getOrderStatus()));
			if (filter.getWholesalerId() != null)
				criteria.add(Restrictions.eq("refferalUser.id", filter.getWholesalerId()));
			if (filter.getFromDate() != null || filter.getToDate() != null) {
				LocalDate fDate = new LocalDate(filter.getFromDate(filter.getToDate()));
				LocalDate tDate = new LocalDate(filter.getToDate(new Date())).plusDays(1);
				criteria.add(Restrictions.between("createTimestamp", fDate.toDate(), tDate.toDate()));
			}
			if (filter.getFromExpectedDate() != null || filter.getToExpectedDate() != null) {
				LocalDate fDate = new LocalDate(filter.getFromExpectedDate(filter.getToDate()));
				LocalDate tDate = new LocalDate(filter.getToExpectedDate(new Date())).plusDays(1);
				criteria.add(Restrictions.disjunction()
						.add(Restrictions.between("expectedDate", fDate.toDate(), tDate.toDate()))
						.add(Restrictions.conjunction().add(Restrictions.isNull("expectedDate"))
								.add(Restrictions.between("createTimestamp", fDate.minusDays(10).toDate(),
										tDate.minusDays(10).toDate()))));
			}

			if (filter.getProductName() != null) {

				DetachedCriteria itemCrit = DetachedCriteria.forClass(Item.class, "i").createAlias("i.product", "prdt")
						.add(Restrictions.disjunction()
								.add(Restrictions.like("prdt.name", filter.getProductName(), MatchMode.EXACT)))
						.setProjection(Projections.property("i.order.id"));

				criteria.add(Restrictions.disjunction().add(Subqueries.propertyIn("id", itemCrit)));
			}

			if (filter.getCustomerName() != null) {

				DetachedCriteria ic1 = DetachedCriteria.forClass(WholesalerInstantOrder.class, "iwo")
						.add(Restrictions.like("iwo.customerName", filter.getCustomerName(), MatchMode.START))
						.setProjection(Projections.property("iwo.id"));

				DetachedCriteria userCrit = DetachedCriteria.forClass(Order.class, "ord").createAlias("ord.referralUser", "us")
						.add(Restrictions.like("us.name", filter.getCustomerName(), MatchMode.START))
						.add(Restrictions.not(Restrictions.eq("us.id", wholesaler.getId())))
						.setProjection(Projections.property("ord.id"));

				criteria.add(Restrictions.disjunction()
						// .add(Subqueries.propertyIn("id", ic))
						.add(Subqueries.propertyIn("id", ic1)).add(Subqueries.propertyIn("id", userCrit)));
			}

			if (filter.getCustomerMobile() != null) {

				DetachedCriteria ic1 = DetachedCriteria.forClass(WholesalerInstantOrder.class, "iwo")
						.add(Restrictions.like("iwo.customerMobile", filter.getCustomerMobile(), MatchMode.START))
						.setProjection(Projections.property("iwo.id"));

				DetachedCriteria userCrit = DetachedCriteria.forClass(Order.class, "ord").createAlias("ord.referralUser", "us")
						.add(Restrictions.like("us.mobile", filter.getCustomerMobile(), MatchMode.START))
						.add(Restrictions.not(Restrictions.eq("us.id", wholesaler.getId())))
						.setProjection(Projections.property("ord.id"));

				criteria.add(Restrictions.disjunction()
						// .add(Subqueries.propertyIn("id", ic))
						.add(Subqueries.propertyIn("id", ic1)).add(Subqueries.propertyIn("id", userCrit)));
			}

			if (filter.getRetailerId() != null) {

				DetachedCriteria userCrit = DetachedCriteria.forClass(Order.class, "ord").createAlias("ord.user", "us")
						.add(Restrictions.eq("us.id", filter.getRetailerId()))
						.add(Restrictions.not(Restrictions.eq("us.id", wholesaler.getId())))
						.setProjection(Projections.property("ord.id"));

				criteria.add(Restrictions.disjunction()
						// .add(Subqueries.propertyIn("id", ic))
						.add(Subqueries.propertyIn("id", userCrit)));
			}

			if (filter.getCategoryId() != null) {
				criteria.createAlias("item", "i").createAlias("i.product", "prdt").createAlias("prdt.category", "cat");
				criteria.add(Restrictions.eq("cat.id", filter.getCategoryId()));
			}

		}
	}

	@Override
	public List<SearchResource> search(String query, SearchIn searchIn, User user) {
		List<SearchResource> page = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Order.class, "o");

			criteria.add(Restrictions.eq("o.activeFlag", ActiveFlag.ACTIVE));
			if (user instanceof Wholesaler)
				criteria.add(Restrictions.eq("o.wholesaler.id", user.getId()));
			else if (user instanceof Retailer) {
				criteria.add(Restrictions.disjunction()
//				.add(Restrictions.eq("o.user.id", user.getId()))
						 .add(Restrictions.eq("o.customerMobile", user.getMobile()))
						.add(Restrictions.eq("o.referralUser.id", user.getId())));
				// check without mobileno
				// criteria.add(Restrictions.eq("o.user.id", user.getId()));
			}

			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			OrderSearchQueryBuilder qb = new OrderSearchQueryBuilder(criteria, searchIn);
			if (user instanceof Wholesaler)
				qb.setRestrictions(query, (Wholesaler) user);
			else if (user instanceof Retailer)
				qb.setRestrictions(query, (Retailer) user);
			criteria.addOrder(org.hibernate.criterion.Order.desc("createTimestamp")).setMaxResults(10);
			page = qb.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return page;
	}

	@Override
	public void delete(Order order) {
		Session session = sessionFactory.getCurrentSession();
		try {
			order.setOrderNo(OrderNoGenerator.generate(order.getClass(), session));
			session.delete(order);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateCustomerNumber(String oldMobile, String newMobile) {
		Session session = sessionFactory.getCurrentSession();
		try {
			Query query = session
					.createQuery(
							"UPDATE WholesalerInstantOrder as wsInst SET wsInst.customerMobile=:newMobile WHERE customerMobile=:oldMobile")
					.setParameter("oldMobile", oldMobile)
					.setParameter("newMobile", newMobile);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void updateCustomerNumberAndUserReference(String newMobile, User user) {
		Session session = sessionFactory.getCurrentSession();
		try {
			Query query = session
					.createQuery(
							"UPDATE WholesalerInstantOrder as wsInst SET wsInst.customerMobile=:newMobile, wsInst.referralUser=:referralUser WHERE wsInst.customerMobile=:oldMobile AND wsInst.referralUser IS NULL")
					.setParameter("newMobile", newMobile)
					.setParameter("oldMobile", user.getMobile())
					.setParameter("referralUser", user);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

