package com.awl.tch.externalentityImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.aab.exception.AABServiceException;
import com.aab.utility.AABExternalEntity;
import com.awl.tch.bean.Payment;
import com.awl.tch.bridge.ExternalEntityBridge;
import com.awl.tch.bridge.ExternlaEntityInterface;
import com.awl.tch.constant.Constants;
import com.awl.tch.exceptions.TCHServiceException;

@Service(Constants.TCH_AAB_BRDGE)
public class AABBridgeServiceImpl extends ExternalEntityBridge implements ExternlaEntityInterface<Payment> {

	private static Logger logger = LoggerFactory.getLogger(AABBridgeServiceImpl.class);
	
	@Override
	public Payment updateStatus(Payment input) throws TCHServiceException {
		
		logger.debug("Fetched url from DB ->" +getUtilityInfo(input).get(Constants.TCH_URL));
		input.setUrl(getUtilityInfo(input).get(Constants.TCH_URL));
		String status = "";
		try{
			input.setOriginalAmount(AABExternalEntity.setStatus(input.getBranchCode(), input.getReferenceValue(), input.getRetrivalRefNumber(), getUtilityInfo(input).get(Constants.TCH_URL), input.getBinNumber(), input.getLastFourDigitValue()));
			status=Constants.TCH_SUCCESS;
		} catch (AABServiceException e) {
			status=Constants.TCH_UNSUCCESS+":"+e.getErrorCode();
			throw new TCHServiceException(e.getErrorCode(), e.getErrorMessage());
		}finally{
			saveEnqAck(input,status);
		}
		return input;
	}

	@Override
	public Payment getAmount(Payment input) throws TCHServiceException {
		logger.info("inside switch condition oF AAB" );
		logger.debug("utilityDetail.get(Constants.TCH_AAB_URL) :"  + getUtilityInfo(input).get(Constants.TCH_URL));
		input.setUrl(getUtilityInfo(input).get(Constants.TCH_URL));
		try {
			input.setOriginalAmount(AABExternalEntity.getAmount(input.getBranchCode(), input.getReferenceValue(), input.getRetrivalRefNumber(), getUtilityInfo(input).get(Constants.TCH_URL), input.getBinNumber(), input.getLastFourDigitValue()));
		} catch (AABServiceException e) {
			throw new TCHServiceException(e.getErrorCode(), e.getErrorMessage(),e.getResponseCode());
		}
		return input;
	}
	
	

}
