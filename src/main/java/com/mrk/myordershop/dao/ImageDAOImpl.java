package com.mrk.myordershop.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mrk.myordershop.bean.Image;
import com.mrk.myordershop.exception.EntityNotPersistedException;

@Repository
public class ImageDAOImpl implements ImageDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(Image image) throws EntityNotPersistedException {
		Session session = sessionFactory.openSession();
		try {
			session.save(image);
		} catch (HibernateException e) {
			e.printStackTrace();
			 throw new EntityNotPersistedException(Image.class, e.getMessage());
		}  
	}

	@Override
	public void delete(Image image) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.delete(image);
		} catch (HibernateException e) {
			e.printStackTrace();
		}  
	}

}
