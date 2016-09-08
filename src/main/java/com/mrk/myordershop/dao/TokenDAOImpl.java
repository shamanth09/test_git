package com.mrk.myordershop.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mrk.myordershop.bean.Token;
import com.mrk.myordershop.bean.Token.Type;
import com.mrk.myordershop.constant.ActiveFlag;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;

@Repository
public class TokenDAOImpl implements TokenDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(Token token) throws EntityNotPersistedException {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.save(token);
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new EntityNotPersistedException(Token.class,e.getMessage());
		}  
	}

	@Override
	public Token get(Integer id) throws EntityDoseNotExistException {
		Token token = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Token.class);
			criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			criteria.add(Restrictions.eq("id", id));
			List<Token> list = criteria.list();
			if (list.size() < 1)
				throw new EntityDoseNotExistException(Token.class);
			token = list.get(0);
		} catch (HibernateException e) {
			e.printStackTrace();
		}  
		return token;
	}

	@Override
	public Token getByUserAndType(String userId, Type type)
			throws EntityDoseNotExistException {
		Token token = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Token.class);
			criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			criteria.createAlias("user", "u");
			criteria.add(Restrictions.eq("u.id", userId));
			criteria.add(Restrictions.eq("type", type));
			List<Token> list = criteria.list();
			if (list.size() < 1)
				throw new EntityDoseNotExistException(Token.class);
			token = list.get(0);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return token;
	}

	@Override
	public List<Token> findByUser(String userId) {
		List<Token> tokens = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Token.class);
			criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			criteria.createAlias("user", "u");
			criteria.add(Restrictions.eq("u.id", userId));
			tokens = criteria.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		} 
		return tokens;
	}

	@Override
	public Token getByToken(String tokenStr) throws EntityDoseNotExistException {
		Token token = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Token.class);
			criteria.add(Restrictions.eq("activeFlag", ActiveFlag.ACTIVE));
			criteria.add(Restrictions.eq("token", tokenStr));
			List<Token> list = criteria.list();
			if (list.size() < 1)
				throw new EntityDoseNotExistException(Token.class);
			token = list.get(0);
		} catch (HibernateException e) {
			e.printStackTrace();
		}  
		return token;
	}

	@Override
	public void update(Token token) throws EntityNotPersistedException {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.update(token);
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new EntityNotPersistedException(Token.class,e.getMessage());
		}  
	}

	@Override
	public void delete(Token token) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.delete(token);
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new EntityNotPersistedException(Token.class);
		}  
	}
}
