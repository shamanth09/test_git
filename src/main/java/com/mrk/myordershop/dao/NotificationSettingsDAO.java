package com.mrk.myordershop.dao;

import com.mrk.myordershop.bean.NotificationSettings;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;

public interface NotificationSettingsDAO {
	
	void save(NotificationSettings notificationSettings) throws EntityNotPersistedException;
	
	void update(NotificationSettings notificationSettings);
	
	NotificationSettings getNotificationByUserID(String userID) throws EntityDoseNotExistException;
	
	NotificationSettings getNotification(int id)throws EntityDoseNotExistException;
	
}

