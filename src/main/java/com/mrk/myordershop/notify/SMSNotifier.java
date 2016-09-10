package com.mrk.myordershop.notify;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.mrk.myordershop.bean.Device;
import com.mrk.myordershop.bean.Notification;

public class SMSNotifier implements Notifier {
	private Logger log = Logger.getLogger(AndroidNotifier.class);

	private final static String SERVER = "http://123.63.33.43/blank/sms/user/urlsmstemp.php";

	private String webClient;
	private String senderid;
	private String username;
	private String password;

	public SMSNotifier(String senderid, String username, String password) {
		this.senderid = senderid;
		this.username = username;
		this.password = password;
	}

	@Override
	public void send(Device device, Notification notificaion) {
		StringBuilder message = new StringBuilder();
		message.append(notificaion.getMessage());
		message.append(this.webClient != null && !this.webClient.trim().equals("") ? "\n" + this.webClient
				: "\n http://www.myordershop.com");

		MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
		body.add("dest_mobileno", device.getDeviceToken());
		body.add("username", this.username.trim());
		body.add("pass", this.password.trim());
		body.add("senderid", this.senderid.trim());
		body.add("message", message.toString());
		body.add("response", "Y");

		RestTemplate template = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(body, headers);
		String result = template.postForObject(SERVER, entity, String.class);
		log.debug("sms" + body + "==>" + result);
	}

	public String getWebClient() {
		return webClient;
	}

	public void setWebClient(String webClient) {
		this.webClient = webClient;
	}

	// public static void main(String[] args) {
	// SMSNotifier notifier = new SMSNotifier("KAPMSG","kapbulk","kap@user!23");
	// Device d = new Device();
	// d.setDeviceToken("8884307179");
	// Notification no = new Notification();
	// no.setMessage("Hi Naveen");
	// notifier.send(d, no);
	// }

}
