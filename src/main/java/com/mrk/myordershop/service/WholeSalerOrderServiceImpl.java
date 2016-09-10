package com.mrk.myordershop.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mrk.myordershop.bean.Cancellation;
import com.mrk.myordershop.bean.InstantOrder;
import com.mrk.myordershop.bean.InstantWholesalerOrder;
import com.mrk.myordershop.bean.Item;
import com.mrk.myordershop.bean.Order;
import com.mrk.myordershop.bean.Supplier;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.WholesalerInstantOrder;
import com.mrk.myordershop.bean.WholesalerOrder;
import com.mrk.myordershop.bean.dto.SearchFilter;
import com.mrk.myordershop.bean.dto.SearchFilter.SearchIn;
import com.mrk.myordershop.bean.dto.SearchResource;
import com.mrk.myordershop.bean.dto.WholesalerOrderFilter;
import com.mrk.myordershop.bean.mail.MailMessageFactory;
import com.mrk.myordershop.config.annotation.PersistTransactional;
import com.mrk.myordershop.config.annotation.ReadTransactional;
import com.mrk.myordershop.constant.ActiveFlag;
import com.mrk.myordershop.constant.OrderStatus;
import com.mrk.myordershop.dao.AddressDAO;
import com.mrk.myordershop.dao.OrderDAO;
import com.mrk.myordershop.dao.SearchDAO;
import com.mrk.myordershop.dao.WholesalerOrderDAO;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.InvalidFlowException;
import com.mrk.myordershop.notify.annotation.Notify;
import com.mrk.myordershop.notify.annotation.NotifyType;

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
	private OrderService orderService;
	@Autowired
	private UserService userService;
	@Autowired
	private ItemService itemService;
	@Autowired
	private AddressDAO addressDAO;
	@Autowired
	private MailMessageFactory mailMessageFactory;
	@Autowired(required = false)
	private HttpServletRequest request;

	/*
	 * Naveen Mar 27, 2015
	 */
	@Override
	@ReadTransactional
	public WholesalerOrder get(int orderId, Wholesaler wholesaler)
			throws EntityDoseNotExistException {
		return wholesalerOrderDAO.get(orderId, wholesaler);
	}

	@Override
	@ReadTransactional
	public WholesalerOrder get(int orderId, Supplier supplier)
			throws EntityDoseNotExistException {
		return wholesalerOrderDAO.get(orderId);
	}

	/*
	 * Naveen Apr 8, 2015
	 */
	@Override
	@ReadTransactional
	public Page<WholesalerOrder> get(Pageable pageable, Wholesaler wholesaler,
			WholesalerOrderFilter filter) {
		return wholesalerOrderDAO.get(pageable, wholesaler, filter);
	}

	/*
	 * Naveen Apr 9, 2015
	 */
	@Override
	@ReadTransactional
	public Page<WholesalerOrder> get(Pageable pageable, Supplier supplier,
			WholesalerOrderFilter filter) {
		return wholesalerOrderDAO.get(pageable, supplier, filter);
	}

	@Notify(tier = NotifyType.newwsorder, position = 0, currentUserPosition = 1)
	@PersistTransactional
	@Override
	public InstantWholesalerOrder save(InstantWholesalerOrder wholesalerOrder,
			Wholesaler wholesaler) throws EntityDoseNotExistException {
		Order order = orderDAO.getOrder(wholesalerOrder.getOrder().getId(),
				wholesaler);
		if (order.getOrderStatus().equals(OrderStatus.APPROVED)) {
			List<WholesalerOrder> list = wholesalerOrderDAO.getByOrderNo(
					order.getOrderNo(), wholesaler);
			WholesalerOrder activeWSOrder = null;
			for (WholesalerOrder wsOrder : list) {
				/*
				 * order having other than cancel or rejected orderStatus(i.e.,
				 * INPROGRESS/ACTIVE/ etc.,) will throw exception
				 */
				if (!(wsOrder.getOrderStatus() == OrderStatus.CANCELLED || wsOrder
						.getOrderStatus() == OrderStatus.REJECTED)) {
					activeWSOrder = wsOrder;
					// break;
				}
				/*
				 * for orders having orderStaus with CANCELLED OR REJECTED those
				 * orders ActiveFlag will make it as INACTIVE
				 */
				// else if (wsOrder.getActiveFlag() == ActiveFlag.ACTIVE) {
				// wsOrder.setActiveFlag(ActiveFlag.INACTIVE);
				// wholesalerOrderDAO.update(wsOrder);
				// }
			}
			if (list.isEmpty() || activeWSOrder == null) {
				// save order
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
				} else if (order instanceof WholesalerInstantOrder) {
					WholesalerInstantOrder wInsOrder = (WholesalerInstantOrder) order;
					wholesalerOrder.setImage(wInsOrder.getImage());
					wholesalerOrder.setPriority(wInsOrder.getPriority());
				}
				Item wholesalerItem = wholesalerOrder.getItem();
				wholesalerItem.setProduct(order.getItem().getProduct());
				wholesalerItem.setWholesalerOrder(wholesalerOrder);

				wholesalerOrderDAO.save(wholesalerOrder);
				itemService.saveItem(wholesalerItem);

				// clientNotifierService.notifyNewOrder(wholesalerOrder);

			} else
				throw new InvalidFlowException(InstantWholesalerOrder.class,
						"already exist with satatus of "
								+ activeWSOrder.getOrderStatus().getValue());
		} else {
			throw new InvalidFlowException(InstantWholesalerOrder.class,
					" cannot create for non-active retailer order");
		}

		return wholesalerOrder;
	}

	@Override
	@ReadTransactional
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
	@ReadTransactional
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
	@Notify(tier = NotifyType.wsorderstatuschange, currentUserPosition = 2)
	@PersistTransactional
	@Override
	public WholesalerOrder updateOrderStatus(int orderId, OrderStatus status,
			Supplier supplier) throws EntityDoseNotExistException {
		WholesalerOrder wholesalerOrder = wholesalerOrderDAO.get(orderId,
				supplier);

		if ((status.equals(OrderStatus.IN_PROGRESS) && wholesalerOrder
				.getOrderStatus().equals(OrderStatus.ACTIVE))
				|| (status.equals(OrderStatus.DISPATCHED) && wholesalerOrder
						.getOrderStatus().equals(OrderStatus.IN_PROGRESS))
				|| (status.equals(OrderStatus.REJECTED) && wholesalerOrder
						.getOrderStatus().equals(OrderStatus.ACTIVE))) {
			wholesalerOrder.setOrderStatus(status);
			wholesalerOrder.setUpdateTimestamp(new Date());
			wholesalerOrderDAO.update(wholesalerOrder);
			if (status.equals(OrderStatus.IN_PROGRESS)) {
				orderService.updateOrderStatus(wholesalerOrder.getOrder()
						.getId(), OrderStatus.IN_PROGRESS, supplier);
			}
			return wholesalerOrder;
		} else
			throw new InvalidFlowException(Order.class,
					"cannot change status from "
							+ wholesalerOrder.getOrderStatus().getValue()
							+ " to " + status.getValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mrk.myordershop.service.wsaler.WholeSalerOrderService#
	 * updateOrderStatus (int, com.mrk.myordershop.constant.OrderStatus,
	 * com.mrk.myordershop.bean.Wholesaler) Wholesaler can change status to
	 * InProgress and Dispatched for supplier order by role of supplier
	 */
	@Notify(tier = NotifyType.wsorderstatuschange, currentUserPosition = 2)
	@PersistTransactional
	@Override
	public WholesalerOrder updateOrderStatus(int orderId, OrderStatus status,
			Wholesaler wholesaler) throws EntityDoseNotExistException {
		WholesalerOrder wholesalerOrder = wholesalerOrderDAO.get(orderId,
				wholesaler);
		if ((status.equals(OrderStatus.RECEIVED) && wholesalerOrder
				.getOrderStatus().equals(OrderStatus.DISPATCHED))) {
			wholesalerOrder.setOrderStatus(status);
			wholesalerOrder.setUpdateTimestamp(new Date());
			wholesalerOrderDAO.update(wholesalerOrder);
			if (status.equals(OrderStatus.RECEIVED)) {
				orderService.updateOrderStatus(wholesalerOrder.getOrder()
						.getId(), OrderStatus.AVAILABLE, wholesaler);
			}
		} else
			throw new InvalidFlowException(WholesalerOrder.class,
					"cannot change Order Status from "
							+ wholesalerOrder.getOrderStatus().getValue()
							+ " to " + status.getValue());
		return wholesalerOrder;
	}

	@Notify(tier = NotifyType.wsordercancellation, currentUserPosition = 2)
	@PersistTransactional
	@Override
	public WholesalerOrder cancelOrder(int orderId, Cancellation cancellation,
			Wholesaler wholesaler) throws EntityDoseNotExistException {
		WholesalerOrder orderfdb = wholesalerOrderDAO.get(orderId, wholesaler);
		if (orderfdb.getOrderStatus() == OrderStatus.ACTIVE) {
			orderfdb.setOrderStatus(OrderStatus.CANCELLED);
			cancellation.setCreateTimstamp(new Date());
			orderfdb.setCancellation(cancellation);
			wholesalerOrderDAO.update(orderfdb);
			// clientNotifierService.notifyOrderCancel(orderfdb);
		} else
			new InvalidFlowException(WholesalerOrder.class,
					"cannot change Order status from"
							+ orderfdb.getOrderStatus().getValue() + " to "
							+ OrderStatus.CANCELLED.getValue());
		return orderfdb;
	}
}
