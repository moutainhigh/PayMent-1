package com.awl.tch.controller;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import com.awl.tch.bean.ReloadBean;
import com.awl.tch.brandemi.service.BrandEMIService;
import com.awl.tch.server.Request;
import com.awl.tch.server.Response;
import com.awl.tch.util.AppConfigMaster;
import com.awl.tch.util.EMIMaster;
import com.awl.tch.util.ErrorMaster;
import com.awl.tch.util.ISOMessages;
import com.awl.tch.util.JsonHelper;
import com.awl.tch.util.TerminalParameterCacheMaster;

@Controller(value="BRDCACHE")
public class ReloadCacheController extends AbstractController{
	
	private static Logger logger = LoggerFactory.getLogger(ReloadCacheController.class);
	
	@Autowired
	@Qualifier("brandEMIService")
	BrandEMIService brandEmiservice;

	@Override
	public void process(Request requestObj, Response responseObj) {
		logger.debug("Inside reloading cache :" + requestObj);
		ReloadBean reloadBean = new ReloadBean();
		try {
			reloadBean = (ReloadBean) JsonHelper.getActualObject(requestObj, ReloadBean.class);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException
				| ClassNotFoundException | IntrospectionException e) {
			logger.debug("Exiting from reloading" ,e) ;
		}
		logger.debug("reloadBean : "+ reloadBean);
		if(reloadBean != null && reloadBean.getPrm() != null && !reloadBean.getPrm().isEmpty()){
			
			logger.debug("reloading for [ "+reloadBean.getPrm()+"");
			
			try{
			brandEmiservice.reloadMenuObject(reloadBean.getPrm());
			}catch(Exception e){
				logger.debug("Exception occured..",e);
			}
			
			ISOMessages.load();
			ErrorMaster.load();
			EMIMaster.load();
			TerminalParameterCacheMaster.load();
			AppConfigMaster.load();
			
		}else{
			
			logger.debug("reloading without parameter");
			try{
			brandEmiservice.reloadMenuObject();
			}catch(Exception e){
				logger.debug("Exception occured..",e);
			}
			ISOMessages.load();
			ErrorMaster.load();
			AppConfigMaster.load();
			EMIMaster.load();
			TerminalParameterCacheMaster.load();
			
		}
		logger.debug("Cache reloaded");
	}
}
