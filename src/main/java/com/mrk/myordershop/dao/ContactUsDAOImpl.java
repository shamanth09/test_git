package com.mrk.myordershop.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mrk.myordershop.bean.ContactUs;
import com.mrk.myordershop.exception.EntityNotPersistedException;

/**
 * ContactUsDAOImpl.java Naveen Apr 4, 2015
 */
@Repository
public class ContactUsDAOImpl implements ContactUsDAO {

	@Autowired
	private SessionFactory sessionFactory;

	/*
	 * Naveen Apr 4, 2015
	 */
	@Override
	public void saveContactUs(ContactUs contactUs) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.saveOrUpdate(contactUs);
		} catch (HibernateException e) {
			e.printStackTrace();
			 throw new EntityNotPersistedException(ContactUs.class, e.getMessage());
		}  
	}

}
