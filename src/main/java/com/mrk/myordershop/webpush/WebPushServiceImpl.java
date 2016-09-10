package com.mrk.myordershop.webpush;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import com.mrk.myordershop.bean.Notification;

@Service
public class WebPushServiceImpl implements WebPushService {

	private static final long TIMEOUT = 60000L;

	private static final Logger log = Logger
			.getLogger(WebPushServiceImpl.class);

	private Map<String, List<WebDevice>> devicePool = new HashMap<String, List<WebDevice>>();

	@Override
	public void send(String userId, Notification notificaion) {
		WebPushResource resource = new WebPushResource(notificaion.getTier()
				.toString(), notificaion.getMessage());
		resource.addFields(notificaion.getFieldsAsMap());

		log.debug("notification received for " + userId);

		if (devicePool.get(userId) != null)
			while (!devicePool.get(userId).isEmpty()) {
				WebDevice device = devicePool.get(userId).remove(0);
				resource.setSequence(device.getSequence());
				device.getDeferredResult().setResult(
						new ResponseEntity<WebPushResource>(resource,
								HttpStatus.OK));
				log.debug("notification sending for " + userId
						+ "with deviceToken " + device.getDeviceToken());
			}

	}

	@Override
	public WebDevice createDeviceInPool(String userId, String deviceToken,
			int sequence) {

		DeferredResult<ResponseEntity<WebPushResource>> deferredResult = new DeferredResult<ResponseEntity<WebPushResource>>(
				TIMEOUT, new ResponseEntity<WebPushResource>(
						getTimeOutResource(), HttpStatus.NO_CONTENT));

		WebDevice webDevice = new WebDevice(deviceToken, deferredResult,
				sequence);
		List<WebDevice> deviceList = devicePool.get(userId);
		deviceList = deviceList == null ? new ArrayList<WebDevice>()
				: deviceList;
		if (!deviceList.contains(webDevice))
			deviceList.remove(webDevice);
		deviceList.add(webDevice);
		log.trace("webDevice for " + userId + " added to pool");
		devicePool.put(userId, deviceList);
		return webDevice;
	}

	private WebPushResource getTimeOutResource() {
		return new WebPushResource("timeout", "connnection timeout in 60s");
	}
}
