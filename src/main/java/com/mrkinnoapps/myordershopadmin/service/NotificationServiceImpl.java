package com.mrkinnoapps.myordershopadmin.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mrkinnoapps.myordershopadmin.bean.constant.ActiveFlag;
import com.mrkinnoapps.myordershopadmin.bean.constant.RelationStatus;
import com.mrkinnoapps.myordershopadmin.bean.entity.Device;
import com.mrkinnoapps.myordershopadmin.bean.entity.Field;
import com.mrkinnoapps.myordershopadmin.bean.entity.Field.FieldList;
import com.mrkinnoapps.myordershopadmin.bean.entity.Notification;
import com.mrkinnoapps.myordershopadmin.bean.entity.Order;
import com.mrkinnoapps.myordershopadmin.bean.entity.Relation;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.bean.entity.WholesalerInstantOrder;
import com.mrkinnoapps.myordershopadmin.bean.entity.WholesalerOrder;
import com.mrkinnoapps.myordershopadmin.dao.NotificationDAO;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;
import com.mrkinnoapps.myordershopadmin.util.notifier.IOSPNotifier;
import com.mrkinnoapps.myordershopadmin.util.notifier.Notifier;
import com.mrkinnoapps.myordershopadmin.util.notifier.WebNotifier;

@Service
public class NotificationServiceImpl implements NotificationService {

	private static final Logger log = Logger
			.getLogger(NotificationServiceImpl.class);
	@Autowired
	private NotificationDAO notificaionDAO;

	@Autowired
	private DeviceService deviceService;

	@Autowired
	private IOSPNotifier iospNotify;

	@Autowired
	private WebNotifier webNotifier;

	private static String NEW_ORDER_FROM_RT = "New order from %s";
	private static String NEW_ORDER_FROM_WS = "New order for %s";
	private static String NEW_WSORDER_TO_SUPPLIER = "New order from %s";

	private static String ORDER_STATUS_CHANGE_TO_RT = "Your order (%s) is %s";
	private static String WSORDER_STATUS_CHANGE_TO_WS = "Your order (%s) is %s";

	private static String RELATON_REQUESTED_TO = "%s wants to add you in their circle";
	private static String RELATON_ACCEPTED_TO = "%s is in your circle now";
	private static String RELATON_REJECTED_TO = "%s declined your request";
	private static String RELATON_REMOVED_TO = "%s is no longer in your circle";

	@Override
	public void notifyNewOrder(Order order) {
		String message = null;
		FieldList fieldList = Field.fieldList()
				.addField("orderId", order.getId())
				.addField("orderType", "order")
				// .addField(
				// "link",
				// OrderLinkProvider.get(order.getId(), Role.ROLE_WSALER)
				// .getHref())
				.addField("productName", order.getProductName());
		if (order instanceof WholesalerInstantOrder) {
			WholesalerInstantOrder worder = (WholesalerInstantOrder) order;
			message = String
					.format(NEW_ORDER_FROM_WS, worder.getCustomerName());
			fieldList.addField("customerName", worder.getCustomerName());
		} else {
			fieldList.addField("userName", order.getUser().getName());
			message = String.format(NEW_ORDER_FROM_RT, order.getUser()
					.getAddress().getTitle());
		}

		Notification notificaion = new Notification(Notification.TIER.neworder,
				order.getWholesaler().getId());
		notificaion.setActiveFlag(ActiveFlag.ACTIVE);
		notificaion.setMessage(message);
		notificaion.setFields(fieldList);
		new Thread(new SaveAndSender(notificaion)).start();
	}

	@Override
	public void notifyNewOrder(WholesalerOrder order) {

		String message = String.format(NEW_WSORDER_TO_SUPPLIER, order.getUser()
				.getAddress().getTitle());

		Notification notificaion = new Notification(Notification.TIER.neworder,
				order.getSupplier().getId());
		notificaion.setActiveFlag(ActiveFlag.ACTIVE);
		notificaion.setMessage(message);
		notificaion.setFields(Field
				.fieldList()
				.addField("orderId", order.getId())
				.addField("orderType", "wholesalerOrder")
				// .addField(
				// "link",
				// WholesalerOrderLinkProvider.get(order.getId(),
				// Role.ROLE_SUPPLIER).getHref())
				.addField("userName", order.getUser().getName())
				.addField("productName", order.getProductName()));
		new Thread(new SaveAndSender(notificaion)).start();
	}

