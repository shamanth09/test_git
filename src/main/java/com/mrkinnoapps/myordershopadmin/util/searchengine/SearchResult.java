package com.mrkinnoapps.myordershopadmin.util.searchengine;

import java.io.Serializable;

import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;

public interface SearchResult {

	public String getField();

	public String getUrl() throws EntityDoseNotExistException;

	public Serializable getResultId();

	public String getLabel();
	
	public String  getResult() ;

}
