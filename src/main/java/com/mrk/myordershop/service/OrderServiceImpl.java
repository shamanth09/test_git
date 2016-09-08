package com.mrk.myordershop.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mrk.myordershop.bean.Cancellation;
import com.mrk.myordershop.bean.Contact;
import com.mrk.myordershop.bean.Image;
import com.mrk.myordershop.bean.InstantOrder;
import com.mrk.myordershop.bean.Order;
import com.mrk.myordershop.bean.OrderAcceptance;
import com.mrk.myordershop.bean.Relation;
import com.mrk.myordershop.bean.Retailer;
import com.mrk.myordershop.bean.Supplier;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.WholesalerInstantOrder;
import com.mrk.myordershop.bean.dto.OrderFilter;
import com.mrk.myordershop.bean.dto.SearchFilter;
import com.mrk.myordershop.bean.dto.SearchFilter.SearchIn;
import com.mrk.myordershop.bean.dto.SearchResource;
import com.mrk.myordershop.bean.mail.MailMessageFactory;
import com.mrk.myordershop.config.annotation.PersistTransactional;
import com.mrk.myordershop.config.annotation.ReadTransactional;
import com.mrk.myordershop.constant.ActiveFlag;
import com.mrk.myordershop.constant.OrderStatus;
import com.mrk.myordershop.constant.RelationStatus;
import com.mrk.myordershop.dao.AddressDAO;
import com.mrk.myordershop.dao.CategoryDAO;
import com.mrk.myordershop.dao.ImageDAO;
import com.mrk.myordershop.dao.OrderDAO;
import com.mrk.myordershop.dao.SearchDAO;
import com.mrk.myordershop.dao.WholesalerInstantOrderDAO;
import com.mrk.myordershop.dao.WholesalerOrderDAO;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;
import com.mrk.myordershop.exception.InvalidFlowException;
import com.mrk.myordershop.notify.annotation.Notify;
import com.mrk.myordershop.notify.annotation.NotifyType;

/**
 * OrderServiceImpl.java Naveen Mar 27, 2015
 */
@Service
public class OrderServiceImpl implements OrderService {

	private static Logger log = Logger.getLogger(OrderServiceImpl.class
			.getName());
	@Autowired
	private OrderDAO orderDAO;
	@Autowired
	private WholesalerInstantOrderDAO wholesalerInstantOrderDAO;
	@Autowired
	private ItemService itemService;
	@Autowired
	private AddressDAO addressDAO;
	@Autowired
	private CategoryDAO categoryDAO;
	@Autowired
	private UserService userService;
	@Autowired
	private RelationService relationService;

	@Autowired
	@Qualifier(value = "mailMessageFactory")
	private MailMessageFactory mailMessageFactory;
	@Autowired(required = false)
	private HttpServletRequest request;
	@Autowired
	private WholesalerOrderDAO wholesalerOrderDAO;
	@Autowired
	private ContactService contactService;
	@Autowired
	private ImageDAO imageDAO;

	/*
	 * Naveen Mar 27, 2015
	 */
	@Override
	@ReadTransactional
	public Order getOrder(int orderId, User user)
			throws EntityDoseNotExistException {
		return orderDAO.getOrder(orderId, user);
	}

	/*
	 * Naveen Apr 8, 2015
	 */
	@Override
	@ReadTransactional
	public Page<Order> getOrders(Pageable pageable, Wholesaler wholesaler,
			OrderFilter filter) {
		Page<Order> orders = orderDAO.getOrders(pageable, wholesaler, filter);
		return orders;
	}

	/*
	 * Naveen Apr 9, 2015
	 */
	// @Override
	// public Page<Order> getOrders(Pageable pageable, Supplier supplier,
	// OrderFilter filter) {
	// return orderDAO.getOrders(pageable, supplier, filter);
	// }

