package com.mrkinnoapps.myordershopadmin.util.notifier;

import com.mrkinnoapps.myordershopadmin.bean.entity.Device;
import com.mrkinnoapps.myordershopadmin.bean.entity.Notification;

public interface Notifier {

	public void send(Device device, Notification notificaion);
}
