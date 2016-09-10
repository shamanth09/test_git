package com.mrkinnoapps.myordershopadmin.config;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

public class WebAppInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext container) throws ServletException {

		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(DataSourceConfig.class, SecurityConfig.class,
				MailConfig.class);
		container.addListener(new ContextLoaderListener(rootContext));

		FilterRegistration.Dynamic securityFilterChain = container.addFilter(
				"springSecurityFilterChain", new DelegatingFilterProxy(
						"springSecurityFilterChain"));
		securityFilterChain.setAsyncSupported(true);
		securityFilterChain.addMappingForUrlPatterns(null, false, "/*");

		AnnotationConfigWebApplicationContext dispatcherServlet = new AnnotationConfigWebApplicationContext();
		dispatcherServlet.register(WebConfig.class);

		ServletRegistration.Dynamic dispatcher = container.addServlet(
				"DispatcherServlet", new DispatcherServlet(dispatcherServlet));
		dispatcher.setAsyncSupported(true);
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/");
	}
}
