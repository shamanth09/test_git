package com.mrk.myordershop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrk.myordershop.bean.Item;
import com.mrk.myordershop.config.annotation.PersistTransactional;
import com.mrk.myordershop.config.annotation.ReadTransactional;
import com.mrk.myordershop.dao.ItemDAO;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private ItemDAO itemDAO;
	@Autowired
	private WholeSalerOrderService wholeSalerOrderService;

	@Override
	@PersistTransactional
	public void saveItem(Item item) throws EntityNotPersistedException {
		itemDAO.save(item);
	}

	@Override
	@PersistTransactional
	public void update(Item item) {
		itemDAO.update(item);
	}

	@Override
	@ReadTransactional
	public Item getItem(int id) throws EntityDoseNotExistException {
		return itemDAO.get(id);
	}

}
