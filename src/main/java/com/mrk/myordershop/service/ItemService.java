package com.mrk.myordershop.service;

import com.mrk.myordershop.bean.Item;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;

public interface ItemService {

	void saveItem(Item item) throws EntityNotPersistedException;

	Item getItem(int id) throws EntityDoseNotExistException;
	
	public void update(Item item);
}
