package com.mrk.myordershop.log.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrk.myordershop.exception.EntityNotPersistedException;
import com.mrk.myordershop.log.bean.LoginHistory;
import com.mrk.myordershop.log.bean.dto.LoginHistoryFilter;

public interface LoginHistroryDAO {

	void save(LoginHistory loginHistory) throws EntityNotPersistedException;

	void update(LoginHistory loginHistory);

	void delete(LoginHistory loginHistory);

	Page<LoginHistory> get(Pageable pageable, LoginHistoryFilter filter);

}
