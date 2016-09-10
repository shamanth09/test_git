package com.mrkinnoapps.myordershopadmin.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mrkinnoapps.myordershopadmin.bean.constant.ContactGroup;
import com.mrkinnoapps.myordershopadmin.bean.constant.Role;
import com.mrkinnoapps.myordershopadmin.bean.dto.ContactFilter;
import com.mrkinnoapps.myordershopadmin.bean.dto.DuplicateContactsResources;
import com.mrkinnoapps.myordershopadmin.bean.dto.Register;
import com.mrkinnoapps.myordershopadmin.bean.dto.SearchFilter;
import com.mrkinnoapps.myordershopadmin.bean.dto.SearchFilter.SearchIn;
import com.mrkinnoapps.myordershopadmin.bean.dto.SearchResource;
import com.mrkinnoapps.myordershopadmin.bean.entity.Contact;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.bean.entity.Wholesaler;
import com.mrkinnoapps.myordershopadmin.dao.ContactDAO;
import com.mrkinnoapps.myordershopadmin.exception.DuplicateContactsException;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;

@Service
public class ContactServiceImpl implements ContactService {

	private static Logger log = Logger.getLogger(ContactServiceImpl.class);

	@Autowired
	private ContactDAO contactDAO;
	@Autowired
	private SupplierService supplierService;
	@Autowired
	private RetailerService retailerService;
	@Autowired
	private RelationService relationService;

	@Override
	public void addContact(Contact contact, User currentUser)
			throws EntityNotPersistedException {
		contact.setUser(currentUser);
		contact.setCreateTimestamp(new Date());
		contactDAO.save(contact);
	}

	@Override
	public int addContacts(List<Contact> contacts, Wholesaler user,
			ContactGroup contactGroup) throws DuplicateContactsException {
		int count = 0;
		List<DuplicateContactsResources> duplicatContacts = new ArrayList<DuplicateContactsResources>();
		for (Contact contact : contacts) {
			contact.setUser(user);
			contact.setCreateTimestamp(new Date());
			contact.setGroup(contactGroup);
			try {
				contactDAO.save(contact);
				count++;
			} catch (EntityNotPersistedException e) {
				if (e.getMessage().contains("Duplicate")) {
					try {
						Contact contactfdb = contactDAO.getByMobile(contact
								.getMobile());
						if (!contactfdb.getName().equals(contact.getName())) {
							DuplicateContactsResources res = new DuplicateContactsResources(
									contactfdb, contact);
							duplicatContacts.add(res);
						}
					} catch (EntityDoseNotExistException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
		if (!duplicatContacts.isEmpty()) {
			log.info(duplicatContacts.size() + " duplicate contacts");
			if (count == 0) {
				throw new DuplicateContactsException(Contact.class,
						duplicatContacts.size() + " duplicate contacts out of "
								+ contacts.size());
			}

			throw new DuplicateContactsException(Contact.class,
					duplicatContacts.size() + " duplicate contacts out of "
							+ contacts.size(), duplicatContacts);
		}
		return count;
	}

	private Register gerRegister(Contact contact) {
		Register supplier = new Register();
		supplier.setMobile(contact.getMobile());
		supplier.setName(contact.getName());
		supplier.setPassword(contact.getMobile());
		return supplier;
	}

	@Override
	public Page<Contact> getContact(User currentUser,
			ContactFilter contactFilter, Pageable pageable) {
		return contactDAO.getContact(currentUser, contactFilter, pageable);
	}

	@Override
	public Page<Contact> findByName(Wholesaler currentUser, String name,
			Pageable pageable) {
		Page<Contact> page = contactDAO.findByName(currentUser, name, pageable);
		if (page.getContent().isEmpty()) {
			List<User> searchPage = relationService.searchUser(
					Role.ROLE_RETAIL, name, SearchIn.NAME, currentUser);
			List<Contact> contacts = new ArrayList<Contact>();
			for (User user : searchPage) {
				Contact contact = new Contact();
				contact.setEmail(user.getEmail());
				contact.setName(user.getName());
				contact.setMobile(user.getMobile());
				contact.setFirmName(user.getFirmName());
				contact.setCreateTimestamp(new Date());
				contacts.add(contact);
			}
			page = new PageImpl<Contact>(contacts);
		}
		return page;
	}

	@Override
	public Page<Contact> findByMobileNo(Wholesaler currentUser,
			String mobileNo, Pageable pageable) {
		Page<Contact> page = contactDAO.findByMobileNo(currentUser, mobileNo,
				pageable);
		if (page.getContent().isEmpty()) {
			List<User> searchPage = relationService.searchUser(
					Role.ROLE_RETAIL, mobileNo, SearchIn.MOBILE, currentUser);
			List<Contact> contacts = new ArrayList<Contact>();
			for (User user : searchPage) {
				Contact contact = new Contact();
				contact.setEmail(user.getEmail());
				contact.setName(user.getName());
				contact.setMobile(user.getMobile());
				contact.setFirmName(user.getFirmName());
				contact.setCreateTimestamp(new Date());
				contacts.add(contact);
			}
			page = new PageImpl<Contact>(contacts);
		}
		return page;
	}

	@Override
	public Contact get(Integer id, User user)
			throws EntityDoseNotExistException {
		return contactDAO.get(id, user);
	}

	@Override
	public Contact delete(Integer id, User user)
			throws EntityDoseNotExistException {
		Contact contact = contactDAO.get(id, user);
		contactDAO.delete(contact);
		return contact;
	}

	@Override
	public void update(Integer contactId, Contact contact, User user)
			throws EntityDoseNotExistException {
		Contact contactfdb = contactDAO.get(contactId, user);
		contactfdb.setName(contact.getName());
		contactfdb.setMobile(contact.getMobile());
		contactfdb.setEmail(contact.getEmail());
		contactfdb.setFirmName(contact.getFirmName());
		contactDAO.update(contactfdb);
	}

	@Override
	public Contact getByMobile(String mobile)
			throws EntityDoseNotExistException {
		return contactDAO.getByMobile(mobile);
	}

	@Override
	public List<SearchResource> search(User currentUser,
			SearchFilter searchFilter) {
		searchFilter.fillContactSearchIn();
		List<SearchResource> searchResources = new ArrayList<SearchResource>();
		for (SearchIn searchIn : searchFilter.getSearchIn()) {
			searchResources.addAll(contactDAO.search(searchFilter.getQuery(),
					searchIn, currentUser));
		}
		return searchResources;
	}

}
