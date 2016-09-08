package com.mrk.myordershop.exception;

import java.util.List;

import com.mrk.myordershop.resource.DuplicateContactsResources;

public class DuplicateContactsException extends Exception {

	private static final long serialVersionUID = 1L;

	private List<DuplicateContactsResources> duplicateContacts;

	public DuplicateContactsException(Class entity, String message,
			List<DuplicateContactsResources> duplicateContacts) {
		super(message);
		this.duplicateContacts = duplicateContacts;
	}

	public DuplicateContactsException(Class entity, String message) {
		super(message);
	}

	public List<DuplicateContactsResources> getDuplicateContacts() {
		return duplicateContacts;
	}

	public void setDuplicateContacts(
			List<DuplicateContactsResources> duplicateContacts) {
		this.duplicateContacts = duplicateContacts;
	}

}
