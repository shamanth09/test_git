package com.mrk.myordershop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrk.myordershop.bean.MeltingAndSeal;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.config.annotation.ReadTransactional;
import com.mrk.myordershop.dao.MeltingAndSealDAO;
import com.mrk.myordershop.dao.UserDAO;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

@Service
public class MeltingAndSealServiceImpl implements MeltingAndSealService {

	@Autowired
	private MeltingAndSealDAO meltingAndSealDAO;
	@Autowired
	private UserDAO userDAO;

	@Override
	@ReadTransactional
	public MeltingAndSeal get(int id) throws EntityDoseNotExistException {
		return meltingAndSealDAO.get(id);
	}

	@Override
	@ReadTransactional
	public List<MeltingAndSeal> findByWholesalerId(String wholesalerId)
			throws EntityDoseNotExistException {
		Wholesaler wholesaler = (Wholesaler) userDAO.get(wholesalerId);
		return this.find(wholesaler);
	}

	@Override
	@ReadTransactional
	public List<MeltingAndSeal> find(Wholesaler wholesaler)
			throws EntityDoseNotExistException {
		return meltingAndSealDAO.find(wholesaler);
	}

}
