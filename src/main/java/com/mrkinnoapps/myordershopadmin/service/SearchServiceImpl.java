package com.mrkinnoapps.myordershopadmin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrkinnoapps.myordershopadmin.util.searchengine.SearchEngine;
import com.mrkinnoapps.myordershopadmin.util.searchengine.SearchResult;

@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SearchEngine engine;

	@Override
	public List<SearchResult> search(String query, String[] fields) {
		return engine.search(query, fields);
	}

}
