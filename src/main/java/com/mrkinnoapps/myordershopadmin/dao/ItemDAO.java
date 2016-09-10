package com.mrkinnoapps.myordershopadmin.dao;

import com.mrkinnoapps.myordershopadmin.bean.entity.Item;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;

public interface ItemDAO {

	void saveItem(Item item) throws EntityNotPersistedException;

	Item getItem(int id) throws EntityDoseNotExistException;

	void deleteItem(Item item);

	public void update(Item item);

	Item getByOrderId(int orderId);
}
