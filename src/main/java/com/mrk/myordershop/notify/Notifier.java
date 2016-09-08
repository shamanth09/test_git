package com.mrk.myordershop.notify;

import com.mrk.myordershop.bean.Device;
import com.mrk.myordershop.bean.Notification;

public interface Notifier {

	public void send(Device device, Notification notificaion);
}
