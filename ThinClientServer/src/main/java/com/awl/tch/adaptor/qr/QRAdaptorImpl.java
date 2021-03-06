package com.awl.tch.adaptor.qr;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.Const;

import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.awl.tch.bean.DataPrintObject;
import com.awl.tch.bean.Payment;
import com.awl.tch.constant.Constants;
import com.awl.tch.constant.ErrorConstants;
import com.awl.tch.dao.SaleDao;
import com.awl.tch.exceptions.TCHServiceException;
import com.awl.tch.model.PaymentDTO;
import com.awl.tch.model.QRRequestDTO;
import com.awl.tch.upi.bean.QRResponse;
import com.awl.tch.upi.dao.QRDaoImpl;
import com.awl.tch.upi.service.QRWSService;
import com.awl.tch.util.AppConfigMaster;

@Component(Constants.TCH_QR_ADAPTOR)
public class QRAdaptorImpl implements QRAdaptor{

	private static Logger logger = LoggerFactory.getLogger(QRAdaptorImpl.class);
	
	@Autowired
	@Qualifier("qrwsCallService")
	protected QRWSService upiWSService;
	
	@Autowired
	@Qualifier("qrDao")
	QRDaoImpl qrDao;
	
	@Autowired
	@Qualifier(Constants.TCH_SALE_DAO)
	SaleDao saledao;

	@Override
	public Payment getQRCodeString(Payment input) throws TCHServiceException {
		String url = AppConfigMaster.getConfigValue(Constants.TCH_QR, Constants.TCH_QR_URL) + Constants.TCH_QR_GET;
		try{
			QRResponse response = upiWSService.getQRString(url,input.getBankCode(),input.getMerchantId(), input.getTerminalId(), input.getOriginalAmount(),input.getQrType());
			
			if(Constants.TCH_QR_SUCCESS.equals(response.getStatus())){
				input.setQrString(response.getResponseObject().getQrString());
				input.setQrType(response.getResponseObject().getProgramType());
				input.setReferenceValue(response.getResponseObject().getTrId());
				logger.debug("QR TrId ["+input.getReferenceValue()+"]");
				logger.debug("QR string [" + input.getQrString() +"]");
				logger.debug("QR type [" + input.getQrType()+"]");
				QRRequestDTO qrDto = input.paramQRRequestDTO();
				logger.debug("QR DTO :" + qrDto.toString());
				qrDao.save(input.paramQRRequestDTO());
			}else{
				logger.debug("Exception while getting QR string");
				throw new TCHServiceException(ErrorConstants.TCH_Q002,response.getMessage());
			}
		}catch(Exception e){
			logger.debug("Excetion occured " , e);
			throw new TCHServiceException(ErrorConstants.TCH_Q005, e.getMessage());
		}
		return input;
	}

	@Override
	public Payment checkStatus(Payment input) throws TCHServiceException {
		String url = AppConfigMaster.getConfigValue(Constants.TCH_QR, Constants.TCH_QR_URL) + Constants.TCH_QR_CHECKSTATUS;
		QRResponse response = null;
		try {
			response = upiWSService.getACKString(url,input.getBankCode(),input.getMerchantId(), input.getTerminalId() ,input.getQrType()
						, input.getReferenceValue());
		} catch (JSONException e1) {
			logger.debug("Excetion occured " , e1);
			throw new TCHServiceException(ErrorConstants.TCH_Q005, e1.getMessage());
		}
			logger.debug("Status of transaction : [" +response.getStatus()+"]");
			if(Constants.TCH_QR_SUCCESS.equals(response.getStatus())){
				input.setRetrivalRefNumber(response.getResponseObject().getRrn());
				input.setAuthorizationId(response.getResponseObject().getAuthCode());
				input.setStatus(Constants.TCH_QR_SUCCESS);
				logger.debug("RRN [" +input.getRetrivalRefNumber()+"]");
				logger.debug("AUTH id ["+ input.getAuthorizationId()+"]");
				
				List<DataPrintObject> listPrintObj = new ArrayList<DataPrintObject>();
				
				DataPrintObject dataPrintObj = new DataPrintObject();
				dataPrintObj.setPrintLabel("Merchant VPA :");
				dataPrintObj.setPrintVal(response.getResponseObject().getMerchantVpa());
				listPrintObj.add(dataPrintObj);
				
				/*dataPrintObj = new DataPrintObject();
				dataPrintObj.setPrintLabel("Customer VPA :");
				dataPrintObj.setPrintVal(response.getResponseObject().getCustomerVpa());*/
				//listPrintObj.add(dataPrintObj);
				logger.debug("Merchant VPA ["+response.getResponseObject().getMerchantVpa()+"]");
				logger.debug("Customer VPA ["+response.getResponseObject().getCustomerVpa()+"]");
				input.setDataPrintObject(listPrintObj.toArray(new DataPrintObject[listPrintObj.size()]));
				
				qrDao.qrCodeDetails(input); // set batch number
				try{
					savePaymentDTO(input,Constants.TCH_REQUEST_SALE,response.getResponseObject().getMerchantVpa(),response.getResponseObject().getCustomerVpa());
				}catch(Exception e){
					logger.debug("Exception while saving in DB " ,e);
				}
			}else if(Constants.TCH_QR_PENDING.equals(response.getStatus())){
				logger.debug("Exception while getting QR status");
				throw new TCHServiceException(ErrorConstants.TCH_Q001,response.getMessage());
			}else{
				//qrDao.save(input.paramQRRequestDTO());
				logger.debug("Exception while getting QR status");
				throw new TCHServiceException(ErrorConstants.TCH_Q003,response.getMessage());
			}
		return input;
	}

