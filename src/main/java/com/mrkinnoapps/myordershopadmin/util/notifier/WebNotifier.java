package com.mrkinnoapps.myordershopadmin.util.notifier;

import org.springframework.beans.factory.annotation.Autowired;

import com.mrkinnoapps.myordershopadmin.bean.entity.Device;
import com.mrkinnoapps.myordershopadmin.bean.entity.Notification;

public class WebNotifier implements Notifier {

//	@Autowired
//	private WebPushService webPushService;

	@Override
	public void send(Device device, Notification notificaion) {
//		webPushService.send(device.getUserId(), notificaion);
	}

	
}
