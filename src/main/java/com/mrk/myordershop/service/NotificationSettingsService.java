package com.mrk.myordershop.service;

import com.mrk.myordershop.bean.NotificationSettings;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;

public interface NotificationSettingsService {

	void save(NotificationSettings notificationSettings)
			throws EntityNotPersistedException;

	void update(String type, Integer value, User user) throws EntityDoseNotExistException;

	NotificationSettings getNotificationByUserID(String userID)
			throws EntityDoseNotExistException;

	// NotificationSettings getNotification(int id)throws
	// EntityDoseNotExistException;

}
