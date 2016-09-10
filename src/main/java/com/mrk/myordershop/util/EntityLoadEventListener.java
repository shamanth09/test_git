package com.mrk.myordershop.util;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostLoadEvent;
import org.hibernate.event.spi.PostLoadEventListener;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;

//@Component
public class EntityLoadEventListener implements PostLoadEventListener {
	private static final Logger log = Logger
			.getLogger(EntityLoadEventListener.class);

	private static final long serialVersionUID = 1L;

	@Autowired
	private SessionFactory sessionFactory;

	@PostConstruct
	public void registerEvent() {
		EventListenerRegistry registry = ((SessionFactoryImpl) sessionFactory)
				.getServiceRegistry().getService(EventListenerRegistry.class);
		registry.getEventListenerGroup(EventType.POST_LOAD)
				.appendListener(this);
	}

	@Override
	public void onPostLoad(PostLoadEvent event) {

		log.debug("Hibernate Load Event ================================================="
				+ event.getEntity().getClass());
	}
}
