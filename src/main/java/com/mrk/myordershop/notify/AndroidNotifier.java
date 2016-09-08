package com.mrk.myordershop.notify;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mrk.myordershop.bean.Device;
import com.mrk.myordershop.bean.Notification;

public class AndroidNotifier implements Notifier {
	private Logger log = Logger.getLogger(AndroidNotifier.class);

	private final static String GCM_SERVER = "https://android.googleapis.com/gcm/send";

	private String apiKey;

	public AndroidNotifier(String apiKey) {
		this.apiKey = apiKey;
	}

	@Override
	public void send(Device device, Notification notificaion) {
		log.debug(device.getDeviceToken() + "================sending");

		GCMMessageBody body = new GCMMessageBody(device.getDeviceToken());
		body.setData(notificaion.getMessage(), notificaion.getFieldsAsMap());

		RestTemplate template = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "key=" + apiKey);
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<GCMMessageBody> entity = new HttpEntity<GCMMessageBody>(
				body, headers);
		ResponseEntity<String> result = template.postForEntity(GCM_SERVER,
				entity, String.class);
		System.out.println(result.getBody());
	}

	private class GCMMessageBody {

		public GCMMessageBody(String to) {
			this.to = to;
		}

		private String to;

		private Map<String, Object> data;

		public String getTo() {
			return to;
		}

		public void setTo(String to) {
			this.to = to;
		}

		public Map<String, Object> getData() {
			return data;
		}

		public void setData(String message, Map<String, Object> data) {
			this.data = data;
			this.data.put("message", message);
		}

	}
	
	public class GCMResponse{
		@JsonProperty("success")
		private int success;
		
		@JsonProperty("failure")
		private int failure;
		
		@JsonProperty("canonical_ids")
		private int canonicalIds;
		
		
		@JsonProperty("multicast_id")
		private int multicastId;
		
		
		@JsonProperty("results")
		private List<Map<String, Object>> results;


		public GCMResponse() { }
		
		public int getSuccess() {
			return success;
		}


		public void setSuccess(int success) {
			this.success = success;
		}


		public int getFailure() {
			return failure;
		}


		public void setFailure(int failure) {
			this.failure = failure;
		}


		public int getCanonicalIds() {
			return canonicalIds;
		}


		public void setCanonicalIds(int canonicalIds) {
			this.canonicalIds = canonicalIds;
		}


		public int getMulticastId() {
			return multicastId;
		}


		public void setMulticastId(int multicastId) {
			this.multicastId = multicastId;
		}


		public List<Map<String, Object>> getResults() {
			return results;
		}


		public void setResults(List<Map<String, Object>> results) {
			this.results = results;
		}


		@Override
		public String toString() {
			return "GCMResponse [success=" + success + ", failure=" + failure
					+ ", canonicalIds=" + canonicalIds + ", multicastId="
					+ multicastId + ", results=" + results + "]";
		}
		
		
	}

//	public static void main(String[] args) {
//		AndroidNotifier notifier = new AndroidNotifier(
//				"AIzaSyAfQSgCODHSqXSfPyoC4maZsg5FAxnRK8g");
//		notifier.send(null, null);

		// try {
		// // Create connection to send GCM Message request.
		// URL url = new URL("https://android.googleapis.com/gcm/send");
		// HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// conn.setRequestProperty("Authorization",
		// "key=AIzaSyAfQSgCODHSqXSfPyoC4maZsg5FAxnRK8g");
		// conn.setRequestProperty("Content-Type", "application/json");
		// conn.setRequestMethod("POST");
		// conn.setDoOutput(true);
		//
		// // Send GCM message content.
		// OutputStream outputStream = conn.getOutputStream();
		// outputStream
		// .write("{\"to\":\"f-KZYVi65YU:APA91bEDQSEM9JiJubTe_gSdlXLbBCGL3smU10JaqG8C6Lkxg-n4tFgYDfnEis3ZbS3ZQpkitQl4e_iRyx9bwrJIuaGJ2aGa2DU7HoV1vlqSC8bBaeZZNTKnsHBGEcm0oiHVx3ZEizYg\",\"data\":{\"message\":\"aaa\"}}"
		// .getBytes());
		//
		// // Read GCM response.
		// InputStream inputStream = conn.getInputStream();
		// String resp = IOUtils.toString(inputStream);
		// System.out.println(resp);
		// System.out
		// .println("Check your device/emulator for notification or logcat for "
		// + "confirmation of the receipt of the GCM message.");
		// } catch (IOException e) {
		// System.out.println("Unable to send GCM message.");
		// System.out
		// .println("Please ensure that API_KEY has been replaced by the server "
		// +
		// "API key, and that the device's registration token is correct (if specified).");
		// e.printStackTrace();
		// }
//	}
}
