package com.mrk.myordershop.service;

import com.mrk.myordershop.bean.Token;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;

public interface TokenService {

	Token createToken(int timeout, Token.Type type, User user)
			throws EntityNotPersistedException;

	Token getToken(Token.Type type, String userId)
			throws EntityDoseNotExistException;

	Token get(String tokenString) throws EntityDoseNotExistException;

	void acceptToken(Token token);
}
