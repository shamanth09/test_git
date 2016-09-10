package com.mrk.myordershop.log.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.mail.MailMessageFactory;
import com.mrk.myordershop.config.annotation.PersistTransactional;
import com.mrk.myordershop.config.annotation.ReadTransactional;
import com.mrk.myordershop.dao.UserDaoImpl;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.log.bean.LoginHistory;
import com.mrk.myordershop.log.bean.dto.LoginHistoryFilter;
import com.mrk.myordershop.log.constant.Attempt;
import com.mrk.myordershop.log.dao.LoginHistroryDAO;

@Component
public class LoginHistoryServiceImpl implements LoginHistoryService {

	private static final Logger log = Logger
			.getLogger(LoginHistoryServiceImpl.class);

	private static final int MAXIMUM_ELEMENT_POSISTION = 4;
	@Autowired
	private LoginHistroryDAO loginHistroryDAO;
	@Autowired
	private UserDaoImpl userDaoImpl;
	@Autowired
	@Qualifier("mailMessageFactory")
	MailMessageFactory mailMessageFactory;

	@Override
	@PersistTransactional
	public void loginSuccessAttempt(String userId, LoginHistory loginHistory) {
		try {
			User user = userDaoImpl.get(userId);
			loginHistory.setUserId(user.getId());
			loginHistory.setAttempt(Attempt.SUCCESS);
			LoginHistory fdhiHistory = getElementAt(MAXIMUM_ELEMENT_POSISTION,
					user.getId());
			if (fdhiHistory != null)
				loginHistroryDAO.delete(fdhiHistory);
			loginHistroryDAO.save(loginHistory);
			mailMessageFactory.getLoginAttemptMessage()
					.send(user, loginHistory);
		} catch (EntityDoseNotExistException e) {
			e.printStackTrace();
		}

	}

	@Override
	@PersistTransactional
	public void loginFailureAttempt(String username, LoginHistory loginHistory) {
		try {
			User user = userDaoImpl.getUserByMobieOrEmail(username);
			loginHistory.setUserId(user.getId());
			loginHistory.setAttempt(Attempt.FAILUR);
			LoginHistory fdhiHistory = getElementAt(MAXIMUM_ELEMENT_POSISTION,
					user.getId());
			if (fdhiHistory != null)
				loginHistroryDAO.delete(fdhiHistory);
			loginHistroryDAO.save(loginHistory);
			mailMessageFactory.getLoginAttemptMessage()
					.send(user, loginHistory);
		} catch (EntityDoseNotExistException e) {
		}
	}

	@Override
	@ReadTransactional
	public Page<LoginHistory> get(Pageable pageable, LoginHistoryFilter filter) {
		return loginHistroryDAO.get(pageable, filter);
	}

	private LoginHistory getElementAt(int position, String userId) {
		LoginHistory history = null;
		LoginHistoryFilter filter = new LoginHistoryFilter();
		filter.setUserId(userId);
		PageRequest pageable = new PageRequest(position, 1);
		Page<LoginHistory> page = loginHistroryDAO.get(pageable, filter);
		if (page.getContent() != null && !page.getContent().isEmpty()) {
			history = page.getContent().get(0);
		}

		return history;
	}
}
