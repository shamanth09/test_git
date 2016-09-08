package com.mrk.myordershop.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.UserRole;
import com.mrk.myordershop.bean.dto.SearchFilter.SearchIn;
import com.mrk.myordershop.bean.dto.UserFilter;
import com.mrk.myordershop.bean.dto.UserSearchResource;
import com.mrk.myordershop.constant.ActiveFlag;
import com.mrk.myordershop.constant.Role;
import com.mrk.myordershop.dao.querybuilder.UserSearchQueryBuilder;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;

@Repository
public class UserDaoImpl implements UserDAO {

	private static Logger log = Logger.getLogger(UserDaoImpl.class.getName());
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(User user) throws EntityNotPersistedException {

		Session session = sessionFactory.getCurrentSession();
		try {
			session.save(user);
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new EntityNotPersistedException(User.class, e.getCause().getMessage());
		}
	}

	@Override
	public void update(User user) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.update(user);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}

	@Override
	public User get(String id, ActiveFlag activeFlag) throws EntityDoseNotExistException {
		User user = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Query query = session.createQuery("from User as u where u.id=:userId and u.activeFlag=:activeFlag");
			query.setParameter("userId", id);
			query.setParameter("activeFlag", activeFlag);
			if (query.list().size() < 1)
				throw new EntityDoseNotExistException(User.class);
			user = (User) query.list().get(0);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public User get(String id) throws EntityDoseNotExistException {
		User user = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Query query = session.createQuery("from User as u where u.id=:userId");
			query.setParameter("userId", id);
			if (query.list().size() < 1)
				throw new EntityDoseNotExistException(User.class);
			user = (User) query.list().get(0);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public void saveUserRole(UserRole user) {

		Session session = sessionFactory.getCurrentSession();
		try {
			session.save(user);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}

	@Override
	public User getUserByMobieOrEmail(String mobieOrEmail, ActiveFlag activeFlag) throws EntityDoseNotExistException {
		User user = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Query query = session.createQuery(
					"from User as u where (u.email=:email or u.mobile=:email) and u.activeFlag=:activeFlag");
			query.setParameter("email", mobieOrEmail);
			query.setParameter("activeFlag", activeFlag);
			if (query.list().size() < 1)
				throw new EntityDoseNotExistException(User.class);
			user = (User) query.list().get(0);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public User getUserByMobieOrEmail(String mobieOrEmail) throws EntityDoseNotExistException {
		User user = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Query query = session.createQuery("from User as u where (u.email=:email or u.mobile=:email)");
			query.setParameter("email", mobieOrEmail);
			if (query.list().size() < 1)
				throw new EntityDoseNotExistException(User.class);
			user = (User) query.list().get(0);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public UserRole getUserRole(Role role) throws EntityDoseNotExistException {
		UserRole userRole = null;
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from UserRole as r where r.role=:role");
		query.setParameter("role", role);
		if (query.list().size() < 1)
			throw new EntityDoseNotExistException();
		userRole = (UserRole) query.list().get(0);
		return userRole;
	}

	/*
	 * Naveen Apr 10, 2015
	 */
	@Override
	public List<User> getByRole(Role role) {
		List<User> users = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Query query = session.createQuery(
					"select distinct user from User as user join user.userRoles as ur where ur.role in (:role)");
			query.setParameter("role", role);
			users = query.list();
			log.info("get " + role + " Size is " + users.size());
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return users;
	}

	@Override
	public List<UserSearchResource> search(Role role, String query, SearchIn field, User user, String[] excludeUsers) {
		List<UserSearchResource> page = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(User.class, "u");

			criteria.add(Restrictions.eq("u.activeFlag", ActiveFlag.ACTIVE));

			criteria.createAlias("userRoles", "roles");
			criteria.add(Restrictions.eq("roles.role", role));

			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			UserSearchQueryBuilder qb = new UserSearchQueryBuilder(criteria, field, role);
			qb.setRestrictions(query, user, excludeUsers);

			criteria.addOrder(org.hibernate.criterion.Order.desc("createTimestamp")).setMaxResults(10);
			page = qb.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return page;
	}

	@Override
	public Page<User> find(UserFilter filter, Pageable pageable, User user) {
		Page<User> page = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(User.class, "u");

			criteria.add(Restrictions.eq("u.activeFlag", ActiveFlag.ACTIVE));

			addCriteria(criteria, filter);
			int total = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
			criteria.setProjection(null);

			criteria.setMaxResults(pageable.getPageSize());
			criteria.setFirstResult(pageable.getOffset());
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			page = new PageImpl<User>(criteria.list(), pageable, total);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return page;
	}

	private void addCriteria(Criteria criteria, UserFilter filter) {
		if (filter.getEmail() != null && !filter.getEmail().equals("")) {
			criteria.add(Restrictions.like("email", filter.getEmail().trim(), MatchMode.ANYWHERE));
		}
		if (filter.getMobile() != null && !filter.getMobile().equals("")) {
			criteria.add(Restrictions.like("mobile", filter.getMobile().trim(), MatchMode.ANYWHERE));
		}
		if (filter.getName() != null && !filter.getName().equals("")) {
			criteria.add(Restrictions.like("name", filter.getName().trim(), MatchMode.ANYWHERE));
		}
		if (filter.getRole() != null) {
			criteria.createAlias("userRoles", "roles");
			criteria.add(Restrictions.eq("roles.role", filter.getRole()));
		}
	}

	@Override
	public void delete(User user) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.delete(user);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}
}
