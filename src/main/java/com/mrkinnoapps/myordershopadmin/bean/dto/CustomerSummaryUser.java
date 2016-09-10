package com.mrkinnoapps.myordershopadmin.bean.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;

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
