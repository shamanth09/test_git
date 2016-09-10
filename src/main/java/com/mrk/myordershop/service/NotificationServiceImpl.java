package com.mrk.myordershop.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mrk.myordershop.bean.Notification;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.config.annotation.PersistTransactional;
import com.mrk.myordershop.config.annotation.ReadTransactional;
import com.mrk.myordershop.dao.NotificationDAO;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;

@Service
public class NotificationServiceImpl implements NotificationService {

	private static final Logger log = Logger.getLogger(NotificationServiceImpl.class);

	@Autowired
	private NotificationDAO notificaionDAO;

	@Override
	@ReadTransactional
	public Page<Notification> findByUserId(String currentUserId, Pageable pageable) {
		return notificaionDAO.findByUserId(currentUserId, pageable);
	}

	@Override
	@PersistTransactional
	public Notification deleteNotifi(Integer notificaionId, User currentUser)
			throws EntityDoseNotExistException, EntityNotPersistedException {
		Notification notificaion = notificaionDAO.get(notificaionId);
		notificaionDAO.delete(notificaion);
		return notificaion;
	}

	@PersistTransactional
	@Override
	public void clearNotifi(User currentUser) {
		notificaionDAO.deleteAll(currentUser);
	}

	@PersistTransactional
	@Override
	public void save(Notification notification) throws EntityNotPersistedException {
		notificaionDAO.save(notification);
	}
}
