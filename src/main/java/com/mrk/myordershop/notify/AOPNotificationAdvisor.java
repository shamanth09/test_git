package com.mrk.myordershop.notify;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import com.mrk.myordershop.bean.InstantOrder;
import com.mrk.myordershop.bean.Relation;
import com.mrk.myordershop.bean.Retailer;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.WholesalerInstantOrder;
import com.mrk.myordershop.bean.WholesalerOrder;
import com.mrk.myordershop.notify.annotation.Notify;

@Aspect
@Order(100)
public class AOPNotificationAdvisor {
	private final static Logger log = Logger.getLogger(AOPNotificationAdvisor.class);

	@Autowired
	private NotificationManager NotificationService;

	@Pointcut("execution(* com.mrk.myordershop.service.*.*(..))")
	public void allServiceMethodes() {
	}

	// @Around("execution(*
	// com.mrk.myordershop.service.OrderServiceImpl.getOrder(..))")
	// public Object test(ProceedingJoinPoint jointPoint) throws Throwable {
	// log.trace("started Notify --" + jointPoint.getSignature().getName());
	// return jointPoint.proceed();
	// }

	@Around(value = "allServiceMethodes() && @annotation(notify)")
	public Object notifyFromServer(ProceedingJoinPoint jointPoint, Notify notify) throws Throwable {
		log.trace("started Notify --" + jointPoint.getSignature().getName());
		log.trace("started currentUserPosition --" +notify.currentUserPosition());
		try {
			Object returnedObj = jointPoint.proceed();
			switch (notify.tier()) {
			case neworder: {
				com.mrk.myordershop.bean.Order order = (com.mrk.myordershop.bean.Order) jointPoint.getArgs()[notify
						.position()];
				if (order instanceof InstantOrder) {
					Retailer retailer = (Retailer) jointPoint.getArgs()[notify.currentUserPosition()];
					NotificationService.notifyNewOrder((InstantOrder) order, retailer);
				} else if (order instanceof WholesalerInstantOrder) {
					Wholesaler wholesaler = (Wholesaler) jointPoint.getArgs()[notify.currentUserPosition()];
					NotificationService.notifyNewOrder((WholesalerInstantOrder) order, wholesaler);
				}
				log.trace("Notify -- neworder");
			}
				break;
			case newwsorder: {
				WholesalerOrder wsorder = (WholesalerOrder) jointPoint.getArgs()[notify.position()];
				Wholesaler wholesaler = (Wholesaler) jointPoint.getArgs()[notify.currentUserPosition()];
				NotificationService.notifyNewOrder(wsorder, wholesaler);
				log.trace("Notify -- newwsorder");
			}
				break;
			case orderstatuschange: {
				if (returnedObj instanceof com.mrk.myordershop.bean.Order) {
					com.mrk.myordershop.bean.Order order = (com.mrk.myordershop.bean.Order) returnedObj;
					NotificationService.notifyOrderStatus(order);
				}
				log.trace("Notify -- orderstatuschange");
			}
				break;
			case ordercancellation: {
				com.mrk.myordershop.bean.Order order = (com.mrk.myordershop.bean.Order) returnedObj;
				NotificationService.notifyOrderCancel(order);
				log.trace("Notify -- ordercancellation");
			}
				break;
			case wsorderstatuschange: {
				if (returnedObj instanceof WholesalerOrder) {
					WholesalerOrder order = (WholesalerOrder) returnedObj;
					NotificationService.notifyOrderStatus(order);
				}
				log.trace("Notify -- wsorderstatuschange");
			}
				break;
			case wsordercancellation: {
				WholesalerOrder order = (WholesalerOrder) returnedObj;
				NotificationService.notifyOrderCancel(order);
			}
				break;
			case relationrequested: {
				Relation relation = (Relation) returnedObj;
				User currentUser = (User) jointPoint.getArgs()[notify.currentUserPosition()];
				NotificationService.notifyAddRelation(currentUser, relation);
				log.trace("Notify -- relationrequested");
			}
				break;

			case relationstatuschange: {
				Relation relation = (Relation) returnedObj;
				User currentUser = (User) jointPoint.getArgs()[notify.currentUserPosition()];
				NotificationService.notifyChangeRelationStatus(currentUser, relation);
				log.trace("Notify -- relationstatuschange");
			}
				break;

			case relationremoved: {
				User currentUser = (User) jointPoint.getArgs()[notify.currentUserPosition()];
				String userId = (String) jointPoint.getArgs()[0];
				NotificationService.notifyDeleteRelation(currentUser, userId);
				log.trace("Notify -- relationremoved");
			}
				break;
			}
			return returnedObj;
		} catch (Exception e) {
			throw e;
		}
	}

}
