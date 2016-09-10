package com.mrkinnoapps.myordershopadmin.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mrkinnoapps.myordershopadmin.service.DeviceService;

@Controller
@RequestMapping("/v1/admin/dashboard")
public class DashboardController {
	
	@Autowired
	private DeviceService deviceService ;

	@RequestMapping(method = RequestMethod.GET)
	public String getDashboard(ModelMap map) {
		map.addAttribute("myordershopweb", deviceService.deviceCount("myordershopweb"));
		map.addAttribute("myordershopios", deviceService.deviceCount("myordershopios"));
		map.addAttribute("myordershopandroid", deviceService.deviceCount("myordershopandroid"));
		return "admin/dashboard/dashboard";
	}
	
	
	
}
