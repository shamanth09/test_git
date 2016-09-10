package com.mrk.myordershop.util.dbcreator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.mrk.myordershop.bean.Measurement;
import com.mrk.myordershop.config.annotation.PersistTransactional;
import com.mrk.myordershop.dao.MeasurementDAO;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

/**
 * RetailerCreate.java Naveen Apr 7, 2015
 */
@Component
public class MeasurementCreate {

	@Autowired
	private MeasurementDAO measurementDAO;

	public MeasurementCreate() {
	}

	@PersistTransactional
	public void create() {
		for (Measurement.v measurement : Measurement.v.values()) {
			try {
				measurementDAO.get(measurement);
			} catch (EntityDoseNotExistException e) {
				Measurement m = new Measurement(measurement);
				measurementDAO.save(m);
			}
		}
	}

	public static void main(String arg[]) throws EntityDoseNotExistException {
		ApplicationContext appContext = new ClassPathXmlApplicationContext("META-INF/spring/application-context.xml");
		MeasurementCreate measurementCreate = appContext.getBean(MeasurementCreate.class);
		measurementCreate.create();
	}
}