	@Override
	public void notifyOrderStatus(Order order) {

		String message = String.format(ORDER_STATUS_CHANGE_TO_RT,
				order.getOrderNo(), order.getOrderStatus().getValue());

		Notification notificaion = new Notification(
				Notification.TIER.orderstatuschange, order.getUser().getId());
		notificaion.setActiveFlag(ActiveFlag.ACTIVE);
		notificaion.setMessage(message);
		notificaion.setFields(Field
				.fieldList()
				.addField("orderId", order.getId())
				.addField("orderType", "order")
				.addField("status", order.getOrderStatus())
				// .addField(
				// "link",
				// OrderLinkProvider.get(order.getId(), Role.ROLE_RETAIL)
				// .getHref())
				.addField("userName", order.getWholesaler().getName())
				.addField("productName", order.getProductName()));
		new Thread(new SaveAndSender(notificaion)).start();
	}

	@Override
	public void notifyOrderCancel(Order order) {

		String message = String.format(ORDER_STATUS_CHANGE_TO_RT,
				order.getOrderNo(), order.getOrderStatus().getValue());

		Notification notificaion = new Notification(
				Notification.TIER.orderstatuschange, order.getWholesaler()
						.getId());
		notificaion.setActiveFlag(ActiveFlag.ACTIVE);
		notificaion.setMessage(message);
		notificaion.setFields(Field
				.fieldList()
				.addField("orderId", order.getId())
				.addField("orderType", "order")
				.addField("status", order.getOrderStatus())
				// .addField(
				// "link",
				// OrderLinkProvider.get(order.getId(), Role.ROLE_WSALER)
				// .getHref())
				.addField("userName", order.getUser().getName())
				.addField("productName", order.getProductName()));
		new Thread(new SaveAndSender(notificaion)).start();
	}

	@Override
	public void notifyOrderCancel(WholesalerOrder order) {

		String message = String.format(ORDER_STATUS_CHANGE_TO_RT,
				order.getOrderNo(), order.getOrderStatus().getValue());

		Notification notificaion = new Notification(
				Notification.TIER.orderstatuschange, order.getSupplier()
						.getId());
		notificaion.setActiveFlag(ActiveFlag.ACTIVE);
		notificaion.setMessage(message);
		notificaion.setFields(Field
				.fieldList()
				.addField("orderId", order.getId())
				.addField("orderType", "order")
				.addField("status", order.getOrderStatus())
				// .addField(
				// "link",
				// OrderLinkProvider.get(order.getId(), Role.ROLE_WSALER)
				// .getHref())
				.addField("userName", order.getUser().getName())
				.addField("productName", order.getProductName()));
		new Thread(new SaveAndSender(notificaion)).start();
	}

	@Override
	public void notifyOrderStatus(WholesalerOrder order) {

		String message = String.format(WSORDER_STATUS_CHANGE_TO_WS,
				order.getOrderNo(), order.getOrderStatus().getValue());

		Notification notificaion = new Notification(
				Notification.TIER.orderstatuschange, order.getUser().getId());
		notificaion.setActiveFlag(ActiveFlag.ACTIVE);
		notificaion.setMessage(message);
		notificaion.setFields(Field
				.fieldList()
				.addField("orderId", order.getId())
				.addField("orderType", "wholesalerOrder")
				.addField("status", order.getOrderStatus())
				// .addField(
				// "link",
				// WholesalerOrderLinkProvider.get(order.getId(),
				// Role.ROLE_WSALER).getHref())
				.addField("userName", order.getSupplier().getName())
				.addField("productName", order.getProductName()));
		new Thread(new SaveAndSender(notificaion)).start();
	}

	@Override
	public void notifyAddRelation(User currentUser, Relation relation) {
		relation.setCurrentUserEmail(currentUser.getEmail());
		log.debug("current User +" + currentUser.getId());
		log.debug("relation user" + relation.getUser());
		String message = String.format(RELATON_REQUESTED_TO, relation
				.getPrimaryUser().getName());

		Notification notificaion = new Notification(
				Notification.TIER.relationrequest, relation.getUser().getId());
		notificaion.setActiveFlag(ActiveFlag.ACTIVE);
		notificaion.setMessage(message);
		notificaion.setFields(Field.fieldList()
				.addField("userId", currentUser.getId())
				.addField("userName", currentUser.getName())
				.addField("role", currentUser.getClass().getSimpleName())
		// .addField(
		// "link",
		// RelationLinkProvider.get(currentUser.getId())
		// .getHref())
				);
		new Thread(new SaveAndSender(notificaion)).start();

	}

