package com.mrkinnoapps.myordershopadmin.dao;

import java.util.List;

import com.mrkinnoapps.myordershopadmin.bean.dto.SearchFilter.SearchIn;
import com.mrkinnoapps.myordershopadmin.bean.dto.SearchResource;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;

public interface SearchDAO {

	List<SearchResource> search(String query, SearchIn searchIn, User user);
}
