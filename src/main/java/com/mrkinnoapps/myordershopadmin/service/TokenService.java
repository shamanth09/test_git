package com.mrkinnoapps.myordershopadmin.service;

import java.util.List;

import com.mrkinnoapps.myordershopadmin.bean.entity.Token;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;

public interface TokenService {

	Token createToken(int timeout, Token.Type type, User user)
			throws EntityNotPersistedException;

	Token getToken(Token.Type type, String userId)
			throws EntityDoseNotExistException;

	Token get(String tokenString) throws EntityDoseNotExistException;

	void acceptToken(Token token) throws EntityNotPersistedException ;
	
	List<Token> findByUser(String userId);
	
	void delete(int tokenId) throws EntityDoseNotExistException, EntityNotPersistedException;
}