	@Override
	@Notify(tier = NotifyType.orderstatuschange, currentUserPosition = 2)
	@PersistTransactional
	public Order updateOrderStatus(int orderId, OrderStatus status,
			Wholesaler wholesaler) throws EntityDoseNotExistException {
		Order orderfdb = null;
		if (status.equals(OrderStatus.DELIVERED)
				|| status.equals(OrderStatus.AVAILABLE)
				|| status.equals(OrderStatus.REJECTED)) {
			orderfdb = orderDAO.getOrder(orderId, wholesaler);
			if ((orderfdb.getOrderStatus().equals(OrderStatus.IN_PROGRESS) && status
					.equals(OrderStatus.AVAILABLE))
					|| (orderfdb.getOrderStatus().equals(OrderStatus.AVAILABLE) && status
							.equals(OrderStatus.DELIVERED))
					|| (orderfdb.getOrderStatus().equals(OrderStatus.ACTIVE) && status
							.equals(OrderStatus.REJECTED))) {
				orderfdb.setOrderStatus(status);
				orderfdb.setUpdateTimestamp(new Date());
				orderDAO.update(orderfdb);
			} else
				throw new InvalidFlowException(WholesalerInstantOrder.class,
						"can not change from "
								+ orderfdb.getOrderStatus().getValue() + " to "
								+ status.getValue());
		} else
			throw new InvalidFlowException(WholesalerInstantOrder.class,
					"can not change to " + status.getValue());
		return orderfdb;
	}

	@Override
	@Notify(tier = NotifyType.orderstatuschange, currentUserPosition = 2)
	@PersistTransactional
	public Order updateOrderStatus(int orderId, OrderStatus status,
			Supplier supplier) throws EntityDoseNotExistException {
		Order orderfdb = null;
		if (status.equals(OrderStatus.IN_PROGRESS)) {
			orderfdb = orderDAO.getOrder(orderId);
			if (orderfdb.getOrderStatus().equals(OrderStatus.APPROVED)) {
				orderfdb.setOrderStatus(status);
				orderfdb.setUpdateTimestamp(new Date());
				orderDAO.update(orderfdb);
			} else
				throw new InvalidFlowException(WholesalerInstantOrder.class,
						"can not change from "
								+ orderfdb.getOrderStatus().getValue() + " to "
								+ status.getValue());
		} else
			throw new InvalidFlowException(WholesalerInstantOrder.class,
					"can not change to " + status.getValue());
		return orderfdb;
	}

	@Override
	@Notify(tier = NotifyType.ordercancellation, currentUserPosition = 2)
	@PersistTransactional
	public Order cancelOrder(int orderId, Cancellation cancellation,
			Retailer retailer) throws EntityDoseNotExistException {
		Order orderfdb = orderDAO.getOrder(orderId, retailer);
		if (orderfdb.getOrderStatus().equals(OrderStatus.ACTIVE)
				|| orderfdb.getOrderStatus().equals(OrderStatus.APPROVED)) {
			cancellation.setCreateTimstamp(new Date());
			orderfdb.setOrderStatus(OrderStatus.CANCELLED);
			orderfdb.setCancellation(cancellation);
			orderfdb.setUpdateTimestamp(new Date());
			orderDAO.update(orderfdb);
			// clientNotifierService.notifyOrderCancel(orderfdb);
			return orderfdb;
		} else
			throw new InvalidFlowException(Order.class,
					" cannot cancel in this status");

	}

	@Notify(tier = NotifyType.ordercancellation, currentUserPosition = 2)
	@PersistTransactional
	@Override
	public Order cancelOrder(int orderId, Cancellation cancellation,
			Wholesaler wholesaler) throws EntityDoseNotExistException {
		cancellation.setCreateTimstamp(new Date());
		WholesalerInstantOrder orderfdb = null;
		Order order = orderDAO.getOrder(orderId, wholesaler);
		if (order instanceof WholesalerInstantOrder) {
			orderfdb = (WholesalerInstantOrder) order;
			if (orderfdb.getOrderStatus().equals(OrderStatus.ACTIVE)
					|| orderfdb.getOrderStatus().equals(OrderStatus.APPROVED)) {
				cancellation.setCreateTimstamp(new Date());
				orderfdb.setOrderStatus(OrderStatus.CANCELLED);
				orderfdb.setCancellation(cancellation);
				orderfdb.setUpdateTimestamp(new Date());
				orderDAO.update(orderfdb);
				// clientNotifierService.notifyOrderCancel(orderfdb);
			} else
				throw new InvalidFlowException(Order.class,
						" cannot cancel in this status");
		} else {
			throw new EntityDoseNotExistException(Order.class);
		}
		return order;
	}

