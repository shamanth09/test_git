package com.mrkinnoapps.myordershopadmin.dao;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.mrkinnoapps.myordershopadmin.bean.constant.ActiveFlag;
import com.mrkinnoapps.myordershopadmin.bean.constant.OrderStatus;
import com.mrkinnoapps.myordershopadmin.bean.dto.OrderFilter;
import com.mrkinnoapps.myordershopadmin.bean.dto.SearchFilter.SearchIn;
import com.mrkinnoapps.myordershopadmin.bean.dto.SearchResource;
import com.mrkinnoapps.myordershopadmin.bean.entity.Item;
import com.mrkinnoapps.myordershopadmin.bean.entity.Order;
import com.mrkinnoapps.myordershopadmin.bean.entity.Retailer;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.bean.entity.Wholesaler;
import com.mrkinnoapps.myordershopadmin.bean.entity.WholesalerOrder;
import com.mrkinnoapps.myordershopadmin.dao.component.CoalesceOrder;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.util.OrderNoGenerator;

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
	public Order saveOrder(Order order) {
		Integer id=null;
		 Session session = sessionFactory.openSession();
		 try {
		 Transaction transaction = session.beginTransaction();
		 order.setOrderNo(OrderNoGenerator.generate(order.getClass(),
		 session));
		  id=(Integer)session.save(order);
		  order.setId(id);
		 transaction.commit();
		 } catch (HibernateException e) {
		 e.printStackTrace();
		 } finally {
		 session.close();
		 }
    return order;
	}

	@Override
	public void update(Order order) {
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			session.update(order);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	/*
	 * Naveen Mar 27, 2015
	 */
	@Override
	public Order getOrder(int orderId, User user)
			throws EntityDoseNotExistException {
		Order order = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Order.class);
			criteria.add(Restrictions.eq("id", orderId));
			if (user instanceof Wholesaler)
				criteria.add(Restrictions.eq("wholesaler.id", user.getId()));
			else if (user instanceof Retailer) {
				criteria.add(Restrictions
						.disjunction()
						.add(Restrictions.eq("user.id", user.getId()))
						.add(Restrictions.eq("customerMobile", user.getMobile())));
			}
			if (criteria.list().size() < 1)
				throw new EntityDoseNotExistException(Order.class);
			order = (Order) criteria.list().get(0);
			if (user instanceof Wholesaler)
				try {
					WholesalerOrder o = wholesalerOrderDAO
							.getCurrentWholesalerOrderByOrderId(order.getId(),
									(Wholesaler) user);
					order.setCurrentWholesalerOrder(o);
				} catch (EntityDoseNotExistException e) {
				}
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
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
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Order.class);
			criteria.add(Restrictions.eq("id", orderId));
			if (criteria.list().size() < 1)
				throw new EntityDoseNotExistException(Order.class);
			order = (Order) criteria.list().get(0);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return order;
	}

	/*
	 * Naveen Apr 8, 2015
	 */
	@Override
	public Order getOrder(String orderNo, User user)
			throws EntityDoseNotExistException {
		Order order = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Order.class);
			criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			criteria.add(Restrictions.eq("orderNo", orderNo));

			if (user instanceof Wholesaler)
				criteria.add(Restrictions.eq("wholesaler.id", user.getId()));
			else if (user instanceof Retailer) {
				criteria.add(Restrictions
						.disjunction()
						.add(Restrictions.eq("user.id", user.getId()))
						.add(Restrictions.eq("customerMobile", user.getMobile())));
			}

			if (criteria.list().size() < 1)
				throw new EntityDoseNotExistException();
			order = (Order) criteria.list().get(0);
			if (user instanceof Wholesaler)
				try {
					WholesalerOrder o = wholesalerOrderDAO
							.getCurrentWholesalerOrderByOrderId(order.getId(),
									(Wholesaler) user);
					order.setCurrentWholesalerOrder(o);
				} catch (EntityDoseNotExistException e) {
				}
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return order;
	}

	@Override
	public Page<Order> getOrders(Pageable pageable, Retailer retailer,
			OrderFilter filter) {
		Page<Order> page = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Order.class);
			// criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			criteria.add(Restrictions
					.disjunction()
					.add(Restrictions.eq("user.id", retailer.getId()))
					.add(Restrictions.eq("customerMobile", retailer.getMobile())));
			if (pageable != null) {
				buildSort(criteria, pageable);
			}
			buildCriteria(criteria, filter, retailer);

			int total = ((Number) criteria
					.setProjection(Projections.rowCount()).uniqueResult())
					.intValue();
			criteria.setProjection(null);
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			if (pageable != null) {
				criteria.setFirstResult(pageable.getOffset());
				criteria.setMaxResults(pageable.getPageSize());
			}

			page = new PageImpl<Order>(criteria.list(), pageable, total);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return page;
	}

	@Override
	public Page<Order> getOrders(Pageable pageable, OrderFilter filter) {
		Page<Order> page = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Order.class);
			if (pageable != null) {
				buildSort(criteria, pageable);
			}
			buildCriteria(criteria, filter);

			int total = ((Number) criteria
					.setProjection(Projections.rowCount()).uniqueResult())
					.intValue();

			criteria.setProjection(null);
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			if (pageable != null) {
				criteria.setFirstResult(pageable.getOffset());
				criteria.setMaxResults(pageable.getPageSize());
			}

			page = new PageImpl<Order>(criteria.list(), pageable, total);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return page;
	}

	private void buildCriteria(Criteria criteria, OrderFilter filter) {

		if (filter != null && criteria != null) {

			if (filter.getOrderStatus() != null) {
				criteria.add(Restrictions.in("orderStatus",
						filter.getOrderStatus()));
			}
			if (filter.getFromDate() != null || filter.getToDate() != null) {
				LocalDate fDate = new LocalDate(filter.getFromDate(filter
						.getToDate()));
				LocalDate tDate = new LocalDate(filter.getToDate(new Date()))
				.plusDays(1);
				criteria.add(Restrictions.between("createTimestamp",
						fDate.toDate(), tDate.toDate()));
			}
			if (filter.getFromExpectedDate() != null
					|| filter.getToExpectedDate() != null) {
				LocalDate fDate = new LocalDate(
						filter.getFromExpectedDate(filter.getToDate()));
				LocalDate tDate = new LocalDate(
						filter.getToExpectedDate(new Date())).plusDays(1);
				criteria.add(Restrictions
						.disjunction()
						.add(Restrictions.between("expectedDate",
								fDate.toDate(), tDate.toDate()))
								.add(Restrictions
										.conjunction()
										.add(Restrictions.isNull("expectedDate"))
										.add(Restrictions.between("createTimestamp",
												fDate.minusDays(10).toDate(), tDate
												.minusDays(10).toDate()))));
			}
			
			if (filter.getUserName() != null) {

				DetachedCriteria itemCrit = DetachedCriteria
						.forClass(Order.class, "o")
						.createAlias("o.user", "u")
						.add(Restrictions.disjunction().add(
								Restrictions.in("u.name",
										filter.getUserName()
										)))
										.setProjection(Projections.property("o.id"));

				criteria.add(Restrictions.disjunction().add(
						Subqueries.propertyIn("id", itemCrit)));
			}

			if (filter.getProductName() != null) {

				DetachedCriteria itemCrit = DetachedCriteria
						.forClass(Item.class, "i")
						.createAlias("i.product", "prdt")
						.add(Restrictions.disjunction().add(
								Restrictions.in("prdt.name",
										filter.getProductName()
										)))
										.setProjection(Projections.property("i.order.id"));

				criteria.add(Restrictions.disjunction().add(
						Subqueries.propertyIn("id", itemCrit)));
			}

			if (filter.getProductId() != null) {

				DetachedCriteria itemCrit = DetachedCriteria
						.forClass(Item.class, "i")
						.createAlias("i.product", "prdt")
						.add(Restrictions.disjunction().add(
								Restrictions.eq("prdt.id",
										filter.getProductId())))
										.setProjection(Projections.property("i.order.id"));

				criteria.add(Restrictions.disjunction().add(
						Subqueries.propertyIn("id", itemCrit)));
			}

			if (filter.getCustomerName() != null) {
				criteria.add(Restrictions.in("customerName",filter.getCustomerName()));
			}

			if (filter.getCustomerMobile() != null) {
				criteria.add(Restrictions.in("customerMobile",filter.getCustomerMobile()));
			}

			/*
			 * if (filter.getRetailerId() != null) {
			 * 
			 * DetachedCriteria userCrit = DetachedCriteria
			 * .forClass(Order.class, "ord") .createAlias("ord.user", "us")
			 * .add(Restrictions.eq("us.id", filter.getRetailerId()))
			 * .add(Restrictions.not(Restrictions.eq("us.id",
			 * wholesaler.getId())))
			 * .setProjection(Projections.property("ord.id"));
			 * 
			 * criteria.add(Restrictions.disjunction() //
			 * .add(Subqueries.propertyIn("id", ic))
			 * .add(Subqueries.propertyIn("id", userCrit))); }
			 */

			if (filter.getCategoryId() != null) {
				criteria.createAlias("item", "i")
				.createAlias("i.product", "prdt")
				.createAlias("prdt.category", "cat");
				criteria.add(Restrictions.eq("cat.id", filter.getCategoryId()));
			}

		}
	}

	/*
	 * Naveen Apr 8, 2015
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Page<Order> getOrders(Pageable pageable, Wholesaler wholesaler,
			OrderFilter filter) {
		Page<Order> page = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Order.class, "o");
			criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			criteria.add(Restrictions.eq("wholesaler", wholesaler));
			// criteria.setFetchMode("wholesalerOrders", FetchMode.JOIN);
			if (pageable != null)
				buildSort(criteria, pageable);
			buildCriteria(criteria, filter, wholesaler);

			int total = ((Number) criteria
					.setProjection(Projections.rowCount()).uniqueResult())
					.intValue();
			criteria.setProjection(null);
			if (pageable != null)
				criteria.setMaxResults(pageable.getPageSize()).setFirstResult(
						pageable.getOffset());
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			List<Order> orders = criteria.list();
			for (Order order : orders) {
				try {
					WholesalerOrder o = wholesalerOrderDAO
							.getCurrentWholesalerOrderByOrderId(order.getId(),
									wholesaler);
					order.setCurrentWholesalerOrder(o);
				} catch (EntityDoseNotExistException e) {
				}
			}
			page = new PageImpl<Order>(orders, pageable, total);
			log.debug("total = " + total + " result = "
					+ criteria.list().size());
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return page;
	}

	@Override
	public Page<Order> deliversBy(Date date, Pageable pageable,
			Wholesaler wholesaler) {
		Page<Order> page = null;
		Session session = sessionFactory.openSession();
		try {
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Order.class, "o");
			criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			criteria.add(Restrictions.eq("wholesaler", wholesaler));

			Criterion beforeDate = Restrictions
					.conjunction()
					.add(Restrictions.le("expectedDate", date))
					.add(Restrictions.not(Restrictions
							.in("orderStatus",
									new OrderStatus[] { OrderStatus.DELIVERED,
									OrderStatus.CANCELLED,
									OrderStatus.REJECTED })));

			Criterion afterDate = Restrictions
					.conjunction()
					.add(Restrictions.gt("expectedDate", date))
					.add(Restrictions.not(Restrictions
							.in("orderStatus",
									new OrderStatus[] { OrderStatus.DELIVERED,
									OrderStatus.CANCELLED,
									OrderStatus.REJECTED })))
									.add(Restrictions.eq("orderStatus", OrderStatus.AVAILABLE));

			Criterion beforeDateOfCreate = Restrictions
					.conjunction()
					.add(Restrictions.isNull("expectedDate"))
					.add(Restrictions.le("createTimestamp", new LocalDate(date)
					.minusDays(10).toDate()))
					.add(Restrictions.not(Restrictions
							.in("orderStatus",
									new OrderStatus[] { OrderStatus.DELIVERED,
									OrderStatus.CANCELLED,
									OrderStatus.REJECTED })));

			Criterion afterDateOfCreate = Restrictions
					.conjunction()
					.add(Restrictions.isNull("expectedDate"))
					.add(Restrictions.gt("createTimestamp", new LocalDate(date)
					.minusDays(10).toDate()))
					.add(Restrictions.not(Restrictions
							.in("orderStatus",
									new OrderStatus[] { OrderStatus.DELIVERED,
									OrderStatus.CANCELLED,
									OrderStatus.REJECTED })))
									.add(Restrictions.eq("orderStatus", OrderStatus.AVAILABLE));

			criteria.add(Restrictions.disjunction().add(beforeDate)
					.add(beforeDateOfCreate).add(afterDate)
					.add(afterDateOfCreate));
			criteria.addOrder(org.hibernate.criterion.Order
					.desc("createTimestamp"));
			int total = ((Number) criteria
					.setProjection(Projections.rowCount()).uniqueResult())
					.intValue();
			criteria.setProjection(null);

			if (pageable != null)
				criteria.setMaxResults(pageable.getPageSize()).setFirstResult(
						pageable.getOffset());
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			page = new PageImpl<Order>(criteria.list(), pageable, total);

			session.getTransaction().commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
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
			Iterator<org.springframework.data.domain.Sort.Order> i = pageable
					.getSort().iterator();
			while (i.hasNext()) {
				org.springframework.data.domain.Sort.Order order = i.next();
				log.debug("order page sort==" + order.getProperty()
						+ " dir.as=" + order.getDirection());
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

	private void buildCriteria(Criteria criteria, OrderFilter filter,
			User wholesaler) {
		if (filter != null && criteria != null) {

			if (filter.getOrderStatus() != null) {
				criteria.add(Restrictions.in("orderStatus",
						filter.getOrderStatus()));
			}
			if (filter.getFromDate() != null || filter.getToDate() != null) {
				LocalDate fDate = new LocalDate(filter.getFromDate(filter
						.getToDate()));
				LocalDate tDate = new LocalDate(filter.getToDate(new Date()))
				.plusDays(1);
				criteria.add(Restrictions.between("createTimestamp",
						fDate.toDate(), tDate.toDate()));
			}
			if (filter.getFromExpectedDate() != null
					|| filter.getToExpectedDate() != null) {
				LocalDate fDate = new LocalDate(
						filter.getFromExpectedDate(filter.getToDate()));
				LocalDate tDate = new LocalDate(
						filter.getToExpectedDate(new Date())).plusDays(1);
				criteria.add(Restrictions
						.disjunction()
						.add(Restrictions.between("expectedDate",
								fDate.toDate(), tDate.toDate()))
								.add(Restrictions
										.conjunction()
										.add(Restrictions.isNull("expectedDate"))
										.add(Restrictions.between("createTimestamp",
												fDate.minusDays(10).toDate(), tDate
												.minusDays(10).toDate()))));
			}

			if (filter.getProductName() != null) {

				/*DetachedCriteria itemCrit = DetachedCriteria
						.forClass(Item.class, "i")
						.createAlias("i.product", "prdt")
						.add(Restrictions.disjunction().add(
								Restrictions.like("prdt.name",
										filter.getProductName(),
										MatchMode.EXACT)))
										.setProjection(Projections.property("i.order.id"));

				criteria.add(Restrictions.disjunction().add(
						Subqueries.propertyIn("id", itemCrit)));*/
			}

			if (filter.getCustomerName() != null) {

				/*DetachedCriteria ic1 = DetachedCriteria
						.forClass(WholesalerInstantOrder.class, "iwo")
						.add(Restrictions.like("iwo.customerName",
								filter.getCustomerName(), MatchMode.START))
						.setProjection(Projections.property("iwo.id"));

				DetachedCriteria userCrit = DetachedCriteria
						.forClass(Order.class, "ord")
						.createAlias("ord.user", "us")
						.add(Restrictions.like("us.name",
								filter.getCustomerName(), MatchMode.START))
						.add(Restrictions.not(Restrictions.eq("us.id",
								wholesaler.getId())))
						.setProjection(Projections.property("ord.id"));

				criteria.add(Restrictions.disjunction()
						// .add(Subqueries.propertyIn("id", ic))
						.add(Subqueries.propertyIn("id", ic1))
						.add(Subqueries.propertyIn("id", userCrit)));*/
			}

			if (filter.getCustomerMobile() != null) {

				/*DetachedCriteria ic1 = DetachedCriteria
						.forClass(WholesalerInstantOrder.class, "iwo")
						.add(Restrictions.like("iwo.customerMobile",
								filter.getCustomerMobile(), MatchMode.START))
						.setProjection(Projections.property("iwo.id"));

				DetachedCriteria userCrit = DetachedCriteria
						.forClass(Order.class, "ord")
						.createAlias("ord.user", "us")
						.add(Restrictions.like("us.mobile",
								filter.getCustomerMobile(), MatchMode.START))
						.add(Restrictions.not(Restrictions.eq("us.id",
								wholesaler.getId())))
						.setProjection(Projections.property("ord.id"));

				criteria.add(Restrictions.disjunction()
						// .add(Subqueries.propertyIn("id", ic))
						.add(Subqueries.propertyIn("id", ic1))
						.add(Subqueries.propertyIn("id", userCrit)));*/
			}

			if (filter.getRetailerId() != null) {

				DetachedCriteria userCrit = DetachedCriteria
						.forClass(Order.class, "ord")
						.createAlias("ord.user", "us")
						.add(Restrictions.eq("us.id", filter.getRetailerId()))
						.add(Restrictions.not(Restrictions.eq("us.id",
								wholesaler.getId())))
								.setProjection(Projections.property("ord.id"));

				criteria.add(Restrictions.disjunction()
						// .add(Subqueries.propertyIn("id", ic))
						.add(Subqueries.propertyIn("id", userCrit)));
			}

			if (filter.getCategoryId() != null) {
				criteria.createAlias("item", "i")
				.createAlias("i.product", "prdt")
				.createAlias("prdt.category", "cat");
				criteria.add(Restrictions.eq("cat.id", filter.getCategoryId()));
			}

		}
	}

	@Override
	public List<SearchResource> search(String query, SearchIn searchIn,
			User user) {
		List<SearchResource> page = null;
		// Session session = sessionFactory.openSession();
		// try {
		// Transaction transaction = session.beginTransaction();
		// Criteria criteria = session.createCriteria(Order.class, "o");
		//
		// criteria.add(Restrictions.eq("o.activeFlag", ActiveFlag.ACTIVE));
		// if (user instanceof Wholesaler)
		// criteria.add(Restrictions.eq("o.wholesaler.id", user.getId()));
		// else if (user instanceof Retailer) {
		// criteria.add(Restrictions
		// .disjunction()
		// .add(Restrictions.eq("o.user.id", user.getId()))
		// .add(Restrictions.eq("o.customerMobile",
		// user.getMobile())));
		// // check without mobileno
		// // criteria.add(Restrictions.eq("o.user.id", user.getId()));
		// }
		//
		// criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		//
		// OrderSearchQueryBuilder qb = new OrderSearchQueryBuilder(criteria,
		// searchIn);
		// if (user instanceof Wholesaler)
		// qb.setRestrictions(query, (Wholesaler) user);
		// else if (user instanceof Retailer)
		// qb.setRestrictions(query, (Retailer) user);
		// criteria.addOrder(
		// org.hibernate.criterion.Order.desc("createTimestamp"))
		// .setMaxResults(10);
		// page = qb.list();
		// transaction.commit();
		// } catch (HibernateException e) {
		// e.printStackTrace();
		// } finally {
		// session.close();
		// }
		return page;
	}

	@Override
	public void delete(Order order) {
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			session.delete(order);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	@Override
	public User getUser(Integer orderId) {
		String hql = "select o.user from Order o where o.id=:id1";
		Session session = sessionFactory.openSession();
		User user = null;
		try {
			Transaction transaction = session.beginTransaction();
			user = (User) session.createQuery(hql).setParameter("id1", orderId)
					.list().get(0);
			transaction.commit();
		} catch (HibernateException e) {
			System.out.println("OrderDAOImpl.getUser()Exception");
			e.printStackTrace();
		} finally {
			session.close();
		}
		return user;
	}

	@Override
	public Integer getOrderCount(User user) {
		Session session = sessionFactory.openSession();
		Integer total = 0;
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Order.class);

			if (user instanceof Wholesaler)
				criteria.add(Restrictions.eq("wholesaler.id", user.getId()));
			else if (user instanceof Retailer) {
				criteria.add(Restrictions
						.disjunction()
						.add(Restrictions.eq("user.id", user.getId()))
						.add(Restrictions.eq("customerMobile", user.getMobile())));
			}
			total = ((Number) criteria.setProjection(Projections.rowCount())
					.uniqueResult()).intValue();
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return total;
	}
}
