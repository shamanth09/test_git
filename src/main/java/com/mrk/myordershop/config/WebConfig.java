package com.mrk.myordershop.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.HateoasSortHandlerMethodArgumentResolver;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.PagedResourcesAssemblerArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.mrk.myordershop.security.oauth.resolver.ApiControllerArgumentResolverm;

@Configuration
@EnableWebMvc
@EnableSpringDataWebSupport
@EnableHypermediaSupport(type = { HypermediaType.HAL })
public class WebConfig extends WebMvcConfigurerAdapter {

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer c) {
		c.defaultContentType(MediaTypes.HAL_JSON);
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(pageableResolver());
		argumentResolvers.add(sortResolver());
		argumentResolvers.add(pagedResourcesAssemblerArgumentResolver());
		argumentResolvers.add(getApiControllerArgumentResolverm());
	}

	// ArgumentResolvers
	@Bean
	public HateoasPageableHandlerMethodArgumentResolver pageableResolver() {
		return new HateoasPageableHandlerMethodArgumentResolver(sortResolver());
	}

	@Bean
	public HateoasSortHandlerMethodArgumentResolver sortResolver() {
		return new HateoasSortHandlerMethodArgumentResolver();
	}

	@Bean
	public PagedResourcesAssembler<?> pagedResourcesAssembler() {
		return new PagedResourcesAssembler<Object>(pageableResolver(), null);
	}

	@Bean
	public PagedResourcesAssemblerArgumentResolver pagedResourcesAssemblerArgumentResolver() {
		return new PagedResourcesAssemblerArgumentResolver(pageableResolver(), null);
	}

	@Bean
	public ApiControllerArgumentResolverm getApiControllerArgumentResolverm() {
		return new ApiControllerArgumentResolverm();
	}

	@Bean(name = "messageSource")
	public ReloadableResourceBundleMessageSource messageSource() {
	  ReloadableResourceBundleMessageSource messageBundle = new ReloadableResourceBundleMessageSource();
	  messageBundle.setBasename("classpath:META-INF/messages");
	  messageBundle.setDefaultEncoding("UTF-8");
	  return messageBundle;
	}
}
