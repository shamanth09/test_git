package com.mrkinnoapps.myordershopadmin.controller.admin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mrkinnoapps.myordershopadmin.bean.entity.MeltingAndSeal;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;
import com.mrkinnoapps.myordershopadmin.service.MeltingAndSealService;

@Controller
@RequestMapping("/v1/admin/melting-seal")
public class MeltingAndSealController {
	
	@Autowired
	private MeltingAndSealService meltingAndSealService;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/xhr/update/{userId}",method = RequestMethod.POST)
	public ResponseEntity updateMeltAndSeal(@PathVariable String userId, @ModelAttribute MeltingAndSeal meltingAndSeal) throws EntityDoseNotExistException, EntityNotPersistedException{
		MeltingAndSeal seal = meltingAndSealService.updateMeltAndSeal(meltingAndSeal,userId);
		return new ResponseEntity(seal,HttpStatus.OK);
	}
}
