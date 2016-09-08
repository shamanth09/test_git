package com.mrk.myordershop.apicontroller.retail;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mrk.myordershop.assembler.UserAssembler;
import com.mrk.myordershop.assembler.UserSearchAssembler;
import com.mrk.myordershop.assembler.linkprovider.UserLinkProvider;
import com.mrk.myordershop.bean.Retailer;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.dto.UserFilter;
import com.mrk.myordershop.bean.dto.UserSearchFilter;
import com.mrk.myordershop.bean.dto.UserSearchResource;
import com.mrk.myordershop.constant.Role;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.security.oauth.resolver.Owner;
import com.mrk.myordershop.service.UserService;

import io.swagger.annotations.Api;

@Controller("retailerUserController")
@RequestMapping(value = "/api/v1/rt/users")
@Api(value="User Details", tags={"User Details"})
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private UserAssembler userAssembler;
	@Autowired
	private UserSearchAssembler userSearchAssembler;

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<PagedResources<UserSearchResource>> searchOnOrder(
			@Owner Retailer user, @ModelAttribute UserSearchFilter filter,
			@RequestParam("query") String query,
			@PageableDefault(size = 10) Pageable pageable,
			PagedResourcesAssembler<UserSearchResource> pagedResourcesAssembler) {
		filter.setRole(Role.ROLE_WSALER);
		List<UserSearchResource> result = userService.search(filter, user,
				pageable);
		return new ResponseEntity<PagedResources<UserSearchResource>>(
				pagedResourcesAssembler.toResource(
						new PageImpl<UserSearchResource>(result),
						userSearchAssembler), HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity findUser(@ModelAttribute UserFilter filter,
			@PageableDefault(size = 10) Pageable pageable,
			@Owner Retailer retailer,
			PagedResourcesAssembler pagedResourcesAssembler,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "mobile", required = false) String mobile)
			throws EntityDoseNotExistException {
		if (filter != null)
			filter.setRole(Role.ROLE_WSALER);
		Page<User> users = userService.find(filter, pageable, retailer);
		return new ResponseEntity(pagedResourcesAssembler.toResource(users,
				userAssembler, UserLinkProvider.get(filter)), HttpStatus.OK);
	}
}
