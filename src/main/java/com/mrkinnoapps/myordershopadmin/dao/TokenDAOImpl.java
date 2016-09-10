package com.mrkinnoapps.myordershopadmin.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mrkinnoapps.myordershopadmin.bean.constant.ActiveFlag;
import com.mrkinnoapps.myordershopadmin.bean.entity.Token;
import com.mrkinnoapps.myordershopadmin.bean.entity.Token.Type;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;

@Repository
public class TokenDAOImpl implements TokenDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(Token token) throws EntityNotPersistedException {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.save(token);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			transaction.rollback();
			throw new EntityNotPersistedException(Token.class);
		} finally {
			session.close();
		}

	}

	@Override
	public Token get(Integer id) throws EntityDoseNotExistException {
		Token token = null;
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Token.class);
			criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			criteria.add(Restrictions.eq("id", id));
			List<Token> list = criteria.list();
			if (list.size() < 1)
				throw new EntityDoseNotExistException(Token.class);
			token = list.get(0);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			transaction.rollback();
		} finally {
			session.close();
		}
		return token;
	}

	@Override
	public Token getByUserAndType(String userId, Type type)
			throws EntityDoseNotExistException {
		Token token = null;
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Token.class);
			criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			criteria.createAlias("user", "u");
			criteria.add(Restrictions.eq("u.id", userId));
			criteria.add(Restrictions.eq("type", type));
			List<Token> list = criteria.list();
			if (list.size() < 1)
				throw new EntityDoseNotExistException(Token.class);
			token = list.get(0);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			transaction.rollback();
		} finally {
			session.close();
		}
		return token;
	}

	@Override
	public List<Token> findByUser(String userId) {
		List<Token> tokens = null;
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Token.class);
			criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			criteria.createAlias("user", "u");
			criteria.add(Restrictions.eq("u.id", userId));
			tokens = criteria.list();
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			transaction.rollback();
		} finally {
			session.close();
		}
		return tokens;
	}

	@Override
	public Token getByToken(String tokenStr) throws EntityDoseNotExistException {
		Token token = null;
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Token.class);
			criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			criteria.add(Restrictions.eq("token", tokenStr));
			List<Token> list = criteria.list();
			if (list.size() < 1)
				throw new EntityDoseNotExistException(Token.class);
			token = list.get(0);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			transaction.rollback();
		} finally {
			session.close();
		}
		return token;
	}

	@Override
	public void update(Token token) throws EntityNotPersistedException {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.update(token);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			transaction.rollback();
			throw new EntityNotPersistedException(Token.class);
		} finally {
			session.close();
		}
	}

	@Override
	public void delete(Token token) throws EntityNotPersistedException {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.delete(token);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			transaction.rollback();
			throw new EntityNotPersistedException(Token.class);
		} finally {
			session.close();
		}
	}

}
