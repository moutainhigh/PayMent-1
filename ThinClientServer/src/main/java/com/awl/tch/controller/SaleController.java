package com.awl.tch.controller;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import com.awl.tch.bean.Payment;
import com.awl.tch.constant.Constants;
import com.awl.tch.constant.ErrorConstants;
import com.awl.tch.exceptions.TCHServiceException;
import com.awl.tch.server.Request;
import com.awl.tch.server.Response;
import com.awl.tch.service.SaleService;
import com.awl.tch.util.ErrorMaster;
import com.awl.tch.util.JsonHelper;
/**
* <h1>Sale</h1>
* SaleController is going to process sale transaction
* <p>
* In which request parameter must contain mandatory 
* field as <tt>Terminal serial number</tt>,<tt>Original amount</tt>
* which is going to make use for making <tt>ISO200</tt> packet
* <p>
* The request going to send to switch and after that respective 
* response code and related information going to get from switch.
* 
* <b>Note:</b> All the mandatory field must be there
* to get appropriate response from switch.
*
* @author  Ashish Bhavsar
* @version 1.0
* @since   2016-09-21
*/
@Controller(value="SALE")
public class SaleController extends AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(SaleController.class);

	@Autowired
	@Qualifier("paymentSaleService")
	SaleService paymentSaleService;
	
	/**
	   *This method take request in <tt>json</tt> format and proceed with
	   *it and then store response in response object in <tt>json</tt> format.
	   *
	   * @param reqObj This is the first paramter and it contain actual request object.
	   * @param resObj  This is the second parameter and it contain response object.
	   */
	@Override
	public void process(Request requestObj, Response responseObj) {

		logger.debug("Initiate sale request");
		Payment saleRequest = new Payment();
		try {
			try {
				saleRequest = (Payment) JsonHelper.getActualObject(requestObj, Payment.class);
			} catch (InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException
					| IntrospectionException e) {
				responseObj.setErrorPresent(true);
				e.printStackTrace();
				logger.debug(e.getMessage());
				Response.setErrorResponseObject(responseObj,ErrorConstants.TCH_Z002, ErrorMaster.get(ErrorConstants.TCH_Z002));
				return;
			}
			if(saleRequest.getMenuobj() != null && !("WLT").equals(saleRequest.getCardEntryMode())){		// Changes for UPI integration
				try {
					saleRequest = paymentSaleService.serviceQR(saleRequest);
				} catch (TCHServiceException e) {
					responseObj.setErrorPresent(true);
					if(saleRequest.getHealthObject() != null){
						logger.info("Setting health object information in error");
						Response.setErrorResponseObject(responseObj,e.getErrorCode(),e.getErrorMessage(),e.getResponseCode(),e.getIssuerField55(),saleRequest.getHealthObject());
					}else{
						Response.setErrorResponseObject(responseObj,e.getErrorCode(),e.getErrorMessage(),e.getResponseCode(),e.getIssuerField55());
					}
					logger.debug(e.getMessage());
					return;
				}
				logger.debug("Setting sale response");
				Response.setResponseObject(responseObj,saleRequest);
			}else if(saleRequest.getMenuobj() != null && ("WLT").equals(saleRequest.getCardEntryMode())){ //changes by Pooja for Freecharge Wallet
				try{
					if ( null != saleRequest.getMenuobj()[1] && Constants.WALLET_FREECHARGE.equals(saleRequest.getMenuobj()[1].getDisplayName())){
						saleRequest.setAppName(Constants.WALLET_FREECHARGE);
						saleRequest = paymentSaleService.serviceFC(saleRequest);
					}
				} catch (TCHServiceException e){
					responseObj.setErrorPresent(true);
					if(saleRequest.getHealthObject() != null){
						logger.info("Setting health object information in error");
						Response.setErrorResponseObject(responseObj,e.getErrorCode(),e.getErrorMessage(),e.getResponseCode(),e.getIssuerField55(),saleRequest.getHealthObject());
					}else{
						Response.setErrorResponseObject(responseObj,e.getErrorCode(),e.getErrorMessage(),e.getResponseCode(),e.getIssuerField55());
					}
					logger.debug(e.getMessage());
					return;
				}
				logger.debug("Setting FcSale response");
				Response.setResponseObject(responseObj,saleRequest);
			}else{
				try {
					saleRequest = paymentSaleService.service(saleRequest);
				} catch (TCHServiceException e) {
					responseObj.setErrorPresent(true);
					if(saleRequest.getHealthObject() != null){
						logger.info("Setting health object information in error");
						Response.setErrorResponseObject(responseObj,e.getErrorCode(),e.getErrorMessage(),e.getResponseCode(),e.getIssuerField55(),saleRequest.getHealthObject());
					}else{
						Response.setErrorResponseObject(responseObj,e.getErrorCode(),e.getErrorMessage(),e.getResponseCode(),e.getIssuerField55());
					}
					logger.debug(e.getMessage());
					return;
				}
				logger.debug("Setting sale response");
				Response.setResponseObject(responseObj,saleRequest);
			}
		} catch (Exception e) {
			logger.error("Exception in processing Sale request :"+e.getMessage(),e);
			logger.debug(e.getMessage());
			if(saleRequest != null){
				if(ErrorConstants.TCH_S999.equals(saleRequest.getDecisionFlag())){
					logger.debug("PCR raised and hence S-999");
					Response.setErrorResponseObject(responseObj,ErrorConstants.TCH_S999,ErrorMaster.get(ErrorConstants.TCH_S999));
					logger.debug("Actual exception :" + e.getMessage());
				}
				else
					Response.setErrorResponseObject(responseObj,ErrorConstants.TCH_S001,ErrorMaster.get(ErrorConstants.TCH_S001));
			}
		}
	}

}
