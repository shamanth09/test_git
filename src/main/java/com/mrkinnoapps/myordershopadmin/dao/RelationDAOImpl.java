package com.mrkinnoapps.myordershopadmin.dao;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Repository;

import com.mrkinnoapps.myordershopadmin.bean.constant.RelationDirection;
import com.mrkinnoapps.myordershopadmin.bean.constant.RelationStatus;
import com.mrkinnoapps.myordershopadmin.bean.constant.Role;
import com.mrkinnoapps.myordershopadmin.bean.dto.RelationFilter;
import com.mrkinnoapps.myordershopadmin.bean.dto.SearchFilter.SearchIn;
import com.mrkinnoapps.myordershopadmin.bean.dto.UserSearchResource;
import com.mrkinnoapps.myordershopadmin.bean.entity.Relation;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;

@Repository
public class RelationDAOImpl implements RelationDAO {

	private static Logger log = Logger.getLogger(RelationDAO.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(Relation relation) {
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			session.save(relation);
			session.refresh(relation);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	@Override
	public void update(Relation relation) {
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			session.update(relation);
			transaction.commit();
			session.refresh(relation);
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	@Override
	public void delete(Relation relation) {
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			session.delete(relation);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	@Override
	public Relation get(Long id, User currentUser)
			throws EntityDoseNotExistException {
		Relation relation = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Relation.class, "r");
			criteria.createAlias("primaryUser", "pUser").createAlias(
					"secondaryUser", "sUser");
			criteria.add(Restrictions.eq("r.id", id));
			criteria.add(Restrictions.disjunction()
					.add(Restrictions.eq("pUser.id", currentUser.getId()))
					.add(Restrictions.eq("sUser.id", currentUser.getId())));
			if (criteria.list().size() < 1)
				throw new EntityDoseNotExistException();
			relation = (Relation) criteria.list().get(0);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return relation;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Relation getByPrimaryAndSecondaryUserId(String primaryUserId,
			String secondaryUserId) throws EntityDoseNotExistException {
		Relation relation = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Relation.class);
			criteria.createAlias("primaryUser", "pUser").createAlias(
					"secondaryUser", "sUser");
			criteria.add(Restrictions.conjunction()
					.add(Restrictions.eq("pUser.id", primaryUserId))
					.add(Restrictions.eq("sUser.id", secondaryUserId)));

			java.util.List<Relation> relations = criteria.list();
			if (relations.size() < 1)
				throw new EntityDoseNotExistException(Relation.class);
			relation = relations.get(0);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return relation;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Relation getByUsers(String userId1, String userId2)
			throws EntityDoseNotExistException {
		Relation relation = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Relation.class);
			criteria.createAlias("primaryUser", "pUser").createAlias(
					"secondaryUser", "sUser");
			criteria.add(Restrictions
					.disjunction()
					.add(Restrictions.conjunction()
							.add(Restrictions.eq("pUser.id", userId1))
							.add(Restrictions.eq("sUser.id", userId2)))
					.add(Restrictions.conjunction()
							.add(Restrictions.eq("pUser.id", userId2))
							.add(Restrictions.eq("sUser.id", userId1))));

			List<Relation> relations = criteria.list();
			if (relations.size() < 1)
				throw new EntityDoseNotExistException(Relation.class);
			relation = relations.get(0);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return relation;
	}

	@Override
	public Page<Relation> findByPrimaryOrSecondaryUserId(String userId,
			Pageable pageable, RelationFilter filter) {
		Page<Relation> pages = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Relation.class);
			criteria.createAlias("primaryUser", "pUser").createAlias(
					"secondaryUser", "sUser");
			criteria.add(Restrictions.disjunction()
					.add(Restrictions.eq("pUser.id", userId))
					.add(Restrictions.eq("sUser.id", userId)));

			if (filter != null)
				addFilter(criteria, filter, userId);
			int total = ((Number) criteria
					.setProjection(Projections.rowCount()).uniqueResult())
					.intValue();

			criteria.setProjection(null);
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			if (pageable != null) {
				if (pageable.getSort() != null) {
					Iterator<Order> orders = pageable.getSort().iterator();
					while (orders.hasNext()) {
						Order order = orders.next();
						switch (order.getProperty()) {
						case "createDate":
							if (order.isAscending())
								criteria.addOrder(org.hibernate.criterion.Order
										.asc("createTimestamp"));
							else
								criteria.addOrder(org.hibernate.criterion.Order
										.desc("createTimestamp"));
							break;
						default:
							criteria.addOrder(org.hibernate.criterion.Order
									.desc("createTimeStamp"));
							break;
						}
					}
				} else {
					criteria.addOrder(org.hibernate.criterion.Order
							.desc("createTimeStamp"));
				}
				criteria.setMaxResults(pageable.getPageSize()).setFirstResult(
						pageable.getOffset());
			}

			pages = new PageImpl<Relation>(criteria.list(), pageable, total);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return pages;
	}

	private void addFilter(Criteria criteria, RelationFilter filter,
			String currentUserId) {

		if (filter.getStatus() != null)
			criteria.add(Restrictions.eq("status", filter.getStatus()));
		if (filter.getUserRole() != null) {
			criteria.createAlias("pUser.userRoles", "pRoll").createAlias(
					"sUser.userRoles", "sRoll");
			criteria.add(Restrictions
					.disjunction()
					.add(Restrictions
							.conjunction()
							.add(Restrictions.eq("pRoll.role",
									filter.getUserRole()))
							.add(Restrictions.eq("sUser.id", currentUserId)))
					.add(Restrictions
							.conjunction()
							.add(Restrictions.eq("sRoll.role",
									filter.getUserRole()))
							.add(Restrictions.eq("pUser.id", currentUserId))));
		}
		if (filter.getName() != null) {
			criteria.add(Restrictions
					.disjunction()
					.add(Restrictions
							.conjunction()
							.add(Restrictions.like("pUser.name",
									filter.getName(), MatchMode.ANYWHERE))
							.add(Restrictions.eq("sUser.id", currentUserId)))
					.add(Restrictions
							.conjunction()
							.add(Restrictions.like("sUser.name",
									filter.getName(), MatchMode.ANYWHERE))
							.add(Restrictions.eq("pUser.id", currentUserId))));
		}
		if (filter.getEmail() != null) {
			criteria.add(Restrictions
					.disjunction()
					.add(Restrictions
							.conjunction()
							.add(Restrictions.eq("pUser.email",
									filter.getEmail()))
							.add(Restrictions.eq("sUser.id", currentUserId)))
					.add(Restrictions
							.conjunction()
							.add(Restrictions.eq("sUser.email",
									filter.getEmail()))
							.add(Restrictions.eq("pUser.id", currentUserId))));
		}
		if (filter.getMobile() != null) {
			criteria.add(Restrictions
					.disjunction()
					.add(Restrictions
							.conjunction()
							.add(Restrictions.eq("pUser.mobile",
									filter.getMobile()))
							.add(Restrictions.eq("sUser.id", currentUserId)))
					.add(Restrictions
							.conjunction()
							.add(Restrictions.eq("sUser.mobile",
									filter.getMobile()))
							.add(Restrictions.eq("pUser.id", currentUserId))));
		}

		if (filter.getDirection() != null) {
			if (filter.getDirection().equals(RelationDirection.FROM_YOU)) {
				criteria.add(Restrictions.eq("pUser.id", currentUserId));
			} else if (filter.getDirection().equals(RelationDirection.TO_YOU)) {
				criteria.add(Restrictions.eq("sUser.id", currentUserId));
			}
		}
	}

	@Override
	public List<UserSearchResource> search(Role role, String query,
			SearchIn field, User user) {
		List<UserSearchResource> page = null;
		// Session session = sessionFactory.openSession();
		// try {
		// Transaction transaction = session.beginTransaction();
		// Criteria criteria = session.createCriteria(Relation.class, "u");
		//
		// criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		//
		// RelationSearchQueryBuilder qb = new RelationSearchQueryBuilder(
		// criteria, field, role);
		// qb.setRestrictions(query, user);
		//
		// page = qb.list(user);
		// log.debug(role + "=" + field + "=" + user.getId() + "=" + query);
		// log.debug(page.size());
		// transaction.commit();
		// } catch (HibernateException e) {
		// e.printStackTrace();
		// } finally {
		// session.close();
		// }
		return page;
	}

	@Override
	public int getRelatonCount(User user, Role role,
			RelationStatus RelationStatus) {
		int total = 0;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Relation.class, "r");
			criteria.createAlias("primaryUser", "pUser")
					.createAlias("secondaryUser", "sUser")
					.createAlias("pUser.userRoles", "pRoll")
					.createAlias("sUser.userRoles", "sRoll");
			criteria.add(Restrictions.eq("status", RelationStatus));
			criteria.add(Restrictions
					.disjunction()
					.add(Restrictions.conjunction()
							.add(Restrictions.eq("pRoll.role", role))
							.add(Restrictions.eq("secondaryUser", user)))
					.add(Restrictions.conjunction()
							.add(Restrictions.eq("sRoll.role", role))
							.add(Restrictions.eq("primaryUser", user))));
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
