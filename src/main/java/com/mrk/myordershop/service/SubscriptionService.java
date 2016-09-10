package com.mrk.myordershop.service;

import com.mrk.myordershop.bean.Subscription;
import com.mrk.myordershop.exception.EntityNotPersistedException;

public interface SubscriptionService {

	void subscrip(Subscription subscription) throws EntityNotPersistedException;
}
