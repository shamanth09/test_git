package com.mrkinnoapps.myordershopadmin.dao;

import java.util.List;

import com.mrkinnoapps.myordershopadmin.bean.entity.Token;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;

public interface TokenDAO {

	void save(Token token) throws EntityNotPersistedException;

	Token get(Integer id) throws EntityDoseNotExistException;

	Token getByToken(String token) throws EntityDoseNotExistException;

	Token getByUserAndType(String userId, Token.Type type)
			throws EntityDoseNotExistException;

	List<Token> findByUser(String userId);

	void update(Token token) throws EntityNotPersistedException;

	void delete(Token token) throws EntityNotPersistedException;

}
