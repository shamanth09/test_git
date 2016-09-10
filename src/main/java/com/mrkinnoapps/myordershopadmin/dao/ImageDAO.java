package com.mrkinnoapps.myordershopadmin.dao;

import com.mrkinnoapps.myordershopadmin.bean.entity.Image;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;

public interface ImageDAO {

	void save(Image image) throws EntityNotPersistedException;
	
	void delete(Image image);
}
