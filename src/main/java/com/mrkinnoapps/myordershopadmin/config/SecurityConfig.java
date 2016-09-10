package com.mrkinnoapps.myordershopadmin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.mrkinnoapps.myordershopadmin.security.service.CustomUserDetailsService;
import com.mrkinnoapps.myordershopadmin.security.service.SimpleUrlAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:/META-INF/db.properties")	
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Value("${mosadmin.username}")
	private String username;
	
	@Value("${mosadmin.password}")
	private String password;
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
	        .antMatchers("/resources/**").permitAll() 
	        .antMatchers("/login").anonymous()
	        .antMatchers("/cmn/**").permitAll()
	        .antMatchers("/v1/admin/**").access("hasRole('ROLE_ADMIN')")
	        .anyRequest().authenticated()
	        .and()
	        .userDetailsService(this.userDetailsService())
	    .formLogin()
	    	.loginProcessingUrl("/login")
	    	.defaultSuccessUrl("/home")
	        .loginPage("/login").usernameParameter("username").passwordParameter("password")
//	        .successHandler(getAuthenticationSuccessHandler())
	        .and()
	    .logout()                                    
	        .logoutRequestMatcher(new AntPathRequestMatcher("/logout","GET"))
	        .logoutSuccessUrl("/login")
	        .invalidateHttpSession(true)
	        .and()
	    .exceptionHandling().accessDeniedPage("/403");

	}
	
	@Override
	protected UserDetailsService userDetailsService() {
		return new CustomUserDetailsService(username, password);
	}
	
	@Bean
	public SimpleUrlAuthenticationSuccessHandler getAuthenticationSuccessHandler() {
		return new SimpleUrlAuthenticationSuccessHandler();
	}
}
