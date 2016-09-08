package com.mrk.myordershop.apicontroller.wsaler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mrk.myordershop.bean.MeltingAndSeal;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.security.oauth.resolver.Owner;
import com.mrk.myordershop.service.MeltingAndSealService;

import io.swagger.annotations.Api;

@Controller("meltingAndSealController")
@RequestMapping("/api/v1/ws/mlandsl")
@Api(value="Melting And Seal Details", tags={"Melting And Seal Details"})
public class MeltingAndSealController {

	@Autowired
	private MeltingAndSealService meltingAndSealService;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<MeltingAndSeal>> find(
			@Owner Wholesaler wholesaler) throws EntityDoseNotExistException {
		List<MeltingAndSeal> meltingAndSeals = meltingAndSealService
				.find(wholesaler);
		return new ResponseEntity<List<MeltingAndSeal>>(meltingAndSeals,
				HttpStatus.OK);
	}
}
