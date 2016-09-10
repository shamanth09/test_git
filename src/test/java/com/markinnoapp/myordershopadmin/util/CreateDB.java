package com.markinnoapp.myordershopadmin.util;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;


/**
 * CreateDB.java Naveen Apr 11, 2015
 */
public class CreateDB {
	private static final Logger log = Logger.getLogger(CreateDB.class);
	@Autowired
	private DataSource dataSourcse;

	@PostConstruct
	public void firstExecute() throws SQLException, DatabaseUnitException, FileNotFoundException, IOException
	{
		System.out.println("creating database--------------------------");
		InputStream inputStream = getClass().getResourceAsStream("common.xml");
        Reader reader = new InputStreamReader(inputStream);
        FlatXmlDataSet dataset = new FlatXmlDataSet(reader);

		Connection connection=dataSourcse.getConnection();
		try{
			IDatabaseConnection conn = new DatabaseConnection( connection );
			DatabaseOperation.CLEAN_INSERT.execute(conn, dataset);
		}
		finally{
			DataSourceUtils.releaseConnection(connection, dataSourcse);
		}

	}

	@PreDestroy
	public void lastExecute() throws Exception
	{
		System.out.println("destroing everything ------------------------");
		InputStream inputStream = getClass().getResourceAsStream("common.xml");
        Reader reader = new InputStreamReader(inputStream);
        FlatXmlDataSet dataset = new FlatXmlDataSet(reader);
		
		Connection connection=dataSourcse.getConnection();
		try{
			IDatabaseConnection conn = new DatabaseConnection( connection );
			DatabaseOperation.DELETE_ALL.execute(conn, dataset);
		}
		finally{
			DataSourceUtils.releaseConnection(connection, dataSourcse);
		}
	}
}
