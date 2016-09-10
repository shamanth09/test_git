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
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.mrkinnoapps.myordershopadmin.bean.constant.ActiveFlag;
import com.mrkinnoapps.myordershopadmin.bean.constant.OrderStatus;
import com.mrkinnoapps.myordershopadmin.bean.dto.SearchFilter.SearchIn;
import com.mrkinnoapps.myordershopadmin.bean.dto.SearchResource;
import com.mrkinnoapps.myordershopadmin.bean.dto.WholesalerOrderFilter;
import com.mrkinnoapps.myordershopadmin.bean.entity.Item;
import com.mrkinnoapps.myordershopadmin.bean.entity.Order;
import com.mrkinnoapps.myordershopadmin.bean.entity.Supplier;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.bean.entity.Wholesaler;
import com.mrkinnoapps.myordershopadmin.bean.entity.WholesalerOrder;
import com.mrkinnoapps.myordershopadmin.dao.component.CoalesceOrder;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;

/**
 * @author Naveen
 * 
 */
@Repository
public class WholesalerOrderDAOImpl implements WholesalerOrderDAO {

	private static Logger log = Logger.getLogger(WholesalerOrderDAOImpl.class.getName());
	@Autowired
	private SessionFactory sessionFactory;

	/*
	 * Naveen Mar 27, 2015
	 */
	@Override
	public void save(WholesalerOrder order) throws EntityDoseNotExistException {
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			session.saveOrUpdate(order);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new EntityDoseNotExistException(WholesalerOrder.class);
		} finally {
			session.close();
		}

	}

	@Override
	public void update(WholesalerOrder wholesalerOrder) {
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			session.update(wholesalerOrder);
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
	public WholesalerOrder get(int orderId, Wholesaler wholesaler) throws EntityDoseNotExistException {
		WholesalerOrder order = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(WholesalerOrder.class);
			criteria.add(Restrictions.eq("id", orderId));
			criteria.add(Restrictions.eq("user", wholesaler));
			if (criteria.list().size() < 1)
				throw new EntityDoseNotExistException();
			order = (WholesalerOrder) criteria.list().get(0);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return order;
	}

	@Override
	public WholesalerOrder get(int orderId, Supplier supplier) throws EntityDoseNotExistException {
		WholesalerOrder order = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(WholesalerOrder.class);
			criteria.add(Restrictions.eq("id", orderId));
			criteria.add(Restrictions.eq("supplier", supplier));
			if (criteria.list().size() < 1)
				throw new EntityDoseNotExistException();
			order = (WholesalerOrder) criteria.list().get(0);
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
	public List<WholesalerOrder> getByOrderNo(String orderNo, Wholesaler wholesaler) {
		List<WholesalerOrder> orders = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(WholesalerOrder.class);
			criteria.createAlias("order", "rorder");
			criteria.add(Restrictions.eq("rorder.orderNo", orderNo));
			criteria.add(Restrictions.eq("user", wholesaler));
			orders = criteria.list();
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return orders;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mrk.myordershop.dao.WholesalerOrderDAO#getByOrderNo(java.lang.String)
	 */
	@Override
	public List<WholesalerOrder> getByOrderNo(String orderNo) {
		List<WholesalerOrder> orders = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(WholesalerOrder.class);
			criteria.createAlias("order", "rorder");
			criteria.add(Restrictions.eq("rorder.orderNo", orderNo));
			orders = criteria.list();
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return orders;
	}

	@Override
	public List<WholesalerOrder> getByOrderNo(String orderNo, Supplier supplier) {
		List<WholesalerOrder> orders = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(WholesalerOrder.class);
			criteria.createAlias("order", "rorder");
			criteria.add(Restrictions.eq("rorder.orderNo", orderNo));
			criteria.add(Restrictions.eq("supplier", supplier));
			orders = criteria.list();
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return orders;
	}

	/*
	 * Naveen Mar 27, 2015
	 */
	@Override
	public WholesalerOrder getCurrentWholesalerOrderByOrderId(int orderId, Wholesaler wholesaler)
			throws EntityDoseNotExistException {
		WholesalerOrder order = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(WholesalerOrder.class);
			criteria.add(Restrictions.eq("order.id", orderId));
			criteria.add(Restrictions.eq("user", wholesaler));
			int count = criteria.list().size();
			if (count < 1)
				throw new EntityDoseNotExistException();
			if (count > 1)
				criteria.add(Restrictions.not(Restrictions.eq("orderStatus", OrderStatus.REJECTED)));
			order = (WholesalerOrder) criteria.list().get(0);
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
	public Page<WholesalerOrder> get(Pageable pageable, Wholesaler wholesaler, WholesalerOrderFilter filter) {
		Page<WholesalerOrder> page = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(WholesalerOrder.class);
			criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			criteria.add(Restrictions.eq("user.id", wholesaler.getId()));
			if (pageable != null)
				buildSort(criteria, pageable);
			buildCriteria(criteria, filter);
			int total = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
			criteria.setProjection(null);
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			if (pageable != null)
				criteria.setFirstResult(pageable.getOffset()).setMaxResults(pageable.getPageSize());

			page = new PageImpl<WholesalerOrder>(criteria.list(), pageable, total);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return page;
	}

	@Override
	public Page<WholesalerOrder> get(Pageable pageable, Supplier supplier, WholesalerOrderFilter filter) {
		Page<WholesalerOrder> page = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(WholesalerOrder.class);
			criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			criteria.add(Restrictions.eq("supplier.id", supplier.getId()));

			if (pageable != null)
				buildSort(criteria, pageable);
			buildCriteria(criteria, filter);
			int total = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
			criteria.setProjection(null);

			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			if (pageable != null)
				criteria.setFirstResult(pageable.getOffset()).setMaxResults(pageable.getPageSize());

			page = new PageImpl<WholesalerOrder>(criteria.list(), pageable, total);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return page;
	}

	private void buildCriteria(Criteria criteria, WholesalerOrderFilter filter) {
		if (filter != null && criteria != null) {
			if (filter.getOrderStatus() != null)
				criteria.add(Restrictions.in("orderStatus", filter.getOrderStatus()));
			if (filter.getFromDate() != null || filter.getToDate() != null) {
				LocalDate fDate = new LocalDate(filter.getFromDate(filter.getToDate()));
				LocalDate tDate = new LocalDate(filter.getToDate(new Date())).plusDays(1);
				criteria.add(Restrictions.between("createTimestamp", fDate.toDate(), tDate.toDate()));
			}
			if (filter.getProductName() != null) {

				DetachedCriteria itemCrit = DetachedCriteria.forClass(Item.class, "i").createAlias("i.product", "prdt")
						.add(Restrictions.disjunction()
								.add(Restrictions.like("prdt.name", filter.getProductName(), MatchMode.EXACT)))
								.setProjection(Projections.property("i.wholesalerOrder.id"));

				criteria.add(Subqueries.propertyIn("id", itemCrit));
			}

			if (filter.getSupplierId() != null) {
				criteria.createAlias("supplier", "us").add(Restrictions.eq("us.id", filter.getSupplierId()));
			}

			if (filter.getSupplierName() != null) {
				
				criteria.createAlias("supplier", "us").add(Restrictions.in("us.name", filter.getSupplierName()));
			}

			if (filter.getSupplierMobileNo() != null) {
				criteria.createAlias("supplier", "us").add(Restrictions.in("us.mobile", filter.getSupplierMobileNo()));
			}
			
			if (filter.getOrderNo() != null) {
				criteria.createAlias("order", "or").add(Restrictions.in("or.orderNo", filter.getOrderNo()));
			}

			if (filter.getUserID() != null) {
				criteria.createAlias("user", "us").add(Restrictions.eq("us.id", filter.getUserID()));
			}
		}
	}

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
				case "product":
					criteria.createAlias("wholesalerItem", "w");
					criteria.createAlias("w.product", "p", JoinType.LEFT_OUTER_JOIN,
							Restrictions.isNotNull("w.product"));
					if (order.isAscending()) {
						criteria.addOrder(CoalesceOrder.asc("productName", "p.name"));
					} else {
						criteria.addOrder(CoalesceOrder.desc("productName", "p.name"));
					}
					break;
				case "supplier":
					criteria.createAlias("supplier", "u");
					if (order.isAscending()) {
						criteria.addOrder(org.hibernate.criterion.Order.asc("u.name"));
					} else {
						criteria.addOrder(org.hibernate.criterion.Order.desc("u.name"));
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

	@Override
	public List<SearchResource> search(String query, SearchIn filterField, User user) {
		List<SearchResource> page = new ArrayList<SearchResource>();
		// Session session = sessionFactory.openSession();
		// try {
		// Transaction transaction = session.beginTransaction();
		// Criteria criteria = session.createCriteria(WholesalerOrder.class,
		// "o");
		// criteria.add(Restrictions.eq("o.activeFlag", ActiveFlag.ACTIVE));
		//
		// if (user instanceof Wholesaler)
		// criteria.add(Restrictions.eq("o.user.id", user.getId()));
		// else if (user instanceof Supplier)
		// criteria.add(Restrictions.eq("o.supplier.id", user.getId()));
		//
		// criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		//
		// WholesalerOrderSearchQueryBuilder qb = new
		// WholesalerOrderSearchQueryBuilder(
		// criteria, filterField);
		// qb.setRestrictions(query);
		//
		// criteria.addOrder(org.hibernate.criterion.Order
		// .desc("createTimestamp"));
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
	public void delete(WholesalerOrder wholesalerOrder) {
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			session.delete(wholesalerOrder);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	/* 
	 * Mallinath May 28, 2016
	 */
	@Override
	public Page<WholesalerOrder> get(Pageable pageable,
			WholesalerOrderFilter filter) {
		Page<WholesalerOrder> page = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(WholesalerOrder.class);
			if (pageable != null)
				buildSort(criteria, pageable);
			buildCriteria(criteria, filter);
			int total = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
			criteria.setProjection(null);
           System.out.println("total wholsalers-----"+total);
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			if (pageable != null)
				criteria.setFirstResult(pageable.getOffset()).setMaxResults(pageable.getPageSize());
			System.out.println("~~~`"+pageable.getOffset()+"~~~~~~"+pageable.getPageSize());

			page = new PageImpl<WholesalerOrder>(criteria.list(), pageable, total);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		System.out.println("~~~~"+page.getContent().size());
		return page;
	}

	/* 
	 * Mallinath May 30, 2016
	 */

	@Override
	public WholesalerOrder getOrder(Integer orderId) throws EntityDoseNotExistException {
		WholesalerOrder order = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(WholesalerOrder.class);
			criteria.add(Restrictions.eq("id", orderId));
			if (criteria.list().size() < 1)
				throw new EntityDoseNotExistException(WholesalerOrder.class);
			order = (WholesalerOrder) criteria.list().get(0);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return order;
	}


}
