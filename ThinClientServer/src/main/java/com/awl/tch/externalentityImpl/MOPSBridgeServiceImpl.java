package com.awl.tch.externalentityImpl;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.awl.tch.bean.Payment;
import com.awl.tch.bridge.ExternalEntityBridge;
import com.awl.tch.bridge.ExternlaEntityInterface;
import com.awl.tch.constant.Constants;
import com.awl.tch.exceptions.TCHServiceException;
import com.awl.tch.model.MposDTO;
import com.awl.tch.mpos.bean.MOPSBean;
import com.awl.tch.mpos.bean.MOPSResponse;
import com.awl.tch.mpos.exception.SbiMopsException;
import com.awl.tch.service.AbstractServiceImpl;
import com.awl.tch.util.UtilityHelper;

@Service(Constants.TCH_MOP_BRIDGE)
public class MOPSBridgeServiceImpl extends ExternalEntityBridge implements ExternlaEntityInterface<Payment>{

	private static Logger logger = LoggerFactory.getLogger(MOPSBridgeServiceImpl.class);
	
	@Override
	public Payment updateStatus(Payment input) throws TCHServiceException {
		logger.debug("ACK to MPOS");
		MOPSBean sbiMpos=new MOPSBean();
		
		sbiMpos.setBankRefNo(input.getReferenceValue());
		BigDecimal amount= new BigDecimal(input.getOriginalAmount());
		amount = amount.divide(AbstractServiceImpl.HUNDRED);
		sbiMpos.setAmount(amount.toPlainString());
		sbiMpos.setDateTime(UtilityHelper.getDateTime());
		sbiMpos.setStatusCode("00");
		sbiMpos.setStatusDescription("transaction found.");
		sbiMpos.setStatus("Success");
		
		String url=(new StringBuilder(getUtilityInfo(input).get(Constants.TCH_URL)).append(Constants.TCH_MPOS_STATUS)).toString();
		MOPSResponse mopsRes = null;
		try{
			mopsRes = mopService.updateStatus(sbiMpos, url);
			
			MposDTO mposDto=new MposDTO();
			mposDto.setReqType("ENQUIRYACK");
			mposDto.setBankRefNo(input.getReferenceValue());
			mposDto.setAmount(input.getOriginalAmount());
			mposDto.setDateTime(UtilityHelper.getDateTime());
			mposDto.setStatus("SUCCESS");
			mposDto.setStatusCode(mopsRes.getStatusCode());
			mposDto.setStatusDescription(mopsRes.getStatusDescription());
			logger.debug("Mops Object :" + mposDto);
			mopDao.save(mposDto);
		}catch (SbiMopsException e1) {
			logger.debug("Exception Occured",e1);
			throw new TCHServiceException(e1.getErrorCode(),e1.getErrorMessage());
		} 
		return input;
	}

	@Override
	public Payment getAmount(Payment input) throws TCHServiceException {
		logger.debug("inside getAmount");
		MOPSBean mposBean = new MOPSBean();
		mposBean.setBankRefNo(input.getReferenceValue());
		mposBean.setDateTime(UtilityHelper.getDateTime());
		MOPSResponse sbiMopsRes = null;
		try{
			String url = new StringBuilder(getUtilityInfo(input).get(Constants.TCH_URL)).append(Constants.TCH_MPOS_AMOUNT).toString();
			logger.debug("URL : "  +url);
			sbiMopsRes = mopService.getAmount(mposBean, url);
			input.setStatus(sbiMopsRes.getStatus());
			BigDecimal amount= new BigDecimal(sbiMopsRes.getAmount());
			amount = amount.multiply(AbstractServiceImpl.HUNDRED);
			input.setOriginalAmount(String.valueOf(amount.intValue()));
			logger.debug("amount after conversion " + input.getOriginalAmount());
		}  catch (SbiMopsException e) {
			logger.debug("Exception Occured",e);
			throw new TCHServiceException(e.getErrorCode(),e.getErrorMessage());
		} 
		// saving data for mops
		MposDTO mposDto=new MposDTO();
		mposDto.setDateTime(UtilityHelper.getDateTime());
		mposDto.setBankRefNo(input.getReferenceValue());
		mposDto.setReqType("ENQUIRY");
		mposDto.setAmount(input.getOriginalAmount());
		mposDto.setStatus(sbiMopsRes.getStatus());
		mposDto.setStatusCode(sbiMopsRes.getStatusCode());
		mposDto.setStatusDescription(sbiMopsRes.getStatusDescription());
		mopDao.save(mposDto);
		
		return input;
	}

}
