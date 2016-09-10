package com.mrkinnoapps.myordershopadmin.config;

import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.HateoasSortHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.extras.tiles2.dialect.TilesDialect;
import org.thymeleaf.extras.tiles2.spring4.web.configurer.ThymeleafTilesConfigurer;
import org.thymeleaf.extras.tiles2.spring4.web.view.ThymeleafTilesView;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

@Configuration
@ComponentScan(basePackages = "com.mrkinnoapps.myordershopadmin.*")
@EnableWebMvc
@EnableSpringDataWebSupport
public class WebConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addArgumentResolvers(
			List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(pageableResolver());
		argumentResolvers.add(sortResolver());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
	 * #addResourceHandlers(org.springframework.web.servlet.config.annotation.
	 * ResourceHandlerRegistry) to allow resources
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations(
				"/WEB-INF/resources/");
	}

	/* ViewResolver */
	@Bean
	public ViewResolver tilesViewResolver() {
		ThymeleafViewResolver vr = new ThymeleafViewResolver();
		vr.setTemplateEngine(templateEngine());
		vr.setViewClass(ThymeleafTilesView.class);
		vr.setCharacterEncoding("UTF-8");
		vr.setOrder(Ordered.LOWEST_PRECEDENCE);
		return vr;
	}

	@Bean
	public ITemplateResolver templateResolver() {
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
		templateResolver.setPrefix("/WEB-INF/views");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode("HTML5");
		templateResolver.setCharacterEncoding("UTF-8");
		templateResolver.setCacheable(false);
		return templateResolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());
		templateEngine.addDialect(new TilesDialect());
		return templateEngine;
	}

	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("META-INF/messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	@Bean
	public ThymeleafTilesConfigurer tilesConfigurer() {
		ThymeleafTilesConfigurer ttc = new ThymeleafTilesConfigurer();
		ttc.setDefinitions("/WEB-INF/views/config/tiles-defs.xml");
		return ttc;
	}

	// @Bean
	// public ReloadableResourceBundleMessageSource iMessageResolver() {
	// ReloadableResourceBundleMessageSource messageSource = new
	// ReloadableResourceBundleMessageSource();
	// Properties p = new Properties();
	// try {
	// p.load(new FileInputStream("classpath:/META-INF/messages.properties"));
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// messageSource.setCommonMessages(p);
	// messageSource.setDefaultEncoding("UTF-8");
	// return messageSource;
	// }

	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver getMultipartResolver() {
		return new CommonsMultipartResolver();
	}

	/* MethodArgumentResolver */
	@Bean
	public HateoasPageableHandlerMethodArgumentResolver pageableResolver() {
		return new HateoasPageableHandlerMethodArgumentResolver(sortResolver());
	}

	@Bean
	public HateoasSortHandlerMethodArgumentResolver sortResolver() {
		return new HateoasSortHandlerMethodArgumentResolver();
	}
	
}
