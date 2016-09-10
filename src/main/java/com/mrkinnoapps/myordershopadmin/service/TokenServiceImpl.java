package com.mrkinnoapps.myordershopadmin.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrkinnoapps.myordershopadmin.bean.constant.ActiveFlag;
import com.mrkinnoapps.myordershopadmin.bean.entity.Token;
import com.mrkinnoapps.myordershopadmin.bean.entity.Token.Type;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.dao.TokenDAO;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;
import com.mrkinnoapps.myordershopadmin.exception.InvalidDataException;

@Service
public class TokenServiceImpl implements TokenService {

	@Autowired
	private TokenDAO tokenDAO;

	@Override
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
	public Token get(String tokenString) throws EntityDoseNotExistException {
		Token token = tokenDAO.getByToken(tokenString);
		if (token.isExpire()) {
			throw new InvalidDataException("token expired");
		}
		return token;
	}

	@Override
	public void acceptToken(Token token) throws EntityNotPersistedException {
		token.setActiveFlag(ActiveFlag.ACCEPT);
		tokenDAO.update(token);
	}

	@Override
	public Token getToken(Type type, String userId)
			throws EntityDoseNotExistException {
		return tokenDAO.getByUserAndType(userId, type);
	}

	@Override
	public List<Token> findByUser(String userId) {
		
		return tokenDAO.findByUser(userId);
	}

	@Override
	public void delete(int tokenId) throws EntityDoseNotExistException, EntityNotPersistedException {
		Token token = tokenDAO.get(tokenId);
		tokenDAO.delete(token);
	}

	

}
