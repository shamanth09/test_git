package com.mrk.myordershop.apicontroller.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mrk.myordershop.assembler.CatalogDesignAssembler;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.resource.Resource;
import com.mrk.myordershop.service.CatalogService;

import io.swagger.annotations.Api;

@Controller
@RequestMapping("/api/v1")
@Api(value="Catalog", tags={"Catalog Details"})
public class CatalogController {

	@Autowired
	private CatalogService catalogService;

	@Autowired
	private CatalogDesignAssembler assembler;

	@RequestMapping(value = "/catalogs/{id}/pages", method = RequestMethod.GET)
	public ResponseEntity<List<Integer>> getPages(
			@PathVariable("id") Integer catalogId) {
		return new ResponseEntity<List<Integer>>(
				catalogService.groupByPageNo(catalogId), HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/designs", method = RequestMethod.GET)
	public ResponseEntity<PagedResources> findDesign(
			@RequestParam("catalogId") Integer catalogId,
			@RequestParam("page") Integer page,
			PagedResourcesAssembler pagedAssembler) {
		return new ResponseEntity<PagedResources>(pagedAssembler.toResource(
				catalogService.findDesignByPage(catalogId, page), assembler),
				HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/designs/{id}", method = RequestMethod.GET)
	public ResponseEntity<Resource> getDesign(@PathVariable("id") Integer id)
			throws EntityDoseNotExistException {
		return new ResponseEntity<Resource>(assembler.toResource(catalogService
				.getDesign(id)), HttpStatus.OK);
	}

	@RequestMapping(value = "/designs/{id}/images", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getDesignImage(@PathVariable("id") Integer id)
			throws EntityDoseNotExistException {
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.IMAGE_PNG);
		return new ResponseEntity<byte[]>(catalogService.getDesign(id)
				.getImage().getImageArray(), header, HttpStatus.OK);
	}

}
