package com.mrk.myordershop.log.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrk.myordershop.log.bean.LoginHistory;
import com.mrk.myordershop.log.bean.dto.LoginHistoryFilter;

public interface LoginHistoryService {

	void loginSuccessAttempt(String username, LoginHistory loginHistory);

	void loginFailureAttempt(String username, LoginHistory loginHistory);

//	LoginHistory update(int id, LoginHistory history);

	Page<LoginHistory> get(Pageable pageable, LoginHistoryFilter filter);
}
