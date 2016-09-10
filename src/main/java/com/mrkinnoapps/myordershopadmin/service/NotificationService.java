package com.mrkinnoapps.myordershopadmin.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrkinnoapps.myordershopadmin.bean.entity.Notification;
import com.mrkinnoapps.myordershopadmin.bean.entity.Order;
import com.mrkinnoapps.myordershopadmin.bean.entity.Relation;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.bean.entity.WholesalerOrder;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;

public interface NotificationService {

	void notifyNewOrder(Order order);

	void notifyNewOrder(WholesalerOrder order);

	void notifyOrderStatus(Order order);
	
	void notifyOrderCancel(Order order);
	
	void notifyOrderCancel(WholesalerOrder order);

	void notifyOrderStatus(WholesalerOrder order);

	void notifyAddRelation(User currentUser, Relation relation);

	void notifyDeleteRelation(User currentUser, Relation relation);

	void notifyChangeRelationStatus(User currentUser, Relation relation);

	Page<Notification> findNotification(User currentUser, Pageable pageable);

	Notification deleteNotifi(Integer notificaionId, User currentUser) throws EntityDoseNotExistException, EntityNotPersistedException;
	
	void clearNotifi( User currentUser);
}
