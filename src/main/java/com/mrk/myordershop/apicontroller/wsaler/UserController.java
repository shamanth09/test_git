package com.mrk.myordershop.apicontroller.wsaler;

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
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.dto.UserFilter;
import com.mrk.myordershop.bean.dto.UserSearchFilter;
import com.mrk.myordershop.bean.dto.UserSearchResource;
import com.mrk.myordershop.constant.Role;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.security.oauth.resolver.Owner;
import com.mrk.myordershop.service.UserService;

import io.swagger.annotations.Api;

@Controller("wsalerUserController")
@RequestMapping(value = "/api/v1/ws/users")
@Api(value="User Details", tags={"User Details"})
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private UserAssembler userAssembler;
	@Autowired
	private UserSearchAssembler userSearchAssembler;

//	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
//	public ResponseEntity<Resource> getUser(@PathVariable String userId)
//			throws EntityDoseNotExistException {
//		User user = userService.getUser(userId);
//		Resource<User> userResource = userAssembler.toResource(user);
//		return new ResponseEntity<Resource>(userResource, HttpStatus.OK);
//	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity findUser(@ModelAttribute UserFilter filter,
			@PageableDefault(size = 10) Pageable pageable,
			@Owner Wholesaler wholesaler,
			PagedResourcesAssembler pagedResourcesAssembler,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "mobile", required = false) String mobile,
			@RequestParam(value = "role", required = false) Role role)
			throws EntityDoseNotExistException {
		if (role != null
				&& (role.equals(Role.ROLE_RETAIL) || role
						.equals(Role.ROLE_SUPPLIER))) {
			Page<User> users = userService.find(filter, pageable, wholesaler);
			return new ResponseEntity(pagedResourcesAssembler.toResource(users,
					userAssembler, UserLinkProvider.get(filter)), HttpStatus.OK);
		} else
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<PagedResources<UserSearchResource>> searchOnOrder(
			@Owner Wholesaler user,@ModelAttribute UserSearchFilter filter, @RequestParam("query") String query,
			@RequestParam("role") Role role,
			@PageableDefault(size = 10) Pageable pageable,
			PagedResourcesAssembler<UserSearchResource> pagedResourcesAssembler) {
		if (role.equals(Role.ROLE_RETAIL) || role.equals(Role.ROLE_SUPPLIER)) {
			List<UserSearchResource> result = userService.search(filter,
					user, pageable);
			return new ResponseEntity<PagedResources<UserSearchResource>>(
					pagedResourcesAssembler.toResource(
							new PageImpl<UserSearchResource>(result),
							userSearchAssembler), HttpStatus.OK);
		} else
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
	}
}
