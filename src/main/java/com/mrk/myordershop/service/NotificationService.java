package com.mrk.myordershop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrk.myordershop.bean.Notification;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;

public interface NotificationService {

	void save(Notification notification) throws EntityNotPersistedException;

	Page<Notification> findByUserId(String currentUserId, Pageable pageable);

	Notification deleteNotifi(Integer notificaionId, User currentUser)
			throws EntityDoseNotExistException, EntityNotPersistedException;

	void clearNotifi(User currentUser);
}
