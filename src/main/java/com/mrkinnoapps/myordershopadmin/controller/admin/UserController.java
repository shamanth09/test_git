package com.mrkinnoapps.myordershopadmin.controller.admin;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mrkinnoapps.myordershopadmin.bean.constant.ActiveFlag;
import com.mrkinnoapps.myordershopadmin.bean.dto.NotRegisteredUser;
import com.mrkinnoapps.myordershopadmin.bean.dto.UserCredential;
import com.mrkinnoapps.myordershopadmin.bean.dto.UserFilter;
import com.mrkinnoapps.myordershopadmin.bean.entity.Device;
import com.mrkinnoapps.myordershopadmin.bean.entity.Token;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.bean.entity.UserRole;
import com.mrkinnoapps.myordershopadmin.bean.entity.Wholesaler;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotValidException;
import com.mrkinnoapps.myordershopadmin.resource.ErrorResource;
import com.mrkinnoapps.myordershopadmin.service.DeviceService;
import com.mrkinnoapps.myordershopadmin.service.MeltingAndSealService;
import com.mrkinnoapps.myordershopadmin.service.OrderService;
import com.mrkinnoapps.myordershopadmin.service.OrderSummaryService;
import com.mrkinnoapps.myordershopadmin.service.RelationService;
import com.mrkinnoapps.myordershopadmin.service.TokenService;
import com.mrkinnoapps.myordershopadmin.service.UserService;
import com.mrkinnoapps.myordershopadmin.service.WholesalerOrderSummaryService;
import com.mrkinnoapps.myordershopadmin.util.searchengine.SearchResult;
import com.mrkinnoapps.myordershopadmin.validtors.UserValidator;

@Controller
@RequestMapping("/v1/admin/users")
public class UserController {

	private static Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderSummaryService orderSummaryService;
	@Autowired
	private WholesalerOrderSummaryService wholesalerOrderSummaryService;
	@Autowired
	private UserValidator userValidator;
	@Autowired
	private RelationService relationService;
	@Autowired
	private MeltingAndSealService meltingAndSealService;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private DeviceService deviceService;

