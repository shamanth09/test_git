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
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.amazonaws.util.json.JSONObject;
import com.markinnoapp.myordershopadmin.util.UtilClass;
import com.mrkinnoapps.myordershopadmin.bean.constant.ActiveFlag;
import com.mrkinnoapps.myordershopadmin.bean.constant.OrderStatus;
import com.mrkinnoapps.myordershopadmin.bean.constant.OrderType;
import com.mrkinnoapps.myordershopadmin.bean.entity.Detail;
import com.mrkinnoapps.myordershopadmin.bean.entity.InstantOrder;
import com.mrkinnoapps.myordershopadmin.bean.entity.Item;
import com.mrkinnoapps.myordershopadmin.bean.entity.MeltingAndSeal;
import com.mrkinnoapps.myordershopadmin.bean.entity.Order;
import com.mrkinnoapps.myordershopadmin.bean.entity.Product;
import com.mrkinnoapps.myordershopadmin.bean.entity.Supplier;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.bean.entity.Wholesaler;
import com.mrkinnoapps.myordershopadmin.bean.entity.WholesalerInstantOrder;
import com.mrkinnoapps.myordershopadmin.bean.entity.WholesalerOrder;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;
import com.mrkinnoapps.myordershopadmin.service.ItemService;
import com.mrkinnoapps.myordershopadmin.service.MeltingAndSealService;
import com.mrkinnoapps.myordershopadmin.service.OrderService;
import com.mrkinnoapps.myordershopadmin.service.UserService;
import com.mrkinnoapps.myordershopadmin.service.WholeSalerOrderService;

/**
 * @author Mallinath Jun 24, 2016  
 */
