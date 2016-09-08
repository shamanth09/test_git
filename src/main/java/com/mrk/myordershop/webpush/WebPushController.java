package com.mrk.myordershop.webpush;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.async.DeferredResult;

import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.security.oauth.resolver.Owner;

@Controller
@RequestMapping("/api/v1/pull")
public class WebPushController {

	@Autowired
	private WebPushService webPushService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public DeferredResult<ResponseEntity<WebPushResource>> pull(
			@Owner User user, @RequestParam("sequence") int sequence,
			@RequestParam("device_token") String deviceToken) {
		return webPushService.createDeviceInPool(user.getId(), deviceToken,
				sequence).getDeferredResult();
	}
}
