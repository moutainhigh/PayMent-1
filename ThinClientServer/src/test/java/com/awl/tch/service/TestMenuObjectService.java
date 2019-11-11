package com.awl.tch.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.awl.tch.bean.MenuObject;

public class TestMenuObjectService {

	private ApplicationContext context;
	private MenuObjectServiceImpl menuObjectService;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		context = new ClassPathXmlApplicationContext("spring.xml");
		menuObjectService = context.getBean("menuObjectService",MenuObjectServiceImpl.class);
	}


	 
	
	
	/*@Test
	public void testMenuList1() {
		MenuObject[] menArray =  menuObjectService.getMenuList();
		for(MenuObject menu : menArray )
		{
			System.out.println(menu);
		}
		Assert.assertTrue(menArray.length > 0);
	}*/
	
	@Test
	public void testMenuList2() {
		String arr[] = {"WALLET"};
		String submenuArry = "FC";
		MenuObject[] menArray =  menuObjectService.getMenuList("SALE",arr,submenuArry);
		for(MenuObject menu : menArray )
		{
			System.out.println(menu);
		}
		Assert.assertTrue(menArray.length > 0);
	}
	
	@Test
	@Ignore
	public void testGetMenuObjectValue() throws Exception {
		
		MenuObject menuObject1 = new MenuObject(1,4,1,"QR" , "SELECT PAYMENT MODE", null);
		MenuObject menuObject2 = new MenuObject(2,1,2,"BHARAT-QR" , "SELECT QR", null);
		MenuObject[] menuArray = new MenuObject[2];
		menuArray[0] = menuObject1;
		menuArray[1] = menuObject2;
		String str =  menuObjectService.getMenuObjectValue("SALE", menuArray);
		System.out.println(str);
		Assert.assertEquals(str,"5");
	}
	
	
	

}
