package com.mrk.myordershop.util;

import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HibernateSessionFactory {

	private static final SessionFactory sessionFactory;

	private HibernateSessionFactory() {
	}

	static {
		@SuppressWarnings("resource")
		ApplicationContext appContext = new ClassPathXmlApplicationContext(
				"META-INF/spring/application-context.xml");
		sessionFactory = appContext.getBean(SessionFactory.class);
	}

	public SessionFactory getSesstionFactory() {
		return sessionFactory;
	}
}
