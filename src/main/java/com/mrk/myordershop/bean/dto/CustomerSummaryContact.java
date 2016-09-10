package com.mrk.myordershop.bean.dto;

import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonView;
import com.mrk.myordershop.bean.Contact;

@Relation(collectionRelation = "content")
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
