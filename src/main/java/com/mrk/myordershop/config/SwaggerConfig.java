package com.mrk.myordershop.config;

import static com.google.common.base.Predicates.not;
import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.ant;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("api-docs").addResourceLocations(
				"/META-INF/resources/");

		registry.addResourceHandler("/webjars/**").addResourceLocations(
				"/META-INF/resources/webjars/");
	}

	@Bean
	public Docket RetailerApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("Retailer")
				.select().apis(RequestHandlerSelectors.any())
				.paths(ant("/api/**")).paths(not(or(ant("/**/ws/**"),ant("/**/sp/**")))).build().apiInfo(apiInfo());
	}

	@Bean
	public Docket SupplierApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("Supplier")
				.select().apis(RequestHandlerSelectors.any())
				.paths(ant("/api/**")).paths(not(or(ant("/**/ws/**"),ant("/**/rt/**")))).build().apiInfo(apiInfo());
	}

	@Bean
	public Docket WholesalerApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("Wholesaler")
				.select().apis(RequestHandlerSelectors.any())
				.paths(ant("/api/**")).paths(not(or(ant("/**/rt/**"),ant("/**/sp/**")))).build().apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("My Order Shop API")
				.description("My Order Shop API Documentations")
				.termsOfServiceUrl(
						"http://www.myordershop.com/ous_index.html#/app/privacy")
				.contact(
						new Contact("Vikram Jain", "",
								"vikramjain5190@gmail.com")).build();
	}

	// private List<SecurityScheme> scopes() {
	// List<AuthorizationScope> scopes = new ArrayList<AuthorizationScope>();
	// scopes.add(new AuthorizationScope("global", "global"));
	//
	// List<GrantType> grantTypes = new ArrayList<GrantType>();
	// ResourceOwnerPasswordCredentialsGrant implicitGrant = new
	// ResourceOwnerPasswordCredentialsGrant(
	// contextUrl + "token");
	// grantTypes.add(implicitGrant);
	//
	// List<SecurityScheme> response = new ArrayList<SecurityScheme>();
	// OAuth auth = new OAuth("oauth2", scopes, grantTypes);
	// response.add(auth);
	//
	// return response;
	// }

}
