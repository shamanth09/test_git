package com.mrkinnoapps.myordershopadmin.service;

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

import com.mrkinnoapps.myordershopadmin.bean.constant.OrderStatus;
import com.mrkinnoapps.myordershopadmin.bean.dto.OrderFilter;
import com.mrkinnoapps.myordershopadmin.bean.dto.SearchFilter;
import com.mrkinnoapps.myordershopadmin.bean.dto.SearchFilter.SearchIn;
import com.mrkinnoapps.myordershopadmin.bean.dto.SearchResource;
import com.mrkinnoapps.myordershopadmin.bean.entity.Cancellation;
import com.mrkinnoapps.myordershopadmin.bean.entity.Image;
import com.mrkinnoapps.myordershopadmin.bean.entity.InstantOrder;
import com.mrkinnoapps.myordershopadmin.bean.entity.Item;
import com.mrkinnoapps.myordershopadmin.bean.entity.Order;
import com.mrkinnoapps.myordershopadmin.bean.entity.Retailer;
import com.mrkinnoapps.myordershopadmin.bean.entity.Supplier;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.bean.entity.Wholesaler;
import com.mrkinnoapps.myordershopadmin.bean.entity.WholesalerInstantOrder;
import com.mrkinnoapps.myordershopadmin.bean.entity.WholesalerOrder;
import com.mrkinnoapps.myordershopadmin.dao.AddressDAO;
import com.mrkinnoapps.myordershopadmin.dao.CategoryDAO;
import com.mrkinnoapps.myordershopadmin.dao.ImageDAO;
import com.mrkinnoapps.myordershopadmin.dao.InstantOrderDAO;
import com.mrkinnoapps.myordershopadmin.dao.OrderDAO;
import com.mrkinnoapps.myordershopadmin.dao.OrderSearcher;
import com.mrkinnoapps.myordershopadmin.dao.SearchDAO;
import com.mrkinnoapps.myordershopadmin.dao.WholesalerInstantOrderDAO;
import com.mrkinnoapps.myordershopadmin.dao.WholesalerOrderDAO;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;
import com.mrkinnoapps.myordershopadmin.mail.MailMessageFactory;
import com.mrkinnoapps.myordershopadmin.util.searchengine.SearchEngine;

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
	private InstantOrderDAO instantOrderDAO;
	@Autowired
	private WholesalerInstantOrderDAO wholesalerInstantOrderDAO;
	@Autowired
	private ItemService itemService;
	@Autowired
	private ProductService productService;
	@Autowired
	private AddressDAO addressDAO;
	@Autowired
	private ImageDAO imageDAO;
	@Autowired
	private AddressService addressService;
	@Autowired
	private MeltingAndSealService meltingAndSealingService;

	@Autowired
	private CategoryDAO categoryDAO;
	@Autowired
	private UserService userService;
	private SearchEngine engine;
	private OrderSearcher orderSearcher;
	@Autowired
	private NotificationService clientNotifierService;
	@Autowired
	@Qualifier(value = "mailMessageFactory")
	private MailMessageFactory mailMessageFactory;
	@Autowired(required = false)
	private HttpServletRequest request;
	@Autowired
	private WholesalerOrderDAO wholesalerOrderDAO;

	/*
	 * Naveen Mar 27, 2015
	 */
	// @Override
	// public void order(Order order, User user) {
	// order.setOrderStatus(OrderStatus.ACTIVE);
	// order.setUpdateTimestamp(new Date());
	// order.setCreateTimestamp(new Date());
	// order.setWholesaler((Wholesaler) userService.getUsers(Role.ROLE_WSALER)
	// .get(0));
	// orderDAO.saveOrder(order, user);
	// order.getItem().setOrder(order);
	// itemService.update(order.getItem());
	// // Dont what now
	// // mailMessageFactory.getOrderMessage().send(order,
	// // request.getServerName());
	// }

	/*
	 * Naveen Mar 27, 2015
	 */
	@Override
	public Order getOrder(int orderId, String userId)
			throws EntityDoseNotExistException {
		return orderDAO.getOrder(orderId, userService.get(userId));
	}

	@Autowired
	public OrderServiceImpl(OrderSearcher orderSearcher, SearchEngine engine) {
		this.engine = engine;
		this.orderSearcher = orderSearcher;
		engine.searcherRegister(orderSearcher);
	}

	/*
	 * Naveen Apr 8, 2015
	 */
	@Override
	public Page<Order> getOrders(Pageable pageable, Wholesaler wholesaler,
			OrderFilter filter) {
		Page<Order> orders = orderDAO.getOrders(pageable, wholesaler, filter);
		return orders;
	}

	/*
	 * Naveen Apr 8, 2015
	 */
	@Override
	public Order getOrder(String orderNo, User user)
			throws EntityDoseNotExistException {
		return orderDAO.getOrder(orderNo, user);
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
	public Order updateOrderStatus(int orderId, OrderStatus status,
			Wholesaler wholesaler) throws EntityDoseNotExistException {
		Order orderfdb = null;
		if (status.equals(OrderStatus.AVAILABLE)
				|| status.equals(OrderStatus.DELIVERED)
				|| status.equals(OrderStatus.REJECTED)) {

			orderfdb = orderDAO.getOrder(orderId, wholesaler);

			if (status.equals(OrderStatus.CANCELLED)
					&& !orderfdb.getUser().equals(wholesaler))
				throw new EntityDoseNotExistException(Order.class);

			orderfdb.setOrderStatus(status);
			orderfdb.setUpdateTimestamp(new Date());

			// set supplier order status to dispatched automatically
			if (status.equals(OrderStatus.AVAILABLE)) {

				// set order expected date is today when is available
				orderfdb.setExpectedDate(new Date());
				WholesalerOrder wholesalerOrder = wholesalerOrderDAO
						.getCurrentWholesalerOrderByOrderId(orderId, wholesaler);
				wholesalerOrder.setOrderStatus(OrderStatus.DISPATCHED);
				wholesalerOrder.setUpdateTimestamp(new Date());
			}
			orderDAO.update(orderfdb);
			clientNotifierService.notifyOrderStatus(orderfdb);
		} else
			throw new EntityDoseNotExistException();
		return orderfdb;
	}

	@Override
	public Order cancelOrder(int orderId, Cancellation cancellation,
			Retailer retailer) throws EntityDoseNotExistException {
		cancellation.setCreateTimstamp(new Date());
		Order orderfdb = orderDAO.getOrder(orderId, retailer);
		orderfdb.setOrderStatus(OrderStatus.CANCELLED);
		orderfdb.setCancellation(cancellation);
		orderfdb.setUpdateTimestamp(new Date());
		orderDAO.update(orderfdb);
		clientNotifierService.notifyOrderCancel(orderfdb);
		return orderfdb;
	}

	@Override
	public Order cancelOrder(int orderId, Cancellation cancellation,
			Wholesaler wholesaler) throws EntityDoseNotExistException {
		cancellation.setCreateTimstamp(new Date());
		WholesalerInstantOrder orderfdb = null;
		Order order = orderDAO.getOrder(orderId, wholesaler);
		if (order instanceof WholesalerInstantOrder) {
			orderfdb = (WholesalerInstantOrder) order;

			if (!orderfdb.getUser().equals(wholesaler))
				throw new EntityDoseNotExistException(Order.class);

			orderfdb.setOrderStatus(OrderStatus.CANCELLED);
			orderfdb.setCancellation(cancellation);
			orderfdb.setUpdateTimestamp(new Date());
			orderDAO.update(orderfdb);
			clientNotifierService.notifyOrderCancel(orderfdb);
		} else {
			throw new EntityDoseNotExistException(Order.class);
		}
		return orderfdb;
	}

	@Override
	public Page<Order> getOrders(Pageable pageable, String activeId,
			OrderFilter filter) throws EntityDoseNotExistException {
		if (activeId == null || activeId.isEmpty()) {
			return orderDAO.getOrders(pageable, filter);
		} else {
			User user = userService.get(activeId);
			if (user instanceof Retailer) {
				Retailer retailer = (Retailer) user;
				return orderDAO.getOrders(pageable, retailer, filter);
			}
			if (user instanceof Wholesaler) {
				Wholesaler wholesaler = (Wholesaler) user;
				return orderDAO.getOrders(pageable, wholesaler, filter);
			}
			throw new EntityDoseNotExistException(User.class);
		}

	}

	@Override
	public Page<Order> deliversBy(Date date, Pageable pageable,
			Wholesaler wholesaler) {
		date = date != null ? date : new Date();
		return orderDAO.deliversBy(date, pageable, wholesaler);
	}

	@Override
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

	@Override
	public User getUser(Integer orderId) {
		return orderDAO.getUser(orderId);
	}

	@Override
	public Order getOrder(int orderId) throws EntityDoseNotExistException {
		return orderDAO.getOrder(orderId);
	}

	@Override
	public Order updateImage(int orderId, Image image)
			throws EntityDoseNotExistException, EntityNotPersistedException {
		Image oldImage = null;
		Order order = orderDAO.getOrder(orderId);
		imageDAO.save(image);
		if (order instanceof WholesalerInstantOrder) {
			oldImage = ((WholesalerInstantOrder) order).getImage();
			((WholesalerInstantOrder) order).setImage(image);
		} else {
			oldImage = ((InstantOrder) order).getImage();
			((InstantOrder) order).setImage(image);
		}
		orderDAO.update(order);
		if (oldImage != null)
			imageDAO.delete(oldImage);
		return order;
	}

	@Override
	public void update(Order order) throws EntityDoseNotExistException,
			EntityNotPersistedException {
		Order order2=orderDAO.getOrder(order.getId());
		if(order2 instanceof WholesalerInstantOrder)
		{
			((WholesalerInstantOrder) order2).setCustomerName(((WholesalerInstantOrder)order).getCustomerName());
			((WholesalerInstantOrder) order2).setCustomerMobile(((WholesalerInstantOrder)order).getCustomerMobile());
			((WholesalerInstantOrder) order2).setCustomerFirmName(((WholesalerInstantOrder)order).getCustomerFirmName());
			((WholesalerInstantOrder) order2).setRateCut(((WholesalerInstantOrder)order).getRateCut());
			((WholesalerInstantOrder) order2).setAdvance(((WholesalerInstantOrder)order).getAdvance());
			((WholesalerInstantOrder) order2).setSampleDescription(((WholesalerInstantOrder)order).getSampleDescription());
			((WholesalerInstantOrder) order2).setSampleFrom(((WholesalerInstantOrder)order).getSampleFrom());
		}
		order2.setOrderStatus(order.getOrderStatus());
		order2.setDescription(order.getDescription());
		order2.setCreateTimestamp(order.getCreateTimestamp());
		order2.setExpectedDate(order.getExpectedDate());
		order2.setUpdateTimestamp(new Date());
		order2.setActiveFlag(order.getActiveFlag());
		orderDAO.update(order2);
		Integer id=order.getItem().getId();
		Item item=itemService.getItem(id);
		item.setDetail(order.getItem().getDetail());
		if(order.getItem().getMeltingAndSeal()!=null)
		item.setMeltingAndSeal(meltingAndSealingService.get(order.getItem().getMeltingAndSeal().getId()));
		if(order.getItem().getProduct()!=null && order.getItem().getProduct().getId()!=null)
		item.setProduct(productService.get(order.getItem().getProduct().getId()));
		itemService.update(item);
	}

	/* 
	 * Mallinath Jun 9, 2016
	 */
	@Override
	public Order saveOrder(Order order) {
		return orderDAO.saveOrder(order);
	}

	/* 
	 * Mallinath Jun 28, 2016
	 */
	@Override
	public void delete(Order order) {
		itemService.deleteItem(order.getItem());
		orderDAO.delete(order);
		
	}
}
