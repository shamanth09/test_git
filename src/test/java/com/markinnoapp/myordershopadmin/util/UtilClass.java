/**
 * 
 */
package com.markinnoapp.myordershopadmin.util;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;

import com.amazonaws.util.json.JSONObject;
import com.markinnoapp.myordershopadmin.controller.MyTestConfiguration;
import com.mrkinnoapps.myordershopadmin.bean.constant.ActiveFlag;
import com.mrkinnoapps.myordershopadmin.bean.constant.OrderStatus;
import com.mrkinnoapps.myordershopadmin.bean.entity.Detail;
import com.mrkinnoapps.myordershopadmin.bean.entity.InstantOrder;
import com.mrkinnoapps.myordershopadmin.bean.entity.Item;
import com.mrkinnoapps.myordershopadmin.bean.entity.MeltingAndSeal;
import com.mrkinnoapps.myordershopadmin.bean.entity.Order;
import com.mrkinnoapps.myordershopadmin.bean.entity.Product;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.bean.entity.Wholesaler;
import com.mrkinnoapps.myordershopadmin.bean.entity.WholesalerInstantOrder;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;
import com.mrkinnoapps.myordershopadmin.service.ItemService;
import com.mrkinnoapps.myordershopadmin.service.OrderService;
import com.mrkinnoapps.myordershopadmin.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

/**
 * @author Mallinath Jun 24, 2016  
 */
public class UtilClass {

	public String createSupplier(MockMvc mockMvc) throws Exception
	{

		MvcResult resp=mockMvc.perform(post("/v1/admin/users")
				.contentType(MediaType.
						APPLICATION_FORM_URLENCODED)
						.param("address.title","address")
						.param("name", "Naveen")
						.param("activeFlag", "ACTIVE")
						.param("email", "jhjk@ds.com")
						.param("mobile", "7563412892")
						.param("address.pincode", "875634")
						.param("address.area", "bnglre")
						.param("userRoles[0].id","1")
						.param("address.state","karnataka")
						.param("address.country","india")
						.param("address.street","india")
						.param("address.landmark","india")
						.param("address.city","india")
				)
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andReturn();
		String str =resp.getResponse().getContentAsString();
		JSONObject jsonObj=new JSONObject(str);
		return (String)jsonObj.get("id");
	}

	public String createRetailor(MockMvc mockMvc) throws Exception
	{
		MvcResult	resp=mockMvc.perform(post("/v1/admin/users")
				.contentType(MediaType.
						APPLICATION_FORM_URLENCODED)
						.param("address.title","address")
						.param("name", "Naveen123")
						.param("activeFlag", "ACTIVE")
						.param("email", "jhjk@ds123.com")
						.param("mobile", "7845236754")
						.param("address.pincode", "875634")
						.param("address.area", "bnglre")
						.param("userRoles[0].id","3")
						.param("address.state","karnataka")
						.param("address.country","india")
						.param("address.street","india")
						.param("address.landmark","india")
						.param("address.city","india")


				)
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andReturn();
		String str =resp.getResponse().getContentAsString();
		JSONObject jsonObj=new JSONObject(str);
		return (String)jsonObj.get("id");
	}

	public String createWhosaler(MockMvc mockMvc) throws Exception
	{
		MvcResult resp=mockMvc.perform(post("/v1/admin/users")
				.contentType(MediaType.
						APPLICATION_FORM_URLENCODED)
						.param("address.title","address")
						.param("name", "Naveen12")
						.param("activeFlag", "ACTIVE")
						.param("email", "jhjk@ds12.com")
						.param("mobile", "8967452378")
						.param("address.pincode", "875634")
						.param("address.area", "bnglre")
						.param("userRoles[0].id","2")
						.param("address.state","karnataka")
						.param("address.country","india")
						.param("address.street","india")
						.param("address.landmark","india")
						.param("address.city","india")


				)
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andReturn();
		String str =resp.getResponse().getContentAsString();
		JSONObject jsonObj=new JSONObject(str);
		return (String)jsonObj.get("id");
	}

	public List<User> getUsers(MockMvc mockMvc) throws Exception
	{
		MvcResult resp=mockMvc.perform(get("/v1/admin/users/xhr/list"))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/users/xhr/list"))
				.andExpect(model().attributeExists("page","pageable","totalcount"))
				.andReturn();
		Map<String, Object>  map =resp.getModelAndView().getModel();
		Page<User> page=(Page<User>) map.get("page");
		return page.getContent();

	}
	
	public void deleteUsers(List<User> users,UserService userService)
	{
		for(User u:users)
		{
			userService.delete(u);
		}
	}
	
	public Order createRetailorOrder(OrderService orderService,User retailor,User wholesalor,ItemService itemService,MeltingAndSeal ms) throws EntityDoseNotExistException, EntityNotPersistedException
	{
		Order order=new InstantOrder();
		order.setActiveFlag(ActiveFlag.ACTIVE);
		order.setCreateDate(new Date());
		order.setOrderStatus(OrderStatus.ACTIVE);
		order.setUser(retailor);
		order.setWholesaler((Wholesaler)wholesalor);
		order=orderService.saveOrder(order);
		Detail detail=new Detail();
		detail.setLength("6");
		detail.setWeight(10.0);
		detail.setSize("4");
		Item item=new Item();
		item.setActiveFlag(ActiveFlag.ACTIVE);
		if(ms!=null)
			item.setMeltingAndSeal(ms);
		Product product=new Product();
		product.setId(1);
		item.setProduct(product);
		item.setOrder(order);
		item.setDetail(detail);
		itemService.saveItem(item);
		return order;
	}
	
	public Order createWholsalerInstentOrder(User wholesalor,OrderService orderService,MeltingAndSeal ms,ItemService itemService) throws EntityDoseNotExistException, EntityNotPersistedException
	{
		WholesalerInstantOrder order=new WholesalerInstantOrder();
		order.setActiveFlag(ActiveFlag.ACTIVE);
		order.setCreateDate(new Date());
		order.setOrderStatus(OrderStatus.ACTIVE);
		order.setUser(wholesalor);
		order.setWholesaler((Wholesaler)wholesalor);
		order.setCustomerFirmName("MRKInnoAPP");
		order.setAdvance("dont");
		order.setCustomerMobile("9901171722");
		order.setCustomerName("Shanker");
		Order o=order;
		o=orderService.saveOrder(o);
		Detail detail=new Detail();
		detail.setLength("6");
		detail.setWeight(10.0);
		detail.setSize("4");
		Item item=new Item();
		item.setActiveFlag(ActiveFlag.ACTIVE);
		if(ms!=null)
			item.setMeltingAndSeal(ms);
		Product product=new Product();
		product.setId(1);
		item.setProduct(product);
		item.setOrder(o);
		item.setDetail(detail);
		itemService.saveItem(item);
		return o;
	}
	
	public List<Order> getOrders(MockMvc mockMvc) throws Exception{
		MvcResult resp=mockMvc.perform(get("/v1/admin/orders/xhr/list"))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/orders/xhr/list"))
				.andExpect(model().attributeExists("page","pageable","totalcount"))
				.andReturn();
		Map<String, Object>  map =resp.getModelAndView().getModel();
		Page<Order> page=(Page<Order>) map.get("page");
		return page.getContent();
	}
	
	public void deleteOrders(List<Order> orders,OrderService orderService)
	{
		for(Order order:orders)
		{
			orderService.delete(order);
		}
	}

}
