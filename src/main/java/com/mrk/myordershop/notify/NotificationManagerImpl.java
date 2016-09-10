package com.mrk.myordershop.notify;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.mrk.myordershop.assembler.linkprovider.OrderLinkProvider;
import com.mrk.myordershop.assembler.linkprovider.RelationLinkProvider;
import com.mrk.myordershop.assembler.linkprovider.UserLinkProvider;
import com.mrk.myordershop.assembler.linkprovider.WholesalerOrderLinkProvider;
import com.mrk.myordershop.bean.Device;
import com.mrk.myordershop.bean.Field;
import com.mrk.myordershop.bean.Field.FieldList;
import com.mrk.myordershop.bean.InstantOrder;
import com.mrk.myordershop.bean.Notification;
import com.mrk.myordershop.bean.Order;
import com.mrk.myordershop.bean.Relation;
import com.mrk.myordershop.bean.Retailer;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.WholesalerInstantOrder;
import com.mrk.myordershop.bean.WholesalerOrder;
import com.mrk.myordershop.client.service.DeviceService;
import com.mrk.myordershop.constant.ActiveFlag;
import com.mrk.myordershop.constant.OrderStatus;
import com.mrk.myordershop.constant.RelationStatus;
import com.mrk.myordershop.constant.Role;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;
import com.mrk.myordershop.service.NotificationService;
import com.mrk.myordershop.service.OrderService;
import com.mrk.myordershop.service.UserService;
import com.mrk.myordershop.service.WholeSalerOrderService;

@Service
public class NotificationManagerImpl implements NotificationManager {

	private static final Logger log = Logger.getLogger(NotificationManagerImpl.class);

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private WholeSalerOrderService wholesalerOrder;

	@Autowired
	private DeviceService deviceService;

	@Autowired
	private UserService userService;

	@Autowired
	private IOSPNotifier iospNotify;

	@Autowired
	private WebNotifier webNotifier;

	@Autowired
	private AndroidNotifier androidNotifier;

	@Autowired
	private SMSNotifier smsNotifier;

	private static String NEW_ORDER_FROM_RT = "New order from %s";
	private static String NEW_WSORDER_TO_SUPPLIER = "New order from %s";
	private static String NEW_ORDER_FROM_WS = "New order to %s";

	private static String ORDER_STATUS_CHANGE_TO_RT = "Your order (%s) is %s";
	private static String WSORDER_STATUS_CHANGE_TO_WS = "Your order (%s) is %s";

	private static String RELATON_REQUESTED_TO = "%s wants to add you in their circle";
	private static String RELATON_ACCEPTED_TO = "%s is in your circle now";
	private static String RELATON_REJECTED_TO = "%s declined your request";
	private static String RELATON_REMOVED_TO = "%s is no longer in your circle";