@RunWith(SpringJUnit4ClassRunner.class)
@MyTestConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrderControllerIT {
	@Autowired
	private UtilClass utilClass;

	@Autowired
	private WebApplicationContext applicationContaxt;

	public MockMvc mockMvc;

	@Autowired
	private UserService userservice;

	@Autowired
	private OrderService orderService;

	@Autowired
	private ItemService itemService;

	/*@Autowired
	private WholeSalerOrderService wholeSalerOrderService;*/

	@Autowired
	private MeltingAndSealService meltingAndSealService;

	static String suplier=null;
	static String wholsaler=null;
	static String retailer=null;
	static MeltingAndSeal ms=null;
	static List<User> users=null;
	static Order whosalerinstOrder=null;
	static Order instOrder=null;
	static List<Order> orders=null;


	@Before
	public void initGlobalResources() {
		mockMvc = MockMvcBuilders.webAppContextSetup(applicationContaxt)
				.build();
	}

	@Test
	public void a_creatingUsers() throws Exception
	{
		retailer=utilClass.createRetailor(mockMvc);
		suplier=utilClass.createSupplier(mockMvc);
		wholsaler=utilClass.createWhosaler(mockMvc);
		MeltingAndSeal meltingAndSeal=new MeltingAndSeal();
		meltingAndSeal.setActiveFlag(ActiveFlag.ACTIVE);
		meltingAndSeal.setCreateTimestamp(new Date());
		meltingAndSeal.setMelting(55);
		meltingAndSeal.setSeal("S15");
		ms=meltingAndSealService.updateMeltAndSeal(meltingAndSeal, wholsaler);
	}

	@Test
	public void b_createRetailorOrder() throws EntityDoseNotExistException, EntityNotPersistedException
	{
		User retailor=userservice.get(retailer);
		User wholesalor=userservice.get(wholsaler);
		instOrder=utilClass.createRetailorOrder(orderService, retailor, wholesalor, itemService, ms);
	}

	@Test
	public void c_getInstantOrder() throws Exception
	{
		MvcResult resp=mockMvc.perform(get("/v1/admin/orders/xhr/"+instOrder.getId()))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/orders/xhr/orderview"))
				.andExpect(model().attributeExists("order","WholesalerInstantOrder"))
				.andReturn();
		Map<String, Object>  map =resp.getModelAndView().getModel();
		Order order=(Order)map.get("order");
		Assert.assertNotNull(order);
		Assert.assertEquals("AD Kamal Tops",order.getProductName());
		Assert.assertEquals(instOrder.getId(),order.getId());
		Assert.assertEquals(retailer,order.getUser().getId());
		Assert.assertEquals(wholsaler, order.getWholesaler().getId());
		instOrder=order;
	}

	@Test
	public void d_editInstantOrder() throws Exception
	{
		LocalDateTime today=new LocalDateTime();
		Date d=today.toDate();
		DateFormat df = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss 'GMT'");
		String cd =df.format(d);
		mockMvc.perform(post("/v1/admin/orders/xhr/"+instOrder.getId())
				.contentType(MediaType.
						APPLICATION_FORM_URLENCODED)
						.param("id",Integer.toString(instOrder.getId()) )
						.param("item.id",Integer.toString(instOrder.getItem().getId()))
						.param("item.detail.id",Integer.toString(instOrder.getItem().getDetail().getId()) )
						.param("createTimestamp", cd)
						.param("orderStatus","APPROVED")
						.param("expectedDate",cd)
						.param("item.product.id",Integer.toString(instOrder.getItem().getProduct().getId()))
						.param("description", "super")
						.param("activeFlag","ACTIVE")
						.param("item.meltingAndSeal.id",Integer.toString(instOrder.getItem().getMeltingAndSeal().getId()))
						.param("item.detail.length",Integer.toString(6))
						.param("item.detail.weight",Integer.toString(80))
						.param("item.detail.size",Integer.toString(8))
						.param("item.detail.quantity",Integer.toString(2))
						.param("item.detail.huck","")
						.param("item.detail.backChain","")
						.param("item.detail.withScrew","")
						.param("item.detail.initial", "")
						.param("item.detail.sizeType","")
						.param("item.detail.lines", "")
						.param("item.detail.remarks", "")
						.param("item.detail.stoneWeight", "")
						.param("item.product.category.id",Integer.toString(instOrder.getItem().getProduct().getCategory().getId()))
				)
				.andExpect(status().isOk());
	}

	@Test
	public void e_againGetInstantOrder() throws Exception
	{
		MvcResult resp=mockMvc.perform(get("/v1/admin/orders/xhr/"+instOrder.getId()))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/orders/xhr/orderview"))
				.andExpect(model().attributeExists("order","WholesalerInstantOrder"))
				.andReturn();
		Map<String, Object>  map =resp.getModelAndView().getModel();
		Order order=(Order)map.get("order");
		Assert.assertNotNull(order);
		Assert.assertEquals("AD Kamal Tops",order.getProductName());
		Assert.assertEquals(instOrder.getId(),order.getId());
		Assert.assertEquals(retailer,order.getUser().getId());
		Assert.assertEquals(wholsaler, order.getWholesaler().getId());
		Assert.assertEquals("6", order.getItem().getDetail().getLength());
		Assert.assertEquals("super",order.getDescription());
	}

	@Test
	public void f_dcreateWholsalerInstentOrder() throws EntityDoseNotExistException, EntityNotPersistedException
	{
		User wholesalor=userservice.get(wholsaler);
		whosalerinstOrder=utilClass.createWholsalerInstentOrder(wholesalor, orderService, ms, itemService);
	}

	@Test
	public void g_getwholesalorInstantOrder() throws Exception
	{
		MvcResult resp=mockMvc.perform(get("/v1/admin/orders/xhr/"+whosalerinstOrder.getId()))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/orders/xhr/orderview"))
				.andExpect(model().attributeExists("order","WholesalerInstantOrder"))
				.andReturn();
		Map<String, Object>  map =resp.getModelAndView().getModel();
		Order order=(Order)map.get("order");
		Assert.assertNotNull(order);
		whosalerinstOrder=(WholesalerInstantOrder)order;
		Assert.assertEquals("AD Kamal Tops",order.getProductName());
		Assert.assertEquals(whosalerinstOrder.getId(),order.getId());
		Assert.assertEquals(wholsaler,order.getUser().getId());
		Assert.assertEquals(wholsaler, order.getWholesaler().getId());
	}

	@Test
	public void h_editWholsalerInstantOrder() throws Exception
	{
		LocalDateTime today=new LocalDateTime();
		Date d=today.toDate();
		DateFormat df = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss 'GMT'");
		String cd =df.format(d);
		mockMvc.perform(post("/v1/admin/orders/xhr/"+whosalerinstOrder.getId())
				.contentType(MediaType.
						APPLICATION_FORM_URLENCODED)
						.param("id",Integer.toString(whosalerinstOrder.getId()) )
						.param("item.id",Integer.toString(whosalerinstOrder.getItem().getId()))
						.param("item.detail.id",Integer.toString(whosalerinstOrder.getItem().getDetail().getId()) )
						.param("createTimestamp", cd)
						.param("orderStatus","APPROVED")
						.param("expectedDate",cd)
						.param("item.product.id",Integer.toString(whosalerinstOrder.getItem().getProduct().getId()))
						.param("description", "NotSuper")
						.param("activeFlag","ACTIVE")
						.param("item.meltingAndSeal.id",Integer.toString(whosalerinstOrder.getItem().getMeltingAndSeal().getId()))
						.param("item.detail.length",Integer.toString(6))
						.param("item.detail.weight",Integer.toString(80))
						.param("item.detail.size",Integer.toString(8))
						.param("item.detail.quantity",Integer.toString(2))
						.param("item.detail.huck","")
						.param("item.detail.backChain","")
						.param("item.detail.withScrew","")
						.param("item.detail.initial", "")
						.param("item.detail.sizeType","")
						.param("item.detail.lines", "")
						.param("item.detail.remarks", "")
						.param("item.detail.stoneWeight", "")
						.param("item.product.category.id",Integer.toString(whosalerinstOrder.getItem().getProduct().getCategory().getId()))
						.param("rateCut",Integer.toString(8))
						.param("sampleFrom", "ROLE_RETAIL")
						.param("customerName","Modi")
						.param("advance","xyz")
						.param("customerMobile", "8765431209")
						.param("customerFirmName", "LaxmiGold")
						.param("sampleDescription","YLG")
				)
				.andExpect(status().isOk());
	}

	@Test
	public void i_getAgainWholesalorInstantOrder() throws Exception
	{
		MvcResult resp=mockMvc.perform(get("/v1/admin/orders/xhr/"+whosalerinstOrder.getId()))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/orders/xhr/orderview"))
				.andExpect(model().attributeExists("order","WholesalerInstantOrder"))
				.andReturn();
		Map<String, Object>  map =resp.getModelAndView().getModel();
		Order order=(Order)map.get("order");
		Assert.assertNotNull(order);
		Assert.assertEquals("AD Kamal Tops",order.getProductName());
		Assert.assertEquals(whosalerinstOrder.getId(),order.getId());
		Assert.assertEquals(wholsaler,order.getUser().getId());
		Assert.assertEquals(wholsaler, order.getWholesaler().getId());
		Assert.assertEquals("LaxmiGold", ((WholesalerInstantOrder)order).getCustomerFirmName());
		Assert.assertEquals("NotSuper", order.getDescription());
	}

	
	@Test
	public void j_nsearchOrder() throws Exception
	{
		MvcResult resp=mockMvc.perform(get("/v1/admin/search?q="+"srk"+"&fields=customerMobile&fields=customerName&fields=userName&fields=productName"))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andReturn();
		String str =resp.getResponse().getContentAsString();
		Assert.assertNotNull(str);

	}
	@Test
	public void k_filterhOrderByStatus() throws Exception
	{
		MvcResult resp=mockMvc.perform(get("/v1/admin/orders/xhr/list?orderStatus=APPROVED"))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/orders/xhr/list"))
				.andExpect(model().attributeExists("page","pageable","totalcount"))
				.andReturn();
		Map<String, Object>  map =resp.getModelAndView().getModel();
		Object obj= map.get("totalcount");
		Assert.assertEquals(2,obj);

	}
	@Test
	public void l_pfilterhUserByDates() throws Exception
	{

		LocalDateTime to=new LocalDateTime();
		LocalDateTime from =to.minusDays(7);
		Date d1=from.toDate();
		Date d2=to.toDate();
		DateFormat df = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss 'GMT'");
		String dd1 =df.format(d1);
		String dd2=df.format(d2);
		MvcResult resp=mockMvc.perform(get("/v1/admin/orders/xhr/list?fromDate="+dd1+"&toDate="+dd2))
				.andExpect(status().isOk())
				.andReturn();
		Map<String, Object>  map =resp.getModelAndView().getModel();
		Object obj= map.get("totalcount");
		Assert.assertEquals(2,obj);

	}

	@Test
	public void m_getOrders() throws Exception{
		MvcResult resp=mockMvc.perform(get("/v1/admin/orders/xhr/list"))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/orders/xhr/list"))
				.andExpect(model().attributeExists("page","pageable","totalcount"))
				.andReturn();
		Map<String, Object>  map =resp.getModelAndView().getModel();
		Page<Order> page=(Page<Order>) map.get("page");
		orders=page.getContent();
		Assert.assertNotNull(orders);
		Assert.assertEquals(2,(Object)map.get("totalcount"));
		for(Order order:orders)
		{
			if(order instanceof InstantOrder)
				Assert.assertEquals(instOrder.getId(), order.getId());
			else if(order instanceof WholesalerInstantOrder)
				Assert.assertEquals(whosalerinstOrder.getId(), order.getId());
		}
	}
	
	@Test
	public void n_delete()
	{
		utilClass.deleteOrders(orders, orderService);
	}
	
	@Test
	public void o_finalResults() throws Exception
	{
		
		MvcResult resp=mockMvc.perform(get("/v1/admin/orders/xhr/list"))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/orders/xhr/list"))
				.andExpect(model().attributeExists("page","pageable","totalcount"))
				.andReturn();
		Map<String, Object>  map =resp.getModelAndView().getModel();
		Page<Order> page=(Page<Order>) map.get("page");
		orders=page.getContent();
		Assert.assertNotNull(orders);
		Assert.assertEquals(0,(Object)map.get("totalcount"));
	}
	
	@Test
	public void p_getUsers() throws Exception
	{
		users=utilClass.getUsers(mockMvc);
	}
	
	@Test
	public void q_deleteUsers()
	{
		utilClass.deleteUsers(users, userservice);
	}

}
