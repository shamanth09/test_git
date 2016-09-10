package com.mrkinnoapps.myordershopadmin.util;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * SmartIdSequenceGenerator.java Naveen Mar 30, 2015
 */
public class OrderNoGenerator {
	private static final Logger log = Logger.getLogger(OrderNoGenerator.class);

	private static final String format = "%d%d%d";

	public static String generate(Class entity, Session session) {
		String generatedId = null;

		if (!entity.getSuperclass().equals(Object.class))
			entity = entity.getSuperclass();
		Long sequence = getTodayCount(entity, session);
		log.debug(entity.getSimpleName() + "row =========" + sequence);
		Calendar now = Calendar.getInstance();
		String id = String.format(format, now.get(Calendar.DATE),
				now.get(Calendar.MONTH) + 1, (now.get(Calendar.YEAR) - 2000),
				now.get(Calendar.HOUR), now.get(Calendar.MINUTE),
				now.get(Calendar.SECOND));
		if (sequence > 0)
			generatedId = id + (++sequence);
		else {
			generatedId = id + 1;
		} 
		while (isExist(generatedId, entity, session)) {
			generatedId = id + (++sequence);
		}
		return generatedId;
	}

	private static boolean isExist(String id, Class entity, Session session) {
		return ((Long) session.createCriteria(entity)
		.setProjection(Projections.count("id"))
		.add(Restrictions.like("orderNo", id,MatchMode.START)).uniqueResult())>0;
	}

	private static long getTodayCount(Class entity, Session session) {
		Date today = new Date();
		today.setHours(0);
		today.setMinutes(0);
		today.setSeconds(0);
		return (Long) session.createCriteria(entity)
				.setProjection(Projections.count("id"))
				.add(Restrictions.gt("createTimestamp", today)).uniqueResult();
	}
}