	@Override
	public Payment qrCancel(Payment input) throws TCHServiceException {
		String url = AppConfigMaster.getConfigValue(Constants.TCH_QR, Constants.TCH_QR_URL) + Constants.TCH_QR_CAN;
		// TODO Auto-generated method stub
		try{
			QRResponse response = upiWSService.getCANString(url,input.getBankCode(), input.getMerchantId(), input.getTerminalId(), input.getQrType()
					, input.getReferenceValue());
			if(Constants.TCH_QR_SUCCESS.equals(response.getStatus())){
				savePaymentDTO(input,Constants.TCH_REQUEST_SALE,null,null);
			}else{
				logger.debug("Exception while getting QR status");
				throw new TCHServiceException(ErrorConstants.TCH_Q004,response.getMessage());
			}
		}catch(Exception e){
			logger.debug("Excetion occured " , e);
			throw new TCHServiceException(ErrorConstants.TCH_Q005, "WEBSERVICE ISSUE");
		}
		return input;
	}

	@Override
	public Payment refundQr(Payment input) throws TCHServiceException {
		String url = AppConfigMaster.getConfigValue(Constants.TCH_QR, Constants.TCH_QR_URL) + Constants.TCH_QR_REFUND;
			QRResponse response = null;
			try {
				response = upiWSService.getRefundString(url,input.getBankCode(), input.getMerchantId(), input.getTerminalId(),
						input.getOriginalAmount(), input.getReferenceValue(),input.getMobileNumber());
			} catch(Exception e){
				logger.debug("Excetion occured " , e);
				throw new TCHServiceException(ErrorConstants.TCH_Q005, "WEBSERVICE ISSUE");
			}
			logger.debug("Status of transaction : [" +response.getStatus()+"]");
			if(Constants.TCH_QR_SUCCESS.equals(response.getStatus())){
				
				if(response.getResponseObject().getRrn() != null)
					input.setRetrivalRefNumber(response.getResponseObject().getRrn().substring(0,12));
				input.setAuthorizationId(response.getResponseObject().getAuthCode());
				input.setStatus(Constants.TCH_QR_SUCCESS);
				logger.debug("RRN [" +input.getRetrivalRefNumber()+"]");
				logger.debug("AUTH id ["+ input.getAuthorizationId()+"]");
				
				qrDao.qrCodeDetails(input); // set batch number
				//qrDao.update(input.paramQRRequestDTO());
				
				savePaymentDTO(input, Constants.TCH_REQUEST_REFUND,null,null);
				
			}else{
				//qrDao.save(input.paramQRRequestDTO());
				logger.debug("Exception while getting QR status");
				throw new TCHServiceException(ErrorConstants.TCH_Q003,response.getMessage());
			}
		return input;
	}
	
	/**
	 * Save transaction in TCH payment table
	 * @param input
	 */
	private void savePaymentDTO(final Payment input, String rquestType, String merchantVPA, String customerVPA){
		PaymentDTO pDto = input.getPaymentDTO();
		switch(rquestType){
		case Constants.TCH_REQUEST_SALE:
			pDto.setRequestType(Constants.TCH_REQUEST_SALE);
			pDto.setProcessingCode("000000");
			pDto.setMTI("200");
			break;
		case Constants.TCH_REQUEST_REFUND:
			pDto.setRequestType(Constants.TCH_REQUEST_REFUND);
			pDto.setProcessingCode("200000");
			pDto.setMTI("220");
			break;
			default:
		}
		pDto.setBranchCode(null); 
		pDto.setCurrencyCode(356);
		if(Constants.TCH_QRCAN.equals(input.getDecisionFlag())){
			pDto.setResponseCode("99");
		}else{
			pDto.setResponseCode("00");
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd HHmmss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			pDto.setDate((sdf1.format(sdf.parse(input.getDate() + " "
					+ input.getTime()))));
		} catch (ParseException e) {
			logger.debug("Exception"+e);
		}
		pDto.setMerchantVPA(merchantVPA); // NEWLY ADDED FOR SETTLEMENT 19/07/2017 BY ASHISH
		pDto.setCustomerVPA(customerVPA); // NEWLY ADDED FOR SETTLEMENT 19/07/2017 BY ASHISH
		/*
		 * changes for card label as per requirement from bank
		 * 14th aug 2018 
		 */
		try{
			if(input.getMenuobj() != null){
				switch(input.getMenuobj()[2].getDisplayName()){
				case "UPI-QR":
					pDto.setCardLabel("UPI-QR");
					break;
				case "BHARAT-QR":
					pDto.setCardLabel("BHARAT-QR");
					break;
				default:
					pDto.setCardLabel("QR");
					break;
				}
			}
		}catch(Exception e){
			logger.debug("In exception");
			pDto.setCardLabel("QR");
		}
		input.setCardLabel(pDto.getCardLabel());
		/*
		 * end of changes for card label as per requirement from bank
		 * 14th aug 2018 
		 */
		pDto.setAuthorizationId("-");
		pDto.setTime(input.getTime());
		pDto.setTransactionChannel(input.getQrType());
		saledao.save(pDto);
	}
}
