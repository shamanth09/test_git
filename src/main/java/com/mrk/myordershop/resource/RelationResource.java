package com.mrk.myordershop.resource;

import org.springframework.hateoas.ResourceSupport;

import com.mrk.myordershop.bean.User;

public class RelationResource extends ResourceSupport{

	private int id;
	
	private User user;
	
	
}
