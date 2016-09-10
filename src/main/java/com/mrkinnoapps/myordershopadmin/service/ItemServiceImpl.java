package com.mrkinnoapps.myordershopadmin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrkinnoapps.myordershopadmin.bean.entity.Item;
import com.mrkinnoapps.myordershopadmin.dao.ItemDAO;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private ItemDAO itemDAO;
	@Autowired
	private WholeSalerOrderService wholeSalerOrderService;

	@Override
	public void saveItem(Item item) throws EntityNotPersistedException{
		itemDAO.saveItem(item);
	}

	@Override
	public void update(Item item) {
		itemDAO.update(item);
	}

	@Override
	public Item getItem(int id) throws EntityDoseNotExistException {
		return itemDAO.getItem(id);
	}

	@Override
	public void deleteItem(Item item) {
		itemDAO.deleteItem(item);

	}
}
