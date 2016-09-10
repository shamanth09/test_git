package com.mrk.myordershop.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrk.myordershop.bean.Token;
import com.mrk.myordershop.bean.Token.Type;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.config.annotation.PersistTransactional;
import com.mrk.myordershop.config.annotation.ReadTransactional;
import com.mrk.myordershop.constant.ActiveFlag;
import com.mrk.myordershop.dao.TokenDAO;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;
import com.mrk.myordershop.exception.InvalidDataException;

@Service
public class TokenServiceImpl implements TokenService {

	@Autowired
	private TokenDAO tokenDAO;

	@Override
	@PersistTransactional
	public Token createToken(int timeout, Type type, User user)
			throws EntityNotPersistedException {

		Token token = null;
		try {
			token = tokenDAO.getByUserAndType(user.getId(), type);
			token.setUser(user);
			token.setActiveFlag(ActiveFlag.ACTIVE);
			token.setType(type);
			token.setCreateTimestamp(new Date());
			token.setTimeout(timeout);
			token.setToken(UUID.randomUUID().toString());
			tokenDAO.update(token);
		} catch (EntityDoseNotExistException e) {
			token = new Token();
			token.setUser(user);
			token.setActiveFlag(ActiveFlag.ACTIVE);
			token.setType(type);
			token.setCreateTimestamp(new Date());
			token.setTimeout(timeout);
			token.setToken(UUID.randomUUID().toString());
			tokenDAO.save(token);
		}
		return token;
	}

	@Override
	@ReadTransactional
	public Token get(String tokenString) throws EntityDoseNotExistException {
		Token token = tokenDAO.getByToken(tokenString);
		if (token.isExpire()) {
			throw new InvalidDataException("token expired");
		}
		return token;
	}

	@Override
	@PersistTransactional
	public void acceptToken(Token token) {
		token.setActiveFlag(ActiveFlag.ACCEPT);
		tokenDAO.update(token);
	}

	@Override
	@ReadTransactional
	public Token getToken(Type type, String userId)
			throws EntityDoseNotExistException {
		return tokenDAO.getByUserAndType(userId, type);
	}

}
