package com.mrk.myordershop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrk.myordershop.bean.ContactUs;
import com.mrk.myordershop.config.annotation.PersistTransactional;
import com.mrk.myordershop.dao.ContactUsDAO;

/**
 * ContactUsServiceImpl.java
 * Naveen
 * Apr 4, 2015
 */
@Service
public class ContactUsServiceImpl implements ContactUsService{

	@Autowired
	private ContactUsDAO contactUsDAO;
	/* 
	 * Naveen
	 * Apr 4, 2015
	 */
	@Override
	@PersistTransactional
	public void saveContactUs(ContactUs contactUs) {
		contactUsDAO.saveContactUs(contactUs);
	}

}
