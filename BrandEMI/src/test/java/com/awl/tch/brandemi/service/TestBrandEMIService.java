package com.awl.tch.brandemi.service;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.awl.tch.bean.MenuObject;
import com.awl.tch.bean.SuperMenuObject;
import com.awl.tch.brandemi.model.ValidationParameter;

public class TestBrandEMIService {

	private static final Logger logger = LoggerFactory.getLogger(TestBrandEMIService.class);
	private ApplicationContext context;
	@Before
	public void setUp()
	{
		context = new ClassPathXmlApplicationContext("/spring-brandemi.xml");
	}
	
	@Test
	@Ignore
	public void testGetMenuObject() {
		BrandEMIService emiService =  (BrandEMIService) context.getBean("brandEMIService");
		LinkedList<MenuObject> menlist =  emiService.getMenuObject("000056690630018");
		for(MenuObject menuObj : menlist )
		{
			System.out.println(menuObj);
		}
		Assert.assertNotNull(menlist);
	}

	@Test
	@Ignore
	public void testGetSKUCodeMenu() {
		
		
		BrandEMIService emiService =  (BrandEMIService) context.getBean("brandEMIService");
		LinkedList<MenuObject> list =  emiService.getMenuObject("000056690630018");
		for(MenuObject menuObj : list )
		{
			System.out.println(menuObj);
		}
		MenuObject menuObject1 = new MenuObject(0,1,2,"MOBILE" , "Select Category", null);
		MenuObject menuObject2 = new MenuObject(1,2,4,"SAMSUNG" , "Select Brand", null);
		MenuObject menuObject3 = new MenuObject(3,4,20,"MBO" , "Select Scheme", null);
		MenuObject menuObject4 = new MenuObject(19,20,194,"SMART PHONE" , "PRODUCT CATEGORY", null);
		MenuObject menuObject5 = new MenuObject(20,194,195,"S7" , "MODEL NO", null);
		MenuObject menuObject6 = new MenuObject(21,195,196,"5.5 INCH" , "SIZE", null);
		MenuObject menuObject7 = new MenuObject(22,196,197,"white" , "COLOR", null);
		MenuObject[] menuArray = new MenuObject[7];
		menuArray[0] = menuObject1;
		menuArray[1] = menuObject2;
		menuArray[2] = menuObject3;
		menuArray[3] = menuObject4;
		menuArray[4] = menuObject5;
		menuArray[5] = menuObject6;
		menuArray[6] = menuObject7;
		SuperMenuObject obj = new SuperMenuObject();
		obj.setMenuObject(menuArray);
		LinkedList<MenuObject> menlist = emiService.getSKUCodeMenu("000056690630018", obj);
		//LinkedList<MenuObject> list = menuService.getMenuObject("000001005000009");
		System.out.println(menlist);
		Assert.assertNotNull(menlist);
		Assert.assertEquals(1, menlist.size());
	}
	
	@Test
	@Ignore
	public void testGetProductDetails() throws Exception {
		
		
		BrandEMIService emiService =  (BrandEMIService) context.getBean("brandEMIService");
		
		LinkedList<MenuObject> list =  emiService.getMenuObject("075126031302788");
		for(MenuObject menuObj : list )
		{
			System.out.println(menuObj);
		}
		
		MenuObject menuObject1 = new MenuObject(0,1,2,"MOBILE" , "Select Category", null);
		MenuObject menuObject2 = new MenuObject(1,2,4,"SAMSUNG" , "Select Brand", null);
		MenuObject menuObject3 = new MenuObject(3,4,20,"MBO" , "Select Scheme", null);
		MenuObject menuObject4 = new MenuObject(19,20,194,"SMART PHONE" , "PRODUCT CATEGORY", null);
		MenuObject menuObject5 = new MenuObject(20,194,195,"S7" , "MODEL NO", null);
		MenuObject menuObject6 = new MenuObject(21,195,196,"5.5 INCH" , "SIZE", null);
		MenuObject menuObject7 = new MenuObject(22,196,197,"white" , "COLOR", null);
		MenuObject[] menuArray = new MenuObject[7];
		menuArray[0] = menuObject1;
		menuArray[1] = menuObject2;
		menuArray[2] = menuObject3;
		menuArray[3] = menuObject4;
		menuArray[4] = menuObject5;
		menuArray[5] = menuObject6;
		menuArray[6] = menuObject7;
		String []programCode =null;// emiService.getProductDetails("075126031302788", menuArray);
		//LinkedList<MenuObject> list = menuService.getMenuObject("000001005000009");
		System.out.println(Arrays.toString(programCode));
		Assert.assertNotNull(programCode);
	}
	
