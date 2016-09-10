package com.mrk.myordershop.assembler;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.mrk.myordershop.bean.Contact;
import com.mrk.myordershop.resource.Resource;

@Component
public class ContactAssembler extends
		ResourceAssemblerSupport<Contact, Resource> {

	public ContactAssembler() {
		super(Contact.class, Resource.class);
	}

	@Override
	public Resource toResource(Contact contact) {
		Resource<Contact> cResource = new Resource<Contact>(contact);
		return cResource;
	}

}
