package com.mrkinnoapps.myordershopadmin.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mrkinnoapps.myordershopadmin.bean.constant.ActiveFlag;
import com.mrkinnoapps.myordershopadmin.bean.constant.OrderStatus;
import com.mrkinnoapps.myordershopadmin.bean.dto.SearchFilter;
import com.mrkinnoapps.myordershopadmin.bean.dto.SearchFilter.SearchIn;
import com.mrkinnoapps.myordershopadmin.bean.dto.SearchResource;
import com.mrkinnoapps.myordershopadmin.bean.dto.WholesalerOrderFilter;
import com.mrkinnoapps.myordershopadmin.bean.entity.Cancellation;
import com.mrkinnoapps.myordershopadmin.bean.entity.InstantOrder;
import com.mrkinnoapps.myordershopadmin.bean.entity.InstantWholesalerOrder;
import com.mrkinnoapps.myordershopadmin.bean.entity.Item;
import com.mrkinnoapps.myordershopadmin.bean.entity.Order;
import com.mrkinnoapps.myordershopadmin.bean.entity.Supplier;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.bean.entity.Wholesaler;
import com.mrkinnoapps.myordershopadmin.bean.entity.WholesalerInstantOrder;
import com.mrkinnoapps.myordershopadmin.bean.entity.WholesalerOrder;
import com.mrkinnoapps.myordershopadmin.dao.AddressDAO;
import com.mrkinnoapps.myordershopadmin.dao.OrderDAO;
import com.mrkinnoapps.myordershopadmin.dao.OrderSearcher;
import com.mrkinnoapps.myordershopadmin.dao.SearchDAO;
import com.mrkinnoapps.myordershopadmin.dao.WholesalerOrderDAO;
import com.mrkinnoapps.myordershopadmin.dao.WholesalerOrderSearcher;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;
import com.mrkinnoapps.myordershopadmin.mail.MailMessageFactory;
import com.mrkinnoapps.myordershopadmin.util.searchengine.SearchEngine;

/**
 * WholesalerOrderServiceImpl.java Naveen Mar 27, 2015
 */
@Service
public class WholeSalerOrderServiceImpl implements WholeSalerOrderService {

	private static Logger log = Logger
			.getLogger(WholeSalerOrderServiceImpl.class.getName());

	@Autowired
	private WholesalerOrderDAO wholesalerOrderDAO;
	@Autowired
	private OrderDAO orderDAO;
	@Autowired
	private UserService userService;
	@Autowired
	private ItemService itemService;
	@Autowired
	private AddressDAO addressDAO;

	@Autowired
	private MeltingAndSealService meltingAndSealingService;

	@Autowired ProductService productService;
	@Autowired
	private NotificationService clientNotifierService;
	@Autowired
	private MailMessageFactory mailMessageFactory;
	@Autowired(required = false)
	private HttpServletRequest request;



	/**
	 * Mallinath May 31, 2016
	 */
	@Autowired
	public WholeSalerOrderServiceImpl(WholesalerOrderSearcher orderSearcher, SearchEngine engine) {
		engine.searcherRegister(orderSearcher);
	}

	/*
	 * Naveen Mar 27, 2015
	 */
	@Override
	public WholesalerOrder get(int orderId, Wholesaler wholesaler)
			throws EntityDoseNotExistException {
		return wholesalerOrderDAO.get(orderId, wholesaler);
	}

	@Override
	public WholesalerOrder get(int orderId, Supplier supplier)
			throws EntityDoseNotExistException {
		return wholesalerOrderDAO.get(orderId, supplier);
	}

	/*
	 * Naveen Apr 8, 2015
	 */
	@Override
	public Page<WholesalerOrder> get(Pageable pageable, Wholesaler wholesaler,
			WholesalerOrderFilter filter) {
		return wholesalerOrderDAO.get(pageable, wholesaler, filter);
	}

	/*
	 * Naveen Apr 8, 2015
	 */
	@Override
	public List<WholesalerOrder> getByOrderNo(String orderNo,
			Wholesaler wholesaler) {
		return wholesalerOrderDAO.getByOrderNo(orderNo, wholesaler);
	}

	/*
	 * Naveen Apr 9, 2015
	 */
	@Override
	public Page<WholesalerOrder> get(Pageable pageable, Supplier supplier,
			WholesalerOrderFilter filter) {
		return wholesalerOrderDAO.get(pageable, supplier, filter);
	}

