package com.mrkinnoapps.myordershopadmin.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrkinnoapps.myordershopadmin.bean.constant.ContactGroup;
import com.mrkinnoapps.myordershopadmin.bean.dto.ContactFilter;
import com.mrkinnoapps.myordershopadmin.bean.dto.SearchFilter;
import com.mrkinnoapps.myordershopadmin.bean.dto.SearchResource;
import com.mrkinnoapps.myordershopadmin.bean.entity.Contact;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.bean.entity.Wholesaler;
import com.mrkinnoapps.myordershopadmin.exception.DuplicateContactsException;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;

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