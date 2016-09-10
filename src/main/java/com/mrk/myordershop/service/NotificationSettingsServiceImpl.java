package com.mrk.myordershop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrk.myordershop.bean.NotificationSettings;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.config.annotation.PersistTransactional;
import com.mrk.myordershop.config.annotation.ReadTransactional;
import com.mrk.myordershop.dao.NotificationSettingsDAO;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;

@Service
public class NotificationSettingsServiceImpl implements
		NotificationSettingsService {

	@Autowired
	private NotificationSettingsDAO notificationSettingsDAO;

	@Override
	@PersistTransactional
	public void save(NotificationSettings notificationSettings)
			throws EntityNotPersistedException {
		notificationSettingsDAO.save(notificationSettings);
	}

	@Override
	@PersistTransactional
	public void update(String type, Integer value, User user)
			throws EntityDoseNotExistException {
		NotificationSettings settings = notificationSettingsDAO
				.getNotificationByUserID(user.getId());
		switch (type) {
		case "newOrder":
			settings.setNewOrder(value);
			break;
		case "orderStatusChange":
			settings.setOrderStatusChange(value);
			break;
		case "wsOrderStatusChange":
			settings.setWsOrderStatusChange(value);
			break;
		case "relation":
			settings.setRelation(value);
			break;
		case "email":
			settings.setEmail(value);
			break;
		}
		notificationSettingsDAO.update(settings);
	}

	@Override
	@ReadTransactional
	public NotificationSettings getNotificationByUserID(String userId)
			throws EntityDoseNotExistException {
		NotificationSettings notificationSettings = null;
		try {
			notificationSettings = notificationSettingsDAO
					.getNotificationByUserID(userId);
		} catch (EntityDoseNotExistException e) {
			int id = Integer.parseInt(userId);
			notificationSettings = notificationSettingsDAO.getNotification(id);
		}
		return notificationSettings;
	}

}
