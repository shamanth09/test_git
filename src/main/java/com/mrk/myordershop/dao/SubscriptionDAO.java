package com.mrk.myordershop.dao;

import com.mrk.myordershop.bean.Subscription;
import com.mrk.myordershop.exception.EntityNotPersistedException;

public interface SubscriptionDAO {

	void subscrip(Subscription subscription) throws EntityNotPersistedException;
}
