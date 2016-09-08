package com.mrk.myordershop.dao;

import java.util.List;

import com.mrk.myordershop.bean.Measurement;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;

public interface MeasurementDAO {

	void save(Measurement measurement) throws EntityNotPersistedException;

	Measurement get(int id) throws EntityDoseNotExistException;

	Measurement get(Measurement.v name) throws EntityDoseNotExistException;

	void delete(Measurement measurement);

	List<Measurement> fine();
}
