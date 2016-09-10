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

import java.util.List;
import java.util.Map;

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
import com.mrkinnoapps.myordershopadmin.bean.entity.Product;

/**
 * @author Mallinath Jun 29, 2016  
 */
@RunWith(SpringJUnit4ClassRunner.class)
@MyTestConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductControllersIT {

	@Autowired
	private WebApplicationContext applicationContaxt;

	public MockMvc mockMvc=null;

	static int productId;

	static List<Product> products=null;

	@Before
	public void initGlobalResources() {
		mockMvc = MockMvcBuilders.webAppContextSetup(applicationContaxt)
				.build();
	}

	@Test
	public void a_createProduct() throws Exception
	{
		MvcResult resp=mockMvc.perform(post("/v1/admin/products/xhr")
				.contentType(MediaType.
						APPLICATION_FORM_URLENCODED)
						.param("name", "Antique Jhumki")
						.param("sku", "JCP")
						.param("category.id", "2")
						.param("measurements","1")
						.param("measurements","2")
						.param("measurements","3")
				)
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andReturn();
		String str =resp.getResponse().getContentAsString();
		JSONObject jsonObj=new JSONObject(str);
		productId=(Integer)jsonObj.get("id");
	}

	@Test
	public void b_getProduct() throws Exception
	{
		MvcResult resp=mockMvc.perform(get("/v1/admin/products/xhr/"+productId))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/products/xhr/view"))
				.andExpect(model().attributeExists("product"))
				.andReturn();
		Map<String, Object>  map =resp.getModelAndView().getModel();
		Product product=(Product)map.get("product");
		Assert.assertNotNull(product);
		Assert.assertEquals("Antique Jhumki",product.getName());
	}

	@Test
	public void c_edit() throws Exception{
		mockMvc.perform(post("/v1/admin/products/xhr/"+productId)
				.contentType(MediaType.
						APPLICATION_FORM_URLENCODED)
						.param("name", "Loose Balls Jhumki")
						.param("sku", "JCP")
						.param("category.id", "2")
						.param("measurements","1")
						.param("measurements","2")
						.param("measurements","3")
				)
				.andExpect(status().isOk());

	}

	@Test
	public void d_getProductAgain() throws Exception
	{
		MvcResult resp=mockMvc.perform(get("/v1/admin/products/xhr/"+productId))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/products/xhr/view"))
				.andExpect(model().attributeExists("product"))
				.andReturn();
		Map<String, Object>  map =resp.getModelAndView().getModel();
		Product product=(Product)map.get("product");
		Assert.assertNotNull(product);
		Assert.assertEquals("Loose Balls Jhumki",product.getName());
	}

	@Test
	public void e_getProducts() throws Exception
	{
		MvcResult resp=mockMvc.perform(get("/v1/admin/products/xhr/categories/"+2))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/products/xhr/list"))
				.andExpect(model().attributeExists("page","pageable","totalcount"))
				.andReturn();
		Map<String, Object>  map =resp.getModelAndView().getModel();
		int tc=(Integer)map.get("totalcount");
		Page<Product> page=(Page<Product>) map.get("page");
		products= page.getContent();
		Assert.assertEquals(1,tc);
	}

	@Test
	public void f_getCategories() throws Exception
	{
		MvcResult resp=mockMvc.perform(get("/v1/admin/products/xhr/categories"))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/products/xhr/categoryList"))
				.andExpect(model().attributeExists("page","pageable","totalcount"))
				.andReturn();
		Map<String, Object>  map =resp.getModelAndView().getModel();
		int tc=(Integer)map.get("totalcount");
		//Assert.assertEquals(5,tc);
	}

	@Test
	public void g_delete() throws Exception
	{
		for(Product p:products){
			mockMvc.perform(get("/v1/admin/products/xhr/"+p.getId()+"/delete"))
			.andExpect(status().isOk());
		}
	}

	@Test
	public void h_getProductsAgain() throws Exception
	{
		MvcResult resp=mockMvc.perform(get("/v1/admin/products/xhr/categories/"+2))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/products/xhr/list"))
				.andExpect(model().attributeExists("page","pageable","totalcount"))
				.andReturn();
		Map<String, Object>  map =resp.getModelAndView().getModel();
		int tc=(Integer)map.get("totalcount");
		Assert.assertEquals(0,tc);
	}

	/*@Test
	public void uploadimage() throws Exception
	{		
		File f = new File("people4.jpg");
        System.out.println(f.isFile()+"  "+f.getName()+f.exists());
        FileInputStream fi1 = new FileInputStream(f);
        MockMultipartFile firstFile = new MockMultipartFile("file", f.getName(), "multipart/form-data",fi1);
		mockMvc.perform(MockMvcRequestBuilders.fileUpload("/v1/admin/products/xhr/"+productId+"/images")
				.file(firstFile)
				)
				.andExpect(status().isOk());
	}*/
}
