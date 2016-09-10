package com.mrkinnoapps.myordershopadmin.dao;

import java.util.List;

import com.mrkinnoapps.myordershopadmin.bean.entity.MeltingAndSeal;
import com.mrkinnoapps.myordershopadmin.bean.entity.Wholesaler;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;

public interface MeltingAndSealDAO {

	void save(MeltingAndSeal meltingAndSeal) throws EntityNotPersistedException;

	MeltingAndSeal get(int id) throws EntityDoseNotExistException;

	MeltingAndSeal get(String seal, int melting, Wholesaler wholesaler)
			throws EntityDoseNotExistException;

	List<MeltingAndSeal> find(Wholesaler wholesaler);

	void delete(MeltingAndSeal meltingAndSeal);
	
	void update(MeltingAndSeal seal);
}
