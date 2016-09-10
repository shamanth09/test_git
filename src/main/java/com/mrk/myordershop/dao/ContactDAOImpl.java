package com.mrk.myordershop.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
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

import com.mrk.myordershop.bean.Contact;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.dto.SearchFilter.SearchIn;
import com.mrk.myordershop.bean.dto.SearchResource;
import com.mrk.myordershop.bean.dto.filter.ContactFilter;
import com.mrk.myordershop.dao.querybuilder.ContactSearchQueryBuilder;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;

@Repository
public class ContactDAOImpl implements ContactDAO {

	private static final Logger log = Logger.getLogger(ContactDAOImpl.class);
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(Contact contact) throws EntityNotPersistedException {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.save(contact);
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new EntityNotPersistedException(Contact.class, e.getCause().getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<Contact> getContact(User currentUser, ContactFilter contactFilter, Pageable pageable) {
		Page<Contact> page = null;
		long total = 0;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Contact.class, "contact");
			criteria.add(Restrictions.eq("user", currentUser));
			if (contactFilter != null) {
				if (contactFilter.getName() != null && !contactFilter.getName().isEmpty()) {
					criteria.add(Restrictions.like("name", contactFilter.getName(), MatchMode.START));
				}
				if (contactFilter.getFirmName() != null && !contactFilter.getFirmName().isEmpty()) {
					criteria.add(Restrictions.like("firmName", contactFilter.getFirmName(), MatchMode.START));
				}
				if (contactFilter.getEmail() != null && !contactFilter.getEmail().isEmpty()) {
					criteria.add(Restrictions.like("email", contactFilter.getEmail(), MatchMode.START));
				}
				if (contactFilter.getMobile() != null && !contactFilter.getMobile().isEmpty()) {
					criteria.add(Restrictions.like("mobile", contactFilter.getMobile(), MatchMode.START));
				}
			}
			total = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
			criteria.setProjection(null);

			if (pageable != null) {
				criteria.setFirstResult(pageable.getOffset());
				criteria.setMaxResults(pageable.getPageSize());
			}
			List<Contact> contacts = (List<Contact>) criteria.list();
			page = new PageImpl<Contact>(contacts, pageable, total);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return page;
	}

	@Override
	public Page<Contact> findByName(Wholesaler currentUser, String name, Pageable pageable) {
		Page<Contact> page = null;
		long total = 0;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Contact.class, "contact");
			criteria.add(Restrictions.eq("user", currentUser));
			criteria.add(Restrictions.like("name", name, MatchMode.ANYWHERE));

			total = ((Number) criteria.setProjection(Projections.projectionList().add(Projections.rowCount()))
					.uniqueResult()).intValue();
			criteria.setProjection(null);

			if (pageable != null) {
				criteria.setFirstResult(pageable.getOffset());
				criteria.setMaxResults(pageable.getPageSize());
			}

			page = new PageImpl<Contact>(criteria.list(), pageable, total);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return page;
	}

	@Override
	public Page<Contact> findByMobileNo(Wholesaler currentUser, String mobileNo, Pageable pageable) {
		Page<Contact> page = null;
		long total = 0;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Contact.class);
			criteria.add(Restrictions.eq("user.id", currentUser.getId()));
			criteria.add(Restrictions.like("mobile", mobileNo, MatchMode.ANYWHERE));
			total = ((Number) criteria.setProjection(Projections.projectionList().add(Projections.rowCount()))
					.uniqueResult()).intValue();
			criteria.setProjection(null);

			criteria.setFirstResult(pageable.getOffset());
			criteria.setMaxResults(pageable.getPageSize());

			page = new PageImpl<Contact>(criteria.list(), pageable, total);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return page;
	}

	@Override
	public Contact getByMobile(String mobile) throws EntityDoseNotExistException {
		Contact contact = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Contact.class, "contact").add(Restrictions.eq("mobile", mobile));
			if (criteria.list().size() < 1)
				throw new EntityDoseNotExistException();
			contact = (Contact) criteria.list().get(0);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return contact;
	}

	@Override
	public Contact get(int id, User user) throws EntityDoseNotExistException {
		Contact contact = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Contact.class, "contact").add(Restrictions.eq("id", id))
					.add(Restrictions.eq("user", user));
			if (criteria.list().size() < 1)
				throw new EntityDoseNotExistException();
			contact = (Contact) criteria.list().get(0);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return contact;
	}

	@Override
	public Contact getByName(String name) throws EntityDoseNotExistException {
		Contact contact = null;
		Session session = sessionFactory.openSession();
		try {
			Criteria criteria = session.createCriteria(Contact.class, "contact").add(Restrictions.eq("name", name));
			if (criteria.list().size() < 1)
				throw new EntityDoseNotExistException();
			contact = (Contact) criteria.list().get(0);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return contact;
	}

	@Override
	public void update(Contact contact) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.update(contact);
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new EntityNotPersistedException(Contact.class, e.getMessage());
		}
	}

	@Override
	public void delete(Contact contact) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.delete(contact);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<SearchResource> search(String query, SearchIn searchIn, User user) {
		List<SearchResource> list = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Contact.class, "c");
			ContactSearchQueryBuilder builder = new ContactSearchQueryBuilder(criteria, searchIn);
			builder.setRestrictions(query, user);
			list = builder.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return list;
	}
}
