package com.mrkinnoapps.myordershopadmin.dao;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.model.Projection;
import com.mrkinnoapps.myordershopadmin.bean.constant.ActiveFlag;
import com.mrkinnoapps.myordershopadmin.bean.constant.Role;
import com.mrkinnoapps.myordershopadmin.bean.dto.NotRegisteredUser;
import com.mrkinnoapps.myordershopadmin.bean.dto.SearchFilter.SearchIn;
import com.mrkinnoapps.myordershopadmin.bean.dto.UserFilter;
import com.mrkinnoapps.myordershopadmin.bean.dto.UserSearchResource;
import com.mrkinnoapps.myordershopadmin.bean.entity.Order;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.bean.entity.UserRole;
import com.mrkinnoapps.myordershopadmin.bean.entity.WholesalerInstantOrder;
import com.mrkinnoapps.myordershopadmin.dao.querybuilder.UserSearchQueryBuilder;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;
import com.mrkinnoapps.myordershopadmin.util.searchengine.SearchResult;
import com.mrkinnoapps.myordershopadmin.util.searchengine.UserSearchResult;

@Repository
public class UserDaoImpl implements UserDAO {

	private static Logger log = Logger.getLogger(UserDaoImpl.class.getName());
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(User user) throws EntityNotPersistedException {

		Session session = sessionFactory.openSession();
		Transaction transaction = session.getTransaction();
		try {
			transaction.begin();
			session.save(user);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new EntityNotPersistedException(User.class, e.getCause()
					.getMessage());
		} finally {
			session.close();
		}
	}

