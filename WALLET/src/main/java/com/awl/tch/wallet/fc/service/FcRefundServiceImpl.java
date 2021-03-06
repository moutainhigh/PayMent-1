package com.awl.tch.wallet.fc.service;

import java.lang.reflect.Modifier;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.awl.tch.wallet.common.AbstractWalletService;
import com.awl.tch.wallet.common.HttpsClient;
import com.awl.tch.wallet.common.WalletException;
import com.awl.tch.wallet.fc.bean.WalletResponseBean;
import com.awl.tch.wallet.fc.constant.FcConstant;
import com.awl.tch.wallet.fc.model.FcWalletRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * FcVoidServiceImpl is an API for Wallet's void transactions.
 * Method consumeWS is used to call Fc host web-service for void
 * @author pooja.patil
 *
 */
@Service(FcConstant.TCH_FC_REFUND_SERVICE)
public class FcRefundServiceImpl extends AbstractWalletService implements FcService{

	private static Logger logger = LoggerFactory.getLogger(FcRefundServiceImpl.class);
	
	@Override
	public WalletResponseBean cosumeWS(FcWalletRequest request) throws WalletException {

		logger.debug("Entering web service for void");
		Gson g = new GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).create();
		String apiParams = g.toJson(request);
		logger.debug("Plain Text [" +apiParams+"]");
		
		String url = new StringBuilder(request.getUrl()).append(FcConstant.TCH_FC_WALLET_API_VOID).toString();
		logger.debug("URL ["+url+"]");
		
		HashMap<String, String> headerMap = setHeaderParams(apiParams.length());
        String responseJson = HttpsClient.send(url, apiParams, headerMap);
        if (responseJson != null && !responseJson.isEmpty()) {
			logger.debug("Response Json ["+responseJson+"]");
		} else{
			throw new WalletException("Response received as null");
		}
		WalletResponseBean response = (WalletResponseBean) parseGson(FcConstant.TCH_FC_REFUND_SERVICE, responseJson);
        
		logger.debug("Result ["+response+"]");
		logger.debug("Exiting cosumeWS().. Exiting web service for void enquiry");
	
		return response;
        
	}
	
	/**
	 * Setting response in FcWalletRequest object for testing purpose
	 * @param response
	 * @return
	 */
	public static void main(String[] args){
		FcRefundServiceImpl fcVoid = new FcRefundServiceImpl();
		FcWalletRequest voidReq=new FcWalletRequest();
		voidReq.setMerchantId("1");
		voidReq.setTerminalId("TERM0001");
		voidReq.setPlatformId("WB8qmHJoQBrN");
		voidReq.setTransactionRefNumber("45463441232");
		voidReq.setTxnDatenTime("23052016154010");
		voidReq.setProcCode("20");
		voidReq.setServerTxnId("123456789012345678");
		voidReq.setAuthToken("4c0f8dfd0fefd290b33a314d111b75de123d91d9a278556dd199b6a60de92550");
		voidReq.setAdditinalInfo(null);
		
		
		try {
			fcVoid.cosumeWS(voidReq)	;
		} catch (WalletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

}