	@Override
	public InstantWholesalerOrder save(InstantWholesalerOrder wholesalerOrder,
			Wholesaler wholesaler) throws EntityDoseNotExistException, EntityNotPersistedException {
		Order order = orderDAO.getOrder(wholesalerOrder.getOrder().getId(),
				wholesaler);

		wholesalerOrder.setUser(wholesaler);
		wholesalerOrder.setActiveFlag(ActiveFlag.ACTIVE);
		wholesalerOrder.setOrderStatus(OrderStatus.ACTIVE);
		wholesalerOrder.setCreateTimestamp(new Date());
		wholesalerOrder.setOrder(order);

		wholesalerOrder.getItem().getDetail()
		.setRemarks(order.getItem().getDetail().getRemarks());

		if (order instanceof InstantOrder) {
			InstantOrder insOrder = (InstantOrder) order;
			wholesalerOrder.setImage(insOrder.getImage());
			order.setOrderStatus(OrderStatus.APPROVED);
			orderDAO.update(order);
		} else if (order instanceof WholesalerInstantOrder) {
			WholesalerInstantOrder wInsOrder = (WholesalerInstantOrder) order;
			wholesalerOrder.setImage(wInsOrder.getImage());
			wholesalerOrder.setPriority(wInsOrder.getPriority());
			order.setOrderStatus(OrderStatus.APPROVED);
			orderDAO.update(order);
		}
		Item wholesalerItem = wholesalerOrder.getItem();
		wholesalerItem.setProduct(order.getItem().getProduct());
		wholesalerItem.setWholesalerOrder(wholesalerOrder);
		for (WholesalerOrder ord : wholesalerOrderDAO.getByOrderNo(
				order.getOrderNo(), wholesaler)) {
			ord.setActiveFlag(ActiveFlag.INACTIVE);
			wholesalerOrderDAO.update(ord);
		}
		wholesalerOrderDAO.save(wholesalerOrder);

		try {
			itemService.saveItem(wholesalerItem);
		} catch (EntityNotPersistedException e) {
			wholesalerOrderDAO.delete(wholesalerOrder);
			throw e;
		}

		clientNotifierService.notifyNewOrder(wholesalerOrder);
		// Dont want now
		// mailMessageFactory.getWholesalerOrderMessage().send(wholesalerOrder,
		// request.getServerName());
		return wholesalerOrder;
	}

	@Override
	public List<SearchResource> search(SearchFilter filter,
			Wholesaler wholesaler) {
		List<SearchResource> result = new ArrayList<SearchResource>();
		filter.fillWholesalerSearchOnWSOrder();
		for (SearchIn searchIn : filter.getSearchIn()) {
			addToSearchResult(result, searchIn, filter.getQuery(), wholesaler,
					wholesalerOrderDAO);
		}
		return result;
	}

	@Override
	public List<SearchResource> search(SearchFilter filter, Supplier supplier) {
		List<SearchResource> result = new ArrayList<SearchResource>();
		filter.fillSupplierSearch();
		for (SearchIn searchIn : filter.getSearchIn()) {
			addToSearchResult(result, searchIn, filter.getQuery(), supplier,
					wholesalerOrderDAO);
		}

		return result;
	}

	private void addToSearchResult(List<SearchResource> result,
			SearchIn searchIn, String query, User user, SearchDAO... dao) {
		for (SearchDAO searchFieldDAO : dao) {
			log.debug("search in " + searchIn);
			List<SearchResource> orderNo = searchFieldDAO.search(query,
					searchIn, user);
			for (SearchResource order : orderNo) {
				order.setLable(searchIn.getSuffix());
				result.add(order);
			}
		}

	}

	/*
	 * Naveen Apr 9, 2015
	 */
	@Override
	public WholesalerOrder updateOrderStatus(int orderId, OrderStatus status,
			Supplier supplier) throws EntityDoseNotExistException {
		WholesalerOrder wholesalerOrder = wholesalerOrderDAO.get(orderId,
				supplier);
		wholesalerOrder.setOrderStatus(status);
		wholesalerOrder.setUpdateTimestamp(new Date());
		wholesalerOrderDAO.update(wholesalerOrder);
		if (status.equals(OrderStatus.IN_PROGRESS)) {
			Order orderfdb = orderDAO.getOrder(wholesalerOrder.getOrder()
					.getId(), supplier);
			orderfdb.setOrderStatus(status);
			orderfdb.setUpdateTimestamp(new Date());
			orderDAO.update(orderfdb);
			if (!(orderfdb instanceof WholesalerInstantOrder))
				clientNotifierService.notifyOrderStatus(orderfdb);
		}
		clientNotifierService.notifyOrderStatus(wholesalerOrder);
		return wholesalerOrder;
	}

