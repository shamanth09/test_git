package com.mrkinnoapps.myordershopadmin.service;

import java.util.List;

import com.mrkinnoapps.myordershopadmin.bean.entity.MeltingAndSeal;
import com.mrkinnoapps.myordershopadmin.bean.entity.Wholesaler;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;

public interface MeltingAndSealService {

	MeltingAndSeal get(int id) throws EntityDoseNotExistException;

	List<MeltingAndSeal> findByWholesalerId(String wholesalerId)
			throws EntityDoseNotExistException;

	List<MeltingAndSeal> find(Wholesaler wholesaler)
			throws EntityDoseNotExistException;

	MeltingAndSeal updateMeltAndSeal(MeltingAndSeal meltingAndSeal, String userId) throws EntityDoseNotExistException, EntityNotPersistedException;

}
