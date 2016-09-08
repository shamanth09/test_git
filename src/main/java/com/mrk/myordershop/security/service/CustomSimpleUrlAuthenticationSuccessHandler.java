package com.mrk.myordershop.security.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.UserRole;
import com.mrk.myordershop.config.annotation.ReadTransactional;
import com.mrk.myordershop.dao.UserDAO;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.service.ItemService;

public class CustomSimpleUrlAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	private static final Logger log = Logger.getLogger(CustomSimpleUrlAuthenticationSuccessHandler.class);

	@Autowired
	private UserDAO userDAO;
	@Autowired
	private ItemService orderService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		HttpSession session = request.getSession();
		String userId = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal())
				.getUsername();

		User user = null;
		try {
			user = resoveUser(userId, session);
		} catch (EntityDoseNotExistException e) {
			throw new IOException();
		}
		boolean isRedirected = false;
		for (UserRole role : user.getUserRoles()) {
			// if (role.getRole().equals(Role.ROLE_RETAIL))
			// isRedirected = resolveFristPage(user, role.getRole(), request,
			// response);
		}
		if (!isRedirected)
			super.onAuthenticationSuccess(request, response, authentication);
	}

	/**
	 * Naveen Apr 4, 2015 void
	 * 
	 */
	// private boolean resolveFristPage(User user, Role role,
	// HttpServletRequest request, HttpServletResponse response) {
	// boolean isRedirected = false;
	// if (role.equals(Role.ROLE_RETAIL)) {
	// try {
	// if (RetailerNavUtil.getUserNavUrl(null, user,
	// RetailerNavUtil.PARAM_HOME) != null) {
	// response.sendRedirect("../"
	// + RetailerNavUtil.getUserNavUrl(null, user,
	// RetailerNavUtil.PARAM_HOME));
	// isRedirected = true;
	// }
	// } catch (IOException e1) {
	// e1.printStackTrace();
	// }
	// } else if (role.equals(Role.ROLE_WSALER)) {
	// try {
	// if (WSalerNavUtil.getUserNavUrl(null, user,
	// RetailerNavUtil.PARAM_HOME) != null) {
	// response.sendRedirect("../"
	// + WSalerNavUtil.getUserNavUrl(null, user,
	// WSalerNavUtil.PARAM_HOME));
	// isRedirected = true;
	// }
	// } catch (IOException e1) {
	// e1.printStackTrace();
	// }
	// } else if (role.equals(Role.ROLE_SUPPLIER)) {
	// try {
	// if (SupplierNavUtil.getUserNavUrl(null, user,
	// RetailerNavUtil.PARAM_HOME) != null) {
	// response.sendRedirect("../"
	// + SupplierNavUtil.getUserNavUrl(null, user,
	// SupplierNavUtil.PARAM_HOME));
	// isRedirected = true;
	// }
	// } catch (IOException e1) {
	// e1.printStackTrace();
	// }
	// }
	// return isRedirected;
	// }

	@ReadTransactional
	private User resoveUser(String userId, HttpSession session) throws EntityDoseNotExistException {
		User user = userDAO.get(userId);
		session.setAttribute("currentUser", user);
		log.debug("_____Auth Success_____ Hi, " + user.getName() + " " + user.getName());
		return user;
	}

}
