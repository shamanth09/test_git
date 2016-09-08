package com.mrk.myordershop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrk.myordershop.bean.Subscription;
import com.mrk.myordershop.config.annotation.PersistTransactional;
import com.mrk.myordershop.dao.SubscriptionDAO;
import com.mrk.myordershop.exception.EntityNotPersistedException;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

	@Autowired
	private SubscriptionDAO subscriptionDAO;

	@Override
	@PersistTransactional
	public void subscrip(Subscription subscription)
			throws EntityNotPersistedException {
		subscriptionDAO.subscrip(subscription);
	}

}