	@Override
	public void notifyNewOrder(InstantOrder order, Retailer retailer) {
		String message = null;
		if (order.getReferralUser() != null) {
			try {
				order = (InstantOrder) orderService.getOrder(order.getId(), retailer);

				FieldList fieldList = Field.fieldList().addField("orderId", order.getId())
						.addField("orderType", "order")
						.addField("link", OrderLinkProvider.get(order.getId(), Role.ROLE_WSALER).getHref())
						.addField("productName", order.getProductName());

				fieldList.addField("userName", order.getReferralUser().getName());
				message = String.format(NEW_ORDER_FROM_RT, order.getReferralUser().getFirmName());

				Notification notification = new Notification(Notification.TIER.neworder, order.getWholesaler().getId());
				notification.setActiveFlag(ActiveFlag.ACTIVE);
				notification.setMessage(message);
				notification.setFields(fieldList);
				new Thread(new SaveAndSender(notification)).start();

			} catch (EntityDoseNotExistException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void notifyNewOrder(WholesalerInstantOrder order, Wholesaler wholesaler) {
		String message = null;
		if (order.getReferralUser() != null) {
			try {
				order = (WholesalerInstantOrder) orderService.getOrder(order.getId(), wholesaler);

				FieldList fieldList = Field.fieldList().addField("orderId", order.getId())
						.addField("orderType", "order")
						.addField("link", OrderLinkProvider.get(order.getId(), Role.ROLE_RETAIL).getHref())
						.addField("productName", order.getProductName())
						.addField("wholesalerName", order.getWholesaler().getName());

				message = String.format(NEW_ORDER_FROM_WS, order.getWholesaler().getFirmName());
				Notification notification = new Notification(Notification.TIER.neworder,
						order.getReferralUser().getId());
				notification.setActiveFlag(ActiveFlag.ACTIVE);
				notification.setMessage(message);
				notification.setFields(fieldList);
				new Thread(new SaveAndSender(notification)).start();
			} catch (EntityDoseNotExistException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void notifyNewOrder(WholesalerOrder order, Wholesaler wholesaler) {

		try {
			order = wholesalerOrder.get(order.getId(), wholesaler);

			String message = String.format(NEW_WSORDER_TO_SUPPLIER, order.getUser().getFirmName());

			Notification notification = new Notification(Notification.TIER.neworder, order.getSupplier().getId());
			notification.setActiveFlag(ActiveFlag.ACTIVE);
			notification.setMessage(message);
			notification.setFields(
					Field.fieldList().addField("orderId", order.getId()).addField("orderType", "wholesalerOrder")
							.addField("link",
									WholesalerOrderLinkProvider.get(order.getId(), Role.ROLE_SUPPLIER).getHref())
					.addField("userName", order.getUser().getName()).addField("productName", order.getProductName()));
			new Thread(new SaveAndSender(notification)).start();
		} catch (EntityDoseNotExistException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void notifyOrderStatus(Order order) {

		if (order.getReferralUser() != null) {
			String message = String.format(ORDER_STATUS_CHANGE_TO_RT, order.getOrderNo(),
					order.getOrderStatus().getValue());

			Notification notification = new Notification(Notification.TIER.orderstatuschange,
					order.getReferralUser().getId());
			notification.setActiveFlag(ActiveFlag.ACTIVE);
			notification.setMessage(message);
			notification.setFields(Field.fieldList().addField("orderId", order.getId()).addField("orderType", "order")
					.addField("status", order.getOrderStatus())
					.addField("link", OrderLinkProvider.get(order.getId(), Role.ROLE_RETAIL).getHref())
					.addField("userName", order.getWholesaler().getName())
					.addField("productName", order.getProductName()));
			new Thread(new SaveAndSender(notification)).start();
		}
	}

	@Override
	public void notifyOrderCancel(Order order) {

		if (order.getReferralUser() != null) {
			String message = String.format(ORDER_STATUS_CHANGE_TO_RT, order.getOrderNo(),
					order.getOrderStatus().getValue());

			Notification notification = new Notification(Notification.TIER.orderstatuschange,
					order.getWholesaler().getId());
			notification.setActiveFlag(ActiveFlag.ACTIVE);
			notification.setMessage(message);
			notification.setFields(Field.fieldList().addField("orderId", order.getId()).addField("orderType", "order")
					.addField("status", order.getOrderStatus())
					.addField("link", OrderLinkProvider.get(order.getId(), Role.ROLE_WSALER).getHref())
					.addField("userName", order.getReferralUser().getName())
					.addField("productName", order.getProductName()));
			new Thread(new SaveAndSender(notification)).start();
		}
	}

	@Override
	public void notifyOrderCancel(WholesalerOrder order) {

		String message = String.format(ORDER_STATUS_CHANGE_TO_RT, order.getOrderNo(),
				order.getOrderStatus().getValue());

		Notification notification = new Notification(Notification.TIER.orderstatuschange, order.getSupplier().getId());
		notification.setActiveFlag(ActiveFlag.ACTIVE);
		notification.setMessage(message);
		notification.setFields(Field.fieldList().addField("orderId", order.getId()).addField("orderType", "order")
				.addField("status", order.getOrderStatus())
				.addField("link", OrderLinkProvider.get(order.getId(), Role.ROLE_SUPPLIER).getHref())
				.addField("userName", order.getUser().getName()).addField("productName", order.getProductName()));
		new Thread(new SaveAndSender(notification)).start();
	}

	@Override
	public void notifyOrderStatus(WholesalerOrder order) {

		String message = String.format(WSORDER_STATUS_CHANGE_TO_WS, order.getOrderNo(),
				order.getOrderStatus().getValue());
		/* if RECEIVED */
		Notification notification = new Notification(Notification.TIER.orderstatuschange,
				order.getOrderStatus() == OrderStatus.RECEIVED ? order.getSupplier().getId() : order.getUser().getId());
		notification.setActiveFlag(ActiveFlag.ACTIVE);
		notification.setMessage(message);
		notification
				.setFields(
						Field.fieldList().addField("orderId", order.getId()).addField("orderType", "wholesalerOrder")
								.addField("status",
										order.getOrderStatus())
								.addField("link",
										WholesalerOrderLinkProvider.get(order.getId(),
												order.getOrderStatus() == OrderStatus.RECEIVED ? Role.ROLE_SUPPLIER
														: Role.ROLE_WSALER)
												.getHref())
								.addField("userName", order.getSupplier().getName())
								.addField("productName", order.getProductName()));
		new Thread(new SaveAndSender(notification)).start();
	}

	@Override
	public void notifyAddRelation(User currentUser, Relation relation) {
		relation.setCurrentUserEmail(currentUser.getEmail());
		log.debug("current User +" + currentUser.getId());
		log.debug("relation user" + relation.getUser());
		String message = String.format(RELATON_REQUESTED_TO, relation.getPrimaryUser().getName());

		Notification notification = new Notification(Notification.TIER.relationrequest, relation.getUser().getId());
		notification.setActiveFlag(ActiveFlag.ACTIVE);
		notification.setMessage(message);
		notification.setFields(Field.fieldList().addField("userId", currentUser.getId())
				.addField("userName", currentUser.getName()).addField("role", currentUser.getClass().getSimpleName())
				.addField("link", RelationLinkProvider.get(currentUser.getId()).getHref()));
		new Thread(new SaveAndSender(notification)).start();

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

		Notification notification = new Notification(Notification.TIER.relationstatuschange,
				relation.getUser().getId());

		notification.setActiveFlag(ActiveFlag.ACTIVE);
		notification.setMessage(message);
		notification.setFields(Field.fieldList().addField("userId", currentUser.getId())
				.addField("userName", currentUser.getName()).addField("role", currentUser.getClass().getSimpleName())
				.addField("status", relation.getStatus())
				.addField("link", RelationLinkProvider.get(currentUser.getId()).getHref()));
		new Thread(new SaveAndSender(notification)).start();
	}

	@Override
	public void notifyDeleteRelation(User currentUser, String userId) {
		String message = String.format(RELATON_REMOVED_TO, currentUser.getName());

		Notification notification = new Notification(Notification.TIER.relationremove, userId);
		notification.setActiveFlag(ActiveFlag.ACTIVE);
		notification.setMessage(message);
		notification.setFields(Field.fieldList().addField("userId", currentUser.getId())
				.addField("userName", currentUser.getName()).addField("role", currentUser.getClass().getSimpleName())
				.addField("link", UserLinkProvider.get(currentUser.getId()).getHref()));
		new Thread(new SaveAndSender(notification)).start();
	}

	// void sendNotifyToDevice(String userId, Notification notification) {
	// List<Device> devices = deviceService.findByUserId(userId);
	// Notifier notifier = null;
	// for (Device device : devices) {
	// notifier = device.getClientId().equals("myordershopios") ? iospNotify
	// : device.getClientId().equals("myordershopweb") ? webNotifier
	// : null;
	// notifier.send(device, notification);
	// }
	// }

	private class SaveAndSender implements Runnable {

		private Notification notification;

		SaveAndSender(Notification notification) {
			this.notification = notification;
		}

		@Override
		public void run() {
			try {
				log.info("saveing notif				notificationService.save(notification);y==" + notification);
				notificationService.save(notification);
				Long total = notificationService.findByUserId(notification.getUserId(), new PageRequest(1, 1))
						.getTotalElements();
				notification.getFields().add(new Field("total", total.toString()));
			} catch (EntityNotPersistedException e) {
				e.printStackTrace();
			}
			List<Device> devices = deviceService.findByUserId(notification.getUserId());
			Notifier notifier = null;
			for (Device device : devices) {
				notifier = device.getClientId().equals("myordershopios") ? iospNotify
						: device.getClientId().equals("myordershopweb") ? webNotifier
								: device.getClientId().equals("myordershopandroid") ? androidNotifier : null;
				// check setting over here and send
				notifier.send(device, notification);
			}
			// for sms
			// check setting over here and send
			try {
				Device device = new Device();
				device.setDeviceToken(userService.get(notification.getUserId()).getMobile());
				// smsNotifier.send(device, notification);
			} catch (EntityDoseNotExistException e) {
				e.printStackTrace();
				log.debug("message send error for " + notification.getUserId());
			}
		}

	}
}
