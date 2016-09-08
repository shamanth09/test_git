package com.mrk.myordershop.webpush;

import com.mrk.myordershop.bean.Notification;

public interface WebPushService {

	void send(String userId, Notification notificaion);

	WebDevice createDeviceInPool(String userId, String deviceToken, int sequence);

}
