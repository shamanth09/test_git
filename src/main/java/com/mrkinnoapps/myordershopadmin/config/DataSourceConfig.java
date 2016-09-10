package com.mrkinnoapps.myordershopadmin.config;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;

import com.mrkinnoapps.myordershopadmin.bean.entity.Address;
import com.mrkinnoapps.myordershopadmin.bean.entity.Cancellation;
import com.mrkinnoapps.myordershopadmin.bean.entity.Catalog;
import com.mrkinnoapps.myordershopadmin.bean.entity.CatalogDesign;
import com.mrkinnoapps.myordershopadmin.bean.entity.Category;
import com.mrkinnoapps.myordershopadmin.bean.entity.Contact;
import com.mrkinnoapps.myordershopadmin.bean.entity.Detail;
import com.mrkinnoapps.myordershopadmin.bean.entity.Device;
import com.mrkinnoapps.myordershopadmin.bean.entity.Field;
import com.mrkinnoapps.myordershopadmin.bean.entity.Image;
import com.mrkinnoapps.myordershopadmin.bean.entity.InstantOrder;
import com.mrkinnoapps.myordershopadmin.bean.entity.InstantWholesalerOrder;
import com.mrkinnoapps.myordershopadmin.bean.entity.Item;
import com.mrkinnoapps.myordershopadmin.bean.entity.LoginHistory;
import com.mrkinnoapps.myordershopadmin.bean.entity.Measurement;
import com.mrkinnoapps.myordershopadmin.bean.entity.MeltingAndSeal;
import com.mrkinnoapps.myordershopadmin.bean.entity.Notification;
import com.mrkinnoapps.myordershopadmin.bean.entity.OauthAccessToken;
import com.mrkinnoapps.myordershopadmin.bean.entity.OauthRefreshToken;
import com.mrkinnoapps.myordershopadmin.bean.entity.Order;
import com.mrkinnoapps.myordershopadmin.bean.entity.Product;
import com.mrkinnoapps.myordershopadmin.bean.entity.Relation;
import com.mrkinnoapps.myordershopadmin.bean.entity.Retailer;
import com.mrkinnoapps.myordershopadmin.bean.entity.Supplier;
import com.mrkinnoapps.myordershopadmin.bean.entity.Token;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.bean.entity.UserRole;
import com.mrkinnoapps.myordershopadmin.bean.entity.Wholesaler;
import com.mrkinnoapps.myordershopadmin.bean.entity.WholesalerInstantOrder;
import com.mrkinnoapps.myordershopadmin.bean.entity.WholesalerOrder;

@Configuration
@PropertySource("classpath:/META-INF/db.properties")
public class DataSourceConfig {

	@Value("${jdbc.driverClassName}")
	private String driverClassName;

	@Value("${jdbc.url}")
	private String url;

	@Value("${jdbc.username}")
	private String username;

	@Value("${jdbc.password}")
	private String password;

	@Bean
	public DataSource dataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		return dataSource;
	}

	@Autowired
	@Bean(name = "sessionFactory")
	public SessionFactory getSessionFactory(DataSource dataSource) {

		LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);

		sessionBuilder.addAnnotatedClasses(Address.class, Cancellation.class, Catalog.class, CatalogDesign.class,
				Category.class, Contact.class, Detail.class, Device.class, Field.class, Image.class, InstantOrder.class,
				InstantWholesalerOrder.class, Item.class, LoginHistory.class, Measurement.class, MeltingAndSeal.class,
				Notification.class, Order.class, Product.class, Relation.class, Retailer.class, Supplier.class,
				Token.class, User.class, UserRole.class, Wholesaler.class, WholesalerInstantOrder.class,
				WholesalerOrder.class, OauthAccessToken.class, OauthRefreshToken.class);

		sessionBuilder.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
		sessionBuilder.setProperty("hibernate.format_sql", "false");
		sessionBuilder.setProperty("hibernate.show_sql", "true");
		sessionBuilder.setProperty("hibernate.hbm2ddl.auto", "update");

		sessionBuilder.setProperty("hibernate.c3p0.min_size", "5");
		sessionBuilder.setProperty("hibernate.c3p0.max_size", "20");
		sessionBuilder.setProperty("hibernate.c3p0.timeout", "300");
		sessionBuilder.setProperty("hibernate.c3p0.max_statements", "50");
		sessionBuilder.setProperty("hibernate.c3p0.idle_test_period", "3000");

		return sessionBuilder.buildSessionFactory();
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
