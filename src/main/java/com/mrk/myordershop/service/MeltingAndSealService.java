package com.mrk.myordershop.service;

import java.util.List;

import com.mrk.myordershop.bean.MeltingAndSeal;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

public interface MeltingAndSealService {

	MeltingAndSeal get(int id) throws EntityDoseNotExistException;

	List<MeltingAndSeal> findByWholesalerId(String wholesalerId)
			throws EntityDoseNotExistException;

	List<MeltingAndSeal> find(Wholesaler wholesaler)
			throws EntityDoseNotExistException;

}
