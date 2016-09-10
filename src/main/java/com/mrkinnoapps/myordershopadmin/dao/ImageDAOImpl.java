package com.mrkinnoapps.myordershopadmin.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mrkinnoapps.myordershopadmin.bean.entity.Image;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;

@Repository
public class ImageDAOImpl implements ImageDAO {
	
	@Autowired private ProductDAO productDAO;

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(Image image) throws EntityNotPersistedException {
		Session session = sessionFactory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			session.save(image);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	@Override
	public void delete(Image image) {
		Session session = sessionFactory.openSession();
		String hql="delete from Image where id=:idd";
		String hql2="from Product  where image.id=:idd";
		Transaction transaction = session.beginTransaction();
		try {
			int size=session.createQuery(hql2).setParameter("idd",image.getId()).list().size();
			System.out.println("this image has not been used-------");
			if(size<=0)
			session.delete(image);	
			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

}
