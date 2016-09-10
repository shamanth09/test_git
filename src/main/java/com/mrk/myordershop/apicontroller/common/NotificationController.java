package com.mrk.myordershop.apicontroller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mrk.myordershop.bean.Notification;
import com.mrk.myordershop.bean.NotificationSettings;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;
import com.mrk.myordershop.notify.NotificationManager;
import com.mrk.myordershop.security.oauth.resolver.Owner;
import com.mrk.myordershop.service.NotificationService;
import com.mrk.myordershop.service.NotificationSettingsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping("/api/v1/notify")
@Api(value = "Notification", tags = { "Manage Notifications" })
public class NotificationController {

	@Autowired
	private NotificationService notificationService;
	@Autowired
	private NotificationSettingsService notificationSettingsService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<PagedResources> findNotification(@Owner User currentUser, Pageable pageable,
			PagedResourcesAssembler<Notification> assembler) {
		Page<Notification> page = notificationService.findByUserId(currentUser.getId(), pageable);
		return new ResponseEntity<PagedResources>(assembler.toResource(page), HttpStatus.OK);
	}

	@RequestMapping(value = "/{notificaionId}/read", method = RequestMethod.PUT)
	public ResponseEntity<Notification> updateStatus(@PathVariable("notificaionId") Integer notificationId,
			@Owner User currentUser) throws EntityDoseNotExistException, EntityNotPersistedException {
		Notification notificaion = notificationService.deleteNotifi(notificationId, currentUser);
		return new ResponseEntity<Notification>(notificaion, HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/all", method = RequestMethod.DELETE)
	public ResponseEntity deleteAll(@Owner User currentUser) {
		notificationService.clearNotifi(currentUser);
		return new ResponseEntity(HttpStatus.OK);
	}

	// @SuppressWarnings("rawtypes")
	// @RequestMapping(value="/save-settings",method=RequestMethod.POST)
	// public ResponseEntity saveNotificationSettings(@PathVariable String
	// userId,@ModelAttribute NotificationSettings notificationSettings)
	// throws EntityNotPersistedException{
	// notificationSettingsService.save(notificationSettings);
	// return new ResponseEntity(HttpStatus.OK);
	// }

	@ApiOperation(value = "update notification setting", notes = "update user notification settings with setting name and , separated String type value")
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/settings", method = RequestMethod.PUT)
	public ResponseEntity updateNotificationSettings(
			@ApiParam(name = "newOrder", allowableValues = "SMS,API") @RequestParam(required = false) String[] newOrder,
			@ApiParam(name = "orderStatusChange", allowableValues = "SMS,API") @RequestParam(required = false) String[] orderStatusChange,
			@ApiParam(name = "wsOrderStatusChange", allowableValues = "SMS,API") @RequestParam(required = false) String[] wsOrderStatusChange,
			@ApiParam(name = "relation", allowableValues = "SMS,API") @RequestParam(required = false) String[] relation,
			@ApiParam(name = "email", allowableValues = "EMAIL") @RequestParam(required = false) String[] email,
			@Owner User currentUser) throws EntityDoseNotExistException {

		if (newOrder != null && newOrder.length > 0) {
			notificationSettingsService.update("newOrder", NotificationSettings.getIntValue(newOrder), currentUser);
		}
		if (orderStatusChange != null && orderStatusChange.length > 0) {
			notificationSettingsService.update("orderStatusChange", NotificationSettings.getIntValue(orderStatusChange),
					currentUser);
		}
		if (wsOrderStatusChange != null && wsOrderStatusChange.length > 0) {
			notificationSettingsService.update("wsOrderStatusChange",
					NotificationSettings.getIntValue(wsOrderStatusChange), currentUser);
		}
		if (relation != null && relation.length > 0) {
			notificationSettingsService.update("relation", NotificationSettings.getIntValue(relation), currentUser);
		}
		if (email != null && email.length > 0) {
			notificationSettingsService.update("email", NotificationSettings.getIntValue(email), currentUser);
		}
		return new ResponseEntity(HttpStatus.OK);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/settings", method = RequestMethod.GET)
	public ResponseEntity getNotificationSettings(@Owner User currentUser) throws EntityDoseNotExistException {
		NotificationSettings notificationSettings = notificationSettingsService
				.getNotificationByUserID(currentUser.getId());
		return new ResponseEntity(notificationSettings, HttpStatus.OK);
	}

	/*
	 * @SuppressWarnings({ "rawtypes", "unchecked" })
	 * 
	 * @RequestMapping(value="/{id}/get-settings",method=RequestMethod.GET)
	 * public ResponseEntity getNotificationSettings(@PathVariable int id)
	 * throws EntityDoseNotExistException { NotificationSettings
	 * notificationSettings = notificationSettingsService.getNotification(id);
	 * return new ResponseEntity(notificationSettings,HttpStatus.OK); }
	 */
}
