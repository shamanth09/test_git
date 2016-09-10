package com.mrk.myordershop.notify;

import org.springframework.beans.factory.annotation.Autowired;

import com.mrk.myordershop.bean.Device;
import com.mrk.myordershop.bean.Notification;
import com.mrk.myordershop.webpush.WebPushService;

public class WebNotifier implements Notifier {

	@Autowired
	private WebPushService webPushService;

	@Override
	public void send(Device device, Notification notificaion) {
		webPushService.send(device.getUserId(), notificaion);
	}

	
}
