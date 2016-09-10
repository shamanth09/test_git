/**
 * 
 */
package com.markinnoapp.myordershopadmin.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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

import com.markinnoapp.myordershopadmin.util.UtilClass;
import com.mrkinnoapps.myordershopadmin.bean.constant.ActiveFlag;
import com.mrkinnoapps.myordershopadmin.bean.constant.OrderStatus;
import com.mrkinnoapps.myordershopadmin.bean.entity.Detail;
import com.mrkinnoapps.myordershopadmin.bean.entity.InstantOrder;
import com.mrkinnoapps.myordershopadmin.bean.entity.InstantWholesalerOrder;
import com.mrkinnoapps.myordershopadmin.bean.entity.Item;
import com.mrkinnoapps.myordershopadmin.bean.entity.MeltingAndSeal;
import com.mrkinnoapps.myordershopadmin.bean.entity.Order;
import com.mrkinnoapps.myordershopadmin.bean.entity.Product;
import com.mrkinnoapps.myordershopadmin.bean.entity.Supplier;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
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
 * @author Mallinath Jun 28, 2016  
 */

@RunWith(SpringJUnit4ClassRunner.class)
@MyTestConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WholesalerOrderControllerIT {

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

	@Autowired
	private WholeSalerOrderService wholeSalerOrderService;

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
	static List<WholesalerOrder> wholesalerOrders=null;
	static WholesalerOrder wholesalerOrder=null;


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
	public void b_createOrders() throws EntityDoseNotExistException, EntityNotPersistedException
	{
		User retailor=userservice.get(retailer);
		User wholesalor=userservice.get(wholsaler);
		instOrder=utilClass.createRetailorOrder(orderService, retailor, wholesalor, itemService, ms);
		whosalerinstOrder=utilClass.createWholsalerInstentOrder(wholesalor, orderService, ms, itemService);
	}

	@Test
	public void c_reateWholsoreOrder() throws EntityDoseNotExistException, EntityNotPersistedException
	{
		User supplier=userservice.get(suplier);
		User wholesalor=userservice.get(wholsaler);
		WholesalerOrder wholesalerOrder=new WholesalerOrder();
		wholesalerOrder.setCreateDate(new Date());
		wholesalerOrder.setDescription("forworded order");
		wholesalerOrder.setExpectedDate(new Date());
		wholesalerOrder.setOrder(instOrder);
		wholesalerOrder.setSupplier((Supplier)supplier);
		wholesalerOrder.setUser(wholesalor);
		wholeSalerOrderService.save(wholesalerOrder);
		WholesalerOrderControllerIT.wholesalerOrder=wholesalerOrder;
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
		item.setOrder(instOrder);
		item.setDetail(detail);
		item.setWholesalerOrder(wholesalerOrder);
		itemService.saveItem(item);
	}
	
	@Test
	public void d_reateInstantWholsoreOrder() throws EntityDoseNotExistException, EntityNotPersistedException
	{
		User supplier=userservice.get(suplier);
		User wholesalor=userservice.get(wholsaler);
		InstantWholesalerOrder wholesalerOrder=new InstantWholesalerOrder();
		wholesalerOrder.setCreateDate(new Date());
		wholesalerOrder.setDescription("forworded order");
		wholesalerOrder.setExpectedDate(new Date());
		wholesalerOrder.setOrder(whosalerinstOrder);
		wholesalerOrder.setSupplier((Supplier)supplier);
		wholesalerOrder.setUser(wholesalor);
		wholesalerOrder.setSampleDescription("new order");
		wholesalerOrder.setOrderStatus(OrderStatus.ACTIVE);
		wholeSalerOrderService.save(wholesalerOrder);
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
		item.setOrder(whosalerinstOrder);
		item.setDetail(detail);
		item.setWholesalerOrder(wholesalerOrder);
		itemService.saveItem(item);
	}

	@Test
	public void e_getWholesalerOrder() throws Exception
	{
		MvcResult resp=mockMvc.perform(get("/v1/admin/wholesalerorders/xhr/"+wholesalerOrder.getId()))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/wholesalerorders/xhr/orderview"))
				.andExpect(model().attributeExists("order","WholesalerInstantOrder","worder"))
				.andReturn();
		Map<String, Object>  map =resp.getModelAndView().getModel();
		WholesalerOrder order=(WholesalerOrder)map.get("worder");
		Assert.assertNotNull(order);
		Assert.assertEquals("AD Kamal Tops",order.getProductName());
		Assert.assertEquals(suplier,order.getSupplier().getId());
		Assert.assertEquals(wholsaler, order.getUser().getId());
		WholesalerOrderControllerIT.wholesalerOrder=order;
	}

	@Test
	public void f_update() throws Exception
	{
		LocalDateTime today=new LocalDateTime();
		Date d=today.toDate();
		DateFormat df = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss 'GMT'");
		String cd =df.format(d);
		System.out.println("????????????????????????????????"+wholesalerOrder.getId());
		mockMvc.perform(post("/v1/admin/wholesalerorders/xhr/"+wholesalerOrder.getId())
				.contentType(MediaType.
						APPLICATION_FORM_URLENCODED)
						.param("id",Integer.toString(wholesalerOrder.getId()) )
						.param("item.id",Integer.toString(wholesalerOrder.getItem().getId()))
						.param("item.detail.id",Integer.toString(wholesalerOrder.getItem().getDetail().getId()) )
						.param("createTimestamp", cd)
						.param("user.id",wholesalerOrder.getUser().getId())
						.param("orderStatus","ACTIVE")
						.param("expectedDate",cd)
						.param("activeFlag", "ACTIVE")
						.param("item.product.id",Integer.toString(wholesalerOrder.getItem().getProduct().getId()))
						.param("description", "gajini")
						.param("activeFlag","REJECTED")
						.param("item.meltingAndSeal.id",Integer.toString(wholesalerOrder.getItem().getMeltingAndSeal().getId()))
						.param("item.detail.length",Integer.toString(107))
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
						.param("item.product.category.id",Integer.toString(wholesalerOrder.getItem().getProduct().getCategory().getId()))
				)
				.andExpect(status().isOk());
		
	}
	
	@Test
	public void g_etWholesalerOrderAgain() throws Exception
	{
		MvcResult resp=mockMvc.perform(get("/v1/admin/wholesalerorders/xhr/"+wholesalerOrder.getId()))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/wholesalerorders/xhr/orderview"))
				.andExpect(model().attributeExists("order","WholesalerInstantOrder","worder"))
				.andReturn();
		Map<String, Object>  map =resp.getModelAndView().getModel();
		WholesalerOrder order=(WholesalerOrder)map.get("worder");
		Assert.assertNotNull(order);
		Assert.assertEquals("107",order.getItem().getDetail().getLength());
	}
	
	@Test
	public void h_nsearchOrder() throws Exception
	{
		MvcResult resp=mockMvc.perform(get("/v1/admin/search?q="+"Naveen"+"fields=supplierName&fields=supplierMobileNo&fields=orderNo&fields=productName"))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andReturn();
		String str =resp.getResponse().getContentAsString();
		Assert.assertNotNull(str);

	}
	@Test
	public void i_ofilterhOrderByStatus() throws Exception
	{
		MvcResult resp=mockMvc.perform(get("/v1/admin/wholesalerorders/xhr/list?orderStatus=ACTIVE"))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/wholesalerorders/xhr/list"))
				.andExpect(model().attributeExists("page","pageable","totalcount"))
				.andReturn();
		Map<String, Object>  map =resp.getModelAndView().getModel();
		Object obj= map.get("totalcount");
		Assert.assertEquals(2,obj);

	}
	@Test
	public void k_pfilterhUserByDates() throws Exception
	{

		LocalDateTime to=new LocalDateTime();
		LocalDateTime from =to.minusDays(7);
		Date d1=from.toDate();
		Date d2=to.toDate();
		DateFormat df = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss 'GMT'");
		String dd1 =df.format(d1);
		String dd2=df.format(d2);
		MvcResult resp=mockMvc.perform(get("/v1/admin/wholesalerorders/xhr/list?fromDate="+dd1+"&toDate="+dd2))
				.andExpect(status().isOk())
				.andReturn();
		Map<String, Object>  map =resp.getModelAndView().getModel();
		Object obj= map.get("totalcount");
		Assert.assertEquals(2,obj);

	}


	@Test
	public void l_getOrders() throws Exception{
		MvcResult resp=mockMvc.perform(get("/v1/admin/wholesalerorders/xhr/list"))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/wholesalerorders/xhr/list"))
				.andExpect(model().attributeExists("page","pageable","totalcount"))
				.andReturn();
		Map<String, Object>  map =resp.getModelAndView().getModel();
		Page<WholesalerOrder> page=(Page<WholesalerOrder>) map.get("page");
		wholesalerOrders=page.getContent();
		Assert.assertNotNull(wholesalerOrders);
		Assert.assertEquals(2,(Object)map.get("totalcount"));
	}
	
	@Test
	public void m_deleteWorders() throws EntityDoseNotExistException
	{
		for(WholesalerOrder order:wholesalerOrders)
		{
			wholeSalerOrderService.delete(order);
		}
	}
	
	@Test
	public void n_finalResult() throws Exception
	{
		MvcResult resp=mockMvc.perform(get("/v1/admin/wholesalerorders/xhr/list"))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/wholesalerorders/xhr/list"))
				.andExpect(model().attributeExists("page","pageable","totalcount"))
				.andReturn();
		Map<String, Object>  map =resp.getModelAndView().getModel();
		Page<WholesalerOrder> page=(Page<WholesalerOrder>) map.get("page");
		wholesalerOrders=page.getContent();
		Assert.assertNotNull(wholesalerOrders);
		Assert.assertEquals(0,(Object)map.get("totalcount"));
		
	}
	
	@Test
	public void o_deleteOthers() throws Exception
	{
		orders=utilClass.getOrders(mockMvc);
		users=utilClass.getUsers(mockMvc);
		utilClass.deleteOrders(orders, orderService);
		utilClass.deleteUsers(users, userservice);
	}
}
