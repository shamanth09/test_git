package com.mrkinnoapps.myordershopadmin.service;

import com.mrkinnoapps.myordershopadmin.bean.entity.Item;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;

public interface ItemService {

	void saveItem(Item item) throws EntityNotPersistedException;

	Item getItem(int id) throws EntityDoseNotExistException;
	
	void deleteItem(Item item);
	
	public void update(Item item);
}
