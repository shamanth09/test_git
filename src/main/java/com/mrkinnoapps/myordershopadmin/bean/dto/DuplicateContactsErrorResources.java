package com.mrkinnoapps.myordershopadmin.bean.dto;

import java.util.List;

import com.mrkinnoapps.myordershopadmin.resource.ErrorResource;

public class DuplicateContactsErrorResources extends ErrorResource {

	private List<DuplicateContactsResources> duplicateContacts;

	public DuplicateContactsErrorResources(String message,
			List<DuplicateContactsResources> duplicateContacts) {
		super("40020",message);
		this.duplicateContacts = duplicateContacts;
	}

	public List<DuplicateContactsResources> getDuplicateContacts() {
		return duplicateContacts;
	}

	public void setDuplicateContacts(
			List<DuplicateContactsResources> duplicateContacts) {
		this.duplicateContacts = duplicateContacts;
	}

}
