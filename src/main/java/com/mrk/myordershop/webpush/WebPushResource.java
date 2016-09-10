package com.mrk.myordershop.webpush;

import java.util.HashMap;
import java.util.Map;

public class WebPushResource {

	private String tier;

	private int sequence;

	private String message;

	private Map<String, Object> content = new HashMap<String, Object>();

	public WebPushResource(String tier) {
		this.tier = tier;
	}

	public WebPushResource(String tier, String message) {
		this.tier = tier;
		this.message = message;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}

	public Map<String, Object> getContent() {
		return content;
	}

	public void setContent(Map<String, Object> content) {
		this.content = content;
	}

	public void addFields(Map<String, Object> fields) {
		for (String key : fields.keySet()) {
			this.content.put(key, fields.get(key));
		}

	}

}