	@Override
	@ReadTransactional
	public Page<Order> getOrders(Pageable pageable, Retailer retailer,
			OrderFilter filter) {
		return orderDAO.getOrders(pageable, retailer, filter);
	}

	@Override
	@ReadTransactional
	public Page<Order> deliversBy(Date date, Pageable pageable,
			Wholesaler wholesaler) {
		date = date != null ? date : new Date();
		return orderDAO.deliversBy(date, pageable, wholesaler);
	}

	@Override
	@ReadTransactional
	public List<SearchResource> search(SearchFilter filter,
			Wholesaler wholesaler) {
		List<SearchResource> result = new ArrayList<SearchResource>();
		filter.fillWholesalerSearchOnOrder();
		for (SearchIn searchIn : filter.getSearchIn()) {

			addToSearchResult(result, filter.getQuery(), searchIn, wholesaler,
					orderDAO);

			if (searchIn.equals(SearchIn.CUSTOMER_NAME)
					|| searchIn.equals(SearchIn.MOBILE)) {
				addToSearchResult(result, filter.getQuery(), searchIn,
						wholesaler, wholesalerInstantOrderDAO);

			}
		}
		return result;
	}

	@Override
	@ReadTransactional
	public List<SearchResource> search(SearchFilter filter, Retailer retailer) {
		List<SearchResource> result = new ArrayList<SearchResource>();
		filter.fillRetailerSearch();
		for (SearchIn searchIn : filter.getSearchIn()) {
			addToSearchResult(result, filter.getQuery(), searchIn, retailer,
					orderDAO);
		}

		return result;
	}

	private void addToSearchResult(List<SearchResource> result, String query,
			SearchIn searchIn, User user, SearchDAO... dao) {
		for (SearchDAO searchFieldDAO : dao) {
			List<SearchResource> orderNo = searchFieldDAO.search(query,
					searchIn, user);
			for (SearchResource order : orderNo) {
				order.setLable(searchIn.getSuffix());
				result.add(order);
			}
		}

	}

	@Notify(tier = NotifyType.neworder, position = 0, currentUserPosition = 1)
	@PersistTransactional
	@Override
	public void saveInstantOrder(InstantOrder instantOrder, Retailer retailer)
			throws EntityDoseNotExistException {
		Relation relation = relationService.get(instantOrder.getWholesaler()
				.getId(), retailer);
		log.trace("relation status - " + relation.getStatus());
		if (relation.getStatus().equals(RelationStatus.ACCEPTED)) {
			instantOrder.setUser(retailer);
			instantOrder.setReferralUser(retailer);
			instantOrder.setActiveFlag(ActiveFlag.ACTIVE);
			instantOrder.setOrderStatus(OrderStatus.ACTIVE);
			instantOrder.setCreateTimestamp(new Date());
			instantOrder.getItem().setOrder(instantOrder);
			orderDAO.save(instantOrder);
			itemService.saveItem(instantOrder.getItem());
		} else
			throw new InvalidFlowException(Relation.class,
					" wholesaler is not in your Circle");
		// clientNotifierService.notifyNewOrder(instantOrder);
	}

	@PersistTransactional
	@Override
	public InstantOrder saveInstantOrderImage(int orderId, Image image,
			Retailer retailer) throws EntityDoseNotExistException {
		InstantOrder instantOrder = null;
		instantOrder = (InstantOrder) orderDAO.getOrder(orderId);
		image.setActiveFlag(ActiveFlag.ACTIVE);
		imageDAO.save(image);
		instantOrder.setImage(image);
		orderDAO.update(instantOrder);
		return instantOrder;
	}

