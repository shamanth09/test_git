package com.mrk.myordershop.dao;

import java.util.List;

import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.dto.SearchFilter.SearchIn;
import com.mrk.myordershop.bean.dto.SearchResource;

public interface SearchDAO {

	List<SearchResource> search(String query, SearchIn searchIn, User user);
}
