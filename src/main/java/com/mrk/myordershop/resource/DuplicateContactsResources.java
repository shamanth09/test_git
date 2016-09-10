package com.mrk.myordershop.resource;

import com.mrk.myordershop.bean.Contact;

public class DuplicateContactsResources {

	private Contact original;

	private Contact duplicate;

	public DuplicateContactsResources(Contact original, Contact duplicate) {
		this.original = original;
		this.duplicate = duplicate;
	}

	public Contact getOriginal() {
		return original;
	}

	public void setOriginal(Contact original) {
		this.original = original;
	}

	public Contact getDuplicate() {
		return duplicate;
	}

	public void setDuplicate(Contact duplicate) {
		this.duplicate = duplicate;
	}

}
