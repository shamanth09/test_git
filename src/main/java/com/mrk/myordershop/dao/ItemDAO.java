package com.mrk.myordershop.dao;

import com.mrk.myordershop.bean.Item;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;

public interface ItemDAO {

	void save(Item item) throws EntityNotPersistedException;

	Item get(int id) throws EntityDoseNotExistException;

	void update(Item item);

}
