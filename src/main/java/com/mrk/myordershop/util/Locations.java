package com.mrk.myordershop.util;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Locations {
	static String url = "http://ip-api.com/json/";
	static HttpClient client = new HttpClient();
	static ObjectMapper mapper = new ObjectMapper();

	public static Location getLocations(String ip) {
		Location location = new Location();

		GetMethod method = new GetMethod(url + ip);

		try {
			int statusCode = client.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + method.getStatusLine());
			}
			byte[] responseBody = method.getResponseBody();
			System.out.println(new String(responseBody));
			location = mapper.readValue(responseBody, Location.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return location;
	}

	public static class Location {

		private String as;
		private String city;
		private String country;
		private String countryCode;
		private String isp;
		private String lat;
		private String lon;
		private String org;
		private String query;
		private String region;
		private String regionName;
		private String status;
		private String timezone;
		private String zip;
		public String getAs() {
			return as;
		}
		public void setAs(String as) {
			this.as = as;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getCountry() {
			return country;
		}
		public void setCountry(String country) {
			this.country = country;
		}
		public String getCountryCode() {
			return countryCode;
		}
		public void setCountryCode(String countryCode) {
			this.countryCode = countryCode;
		}
		public String getIsp() {
			return isp;
		}
		public void setIsp(String isp) {
			this.isp = isp;
		}
		public String getLat() {
			return lat;
		}
		public void setLat(String lat) {
			this.lat = lat;
		}
		public String getLon() {
			return lon;
		}
		public void setLon(String lon) {
			this.lon = lon;
		}
		public String getOrg() {
			return org;
		}
		public void setOrg(String org) {
			this.org = org;
		}
		public String getQuery() {
			return query;
		}
		public void setQuery(String query) {
			this.query = query;
		}
		public String getRegion() {
			return region;
		}
		public void setRegion(String region) {
			this.region = region;
		}
		public String getRegionName() {
			return regionName;
		}
		public void setRegionName(String regionName) {
			this.regionName = regionName;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getTimezone() {
			return timezone;
		}
		public void setTimezone(String timezone) {
			this.timezone = timezone;
		}
		public String getZip() {
			return zip;
		}
		public void setZip(String zip) {
			this.zip = zip;
		}
		@Override
		public String toString() {
			return "Location [as=" + as + ", city=" + city + ", country="
					+ country + ", countryCode=" + countryCode + ", isp=" + isp
					+ ", lat=" + lat + ", lon=" + lon + ", org=" + org
					+ ", query=" + query + ", region=" + region
					+ ", regionName=" + regionName + ", status=" + status
					+ ", timezone=" + timezone + ", zip=" + zip + "]";
		}
		
	}
}
