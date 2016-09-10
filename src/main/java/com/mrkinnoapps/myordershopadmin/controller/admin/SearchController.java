package com.mrkinnoapps.myordershopadmin.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mrkinnoapps.myordershopadmin.service.SearchService;
import com.mrkinnoapps.myordershopadmin.util.searchengine.SearchResult;


@Controller
@RequestMapping("v1/admin/search")
public class SearchController {
	
	@Autowired private SearchService searchservice;
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity test(@RequestParam String q, @RequestParam(required=false) String[] fields) {
		System.out.println(" coming inside");
		List<SearchResult> list=searchservice.search(q, fields);
		return new ResponseEntity(list,HttpStatus.OK);
	}
}