	@Test
	@Ignore
	public void testGetValidationParameter() {
		
		
		BrandEMIService emiService =  (BrandEMIService) context.getBean("brandEMIService");
		
		LinkedList<MenuObject> list =  emiService.getMenuObject("000056690630018");
		for(MenuObject menuObj : list )
		{
			System.out.println(menuObj);
		}
		
		MenuObject menuObject1 = new MenuObject(0,1,2,"MOBILE" , "COLOR", null);
		MenuObject menuObject2 = new MenuObject(1,2,4,"SAMSUNG ELECTRONICS" , "COLOR", null);
		MenuObject menuObject3 = new MenuObject(2,4,6,"MBO" , "COLOR", null);
		MenuObject[] menuArray = new MenuObject[3];
		menuArray[0] = menuObject1;
		menuArray[1] = menuObject2;
		menuArray[2] = menuObject3;
		ValidationParameter parameter = emiService.getValidationParameter("000056690630018",menuArray );
		//LinkedList<MenuObject> list = menuService.getMenuObject("000001005000009");
		Assert.assertNotNull(parameter);
	}
	@Test
	public void testBlockSerialNumber() throws MalformedURLException 	{
		System.setProperty("https.proxyHost", "10.10.15.200");
    	System.setProperty("https.proxyPort", "8080");
		logger.debug("testing testValidateSerialNumber");
		BrandEMIService emiService =  (BrandEMIService) context.getBean("brandEMIService");
		HashMap<String, String> map = new HashMap<>();
		/*map.put("URL", "https://www.mcs-swa.com/CESerialValidate/ValidateSerialService.asmx?wsdl");
		map.put("USERNAME", "ATOS");
		map.put("PASSWORD", "S@5tuDffyC");*/
		map.put("GSM_URL", "http://202.154.175.50/serialnovalidate/service.asmx");
		String response = emiService.blockSerialNumber("LG",map , "354035040094359" , "UA24H4003ARMXL","1");
		logger.debug("testing testValidateSerialNumber end:"+response);
	}
	@Test
	@Ignore
	public void testUnBlockSerialNumber() throws MalformedURLException 	{
		/*System.setProperty("https.proxyHost", "10.10.15.200");
    	System.setProperty("https.proxyPort", "8080");*/
		logger.debug("testing testUnBlockSerialNumber");
		BrandEMIService emiService =  (BrandEMIService) context.getBean("brandEMIService");
		String args [] = {"https://www.mcs-swa.com/CESerialValidate/ValidateSerialService.asmx?wsdl", "ATOS" ,"S@5tuDffyC" , "", "0A0Y3ZNJ428293"};
		HashMap<String, String> map = new HashMap<>();
		/*map.put("URL", "https://www.mcs-swa.com/CESerialValidate/ValidateSerialService.asmx?wsdl");
		map.put("USERNAME", "ATOS");
		map.put("PASSWORD", "S@5tuDffyC");*/
		map.put("GSM_URL", "http://202.154.175.50/serialnovalidate/service.asmx");
		/*String response = emiService.unblockSerialNumber("SAMSUNG",map , "0A0Y3ZNJ428293");*/
		String response = emiService.unblockSerialNumber("LG",map , "354035040094359","LGP715","2");
		logger.debug("testing testUnBlockSerialNumber end:"+response);
	}
	
	@Test
	@Ignore
	public void testGetDealerCode() throws MalformedURLException 	{
		
		BrandEMIService emiService =  (BrandEMIService) context.getBean("brandEMIService");
		
		LinkedList<MenuObject> list =  emiService.getMenuObject("000056690630018");
		for(MenuObject menuObj : list )
		{
			System.out.println(menuObj);
		}
		
		
		MenuObject menuObject1 = new MenuObject(0,1,2,"MOBILE" , "COLOR", null);
		MenuObject menuObject2 = new MenuObject(1,3,11,"SAMSUNG ELECTRONICS" , "COLOR", null);
		MenuObject menuObject3 = new MenuObject(2,4,6,"MBO" , "COLOR", null);
		MenuObject[] menuArray = new MenuObject[3];
		menuArray[0] = menuObject1;
		menuArray[1] = menuObject2;
		menuArray[2] = menuObject3;
		String response = emiService.getDealerCode("000056690630018", menuArray);
		logger.debug("testing getDealerCode end:"+response);
	}
	
	@Test
	@Ignore
	public void testManufacturerDetails() throws MalformedURLException 	{
		
		BrandEMIService emiService =  (BrandEMIService) context.getBean("brandEMIService");
		LinkedList<MenuObject> list =  emiService.getMenuObject("075126031302788");
		//LinkedList<MenuObject> list =  emiService.getMenuObject("000056690630018");
		for(MenuObject menuObj : list)
		{
			System.out.println(menuObj);
		}
		MenuObject menuObject1 = new MenuObject(0,1,2,"MOBILE" , "COLOR", null);
		MenuObject menuObject2 = new MenuObject(1,3,12,"SAMSUNG ELECTRONICS" , "COLOR", null);
		MenuObject menuObject3 = new MenuObject(2,4,6,"MBO" , "COLOR", null);
		MenuObject[] menuArray = new MenuObject[3];
		menuArray[0] = menuObject1;
		menuArray[1] = menuObject2;
		menuArray[2] = menuObject3;
		String[] response = emiService.getManufacturerDetails("000056690630018", menuArray);
		logger.debug("testing testManufacturerDetails end:"+Arrays.toString(response));
	}
}
