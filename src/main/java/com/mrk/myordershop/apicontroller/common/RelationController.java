package com.mrk.myordershop.apicontroller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mrk.myordershop.assembler.RelationAssembler;
import com.mrk.myordershop.assembler.linkprovider.RelationLinkProvider;
import com.mrk.myordershop.bean.Relation;
import com.mrk.myordershop.bean.RelationSetting;
import com.mrk.myordershop.bean.Retailer;
import com.mrk.myordershop.bean.Supplier;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.dto.RelationFilter;
import com.mrk.myordershop.constant.RelationDirection;
import com.mrk.myordershop.constant.RelationStatus;
import com.mrk.myordershop.constant.Role;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.resource.Resource;
import com.mrk.myordershop.security.oauth.resolver.Owner;
import com.mrk.myordershop.service.RelationService;

import io.swagger.annotations.Api;

@Controller
@RequestMapping("/api/v1/relations")
@Api(value="Relations Details", tags={"Manage Relations"})
public class RelationController {

	@Autowired
	private RelationService relationService;
	@Autowired
	private RelationAssembler relationAssembler;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity get(@ModelAttribute RelationFilter filter,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String email,
			@RequestParam(required = false) String mobile,
			@RequestParam(required = false) Role userRole,
			@RequestParam(required = false) RelationDirection direction,
			@Owner User currentUser,
			@PageableDefault(value = 20) Pageable pageable,
			PagedResourcesAssembler pagedResourceAssembler) {

		if ((currentUser instanceof Retailer || currentUser instanceof Supplier)
				&& filter.getUserRole() == null)
			filter.setUserRole(Role.ROLE_WSALER);

		Page<Relation> page = relationService.findByUser(currentUser, pageable,
				filter);
		return new ResponseEntity(pagedResourceAssembler.toResource(page,
				relationAssembler, RelationLinkProvider.get(filter)),
				HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	public ResponseEntity<Resource> get(@PathVariable String userId,
			@Owner User currentUser) throws EntityDoseNotExistException {
		Relation relation = relationService.get(userId, currentUser);
		return new ResponseEntity<Resource>(
				relationAssembler.toResource(relation), HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<Resource> addRelation(@RequestBody User user,
			@Owner User currentUser) throws EntityDoseNotExistException {
		Relation relation = relationService.addRelation(currentUser, user);
		return new ResponseEntity<Resource>(
				relationAssembler.toResource(relation), HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/{userId}/status", method = RequestMethod.PUT)
	public ResponseEntity<Resource> changeStatus(@PathVariable String userId,
			@RequestBody Relation relation, @Owner User currentUser)
			throws EntityDoseNotExistException {
		if (RelationStatus.ACCEPTED.equals(relation.getStatus())) {
			Relation fdbrelation = relationService.acceptRelation(userId,
					currentUser);
			return new ResponseEntity<Resource>(
					relationAssembler.toResource(fdbrelation), HttpStatus.OK);
		} else if (RelationStatus.REJECTED.equals(relation.getStatus())) {
			Relation fdbrelation = relationService.rejectRelation(userId,
					currentUser);
			return new ResponseEntity<Resource>(
					relationAssembler.toResource(fdbrelation), HttpStatus.OK);
		}
		return new ResponseEntity<Resource>(HttpStatus.BAD_REQUEST);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/{userId}/settings", method = RequestMethod.PUT)
	public ResponseEntity<Resource> updateRelationSetting(
			@PathVariable String userId, @RequestBody RelationSetting setting,
			@Owner User currentUser) throws EntityDoseNotExistException {
		Relation fdbrelation = relationService.updateSetting(userId, setting,
				currentUser);
		return new ResponseEntity<Resource>(
				relationAssembler.toResource(fdbrelation), HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
	public ResponseEntity<Resource> removeRelation(@PathVariable String userId,
			@Owner User currentUser) throws EntityDoseNotExistException {
		relationService.removeRelation(userId, currentUser);
		return new ResponseEntity<Resource>(HttpStatus.OK);
	}
}
