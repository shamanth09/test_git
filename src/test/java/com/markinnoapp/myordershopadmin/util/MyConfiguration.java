/**
 * 
 */
package com.markinnoapp.myordershopadmin.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mallinath Jul 1, 2016  
 */
@Configuration
public class MyConfiguration {
	
	@Bean
	public UtilClass utilClass()
	{
		return new UtilClass();
	}
	
	@Bean
	public CreateDB createDB()
	{
		return new CreateDB();
	}

}