	@InitBinder(value = { "user" })
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(userValidator);
	}

	@InitBinder
	private void initBinder1(WebDataBinder binder) {
		binder.addCustomFormatter(new DateFormatter("E MMM dd yyyy HH:mm:ss 'GMT'"));
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<User> saveUser(@Valid @ModelAttribute User user, BindingResult result)
			throws EntityNotPersistedException, EntityNotValidException, EntityDoseNotExistException {
		if (result.hasErrors()) {
			throw new EntityNotValidException(result);
		}
		userService.save(user);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@RequestMapping(value = "/{userId}", method = RequestMethod.POST)
	public ResponseEntity updateUsers(@PathVariable("userId") String userId, @Valid @ModelAttribute User user,
			BindingResult bindingResult) throws EntityDoseNotExistException, EntityNotValidException {
		if (bindingResult.hasErrors()) {
			throw new EntityNotValidException(bindingResult);
		}
		userService.update(user);
		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getUser() {
		return new ModelAndView("admin/users/users");
	}

	@RequestMapping(value = "/xhr/list", method = RequestMethod.GET)
	public String getXHRUsers(ModelMap map,UserFilter filter,
			@PageableDefault(size = 10) Pageable pageable) {
		Page<User> page = userService.find(filter, pageable, null);
		map.addAttribute("page", page);
		map.addAttribute("pageable", pageable);
		map.addAttribute("totalcount",page.getContent().size());
		return "admin/users/xhr/list";
	}

	@RequestMapping(value = "/xhr/form", method = RequestMethod.GET)
	public String getXHRNewUser(ModelMap map) throws EntityDoseNotExistException {
		User user = new User();
		user.getUserRoles().add(new UserRole());
		map.addAttribute("user", user);
		map.addAttribute("roles", userService.getUserRoles());
		return "admin/users/xhr/form";
	}

	@RequestMapping(value = "/xhr/{userId}", method = RequestMethod.GET)
	public ModelAndView getXHRUser(@PathVariable("userId") String userId, ModelMap map)
			throws EntityDoseNotExistException {
		User user = userService.get(userId);
		map.addAttribute("relationSummary", relationService.getActiveRelationCount(userId));
		map.addAttribute("countOfOrders", orderSummaryService.getAliveOrderSummaryByTillDate(null, user));
		map.addAttribute("wholesalerOrderCount",
				wholesalerOrderSummaryService.getAliveOrderSummaryByTillDate(null, user));
		map.addAttribute("user", user);
		return new ModelAndView("admin/users/xhr/view");
	}

	@RequestMapping(value = "/xhr/{userId}/edit-user", method = RequestMethod.GET)
	public String getXHRUserEdit(@PathVariable("userId") String userId, ModelMap map)
			throws EntityDoseNotExistException {
		User user = userService.get(userId);
		map.addAttribute("statuses", new ArrayList<ActiveFlag>(EnumSet.allOf(ActiveFlag.class)));
		map.addAttribute("user", user);
		return "admin/users/xhr/edit";
	}

	@RequestMapping(value = "/xhr/{userId}/setting", method = RequestMethod.GET)
	public String sttingUser(@PathVariable("userId") String userId, ModelMap map) throws EntityDoseNotExistException {
		User user = userService.get(userId);
		map.addAttribute("user", user);
		map.addAttribute("roles", userService.getUserRoles());
		map.addAttribute("statusList", ActiveFlag.values());
		List<Token> tokens = tokenService.findByUser(userId);
		map.addAttribute("tokens",tokens);
		map.addAttribute("tokenSize",tokens.size());
		return "admin/users/xhr/setting";
	}

	@RequestMapping(value = "/xhr/{userId}/chngPass", method = RequestMethod.POST)
	public ResponseEntity chngPass(@PathVariable("userId") String userId, @ModelAttribute UserCredential userCredential,
			ModelMap map) throws EntityDoseNotExistException {
		if (!userCredential.getPassword().equals(userCredential.getConfirmPassword())) {
			return new ResponseEntity(new ErrorResource("40003", "Password not matching"), HttpStatus.BAD_REQUEST);
		}
		userService.changePassword(userCredential.getPassword(), userId);
		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(value = "/xhr/{userId}/chngRole", method = RequestMethod.POST)
	public ResponseEntity chngRole(@PathVariable("userId") String userId, @ModelAttribute User user)
			throws EntityDoseNotExistException, EntityNotPersistedException {
		userService.changeRole(userId, user.getUserRoles().get(0));
		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(value = "/xhr/{userId}/chngSts", method = RequestMethod.POST)
	public ResponseEntity chngSts(@PathVariable("userId") String userId, @ModelAttribute User user)
			throws EntityDoseNotExistException, EntityNotPersistedException {
		userService.changeUserStatus(userId, user);
		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(value = "/xhr/deleteUserToken", method = RequestMethod.GET)
	public ResponseEntity deleteToken(@RequestParam("tokenId") Integer tokenId)
			throws EntityDoseNotExistException, EntityNotPersistedException {
		tokenService.delete(tokenId);
		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(value = "/xhr/meltingAndSeal/{userId}", method = RequestMethod.GET)
	public ResponseEntity getXHRMeltingAndSeal(@PathVariable("userId") String userId)
			throws EntityDoseNotExistException {
		User user = userService.get(userId);
		if (user instanceof Wholesaler) {
			Wholesaler wholesaler = (Wholesaler) user;
			return new ResponseEntity(meltingAndSealService.find(wholesaler), HttpStatus.OK);
		} else {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/xhr/user-list", method = RequestMethod.GET)
	public ResponseEntity getXHRUsers(@RequestParam String q,UserFilter filter) {
		List<SearchResult> list = userService.searchUser(filter,q);
		return new ResponseEntity(list,HttpStatus.OK);
	}

	@RequestMapping(value = "/xhr/user-devices-list/{userId}", method = RequestMethod.GET)
	public ResponseEntity getXHRUserDevices(@PathVariable("userId") String userId) {
		List<Device> list = deviceService.findByUserId(userId);
		return new ResponseEntity(list,HttpStatus.OK);
	}

	@RequestMapping(value = "/xhr/delete-user-device/{deviceId}", method = RequestMethod.GET)
	public ResponseEntity deleteXHRUserDevices(@PathVariable("deviceId") Integer deviceId) throws EntityDoseNotExistException {
		deviceService.delete(deviceId);
		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(value = "/xhr/not-registered-list", method = RequestMethod.GET)
	public String getXHRNotRegisterdList(@PageableDefault(size = 10) Pageable pageable,ModelMap map) throws EntityDoseNotExistException {
		Page<NotRegisteredUser> page=userService.getNotRegisteredUsers(pageable);
		map.addAttribute("page", page);
		map.addAttribute("pageable", pageable);
		map.addAttribute("totalcount",page.getContent().size());
		return "admin/users/xhr/notregistereduser";
	}
}
