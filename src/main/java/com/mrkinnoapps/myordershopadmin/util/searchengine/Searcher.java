package com.mrkinnoapps.myordershopadmin.util.searchengine;

import java.util.List;

public interface Searcher {
	public List<SearchResult> search(String query, String field);
	
	public String[] getFields();
}
