package com.mrk.myordershop.resource;

import java.util.List;

public class DuplicateContactsErrorResources extends ErrorResource {

	private List<DuplicateContactsResources> duplicateContacts;

	public DuplicateContactsErrorResources(String message,
			List<DuplicateContactsResources> duplicateContacts) {
		super(message);
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