	@Override
	public void notifyChangeRelationStatus(User currentUser, Relation relation) {
		relation.setCurrentUserEmail(currentUser.getEmail());
		String message = null;
		if (relation.getStatus().equals(RelationStatus.ACCEPTED)) {
			message = String.format(RELATON_ACCEPTED_TO, currentUser.getName());
		} else if (relation.getStatus().equals(RelationStatus.REJECTED)) {
			message = String.format(RELATON_REJECTED_TO, currentUser.getName());
		}

		Notification notificaion = new Notification(
				Notification.TIER.relationstatuschange, relation.getUser()
						.getId());

		notificaion.setActiveFlag(ActiveFlag.ACTIVE);
		notificaion.setMessage(message);
		notificaion.setFields(Field.fieldList()
				.addField("userId", currentUser.getId())
				.addField("userName", currentUser.getName())
				.addField("role", currentUser.getClass().getSimpleName())
				.addField("status", relation.getStatus())
		// .addField(
		// "link",
		// RelationLinkProvider.get(currentUser.getId())
		// .getHref())
				);
		new Thread(new SaveAndSender(notificaion)).start();
	}

	@Override
	public void notifyDeleteRelation(User currentUser, Relation relation) {
		relation.setCurrentUserEmail(currentUser.getEmail());

		String message = String.format(RELATON_REMOVED_TO,
				currentUser.getName());

		Notification notificaion = new Notification(
				Notification.TIER.relationremove, relation.getUser().getId());
		notificaion.setActiveFlag(ActiveFlag.ACTIVE);
		notificaion.setMessage(message);
		notificaion.setFields(Field.fieldList()
				.addField("userId", currentUser.getId())
				.addField("userName", currentUser.getName())
				.addField("role", currentUser.getClass().getSimpleName())
		// .addField("link",
		// UserLinkProvider.get(currentUser.getId()).getHref())
				);
		new Thread(new SaveAndSender(notificaion)).start();
	}

	@Override
	public Page<Notification> findNotification(User currentUser,
			Pageable pageable) {
		return notificaionDAO.findByUserId(currentUser.getId(), pageable);
	}

	@Override
	public Notification deleteNotifi(Integer notificaionId, User currentUser)
			throws EntityDoseNotExistException, EntityNotPersistedException {
		Notification notificaion = notificaionDAO.get(notificaionId);
		notificaionDAO.delete(notificaion);
		return notificaion;
	}

	// void sendNotifyToDevice(String userId, Notification notificaion) {
	// List<Device> devices = deviceService.findByUserId(userId);
	// Notifier notifier = null;
	// for (Device device : devices) {
	// notifier = device.getClientId().equals("myordershopios") ? iospNotify
	// : device.getClientId().equals("myordershopweb") ? webNotifier
	// : null;
	// notifier.send(device, notificaion);
	// }
	// }

	private class SaveAndSender implements Runnable {

		private Notification notificaion;

		SaveAndSender(Notification notificaion) {
			this.notificaion = notificaion;
		}

		@Override
		public void run() {
			try {
				log.info("saveing notify==" + notificaion);
				notificaionDAO.save(notificaion);
				Long total = notificaionDAO.findByUserId(
						notificaion.getUserId(), new PageRequest(1, 1))
						.getTotalElements();
				notificaion.getFields().add(
						new Field("total", total.toString()));
			} catch (EntityNotPersistedException e) {
				e.printStackTrace();
			}
			List<Device> devices = deviceService.findByUserId(notificaion
					.getUserId());
			Notifier notifier = null;
			for (Device device : devices) {
				notifier = device.getClientId().equals("myordershopios") ? iospNotify
						: device.getClientId().equals("myordershopweb") ? webNotifier
								: null;
				notifier.send(device, notificaion);
			}
		}

	}

	@Override
	public void clearNotifi(User currentUser) {
		notificaionDAO.deleteAll(currentUser);
	}
}
