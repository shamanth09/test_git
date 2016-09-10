package com.mrkinnoapps.myordershopadmin.controller.admin;

import java.util.ArrayList;
import java.util.EnumSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mrkinnoapps.myordershopadmin.bean.constant.RelationStatus;
import com.mrkinnoapps.myordershopadmin.bean.dto.RelationFilter;
import com.mrkinnoapps.myordershopadmin.bean.entity.Relation;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.service.RelationService;
import com.mrkinnoapps.myordershopadmin.service.UserService;

@Controller
@RequestMapping("/v1/admin/relations")
public class RelationController {

	private static Logger log = LoggerFactory.getLogger(RelationController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private RelationService relationService;

	@RequestMapping(value = "/xhr/user-relation/{userId}", method = RequestMethod.GET)
	public String userRelation(@PathVariable("userId") String userId, ModelMap map, RelationFilter filter)
			throws EntityDoseNotExistException {
		User currentUser = userService.get(userId);
		map.addAttribute("user", currentUser);
		map.addAttribute("userRelations", relationService.findByUser(currentUser, null, filter));
		// map.addAttribute("userRelations", relationService.relations(userId));
		return "admin/relations/xhr/list";
	}

	@RequestMapping(value = "/xhr/{currentUser}/user-relation-edit/{Id}", method = RequestMethod.GET)
	public String getXHRUserRelationEdit(@PathVariable("Id") Long relationId,
			@PathVariable("currentUser") String currentUserId, ModelMap map) throws EntityDoseNotExistException {
		User currentUser = userService.get(currentUserId);
		Relation relation = relationService.getRelation(relationId, currentUser);
		map.addAttribute("relation", relation);
		map.addAttribute("relationStatuses", new ArrayList<RelationStatus>(EnumSet.allOf(RelationStatus.class)));
		map.addAttribute("currentUser", currentUser);
		return "admin/relations/xhr/relationEdit";
	}

	@RequestMapping(value = "/xhr/{currentUser}/user-relation-edit/{Id}", method = RequestMethod.POST)
	public ResponseEntity getXHRUserRelationEdit(@PathVariable("Id") Long relationId,
			@PathVariable("currentUser") String currentUserId, @ModelAttribute Relation relation)
					throws EntityDoseNotExistException {
		User currentUser = userService.get(currentUserId);
		relationService.update(relation, currentUser);
		return new ResponseEntity(currentUser, HttpStatus.OK);
	}

	@RequestMapping(value = "/xhr/add-relation/{userId}/{userRole}", method = RequestMethod.GET)
	public String addRelation(@PathVariable("userId") String userId,@PathVariable("userRole") String userRole, ModelMap map, RelationFilter filter)
			throws EntityDoseNotExistException {
		User currentUser = userService.get(userId);
		map.addAttribute("user", currentUser);
		map.addAttribute("userRole",userRole);
		return "admin/relations/xhr/add";
	}

	@RequestMapping(value = "/xhr/add-relation/{userId}", method = RequestMethod.POST)
	public ResponseEntity addRelation(@PathVariable("userId") String userId, @RequestParam String secondaryUser)
			throws EntityDoseNotExistException {
		User currentUser = userService.get(userId);
		User relatedUser = userService.get(secondaryUser);
		relationService.addRelation(currentUser, relatedUser);
		// map.addAttribute("userRelations", relationService.relations(userId));
		return new ResponseEntity(currentUser, HttpStatus.OK);
	}

}
