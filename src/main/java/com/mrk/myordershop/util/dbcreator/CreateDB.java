package com.mrk.myordershop.util.dbcreator;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * CreateDB.java Naveen Apr 11, 2015
 */
@Component
public class CreateDB {
	private static final Logger log = Logger.getLogger(CreateDB.class);
	@Autowired
	private UserRoleCreate userRoleCreate;
	@Autowired
	private ProductAndCategoryCreate nameAndTypeCreate;
	@Autowired
	private CatalogCreate catalogCreate;
	@Autowired
	private MeasurementCreate measurementCreate;

	/**
	 * 
	 */
	public void init() {
		Properties properties = new Properties();
		try {
			properties.load(this.getClass().getResourceAsStream(
					"/META-INF/db.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (properties.get("mos.createDb") != null
				&& properties.get("mos.createDb").equals("true")) {
			log.debug("createDb is enabled");
			userRoleCreate.create();
			measurementCreate.create();
//			nameAndTypeCreate.create(false);
			if (properties.get("mos.createDb.catalogImagePath") != null
					&& !properties.get("mos.createDb.catalogImagePath").equals(
							"")) {
				catalogCreate.create(properties
						.getProperty("mos.createDb.catalogImagePath"));

			}
		}
	}

	public static void main(String[] args) {
		ApplicationContext appContext = new ClassPathXmlApplicationContext(
				"META-INF/spring/application-context.xml");
		appContext.getBean(CreateDB.class);
	}
}
