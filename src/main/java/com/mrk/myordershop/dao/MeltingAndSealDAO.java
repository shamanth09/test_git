package com.mrk.myordershop.dao;

import java.util.List;

import com.mrk.myordershop.bean.MeltingAndSeal;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;

public interface MeltingAndSealDAO {

	void save(MeltingAndSeal meltingAndSeal) throws EntityNotPersistedException;

	MeltingAndSeal get(int id) throws EntityDoseNotExistException;

	MeltingAndSeal get(String seal, int melting, Wholesaler wholesaler)
			throws EntityDoseNotExistException;

	List<MeltingAndSeal> find(Wholesaler wholesaler);

	void delete(MeltingAndSeal meltingAndSeal);
}
