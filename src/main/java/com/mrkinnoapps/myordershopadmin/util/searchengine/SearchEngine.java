package com.mrkinnoapps.myordershopadmin.util.searchengine;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class SearchEngine {

	public List<Searcher> searcherList = new ArrayList<Searcher>();

	public void searcherRegister(Searcher searcher) {
		searcherList.add(searcher);
	}

	private boolean contains(String[] fields, String field) {
		for (String string : fields) {
			if(string.equals(field))
				return true;
		}
		return false;
	}
	
	public boolean isFiledAvailable(String[] fields, String field)
	{
		return contains(fields, field);
	}

	public List<SearchResult> search(String query, String[] fields) {
		List<SearchResult> list = new ArrayList<SearchResult>();
		for (Searcher searcher : this.searcherList) {
			for (String field : searcher.getFields()) {
				if(fields == null || fields.length < 1 || contains(fields, field)){
					List<SearchResult> tList=searcher.search(query, field);
					if(tList!=null && tList.size()>=1)
						list.addAll(tList);
				}
			}
		}
		return list;
	}
}
