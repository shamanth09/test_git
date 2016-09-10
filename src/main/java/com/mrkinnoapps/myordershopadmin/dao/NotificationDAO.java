package com.mrkinnoapps.myordershopadmin.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrkinnoapps.myordershopadmin.bean.entity.Notification;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;

public interface NotificationDAO {

	void save(Notification notificaion) throws EntityNotPersistedException;
	
	void update(Notification notificaion) throws EntityNotPersistedException;

	Notification get(int id) throws EntityDoseNotExistException;

	void delete(Notification notificaion);

	Page<Notification> findByUserId(String userId, Pageable pageable);
	
	void deleteAll(User user);
}
