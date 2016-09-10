package com.mrk.myordershop.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrk.myordershop.bean.Contact;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.dto.SearchFilter;
import com.mrk.myordershop.bean.dto.SearchResource;
import com.mrk.myordershop.bean.dto.filter.ContactFilter;
import com.mrk.myordershop.constant.ContactGroup;
import com.mrk.myordershop.exception.DuplicateContactsException;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;

public interface ContactService {

	public void addContact(Contact contact, User currentUser) throws EntityNotPersistedException;

	public int addContacts(List<Contact> contacts, Wholesaler user,
			ContactGroup contactGroup) throws DuplicateContactsException;

	public Page<Contact> getContact(User currentUser,
			ContactFilter contactFilter, Pageable pageable);

	public List<SearchResource> search(User currentUser, SearchFilter searchFilter);

	public Contact getByMobile(String mobile)
			throws EntityDoseNotExistException;

	public Page<Contact> findByName(Wholesaler currentUser, String name,
			Pageable pageable);

	public Page<Contact> findByMobileNo(Wholesaler currentUser,
			String mobileNo, Pageable pageable);
	
	public Contact get(Integer id, User user) throws EntityDoseNotExistException;
	
	public Contact delete(Integer id, User user) throws EntityDoseNotExistException;

	public void update(Integer contactId, Contact contact, User user) throws EntityDoseNotExistException;

}