package com.mrkinnoapps.myordershopadmin.service;

import java.util.List;


import com.mrkinnoapps.myordershopadmin.util.searchengine.SearchResult;

public interface SearchService {
	
	  public List<SearchResult> search(String query, String[] fields);
	  
}
