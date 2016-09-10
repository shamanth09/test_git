package com.mrk.myordershop.bean.dto;

import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonView;
import com.mrk.myordershop.bean.User;

@Relation(collectionRelation = "content")
public class CustomerSummaryUser extends CustomerSummary {

	@JsonView(View.UserBasic.class)
	private User user;

	public CustomerSummaryUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
