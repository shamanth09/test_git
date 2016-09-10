package com.mrk.myordershop.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrk.myordershop.bean.Contact;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.dto.filter.ContactFilter;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;

public interface ContactDAO extends SearchDAO {

	public void save(Contact contact) throws EntityNotPersistedException;

	public Page<Contact> getContact(User currentUser,ContactFilter contactFilter, Pageable pageable);
	
	public Contact getByMobile(String mobile) throws EntityDoseNotExistException;
	
	public Contact get(int id, User user) throws EntityDoseNotExistException;
	
	public Contact getByName(String name) throws EntityDoseNotExistException;

	public Page<Contact> findByName(Wholesaler currentUser, String name, Pageable pageable);

	public Page<Contact> findByMobileNo(Wholesaler currentUser, String mobileNo, Pageable pageable);

	public void update(Contact contact);
	
	public void delete(Contact contact);

}
