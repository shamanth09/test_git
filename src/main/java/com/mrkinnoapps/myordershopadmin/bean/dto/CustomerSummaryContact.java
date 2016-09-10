package com.mrkinnoapps.myordershopadmin.bean.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.mrkinnoapps.myordershopadmin.bean.entity.Contact;

public class CustomerSummaryContact extends CustomerSummary {

	@JsonView(View.ContactBasic.class)
	private Contact contact;

	public CustomerSummaryContact(Contact contact) {
		this.contact = contact;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}
}
