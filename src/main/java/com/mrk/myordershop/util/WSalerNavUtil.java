package com.mrk.myordershop.util;

import com.mrk.myordershop.bean.Address;

/**
 * WSalerNavUtil.java Naveen Apr 7, 2015
 */
public class WSalerNavUtil {

	public static String PARAM_FIRST_TYME = "&ft=true";
	public static String PARAM_HOME = "&hme";
	public static String PARAM_EMPTY_ADDRESS = "&ademty";
	private static String URL_PDETAIL = "/v1/ws/account/psnldetail?";
	private static String URL_ADDADDRESS = "/v1/ws/account/address/add?";
	private static String URL_HOME = "/v1/home?";

	private static boolean isAddressEmpty(Address address) {
		boolean isEmpty = true;
		if (address != null) {
			isEmpty = false;
			if (address.getStreet() == null || address.getStreet().equals(""))
				isEmpty = true;
			if (address.getCity() == null || address.getCity().equals(""))
				isEmpty = true;
			if (address.getState() == null || address.getState().equals(""))
				isEmpty = true;
		}
		return isEmpty;
	}

//	public static String getUserNavUrl(Cart cart, User user, String param) {
//		String redirectUrl = null;
//		if (cart != null && cart.getItems().size() < 1)
//			redirectUrl = URL_HOME;
//		if (user != null) {
//			if (user.getName() == null
//					|| user.getName().trim().equals(""))
//				redirectUrl = URL_PDETAIL + PARAM_FIRST_TYME + param;
//			if (user.getEmail() == null || user.getEmail().trim().equals(""))
//				redirectUrl = URL_PDETAIL + PARAM_FIRST_TYME + param;
//			if (user.getMobile() == null || user.getMobile().trim().equals(""))
//				redirectUrl = URL_PDETAIL + PARAM_FIRST_TYME + param;
//			if (isAddressEmpty(user.getAddress()))
//				if (redirectUrl != null)
//					redirectUrl += PARAM_EMPTY_ADDRESS;
//				else
//					redirectUrl = URL_ADDADDRESS + PARAM_FIRST_TYME + param
//							+ PARAM_EMPTY_ADDRESS;
//		}
//		return redirectUrl;
//	}

	public static String getNavUrl(String refererUrl) {
		String redirectUrl = null;
		if (refererUrl.indexOf(PARAM_HOME) > 1)
			redirectUrl = URL_HOME;
		if (refererUrl.indexOf(PARAM_EMPTY_ADDRESS) > 1) {
			redirectUrl = URL_ADDADDRESS;
			if (refererUrl.indexOf(PARAM_HOME) > 1)
				redirectUrl += PARAM_HOME;
		}
		return redirectUrl;
	}
}
