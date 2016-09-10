package com.mrkinnoapps.myordershopadmin.util.notifier;

import org.apache.log4j.Logger;

import com.mrkinnoapps.myordershopadmin.bean.entity.Device;
import com.mrkinnoapps.myordershopadmin.bean.entity.Notification;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;

public class IOSPNotifier implements Notifier {
	private Logger log = Logger.getLogger(IOSPNotifier.class);

	private ApnsService service;

	public IOSPNotifier(String passphrase, String p12fileName) {
		if (p12fileName.contains("_dev_")) {
			log.debug("development mod with file :" + p12fileName);
			service = APNS
					.newService()
					.withCert(
							IOSPNotifier.class.getResourceAsStream("/META-INF/"
									+ p12fileName.trim()), passphrase.trim())
					.withSandboxDestination().build(); // dev
		} else {
			log.debug("production mod with file :" + p12fileName);
			service = APNS
					.newService()
					.withCert(
							IOSPNotifier.class.getResourceAsStream("/META-INF/"
									+ p12fileName.trim()), passphrase.trim())
					.withProductionDestination().build(); // production
		}
	}

	@Override
	public void send(Device device, Notification notificaion) {
		String payload = APNS.newPayload().sound("default")
				.alertBody(notificaion.getMessage())
				.badge(new Integer(notificaion.findField("total").getValue()))
				.customFields(notificaion.getFieldsAsMap()).build();
		log.debug(payload);
		try {
			service.push(device.getDeviceToken(), payload);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// public static void main(String[] args) {
	// ApnsService service = null;
	// String p12fileName = "aps_prd_credentials.p12";
	// String passphrase = "9986872935";
	// String token =
	// "e4d6d3a6700ce4c3f4ca5cdffea41e41325528c26f996f27176242c93ee52693";
	// if (p12fileName.contains("_dev_")) {
	// service = APNS
	// .newService()
	// .withCert(
	// IOSPNotifier.class.getResourceAsStream("/META-INF/"
	// + p12fileName), passphrase)
	// .withSandboxDestination().build(); // dev
	// } else {
	// service = APNS
	// .newService()
	// .withCert(
	// IOSPNotifier.class.getResourceAsStream("/META-INF/"
	// + p12fileName), passphrase)
	// .withProductionDestination().build(); // production
	// }
	// service.testConnection();
	//
	// String payload = APNS.newPayload().sound("default")
	// .alertBody("new Order").badge(7)
	// .customField("secret", "what do you think?")
	// .localizedKey("GAME_PLAY_REQUEST_FORMAT")
	// .localizedArguments("Jenna", "Frank").actionKey("Play").build();
	// service.push(token, payload);
	// System.out.println(payload + "===============");
	// }
}