	@Override
	public WholesalerOrder cancelOrder(int orderId, Cancellation cancellation,
			Wholesaler wholesaler) throws EntityDoseNotExistException {
		cancellation.setCreateTimstamp(new Date());
		WholesalerOrder orderfdb = wholesalerOrderDAO.get(orderId, wholesaler);
		orderfdb.setOrderStatus(OrderStatus.CANCELLED);
		orderfdb.setCancellation(cancellation);
		orderfdb.setUpdateTimestamp(new Date());
		wholesalerOrderDAO.update(orderfdb);
		clientNotifierService.notifyOrderCancel(orderfdb);
		return orderfdb;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mrk.myordershop.service.wsaler.WholeSalerOrderService#updateOrderStatus
	 * (int, com.mrk.myordershop.constant.OrderStatus,
	 * com.mrk.myordershop.bean.Wholesaler) Wholesaler can change status to
	 * InProgress and Dispatched for supplier order by role of supplier
	 */
	@Override
	public WholesalerOrder updateOrderStatus(int orderId, OrderStatus status,
			Wholesaler wholesaler) throws EntityDoseNotExistException {
		WholesalerOrder wholesalerOrder = wholesalerOrderDAO.get(orderId,
				wholesaler);
		wholesalerOrder.setOrderStatus(status);
		wholesalerOrder.setUpdateTimestamp(new Date());
		wholesalerOrderDAO.update(wholesalerOrder);
		if (status.equals(OrderStatus.IN_PROGRESS)) {
			Order orderfdb = orderDAO.getOrder(wholesalerOrder.getOrder()
					.getId(), wholesaler);
			orderfdb.setOrderStatus(status);
			orderfdb.setUpdateTimestamp(new Date());
			orderDAO.update(orderfdb);
			if (!(orderfdb instanceof WholesalerInstantOrder))
				clientNotifierService.notifyOrderStatus(orderfdb);
		}
		clientNotifierService.notifyOrderStatus(wholesalerOrder);
		return wholesalerOrder;
	}

	/* 
	 * Mallinath May 28, 2016
	 */
	@Override
	public Page<WholesalerOrder> get(Pageable pageable, String activeId,
			WholesalerOrderFilter filter) {
		Page<WholesalerOrder> page=null;
		if(activeId!=null && !activeId.isEmpty())
		{
			try {
				User user=userService.getActiveUser(activeId);
				if(user instanceof Wholesaler)
					page=wholesalerOrderDAO.get(pageable, (Wholesaler)user, filter);
				else if(user instanceof Supplier)
					page=wholesalerOrderDAO.get(pageable, (Supplier)user, filter);
			} catch (EntityDoseNotExistException e) {
				e.printStackTrace();
			}
		}
		else
		{
			page=wholesalerOrderDAO.get(pageable,filter);
		}
		return page;
	}

	/* 
	 * Mallinath May 30, 2016
	 */
	@Override
	public WholesalerOrder getOrder(Integer worderId)
			throws EntityDoseNotExistException {
		return wholesalerOrderDAO.getOrder(worderId);
	}

	/* 
	 * Mallinath Jun 1, 2016
	 */
	@Override
	public WholesalerOrder getOrder(Integer orderId, String activeId)
			throws EntityDoseNotExistException {
		WholesalerOrder order=null;
		if (activeId == null || activeId.isEmpty()) {
			order=wholesalerOrderDAO.getOrder(orderId);
		} else{
			User user=userService.get(activeId);
			if(user instanceof Supplier)
				order=wholesalerOrderDAO.get(orderId,(Supplier)user);
			else
				order=wholesalerOrderDAO.get(orderId, (Wholesaler)user);
		}

		return order;
	}

	/* 
	 * Mallinath Jun 1, 2016
	 */
	@Override
	public void update(WholesalerOrder wholesalerOrder)
			throws EntityDoseNotExistException {
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~>>>>>>>>"+wholesalerOrder.getDescription());
		WholesalerOrder order=wholesalerOrderDAO.getOrder(wholesalerOrder.getId());
		order.setActiveFlag(wholesalerOrder.getActiveFlag());
		order.setCreateDate(wholesalerOrder.getCreateTimestamp());
		order.setExpectedDate(wholesalerOrder.getExpectedDate());
		order.setDescription(wholesalerOrder.getDescription());
		order.setOrderStatus(wholesalerOrder.getOrderStatus());
		if(wholesalerOrder.getSupplier()== null || order.getSupplier().getId().equals(wholesalerOrder.getSupplier().getId())){}
		else if(!order.getSupplier().getId().equals(wholesalerOrder.getSupplier().getId())){
		order.setSupplier(wholesalerOrder.getSupplier());
		order.setOrderStatus(OrderStatus.ACTIVE);
		}
		order.setUpdateTimestamp(new Date());
		wholesalerOrderDAO.update(order);
		Integer id=wholesalerOrder.getItem().getId();
		Item item=itemService.getItem(id);
		item.setDetail(wholesalerOrder.getItem().getDetail());
		if(wholesalerOrder.getItem().getMeltingAndSeal()!=null)
			item.setMeltingAndSeal(meltingAndSealingService.get(wholesalerOrder.getItem().getMeltingAndSeal().getId()));
		if(wholesalerOrder.getItem().getProduct()!=null && wholesalerOrder.getItem().getProduct().getId()!=null)
			item.setProduct(productService.get(wholesalerOrder.getItem().getProduct().getId()));
		itemService.update(item);
	}

	/* 
	 * Mallinath Jun 29, 2016
	 */
	
	@Override
	public void save(WholesalerOrder order) throws EntityDoseNotExistException {
		  wholesalerOrderDAO.save(order);
	}

	/* 
	 * Mallinath Jun 29, 2016
	 */
	@Override
	public void delete(WholesalerOrder order)
			throws EntityDoseNotExistException {
		itemService.deleteItem(order.getItem());
		wholesalerOrderDAO.delete(order);
		
	}

}
