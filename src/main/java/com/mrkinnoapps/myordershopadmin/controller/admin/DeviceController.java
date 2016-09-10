package com.mrkinnoapps.myordershopadmin.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mrkinnoapps.myordershopadmin.bean.entity.Device;
import com.mrkinnoapps.myordershopadmin.service.DeviceService;

@Controller
@RequestMapping("/v1/admin/devices")
public class DeviceController {
	
	@Autowired
	private DeviceService deviceService ;
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getUser() {
		System.out.println("======================");
		return new ModelAndView("admin/devices/device");
	}
	
	@RequestMapping(value="/xhr/list", method=RequestMethod.GET)
	public String deviceList(ModelMap map,@PageableDefault(size = 10) Pageable pageable) {
		Page<Device> page = deviceService.getList(pageable);
		map.addAttribute("page", page);
		map.addAttribute("pageable",pageable);
		map.addAttribute("totalcount", page.getContent().size());
		return "admin/devices/xhr/list";
	}

}
