package com.mrk.myordershop.dao;

import com.mrk.myordershop.bean.Image;
import com.mrk.myordershop.exception.EntityNotPersistedException;

public interface ImageDAO {

	void save(Image image) throws EntityNotPersistedException;
	
	void delete(Image image);
}