	@Notify(tier = NotifyType.neworder, position = 0, currentUserPosition = 1)
	@PersistTransactional
	@Override
	public void saveInstantOrder(WholesalerInstantOrder instantOrder,
			Wholesaler wholesaler) throws EntityNotPersistedException,
			EntityDoseNotExistException {

		User user = null;
		if (instantOrder.getReferralUser() != null
				&& instantOrder.getReferralUser().getId() != null) {
			user = userService.get(instantOrder.getReferralUser().getId());
		} else if (instantOrder.getCustomerMobile() != null
				&& !instantOrder.getCustomerMobile().isEmpty()) {
			user = userService.getUserByMobieOrEmail(instantOrder
					.getCustomerMobile());
			instantOrder.setCustomerFirmName(null);
			instantOrder.setCustomerMobile(null);
			instantOrder.setCustomerName(null);
		} else
			throw new InvalidFlowException(Retailer.class, " not found");

		Relation relation = relationService.get(user.getId(), wholesaler);
		if (relation.getStatus().equals(RelationStatus.ACCEPTED)) {
			// save order
			if (instantOrder.getAdvance() != null
					&& !instantOrder.getAdvance().isEmpty()
					|| instantOrder.getRateCut() != null
					&& instantOrder.getRateCut() > 0) {
				try {
					OrderAcceptance acceptance = new OrderAcceptance();
					acceptance.setAdvance(Double.parseDouble(instantOrder
							.getAdvance()));
					acceptance.setRateCut(new Double(instantOrder.getRateCut()));
					instantOrder.setOrderStatus(OrderStatus.APPROVED);
					instantOrder.setAcceptance(acceptance);
					instantOrder.setAdvance(null);
					instantOrder.setRateCut(null);
				} catch (NumberFormatException e) {
				}
			}
			instantOrder.setOrderStatus(OrderStatus.ACTIVE);
			instantOrder.setActiveFlag(ActiveFlag.ACTIVE);
			instantOrder.setReferralUser((Retailer) user);
			instantOrder.setCreateTimestamp(new Date());
			instantOrder.getItem().setOrder(instantOrder);
			instantOrder.setWholesaler(wholesaler);
			orderDAO.save(instantOrder);
			itemService.saveItem(instantOrder.getItem());
		} else
			throw new InvalidFlowException(Relation.class,
					" wholesaler is not in your Circle");

		// clientNotifierService.notifyNewOrder(instantOrder);
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
	@PersistTransactional
	public void saveWholesalerInstantOrderImage(int orderId, Image image,
			Wholesaler wholesaler) throws EntityDoseNotExistException {
		WholesalerInstantOrder instantOrder = null;
		Order order = orderDAO.getOrder(orderId);
		if (order instanceof WholesalerInstantOrder) {
			instantOrder = (WholesalerInstantOrder) order;

			image.setActiveFlag(ActiveFlag.ACTIVE);
			imageDAO.save(image);
			instantOrder.setImage(image);
			orderDAO.update(instantOrder);
		} else {
			throw new EntityDoseNotExistException(WholesalerInstantOrder.class);
		}
	}

	@Notify(tier = NotifyType.orderstatuschange, position = 1, currentUserPosition = 2)
	@PersistTransactional
	@Override
	public Order orderApprove(int orderId, OrderAcceptance orderAcceptance,
			Wholesaler wholesaler) throws EntityDoseNotExistException {
		Order order = orderDAO.getOrder(orderId, wholesaler);
		if (order.getOrderStatus() == OrderStatus.ACTIVE) {
			order.setOrderStatus(OrderStatus.APPROVED);
			order.setUpdateTimestamp(new Date());
			if (orderAcceptance != null
					&& (orderAcceptance.getAdvance() != 0
							|| orderAcceptance.getRateCut() != 0 || (orderAcceptance
							.getDescription() != null && !orderAcceptance
							.getDescription().isEmpty()))) {
				order.setAcceptance(orderAcceptance);
			}
			orderDAO.update(order);
		} else
			throw new InvalidFlowException(Order.class,
					" is not in ACTIVE state to APPROVE");
		return order;
	}
}
