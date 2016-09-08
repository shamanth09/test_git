package com.mrk.myordershop.webpush;

import java.util.Date;

import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

public class WebDevice {

	private int sequence;

	private String deviceToken;

	private DeferredResult<ResponseEntity<WebPushResource>> deferredResult;

	private Date addedTimestamp;

	public WebDevice(String deviceToken,
			DeferredResult<ResponseEntity<WebPushResource>> deferred,
			int sequence) {
		this.deviceToken = deviceToken;
		this.deferredResult = deferred;
		this.sequence = sequence;
		this.addedTimestamp = new Date();
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public DeferredResult<ResponseEntity<WebPushResource>> getDeferredResult() {
		return deferredResult;
	}

	public void setDeferredResult(
			DeferredResult<ResponseEntity<WebPushResource>> deferredResult) {
		this.deferredResult = deferredResult;
	}

	public Date getAddedTimestamp() {
		return addedTimestamp;
	}

	public void setAddedTimestamp(Date addedTimestamp) {
		this.addedTimestamp = addedTimestamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((deviceToken == null) ? 0 : deviceToken.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WebDevice other = (WebDevice) obj;
		if (deviceToken == null) {
			if (other.deviceToken != null)
				return false;
		} else if (!deviceToken.equals(other.deviceToken))
			return false;
		return true;
	}

}
