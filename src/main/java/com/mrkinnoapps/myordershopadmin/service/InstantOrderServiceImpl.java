package com.mrkinnoapps.myordershopadmin.service;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrkinnoapps.myordershopadmin.bean.constant.ActiveFlag;
import com.mrkinnoapps.myordershopadmin.bean.constant.OrderStatus;
import com.mrkinnoapps.myordershopadmin.bean.entity.Contact;
import com.mrkinnoapps.myordershopadmin.bean.entity.Image;
import com.mrkinnoapps.myordershopadmin.bean.entity.InstantOrder;
import com.mrkinnoapps.myordershopadmin.bean.entity.Retailer;
import com.mrkinnoapps.myordershopadmin.bean.entity.Wholesaler;
import com.mrkinnoapps.myordershopadmin.bean.entity.WholesalerInstantOrder;
import com.mrkinnoapps.myordershopadmin.dao.ImageDAO;
import com.mrkinnoapps.myordershopadmin.dao.InstantOrderDAO;
import com.mrkinnoapps.myordershopadmin.dao.OrderDAO;
import com.mrkinnoapps.myordershopadmin.dao.WholesalerInstantOrderDAO;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;
import com.mrkinnoapps.myordershopadmin.mail.MailMessageFactory;

@Service
public class InstantOrderServiceImpl implements InstantOrderService {

	@Autowired
	private InstantOrderDAO instantOrderDAO;
	@Autowired
	private OrderDAO orderDAO;
	@Autowired
	private WholesalerInstantOrderDAO wholesalerInstantOrderDAO;
	@Autowired
	private ItemService itemService;
	@Autowired
	private MailMessageFactory mailMessageFactory;
	@Autowired(required = false)
	private HttpServletRequest request;
	@Autowired
	private NotificationService clientNotifierService;
	@Autowired
	private UserService userService;
	@Autowired
	private ContactService contactService;
	@Autowired
	private ImageDAO imageDAO;

	@Override
	public void saveInstantOrder(InstantOrder instantOrder, Retailer retailer) throws EntityNotPersistedException {

		instantOrder.setOrderStatus(OrderStatus.ACTIVE);
		instantOrder.setCreateTimestamp(new Date());
		instantOrder.setUpdateTimestamp(new Date());
		instantOrder.getItem().setOrder(instantOrder);

		// instantOrder.setWholesaler((Wholesaler) userService.getByRole(
		// Role.ROLE_WSALER).get(0));

		instantOrderDAO.save(instantOrder);
		try {
			itemService.saveItem(instantOrder.getItem());

		} catch (EntityNotPersistedException e) {
			orderDAO.delete(instantOrder);
			throw e;
		}

		clientNotifierService.notifyNewOrder(instantOrder);
		// mailMessageFactory.getOrderMessage().send(instantOrder,
		// request.getServerName());

	}

	@Override
	public InstantOrder saveInstantOrderImage(int orderId, Image image,
			Retailer retailer) throws EntityDoseNotExistException, EntityNotPersistedException {
		InstantOrder instantOrder = null;
		instantOrder = instantOrderDAO.getInstantOrder(orderId);
		image.setActiveFlag(ActiveFlag.ACTIVE);
		imageDAO.save(image);
		instantOrder.setImage(image);
		instantOrderDAO.update(instantOrder);
		return instantOrder;
	}

	@Override
	public void saveInstantOrder(WholesalerInstantOrder instantOrder,
			Wholesaler wholesaler) throws EntityNotPersistedException {
		instantOrder.setOrderStatus(OrderStatus.ACTIVE);
		instantOrder.setCreateTimestamp(new Date());
		instantOrder.setUpdateTimestamp(new Date());
		instantOrder.getItem().setOrder(instantOrder);
		instantOrder.setWholesaler(wholesaler);
		wholesalerInstantOrderDAO.save(instantOrder);
		try {
			itemService.saveItem(instantOrder.getItem());
		} catch (EntityNotPersistedException e) {
			orderDAO.delete(instantOrder);
			throw e;
		}
		updateContacts(instantOrder, wholesaler);

		clientNotifierService.notifyNewOrder(instantOrder);
		// mailMessageFactory.getOrderMessage().send(instantOrder,
		// request.getServerName());
	}

	private void updateContacts(WholesalerInstantOrder instantOrder,
			Wholesaler wholesaler) throws EntityNotPersistedException {
		if (instantOrder.getCustomerMobile() != null
				&& !instantOrder.getCustomerMobile().trim().equals("")) {
			try {
				Contact contact = contactService.getByMobile(instantOrder
						.getCustomerMobile());
				if (instantOrder.getCustomerFirmName() != null
						&& !instantOrder.getCustomerFirmName().trim()
								.equals(""))
					contact.setFirmName(instantOrder.getCustomerFirmName());

				if (instantOrder.getCustomerName() != null
						&& !instantOrder.getCustomerName().trim().equals(""))
					contact.setName(instantOrder.getCustomerName());

				contactService.update(contact.getId(), contact, wholesaler);
			} catch (EntityDoseNotExistException e) {
				Contact contact = new Contact();
				contact.setMobile(instantOrder.getCustomerMobile());
				contact.setName(instantOrder.getCustomerName());
				contact.setFirmName(instantOrder.getCustomerFirmName());
				contact.setCreateTimestamp(new Date());
				contactService.addContact(contact, instantOrder.getUser());
			}
		}

	}

	@Override
	public WholesalerInstantOrder getWholesalerInstantOrder(int orderId,
			Wholesaler wholesaler) throws EntityDoseNotExistException {
		return wholesalerInstantOrderDAO.get(orderId);
	}

	@Override
	public void saveWholesalerInstantOrderImage(int orderId, Image image,
			Wholesaler wholesaler) throws EntityDoseNotExistException, EntityNotPersistedException {
		WholesalerInstantOrder instantOrder = null;
		instantOrder = wholesalerInstantOrderDAO.get(orderId);
		image.setActiveFlag(ActiveFlag.ACTIVE);
		imageDAO.save(image);
		instantOrder.setImage(image);
		wholesalerInstantOrderDAO.update(instantOrder);
	}

}