	@Override
	public void update(User user) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.getTransaction();
		try {
			transaction.begin();
			session.update(user);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	@Override
	public User get(String id, ActiveFlag activeFlag)
			throws EntityDoseNotExistException {
		User user = null;
		Session session = sessionFactory.openSession();
		Transaction transaction = session.getTransaction();
		try {
			transaction.begin();
			Query query = session
					.createQuery("from User as u where u.id=:userId and u.activeFlag=:activeFlag");
			query.setParameter("userId", id);
			query.setParameter("activeFlag", activeFlag);
			if (query.list().size() < 1)
				throw new EntityDoseNotExistException(User.class);
			user = (User) query.list().get(0);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return user;
	}

	@Override
	public User get(String id) throws EntityDoseNotExistException {
		User user = null;
		Session session = sessionFactory.openSession();
		Transaction transaction = session.getTransaction();
		try {
			transaction.begin();
			Query query = session
					.createQuery("from User as u where u.id=:userId");
			query.setParameter("userId", id);
			if (query.list().size() < 1)
				throw new EntityDoseNotExistException(User.class);
			user = (User) query.list().get(0);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return user;
	}

	@Override
	public void saveUserRole(UserRole user) {

		Session session = sessionFactory.openSession();
		Transaction transaction = session.getTransaction();
		try {
			transaction.begin();
			session.save(user);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

	}

	@Override
	public User getUserByMobieOrEmail(String mobieOrEmail, ActiveFlag activeFlag)
			throws EntityDoseNotExistException {
		User user = null;
		Session session = sessionFactory.openSession();
		Transaction transaction = session.getTransaction();
		try {
			transaction.begin();
			Query query = session
					.createQuery("from User as u where (u.email=:email or u.mobile=:email) and u.activeFlag=:activeFlag");
			query.setParameter("email", mobieOrEmail);
			query.setParameter("activeFlag", activeFlag);
			if (query.list().size() < 1)
				throw new EntityDoseNotExistException(User.class);
			user = (User) query.list().get(0);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return user;
	}

	@Override
	public User getUserByMobieOrEmail(String mobieOrEmail)
			throws EntityDoseNotExistException {
		User user = null;
		Session session = sessionFactory.openSession();
		Transaction transaction = session.getTransaction();
		try {
			transaction.begin();
			Query query = session
					.createQuery("from User as u where (u.email=:email or u.mobile=:email)");
			query.setParameter("email", mobieOrEmail);
			if (query.list().size() < 1)
				throw new EntityDoseNotExistException(User.class);
			user = (User) query.list().get(0);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return user;
	}

	/*
	 * Naveen Apr 10, 2015
	 */
	@Override
	public List<User> getByRole(Role role) {
		List<User> users = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Query query = session
					.createQuery("select distinct user from User as user join user.userRoles as ur where ur.role in (:role)");
			query.setParameter("role", role);
			users = query.list();
			log.info("get " + role + " Size is " + users.size());
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return users;
	}

	@Override
	public List<UserSearchResource> search(Role role, String query,
			SearchIn field, User user, String[] excludeUsers) {
		List<UserSearchResource> page = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(User.class, "u");

			criteria.add(Restrictions.eq("u.activeFlag", ActiveFlag.ACTIVE));

			criteria.createAlias("userRoles", "roles");
			criteria.add(Restrictions.eq("roles.role", role));

			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			UserSearchQueryBuilder qb = new UserSearchQueryBuilder(criteria,
					field, role);
			qb.setRestrictions(query, user, excludeUsers);

			criteria.addOrder(
					org.hibernate.criterion.Order.desc("createTimestamp"))
					.setMaxResults(10);
			page = qb.list();
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return page;
	}

	@Override
	public Page<User> find(UserFilter filter, Pageable pageable, User user) {
		Page<User> page = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(User.class, "u");

			//criteria.add(Restrictions.eq("u.activeFlag", ActiveFlag.ACTIVE));

			addCriteria(criteria, filter);
			int total = ((Number) criteria
					.setProjection(Projections.rowCount()).uniqueResult())
					.intValue();
			criteria.setProjection(null);

			criteria.setMaxResults(pageable.getPageSize());
			criteria.setFirstResult(pageable.getOffset());
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			page = new PageImpl<User>(criteria.list(), pageable, total);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return page;
	}

	private void addCriteria(Criteria criteria, UserFilter filter) {

		if (filter.getEmail() != null && !filter.getEmail().equals("")) {
			criteria.add(Restrictions.in("email", filter.getEmail()));
		}
		if (filter.getMobile() != null && !filter.getMobile().equals("")) {
			criteria.add(Restrictions.in("mobile", filter.getMobile()));
		}
		if (filter.getUserName() != null && !filter.getUserName().equals("")) {
			criteria.add(Restrictions.in("name", filter.getUserName()));
		}
		if (filter.getRole() != null) {
			criteria.createAlias("userRoles", "roles");
			criteria.add(Restrictions.eq("roles.role", filter.getRole()));
		}
		if(filter.getFromDate() != null){
			criteria.add(Restrictions.ge("createTimestamp", filter.getFromDate()));
		}
		if(filter.getToDate() != null){
			criteria.add(Restrictions.le("createTimestamp", filter.getToDate()));
		}
		if(filter.getActiveFlags()!=null){
			criteria.add(Restrictions.in("activeFlag", filter.getActiveFlags()));
		}
	}

	@Override
	public void delete(User user) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.getTransaction();
		try {
			transaction.begin();
			session.delete(user);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	@Override
	public UserRole getUserRole(int id) throws EntityDoseNotExistException {
		UserRole userRole = null;
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from UserRole as r where r.id=:id");
		query.setParameter("id", id);
		if (query.list().size() < 1)
			throw new EntityDoseNotExistException();
		userRole = (UserRole) query.list().get(0);
		session.close();
		return userRole;
	}

	@Override
	public UserRole getUserRoleByRole(Role role)
			throws EntityDoseNotExistException {
		UserRole userRole = null;
		Session session = sessionFactory.openSession();
		Query query = session
				.createQuery("from UserRole as r where r.role=:role");
		query.setParameter("role", role);
		if (query.list().size() < 1)
			throw new EntityDoseNotExistException();
		userRole = (UserRole) query.list().get(0);
		session.close();
		return userRole;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserRole> getUserRoles() {
		List<UserRole> userRole;
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from UserRole");
		userRole = (List<UserRole>) query.list();
		session.close();
		return userRole;
	}

	@Override
	public void changePassword(String password, String userID) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.getTransaction();
		try {
			transaction.begin();
			Query query = session
					.createQuery("update User set password=:password where id=:userID");
			query.setParameter("userID", userID);
			query.setParameter("password", password);
			query.executeUpdate();
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SearchResult> searchUser(UserFilter filter, String q) {
		List<SearchResult> list = null;
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(User.class);

			criteria.setProjection(Projections.projectionList().add(Projections.property("name"), "result")
					.add(Projections.property("id"), "resultId"))
					.add(Restrictions.like("name", q, MatchMode.ANYWHERE));
			addCriteria(criteria, filter);
			criteria.setResultTransformer(Transformers.aliasToBean(UserSearchResult.class));
			list = (List<SearchResult>) criteria.list();
			for (SearchResult user : list) {
				UserSearchResult userSearchResult = (UserSearchResult) user;
				userSearchResult.setField("User name");
			}
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<NotRegisteredUser> getNotRegisteredUsers(Pageable pageable)
	{
		Page<NotRegisteredUser> page=null;
		List<NotRegisteredUser> objectList=null;
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			DetachedCriteria itemCrit = DetachedCriteria
					.forClass(User.class)
					.add(Restrictions.isNotNull("mobile"))
					.setProjection(Projections.property("mobile"));
			Criteria criteria= session.createCriteria(WholesalerInstantOrder.class);
			criteria.add(Restrictions.disjunction().add(
					Subqueries.propertyNotIn("customerMobile",itemCrit)));
			ProjectionList pl=Projections.projectionList();
			pl.add(Projections.groupProperty("customerMobile"));
			pl.add(Projections.property("customerMobile"),"customerMobile");
			pl.add(Projections.property("customerName"),"customerName");
			criteria.setProjection(pl);
			criteria.setMaxResults(pageable.getPageSize());
			criteria.setFirstResult(pageable.getOffset());
			criteria.setResultTransformer(Transformers.aliasToBean(NotRegisteredUser.class));
			objectList=(List<NotRegisteredUser>)criteria.list();
			criteria=null;
			criteria=session.createCriteria(WholesalerInstantOrder.class);
			criteria.add(Restrictions.disjunction().add(
					Subqueries.propertyNotIn("customerMobile",itemCrit)));
			criteria.setProjection(Projections.countDistinct("customerMobile"));
			long total=(Long)criteria.list().get(0);
			page = new PageImpl<NotRegisteredUser>(objectList, pageable, total);
			transaction.commit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
			session.close();
		}
		return page;
	}
}
