package com.mrk.myordershop.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrk.myordershop.bean.Notification;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;

public interface NotificationDAO {

	void save(Notification notificaion) throws EntityNotPersistedException;
	
	void update(Notification notificaion) throws EntityNotPersistedException;

	Notification get(int id) throws EntityDoseNotExistException;

	void delete(Notification notificaion);

	Page<Notification> findByUserId(String userId, Pageable pageable);
	
	void deleteAll(User user);
}
