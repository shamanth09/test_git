package com.mrk.myordershop.notify;

import com.mrk.myordershop.bean.InstantOrder;
import com.mrk.myordershop.bean.Order;
import com.mrk.myordershop.bean.Relation;
import com.mrk.myordershop.bean.Retailer;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.WholesalerInstantOrder;
import com.mrk.myordershop.bean.WholesalerOrder;

public interface NotificationManager {

	public void notifyNewOrder(InstantOrder order, Retailer retailer);

	void notifyNewOrder(WholesalerInstantOrder order, Wholesaler wholesaler);

	void notifyNewOrder(WholesalerOrder order, Wholesaler wholesaler);

	void notifyOrderStatus(Order order);

	void notifyOrderCancel(Order order);

	void notifyOrderCancel(WholesalerOrder order);

	void notifyOrderStatus(WholesalerOrder order);

	void notifyAddRelation(User currentUser, Relation relation);

	void notifyDeleteRelation(User currentUser, String userId);

	void notifyChangeRelationStatus(User currentUser, Relation relation);

}
