/**
 * 
 */
package com.markinnoapp.myordershopadmin.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.junit.runners.MethodSorters;

import javax.annotation.PostConstruct;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.MediaType;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.amazonaws.util.json.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.markinnoapp.myordershopadmin.util.UtilClass;
import com.mrkinnoapps.myordershopadmin.bean.constant.ActiveFlag;
import com.mrkinnoapps.myordershopadmin.bean.constant.OrderStatus;
import com.mrkinnoapps.myordershopadmin.bean.constant.OrderType;
import com.mrkinnoapps.myordershopadmin.bean.constant.Role;
import com.mrkinnoapps.myordershopadmin.bean.entity.Address;
import com.mrkinnoapps.myordershopadmin.bean.entity.Item;
import com.mrkinnoapps.myordershopadmin.bean.entity.MeltingAndSeal;
import com.mrkinnoapps.myordershopadmin.bean.entity.Order;
import com.mrkinnoapps.myordershopadmin.bean.entity.Product;
import com.mrkinnoapps.myordershopadmin.bean.entity.Supplier;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.bean.entity.UserRole;
import com.mrkinnoapps.myordershopadmin.bean.entity.Wholesaler;
import com.mrkinnoapps.myordershopadmin.bean.entity.WholesalerOrder;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.service.MeltingAndSealService;
import com.mrkinnoapps.myordershopadmin.service.OrderService;
import com.mrkinnoapps.myordershopadmin.service.ProductService;
import com.mrkinnoapps.myordershopadmin.service.UserService;
import com.mrkinnoapps.myordershopadmin.service.WholeSalerOrderService;


/**
 * @author Mallinath Jun 10, 2016  
 */

