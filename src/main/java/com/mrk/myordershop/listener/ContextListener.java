package com.mrk.myordershop.listener;

import java.util.Date;
import java.util.TimeZone;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.joda.time.DateTimeZone;

public class ContextListener implements ServletContextListener {
	private static final Logger log = Logger.getLogger(ContextListener.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		TimeZone.setDefault(TimeZone.getTimeZone("IST"));
		DateTimeZone.setDefault(DateTimeZone.forTimeZone(TimeZone
				.getTimeZone("IST")));
		log.debug("context init==" + new Date());
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}
}