@RunWith(SpringJUnit4ClassRunner.class)
@MyTestConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerIT {

	@Autowired
	private WebApplicationContext applicationContaxt;
	public MockMvc mockMvc;
	@Autowired
	private UserService userservice;

	@Autowired
	private OrderService orderService;

	@Autowired
	private WholeSalerOrderService wholeSalerOrderService;

	@Autowired
	private MeltingAndSealService meltingAndSealService;

	@Autowired
	private UtilClass utilClass;

	static String suplier=null;
	static String wholsaler=null;
	static String retailer=null;
	static String suplierAddress=null;
	static String wholsalerAddress=null;
	static String retailerAddress=null;
	static MeltingAndSeal ms=null;
	static List<User> users=null;
	//	Order instOrder=null;
	//	Order instWholOrder=null;
	@Before
	public void initGlobalResources() {
		mockMvc = MockMvcBuilders.webAppContextSetup(applicationContaxt)
				.build();
	}


	@BeforeClass
	public static void sstartGlobalResources() {
		System.out.println("Integration Test is starting.........");
	}


	@AfterClass
	public static void closeGlobalResources() {
		System.out.println("Integration Test is over.........");
	}

	@Test
	public void temp()
	{
		System.out.println("---"+utilClass);
		Assert.assertEquals(4, 2+2);
	}

	@Test
	public void a_createSupplier() throws Exception
	{
		
		suplier=utilClass.createSupplier(mockMvc);

	}

	@Test
	public void b_getSupplier() throws Exception
	{
		MvcResult resp=mockMvc.perform(get("/v1/admin/users/xhr/"+suplier))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/users/xhr/view"))
				.andExpect(model().attributeExists("relationSummary","countOfOrders","wholesalerOrderCount","user"))
				.andReturn();
		Map<String, Object>  map =resp.getModelAndView().getModel();
		User user=(User)map.get("user");
		Assert.assertNotNull(user);
		suplierAddress=user.getAddress().getId().toString();
		Assert.assertEquals(user.getEmail(), "jhjk@ds.com");
		Assert.assertEquals(user.getMobile(), "7563412892");
		Assert.assertEquals(user.getName(), "Naveen");
		Assert.assertNotNull(user.getAddress());
		Assert.assertEquals(user.getAddress().getCity(),"india");
		Assert.assertEquals(user.getAddress().getCountry(),"india");
		Assert.assertEquals(user.getAddress().getPincode(),"875634");
		Assert.assertEquals(user.getAddress().getArea(),"bnglre");
		Assert.assertEquals(user.getAddress().getState(),"karnataka");
		Assert.assertEquals(user.getAddress().getTitle(),"address");
		Assert.assertNotNull(user.getUserRoles());
		Assert.assertEquals(user.getUserRoles().get(0).getId(),1);
	}

	@Test
	public void c_editSupplier() throws Exception {

		mockMvc.perform(post("/v1/admin/users/"+suplier)
				.contentType(MediaType.
						APPLICATION_FORM_URLENCODED)
						.param("address.title","bangalore")
						.param("id", suplier)
						.param("name", "mallinath")
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
						.param("address.id",suplierAddress)
				)
				.andExpect(status().isOk());
	}
	@Test
	public void d_getSupplierAgain() throws Exception
	{
		MvcResult resp=mockMvc.perform(get("/v1/admin/users/xhr/"+suplier))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/users/xhr/view"))
				.andExpect(model().attributeExists("relationSummary","countOfOrders","wholesalerOrderCount","user"))
				.andReturn();
		Map<String, Object>  map =resp.getModelAndView().getModel();
		User user=(User)map.get("user");
		Assert.assertNotNull(user);
		Assert.assertEquals(user.getEmail(), "jhjk@ds.com");
		Assert.assertEquals(user.getMobile(), "7563412892");
		Assert.assertEquals(user.getName(), "mallinath");
		Assert.assertNotNull(user.getAddress());
		Assert.assertEquals(user.getAddress().getCity(),"india");
		Assert.assertEquals(user.getAddress().getCountry(),"india");
		Assert.assertEquals(user.getAddress().getPincode(),"875634");
		Assert.assertEquals(user.getAddress().getArea(),"bnglre");
		Assert.assertEquals(user.getAddress().getState(),"karnataka");
		Assert.assertEquals(user.getAddress().getTitle(),"bangalore");
		Assert.assertNotNull(user.getUserRoles());
		Assert.assertEquals(1,user.getUserRoles().get(0).getId());
	}

	@Test
	public void e_createRetailor() throws Exception
	{
		retailer=utilClass.createRetailor(mockMvc);
	}
	@Test
	public void f_getRetailer() throws Exception
	{
		MvcResult resp=mockMvc.perform(get("/v1/admin/users/xhr/"+retailer))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/users/xhr/view"))
				.andExpect(model().attributeExists("relationSummary","countOfOrders","wholesalerOrderCount","user"))
				.andReturn();
		Map<String, Object>  map =resp.getModelAndView().getModel();
		User user=(User)map.get("user");
		Assert.assertNotNull(user);
		retailerAddress=user.getAddress().getId().toString();
		Assert.assertEquals(user.getEmail(), "jhjk@ds123.com");
		Assert.assertEquals(user.getMobile(), "7845236754");
		Assert.assertEquals(user.getName(), "Naveen123");
		Assert.assertNotNull(user.getAddress());
		Assert.assertEquals(user.getAddress().getCity(),"india");
		Assert.assertEquals(user.getAddress().getCountry(),"india");
		Assert.assertEquals(user.getAddress().getPincode(),"875634");
		Assert.assertEquals(user.getAddress().getArea(),"bnglre");
		Assert.assertEquals(user.getAddress().getState(),"karnataka");
		Assert.assertEquals(user.getAddress().getTitle(),"address");
		Assert.assertNotNull(user.getUserRoles());
		Assert.assertEquals(3,user.getUserRoles().get(0).getId());
	}

	@Test
	public void g_editRetailor() throws Exception {
		mockMvc.perform(post("/v1/admin/users/"+retailer)
				.contentType(MediaType.
						APPLICATION_FORM_URLENCODED)
						.param("address.title","gulberga")
						.param("id", retailer)
						.param("name", "srk")
						.param("email", "jhjk@ds123.com")
						.param("mobile", "7845236700")
						.param("address.pincode", "875634")
						.param("address.area", "bnglre")
						.param("userRoles[0].id","1")
						.param("address.state","karnataka")
						.param("address.country","india")
						.param("address.street","india")
						.param("address.landmark","india")
						.param("address.city","india")
						.param("address.id",retailerAddress)
				)
				.andExpect(status().isOk());

	}

	@Test
	public void h_getRetailerAgain() throws Exception
	{
		MvcResult resp=mockMvc.perform(get("/v1/admin/users/xhr/"+retailer))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/users/xhr/view"))
				.andExpect(model().attributeExists("relationSummary","countOfOrders","wholesalerOrderCount","user"))
				.andReturn();
		Map<String, Object>  map =resp.getModelAndView().getModel();
		User user=(User)map.get("user");
		Assert.assertNotNull(user);
		Assert.assertEquals(user.getEmail(), "jhjk@ds123.com");
		Assert.assertEquals(user.getMobile(), "7845236700");
		Assert.assertEquals(user.getName(), "srk");
		Assert.assertNotNull(user.getAddress());
		Assert.assertEquals(user.getAddress().getCity(),"india");
		Assert.assertEquals(user.getAddress().getCountry(),"india");
		Assert.assertEquals(user.getAddress().getPincode(),"875634");
		Assert.assertEquals(user.getAddress().getArea(),"bnglre");
		Assert.assertEquals(user.getAddress().getState(),"karnataka");
		Assert.assertEquals(user.getAddress().getTitle(),"gulberga");
		Assert.assertNotNull(user.getUserRoles());
		Assert.assertEquals(3,user.getUserRoles().get(0).getId());
	}

	@Test
	public void i_createWhosaler() throws Exception
	{
		
		wholsaler=utilClass.createWhosaler(mockMvc);
		MeltingAndSeal meltingAndSeal=new MeltingAndSeal();
		meltingAndSeal.setActiveFlag(ActiveFlag.ACTIVE);
		meltingAndSeal.setCreateTimestamp(new Date());
		meltingAndSeal.setMelting(55);
		meltingAndSeal.setSeal("S15");
		ms=meltingAndSealService.updateMeltAndSeal(meltingAndSeal, wholsaler);
	}

	@Test
	public void j_getWhosaler() throws Exception
	{
		MvcResult resp=mockMvc.perform(get("/v1/admin/users/xhr/"+wholsaler))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/users/xhr/view"))
				.andExpect(model().attributeExists("relationSummary","countOfOrders","wholesalerOrderCount","user"))
				.andReturn();
		Map<String, Object>  map =resp.getModelAndView().getModel();
		User user=(User)map.get("user");
		Assert.assertNotNull(user);
		wholsalerAddress=user.getAddress().getId().toString();
		Assert.assertEquals(user.getEmail(), "jhjk@ds12.com");
		Assert.assertEquals(user.getMobile(), "8967452378");
		Assert.assertEquals(user.getName(), "Naveen12");
		Assert.assertNotNull(user.getAddress());
		Assert.assertEquals(user.getAddress().getCity(),"india");
		Assert.assertEquals(user.getAddress().getCountry(),"india");
		Assert.assertEquals(user.getAddress().getPincode(),"875634");
		Assert.assertEquals(user.getAddress().getArea(),"bnglre");
		Assert.assertEquals(user.getAddress().getState(),"karnataka");
		Assert.assertEquals(user.getAddress().getTitle(),"address");
		Assert.assertNotNull(user.getUserRoles());
		Assert.assertEquals(2,user.getUserRoles().get(0).getId());
	}
	@Test
	public void k_editWhosaler() throws Exception {

		mockMvc.perform(post("/v1/admin/users/"+wholsaler)
				.contentType(MediaType.
						APPLICATION_FORM_URLENCODED)
						.param("address.title","delli")
						.param("name", "dinesh")
						.param("id",wholsaler)
						.param("email", "jhjk@ds56789.com")
						.param("mobile", "7563412111")
						.param("address.pincode", "875634")
						.param("address.area", "bnglre")
						.param("userRoles[0].id","1")
						.param("address.state","karnataka")
						.param("address.country","india")
						.param("address.street","india")
						.param("address.landmark","india")
						.param("address.city","india")
						.param("address.id",wholsalerAddress)
				)
				.andExpect(status().isOk());
	}
	@Test
	public void l_getWhosalerAgain() throws Exception
	{
		MvcResult resp=mockMvc.perform(get("/v1/admin/users/xhr/"+wholsaler))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/users/xhr/view"))
				.andExpect(model().attributeExists("relationSummary","countOfOrders","wholesalerOrderCount","user"))
				.andReturn();
		Map<String, Object>  map =resp.getModelAndView().getModel();
		User user=(User)map.get("user");
		Assert.assertNotNull(user);
		Assert.assertEquals(user.getEmail(), "jhjk@ds56789.com");
		Assert.assertEquals(user.getMobile(), "7563412111");
		Assert.assertEquals(user.getName(), "dinesh");
		Assert.assertNotNull(user.getAddress());
		Assert.assertEquals(user.getAddress().getCity(),"india");
		Assert.assertEquals(user.getAddress().getCountry(),"india");
		Assert.assertEquals(user.getAddress().getPincode(),"875634");
		Assert.assertEquals(user.getAddress().getArea(),"bnglre");
		Assert.assertEquals(user.getAddress().getState(),"karnataka");
		Assert.assertEquals(user.getAddress().getTitle(),"delli");
		Assert.assertNotNull(user.getUserRoles());
		Assert.assertEquals(user.getUserRoles().get(0).getId(),2);
	}

	@Test
	public void m_getUsers() throws Exception
	{
		MvcResult resp=mockMvc.perform(get("/v1/admin/users/xhr/list"))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/users/xhr/list"))
				.andExpect(model().attributeExists("page","pageable","totalcount"))
				.andReturn();
		Map<String, Object>  map =resp.getModelAndView().getModel();
		Page<User> page=(Page<User>) map.get("page");
		users=page.getContent();
		for(User u:users)
		{
			if(u.getUserRoles().get(0).getName().equalsIgnoreCase("Wholesaler")){
				Assert.assertEquals(wholsaler, u.getId());
			}
			else if(u.getUserRoles().get(0).getName().equalsIgnoreCase("Supplier"))
			{
				Assert.assertEquals(suplier, u.getId());
			}
			else
			{
				Assert.assertEquals(retailer, u.getId());
			}
		}
		Object totalcount=map.get("totalcount");
		Assert.assertEquals(3, totalcount);
	}

	@Test
	public void n_searchUser() throws Exception
	{
		MvcResult resp=mockMvc.perform(get("/v1/admin/search?q="+"srk"+"&fields=userName&fields=userEmail&fields=userMobile"))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andReturn();
		String str =resp.getResponse().getContentAsString();
		Assert.assertNotNull(str);

	}
	@Test
	public void o_filterhUserByStatus() throws Exception
	{
		MvcResult resp=mockMvc.perform(get("/v1/admin/users/xhr/list?activeFlags=ACTIVE"))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/users/xhr/list"))
				.andExpect(model().attributeExists("page","pageable","totalcount"))
				.andReturn();
		Map<String, Object>  map =resp.getModelAndView().getModel();
		Object obj= map.get("totalcount");
		Assert.assertEquals(0,obj);

	}
	@Test
	public void p_filterhUserByDates() throws Exception
	{

		LocalDateTime to=new LocalDateTime();
		LocalDateTime from =to.minusDays(7);
		Date d1=from.toDate();
		Date d2=to.toDate();
		DateFormat df = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss 'GMT'");
		String dd1 =df.format(d1);
		String dd2=df.format(d2);
		MvcResult resp=mockMvc.perform(get("/v1/admin/users/xhr/list?fromDate="+dd1+"&toDate="+dd2))
				.andExpect(status().isOk())
				.andReturn();
		Map<String, Object>  map =resp.getModelAndView().getModel();
		Object obj= map.get("totalcount");
		Assert.assertEquals(3,obj);

	}


	@Test
	public void q_deleteUsers()
	{
		utilClass.deleteUsers(users, userservice);
	}

	@Test
	public void r_deleteResult() throws Exception
	{
		MvcResult resp=mockMvc.perform(get("/v1/admin/users/xhr/list"))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/users/xhr/list"))
				.andExpect(model().attributeExists("page","pageable","totalcount"))
				.andReturn();
		Map<String, Object>  map =resp.getModelAndView().getModel();
		Object count=map.get("totalcount");
		Assert.assertEquals(0, count);
	}
}
