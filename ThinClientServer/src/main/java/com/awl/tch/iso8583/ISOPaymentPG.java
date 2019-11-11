package com.awl.tch.iso8583;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bouncycastle.math.ec.ECCurve.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.awl.tch.adaptor.brandemi.AbstractBrandEmiAdaptor;
import com.awl.tch.adaptor.brandemi.BrandEmiAdaptorImpl;
import com.awl.tch.bean.DataPrintObject;
import com.awl.tch.bean.Payment;
import com.awl.tch.bean.SuperMenuObject;
import com.awl.tch.bean.TenureObject;
import com.awl.tch.brandemi.util.HelperUtil;
import com.awl.tch.constant.Constants;
import com.awl.tch.constant.Tags;
import com.awl.tch.util.AppConfigMaster;
import com.awl.tch.util.EMIMaster;
import com.awl.tch.util.UtilityHelper;
import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.MessageFactory;
/**
 * <tt>Make and parse ISO packets!</tt>
 * <p>
 * ISO packet for <tt>SALE</tt>, <tt>VOID</tt>, <tt>REFUND</tt>, <tt>REVERSAL</tt>
 * making and parsing.As for each and every packet all the mandatory fields need 
 * to set as per the <tt>ISO 8583</tt> specification.
 * <p>
 * After making of packet in respective format with respective MTI(Message Type Indicator)
 * it will be send to SWITCH (Magnus) and corresponding response from switch 
 * is received with response MTI.
 * <p>
 * Packet creation mainly handle with the help of <tt>config.xml</tt> basis, in which
 * contain various MTI related information.
 * 
 *  
 * @author ashish.bhavsar
 * @version 1.0
 * @since   2016-09-21
 * 
 * @see Config.xml 
 */

public final class ISOPaymentPG{
	
	private static final Logger logger  = LoggerFactory.getLogger(ISOPaymentPG.class);

	
	public static ConcurrentHashMap<String, String> bankName = new ConcurrentHashMap<String,String>();
	public static ConcurrentHashMap<String, String> schema = new ConcurrentHashMap<String,String>();
	
	static{
		bankName.put("HDFC", "HDFC Bank");
		bankName.put("SBI", "SBI GE");
		bankName.put("KOTAK", "Kotak Bank");
		bankName.put("YES", "Yes Bank");
		bankName.put("CBI", "CBI");
		bankName.put("HDBC", "HSBC Bank");
		bankName.put("IBL", "IndusInd Bank");
		bankName.put("PNB", "PNB");
		bankName.put("SCB", "SCB");
		bankName.put("ICICI", "ICICI Bank");
		bankName.put("RBL", "RBL");
		bankName.put("AXIS", "Axis Bank");
		bankName.put("CITI", "Citibank");
		bankName.put("VIGL", "Worldline");
		
		schema.put("V","VISA");
		schema.put("A","AMEX");
		schema.put("R","RUPAY");
		schema.put("D","MAESTRO");
		schema.put("M","MASTERCARD");
	}
	
	
	public static ConcurrentHashMap<String, String> getBankName() {
		return bankName;
	}

	public static void setBankName(ConcurrentHashMap<String, String> bankName) {
		ISOPaymentPG.bankName = bankName;

	}

	private static BigDecimal ONE_HUNDRED = new BigDecimal("100");
		/**Create iso message based on request type.
		 * set MTI and Header globally for ISO message.
		 * @param paymentBean
		 * @param requestType
		 * @return String ISO converted message in string format
		 */
		@SuppressWarnings("unused")
		public static String createISOMessage(Payment paymentBean, String requestType){
			
			IsoMessage iso = getMTI(requestType);
			logger.info("TPDU from table :" + paymentBean.getTpdu());
			logger.info("NII from table :" + paymentBean.getNii());
			logger.debug("TPDU : [" + "49534F383538332D31393837000000010000000500" +"]");
			iso.setIsoHeader(UtilityHelper.unHex("49534F383538332D31393837000000010000000500"));
			iso.setBinary(true);
			iso.setCharacterEncoding("ISO-8859-1");
			
			String terSerNum = paymentBean.getTerminalSerialNumber();
			if(terSerNum.contains("-"))
				terSerNum = terSerNum.replace("-", "") ;
			else
				terSerNum = terSerNum.substring(paymentBean.getTerminalSerialNumber().length()-8);
			
			//String terSerNum = paymentBean.getTerminalSerialNumber().substring(paymentBean.getTerminalSerialNumber().length()-8);
			if(terSerNum.length() < 10)	
				terSerNum =  UtilityHelper.leftPad(terSerNum,10);
			
			paymentBean.setTempSerialNumber(terSerNum);
			
			switch (requestType){
			case Constants.TCH_REQUEST_SALE: 
				iso = createIso200(paymentBean, iso);
				break;
			case Constants.TCH_REQUEST_VOID:
				iso = createIso220(paymentBean, iso);
				break;
			case Constants.TCH_REQUEST_REFUND:
				iso = createIso220refund(paymentBean, iso);
				break;
			case Constants.TCH_REQUEST_REVERSAL:
				iso = createIso400(paymentBean, iso);
				break;
			case Constants.TCH_REQUEST_PREAUTH:
				iso = createIso100(paymentBean, iso);
				break;
			case Constants.TCH_REQUEST_PREAUTHCOMP:
				iso = createIso220PreAuth(paymentBean, iso);
				break;
			case Constants.TCH_REQUEST_SALECSHBK:
				iso = createIso200SaleCashBack(paymentBean, iso);
				break;
			case Constants.TCH_REQUEST_TIPADJUST:
				iso = createIso220tip(paymentBean, iso);
				break;
			case Constants.TCH_BALANCE_ENQ:
				iso = createIso100BalEnq(paymentBean, iso);
				break;
			case Constants.TCH_REQUEST_SALE_OFFLINE:
				iso = createIso220SaleOffline(paymentBean,iso); 
				break;
			default :
				break;
			}
			
			logger.debug("Request to switch | [" +iso.debugString() +"]");
			
			byte[] isoMsg = iso.writeData();
			ByteArrayOutputStream byteOuts = new ByteArrayOutputStream();
			
			try {
				iso.write(byteOuts, 2);
				isoMsg = byteOuts.toByteArray();
			} catch (IOException e) {
				logger.debug("Exception in Sale transaction :",e.getMessage());
			}finally{	
				try {
					byteOuts.close();
				} catch (IOException e) {
				}
			}
			logger.info("Transaction packet :"+UtilityHelper.byteArrayToHexString(isoMsg));
			
			String str ="";
				
			try {
				str = new String(isoMsg,"ISO-8859-1");
			} catch (UnsupportedEncodingException e) {
				logger.error("Exception in sale trancation :",e);
			}
			return str;
		}
		
		/**
		 * Parse iso message based on the request type.
		 * @param responseData get the parsed iso message in string format
		 * @param paymentBean all packet related information
		 * @param requestType type of request like <tt>SALE</tt>,<tt>VOID</tt>,etc.
		 */
		@SuppressWarnings("unused")
		public static void parseIsoMessage(final String responseData,final Payment paymentBean, String requestType){
			
			switch (requestType){
			case Constants.TCH_REQUEST_SALE: 
					parseIso200(responseData, paymentBean);
				break;
			case Constants.TCH_REQUEST_VOID:
					parseIso220(responseData, paymentBean, requestType);
				break;
			case Constants.TCH_REQUEST_REFUND:
					parseIso220(responseData, paymentBean, requestType);
				break;
			case Constants.TCH_REQUEST_REVERSAL:
					parseIso400(responseData, paymentBean);
				break;
			case Constants.TCH_REQUEST_PREAUTH:
				parseIso100(responseData, paymentBean);
				break;
			case Constants.TCH_REQUEST_PREAUTHCOMP:
				parseIso220(responseData, paymentBean, requestType);
				break;
			case Constants.TCH_REQUEST_SALECSHBK:
				parseIso200(responseData, paymentBean);
				break;
			case Constants.TCH_REQUEST_TIPADJUST:
				parseIso220(responseData, paymentBean, requestType);
				break;
			case Constants.TCH_BALANCE_ENQ:
				parseIso100(responseData, paymentBean);
				break;
			case Constants.TCH_REQUEST_SALE_OFFLINE:
				parseIso220(responseData, paymentBean,Constants.TCH_REQUEST_TIPADJUST);
				break;
			default :
			}
				
				//System.out.println("Response data in hex : "  + responseData);				
				
		}
		
		/**parse iso 210 message for sale transaction
		 * @param responseData
		 * @param paymentBean
		 */
		private static void parseIso200(String responseData, Payment paymentBean){
			
			try{
				byte responseByteArray[] = responseData.getBytes("ISO-8859-1");
				String hexDumpResponse = UtilityHelper.byteArrayToHexString(responseByteArray);
				
				logger.info("Sale Response packet :"+hexDumpResponse);
				final MessageFactory<IsoMessage> mfact = UtilityHelper.config("config.xml");
				
				
				
				byte binArray[]  = Arrays.copyOfRange(responseByteArray, 2, responseByteArray.length);
				
				mfact.setUseBinaryMessages(true);
				mfact.setUseBinaryBitmap(true);;
				//mfact.setCharacterEncoding("ISO-8859-1");
				
				IsoMessage m210 = mfact.parseMessage(binArray, 21) ;
				//m210.setBinaryBitmap(true);
				logger.info("ISO RESPONSE FOR 0210");
				logger.info("Field 3 :["+m210.getField(3) +"]");
				logger.info("Field 11 :["+m210.getField(11)+"]");
				logger.info("Field 12 :["+m210.getField(12)+"]");
				logger.info("Field 13 :["+m210.getField(13)+"]");
				logger.info("Field 24 :["+m210.getField(24)+"]");
				logger.info("Field 37 :["+m210.getField(37)+"]");
				logger.info("Field 38 :["+m210.getField(38)+"]");
				logger.info("Field 39 :["+m210.getField(39)+"]");
				logger.info("Field 41 :["+m210.getField(41)+"]");
				logger.info("Field 55 :["+m210.getField(55)+"]");
				logger.info("Field 52 :["+m210.getField(52)+"]");
				logger.info("Field 63 :["+m210.getField(63)+"]");
				
				// setting the responses
				paymentBean.setProcessingCode(m210.getField(3).toString());
				paymentBean.setMTI("200");
				if(m210.getField(39) != null){
					paymentBean.setResponseCode(m210.getField(39).toString());
					logger.debug("Response code: ["+m210.getField(39)+"]" );
				}
				if(m210.getField(37) != null){
					paymentBean.setRetrivalRefNumber(m210.getField(37).toString());
				}
				if(m210.getField(38) != null){
					paymentBean.setAuthorizationId(m210.getField(38).toString());
				}
				if(m210.getField(13) != null){
					//System.out.println("not settting");
					//paymentBean.setDate(m210.getField(13).toString());
				}
				if(m210.getField(12) != null)
					paymentBean.setTime(m210.getField(12).toString());
				if(m210.getField(63) != null){
					logger.debug("Get field 63 : " + m210.getField(63));
					if(Constants.TCH_EMI_SALE.equals(paymentBean.getDecisionFlag()) || Constants.TCH_BRDSALE.equals(paymentBean.getDecisionFlag())){
						/*
						 * 1 char ( Authorization source)E – EHOST M – Mastercard D – MDS V - VISA A – Amex S – Ames Stand-in (0,1)
						 * 15 characters – Transaction ID (1,16)
						 * 1 char ‘C’ (Card type) –  (16,17)
						 * 1 char Value 0 - Domestic card 1 – International card. (This part will be conditional and needs to be handled accordingly @Mosambee end, If it comes it needs to be ignored) (17,18)
						 * ‘F’ + 12 Digit EMI Amount (19,31)
						 * 12 Digit Total amount with interest (31,43)
						 * 12 Digit Effective Cost to the Customer Amount (43,55)
						 * 15 Digit EMI Txn ID (55,70)
						 * 4 Digit ROI % with two decimal digits (70-74)
						 * 2 Digit Tenure value (75-76)
						 * 4 Digit Cash Back % with two decimal digits (77-80)
						 * 12 Digit Cash back amount (81-92)
						 * 4 Digit Processing fees (93-96)
						 * 5 Digit Disclaimer Code 1 (issuer) (97-101)
						 * 5 Digit Disclaimer Code2 (Product) (102-106)
						 * 20 bytes Manufacturer Name (107-126)
						 * 40 bytes Product Description. (127-166)
						 */
						//paymentBean.setSchemaTransactionId(m210.getField(63).toString().substring(6));
						paymentBean.setField63(m210.getField(63).toString());
						getInfoFromTLV(paymentBean);
						setEMIValues(paymentBean);
						logger.debug("data print object :" + paymentBean.getDataPrintObject());
						// set null as value does not needed at the end
					}else{
						paymentBean.setField63(m210.getField(63).toString());
						getInfoFromTLV(paymentBean);
						//paymentBean.setSchemaTransactionId(m210.getField(63).toString().substring(6));
					}
				}
				if(m210.getField(55) != null){
					String field55String = m210.getField(55).toString();
					logger.debug("Get field 55 : " + field55String);
					//paymentBean.setField55(field55String);
					paymentBean.setIssuerField55(field55String);
				}
				logger.debug("Response from switch | [" +m210.debugString() +"]");
			} catch (IOException e) {
				logger.debug("Exception in parsing ISO :"+e.getMessage());
			} catch (ParseException e) {
				logger.debug("Exception in parsing ISO :"+e.getMessage());
			}
		}
		
		private static void parseIso100(String responseData, Payment paymentBean){
			
			try{
				byte responseByteArray[] = responseData.getBytes("ISO-8859-1");
				String hexDumpResponse = UtilityHelper.byteArrayToHexString(responseByteArray);
				
				logger.info("Sale Response packet :"+hexDumpResponse);
				final MessageFactory<IsoMessage> mfact = UtilityHelper.config("config.xml");
				
				byte binArray[]  = Arrays.copyOfRange(responseByteArray, 2, responseByteArray.length);
				
				mfact.setUseBinaryMessages(true);
				mfact.setUseBinaryBitmap(true);;
				//mfact.setCharacterEncoding("ISO-8859-1");
				
				IsoMessage m110 = mfact.parseMessage(binArray, 21) ;
				//m110.setBinaryBitmap(true);
				logger.info("ISO RESPONSE FOR 0110");
				logger.info("Field 3 :["+m110.getField(3) +"]");
				logger.info("Field 4 :["+m110.getField(4) +"]");
				logger.info("Field 11 :["+m110.getField(11)+"]");
				logger.info("Field 12 :["+m110.getField(12)+"]");
				logger.info("Field 13 :["+m110.getField(13)+"]");
				logger.info("Field 24 :["+m110.getField(24)+"]");
				logger.info("Field 37 :["+m110.getField(37)+"]");
				logger.info("Field 38 :["+m110.getField(38)+"]");
				logger.info("Field 39 :["+m110.getField(39)+"]");
				logger.info("Field 41 :["+m110.getField(41)+"]");
				logger.info("Field 55 :["+m110.getField(55)+"]");
				logger.info("Field 52 :["+m110.getField(52)+"]");
				logger.info("Field 63 :["+m110.getField(63)+"]");
				
				// setting the responses
				paymentBean.setProcessingCode(m110.getField(3).toString());
				paymentBean.setMTI("100");
				if(m110.getField(4) != null){
					paymentBean.setOriginalAmount(Integer.valueOf(m110.getField(4).toString()).toString());
				}
				if(m110.getField(39) != null){
					paymentBean.setResponseCode(m110.getField(39).toString());
					logger.debug("Response code: ["+m110.getField(39)+"]" );
				}
				if(m110.getField(37) != null){
					paymentBean.setRetrivalRefNumber(m110.getField(37).toString());
				}
				if(m110.getField(38) != null){
					paymentBean.setAuthorizationId(m110.getField(38).toString());
				}
				if(m110.getField(13) != null){
					//paymentBean.setDate(m110.getField(13).toString());
				}
				if(m110.getField(12) != null)
					paymentBean.setTime(m110.getField(12).toString());
				if(m110.getField(63) != null){
					/*paymentBean.setField63(m110.getField(63).toString());
					getInfoFromTLV(paymentBean);*/
					logger.debug("Get field 63 : " + m110.getField(63));
					paymentBean.setSchemaTransactionId(m110.getField(63).toString().substring(6));
				}
				if(m110.getField(55) != null){
					String field55String = m110.getField(55).toString();
					logger.debug("Get field 55 : " + field55String);
					//paymentBean.setField55(field55String);
					paymentBean.setIssuerField55(field55String);
				}
				
			} catch (IOException e) {
				e.printStackTrace();
				logger.debug("Exception in parsing ISO :"+e.getMessage());
			} catch (ParseException e) {
				e.printStackTrace();
				logger.debug("Exception in parsing ISO :"+e.getMessage());
			}
		}
		
		/**parse iso message with MTI 0x410
		 * @param responseData
		 * @param paymentBean
		 */
		private static void parseIso400(String responseData, Payment paymentBean){
			
			try{
				byte responseByteArray[] = responseData.getBytes("ISO-8859-1");
				String hexDumpResponse = UtilityHelper.byteArrayToHexString(responseByteArray);
				
				logger.info("Reversal Response packet :"+hexDumpResponse);
				final MessageFactory<IsoMessage> mfact = UtilityHelper.config("config.xml");
				
				
				
				byte binArray[]  = Arrays.copyOfRange(responseByteArray, 2, responseByteArray.length);
				
				mfact.setUseBinaryMessages(true);
				mfact.setUseBinaryBitmap(true);;
				//mfact.setCharacterEncoding("ISO-8859-1");
				
				IsoMessage m410 = mfact.parseMessage(binArray, 21) ;
				//m210.setBinaryBitmap(true);
				logger.info("ISO RESPONSE FOR 0410");
				logger.info("Field 3 :["+m410.getField(3) +"]");
				logger.info("Field 11 :["+m410.getField(11)+"]");
				logger.info("Field 12 :["+m410.getField(12)+"]");
				logger.info("Field 13 :["+m410.getField(13)+"]");
				logger.info("Field 24 :["+m410.getField(24)+"]");
				logger.info("Field 37 :["+m410.getField(37)+"]");
				logger.info("Field 38 :["+m410.getField(38)+"]");
				logger.info("Field 39 :["+m410.getField(39)+"]");
				logger.info("Field 41 :["+m410.getField(41)+"]");
				logger.info("Field 55 :["+m410.getField(55)+"]");
				logger.info("Field 63 :["+m410.getField(63)+"]");
				
				// setting the responses
				paymentBean.setProcessingCode(m410.getField(3).toString());
				paymentBean.setMTI("400");
				if(m410.getField(39) != null){
					paymentBean.setResponseCode(m410.getField(39).toString());
					logger.debug("Response code: ["+m410.getField(39)+"]" );
				}
				if(m410.getField(37) != null){
					paymentBean.setRetrivalRefNumber(m410.getField(37).toString());
				}
				if(m410.getField(38) != null){
					paymentBean.setAuthorizationId(m410.getField(38).toString());
				}
				if(m410.getField(13) != null)
					paymentBean.setDate(m410.getField(13).toString());
				if(m410.getField(12) != null)
					paymentBean.setTime(m410.getField(12).toString());
				if(m410.getField(63) != null){
					logger.debug("Get field 63 : " + m410.getField(63));
					/*paymentBean.setField63(m410.getField(63).toString());
					getInfoFromTLV(paymentBean);*/
					paymentBean.setSchemaTransactionId(m410.getField(63).toString());
				}
				if(m410.getField(55) != null){
					logger.debug("Get field 55 : " + m410.getField(55));
					paymentBean.setField55(m410.getField(55).toString());
				}
				
			} catch (IOException e) {
				logger.debug("Exception in parsing ISO :"+e.getMessage(),e);
			} catch (ParseException e) {
				logger.debug("Exception in parsing ISO :"+e.getMessage(),e);
			}
		}
		
		
		/**
		 * parse iso message with MTI 0x230
		 * @param responseData
		 * @param paymentBean
		 */
		private static void parseIso220(final String responseData, final Payment paymentBean, String reqtype){

			
			try{
				byte responseByteArray[] = responseData.getBytes("ISO-8859-1");
				String hexDumpResponse = UtilityHelper.byteArrayToHexString(responseByteArray);
				
				logger.info("Response packet :"+hexDumpResponse);
				final MessageFactory<IsoMessage> mfact = UtilityHelper.config("config.xml");
				
				byte binArray[]  = Arrays.copyOfRange(responseByteArray, 2, responseByteArray.length);
				
				mfact.setUseBinaryMessages(true);
				mfact.setUseBinaryBitmap(true);;
				//mfact.setCharacterEncoding("ISO-8859-1");
				
				IsoMessage m230 = mfact.parseMessage(binArray, 21) ;
				
				//m210.setBinaryBitmap(true);
				logger.info("ISO RESPONSE FOR 0230");
				logger.info("Field 3 :["+m230.getField(3) +"]");
				logger.info("Field 11 :["+m230.getField(11)+"]");
				logger.info("Field 24 :["+m230.getField(24)+"]");
				logger.info("Field 37 :["+m230.getField(37)+"]");
				logger.info("Field 38 :["+m230.getField(38)+"]");
				logger.info("Field 39 :["+m230.getField(39)+"]");
				logger.info("Field 41 :["+m230.getField(41)+"]");
				logger.info("Field 63 :["+m230.getField(63)+"]");
				logger.info("Field 55 :["+m230.getField(55)+"]");
				// setting the responses
				
				/*
				 * Changes for make magnus batch file
				 */
				if(Constants.TCH_REQUEST_TIPADJUST.equals(reqtype)){
					paymentBean.setMTI("200");
					paymentBean.setProcessingCode("000000");
				}else{
					paymentBean.setMTI("220");
					paymentBean.setProcessingCode(m230.getField(3).toString());
				}
				/*
				 * End of Changes for make magnus batch file
				 */
				if(m230.getField(39) != null){
					paymentBean.setResponseCode(m230.getField(39).toString());
					logger.debug("Response code: ["+m230.getField(39)+"]" );
				}
				if(m230.getField(37) != null){
					paymentBean.setRetrivalRefNumber(m230.getField(37).toString());
				}
				if(m230.getField(38) != null){
					paymentBean.setAuthorizationId(m230.getField(38).toString());
				}
				if(m230.getField(63) != null){
					/*logger.debug("Extra object value");
					ExtraObject[] e = new ExtraObject[1];
					ExtraObject e1 = new ExtraObject();
					e1.setExtraDataName("SE number");
					e1.setExtraDataValue(m230.getField(63).toString());
					e[0] = e1;
					paymentBean.setExtraDataObject(e);*/
					
					paymentBean.setField63(m230.getField(63).toString());
					//paymentBean.setSchemaTransactionId(m230.getField(63).toString());
				}
				if(m230.getField(55) != null){
					paymentBean.setIssuerField55(m230.getField(55).toString());
				}
				
			} catch (IOException e) {
				logger.debug("Exception in parsing ISO :"+e.getMessage());
			} catch (ParseException e) {
				logger.debug("Exception in parsing ISO :"+e.getMessage());
			}
		}
		
		
		
		/**
		 * set MTI based on the request type.
		 * @param reqType
		 * @return
		 */
		private static IsoMessage getMTI(String reqType){
			MessageFactory<IsoMessage> msgFact = new MessageFactory<IsoMessage>();
			if(Constants.TCH_REQUEST_SALE.equalsIgnoreCase(reqType))
				return msgFact.newMessage(0x200);
			else if(Constants.TCH_REQUEST_VOID.equalsIgnoreCase(reqType))
				return msgFact.newMessage(0x220);
			else if(Constants.TCH_REQUEST_REFUND.equalsIgnoreCase(reqType))
				return msgFact.newMessage(0x220);
			else if(Constants.TCH_REQUEST_REVERSAL.equalsIgnoreCase(reqType))
				return msgFact.newMessage(0x400);
			else if(Constants.TCH_REQUEST_SALECSHBK.equalsIgnoreCase(reqType))
				return msgFact.newMessage(0x200);
			else if(Constants.TCH_REQUEST_PREAUTHCOMP.equalsIgnoreCase(reqType))
				return msgFact.newMessage(0x220);
			else if(Constants.TCH_REQUEST_TIPADJUST.equalsIgnoreCase(reqType))
				return msgFact.newMessage(0x220);
			else if(Constants.TCH_REQUEST_SALE_OFFLINE.equalsIgnoreCase(reqType))
				return msgFact.newMessage(0x220);
			else 
				return msgFact.newMessage(0x100);
		}
		
		/**create ISO message based on MTI 0x200
		 * @param paymentBean
		 * @param iso
		 * @return
		 */
		private static IsoMessage createIso200(final Payment paymentBean, final IsoMessage iso){
			
			logger.info("ISO REQUEST FOR 0200");
			logger.info("Field 2 :["+paymentBean.getPanNumber() +"]");
			logger.info("Field 3 :["+paymentBean.getAccountType() +"]");
			logger.info("Field 4 :["+paymentBean.getOriginalAmount()+"]");
			logger.info("Field 11 :["+paymentBean.getStanNumber()+"]");
			logger.info("Field 14 :["+paymentBean.getExpiryDate()+"]");
			logger.info("Field 22 :[0"+ paymentBean.getCardEntryMode()+"]");
			logger.info("Field 24 :["+paymentBean.getNii().trim()+"]");
			logger.info("Field 25 :[00]");
			logger.info("Field 35 :["+paymentBean.getTrack2()+"]");
			logger.info("Field 41 :["+paymentBean.getTerminalId()+"]");
			logger.info("Field 42 :["+paymentBean.getMerchantId()+"]");
			if(paymentBean.getField55() != null)
			logger.info("Field 55 :["+paymentBean.getField55()+"]");
			if(paymentBean.getPinBlock() != null)
			logger.info("Field 52 :["+paymentBean.getPinBlock()+"]");
			String field60 = getField60(paymentBean);
			String field62 = getField62(paymentBean);
			String field63iso = getField63(paymentBean);
			
			logger.info("Field 60 :["+field60+"]");
			logger.info("Field 62 :["+field62+"]");
			logger.info("Field 63 :["+field63iso+"]");
			
			iso.setValue(3, paymentBean.getAccountType() , IsoType.NUMERIC, 6);
			iso.setValue(4, paymentBean.getOriginalAmount(), IsoType.NUMERIC, 12);
			iso.setValue(11, Integer.valueOf(paymentBean.getStanNumber()), IsoType.NUMERIC, 6); // stan number
			iso.setValue(22, Integer.valueOf(paymentBean.getCardEntryMode()), IsoType.NUMERIC, 3); 
			iso.setValue(24, paymentBean.getNii().trim() , IsoType.NUMERIC, 3); // NII
			iso.setValue(25, 00 , IsoType.NUMERIC, 2);// pos condition code
			if(paymentBean.getTrack2() != null)
				iso.setValue(35,paymentBean.getTrack2(), new BcdEncodedField35(), IsoType.LLBIN, 37); // track 2 data
			else{
				iso.setValue(2, paymentBean.getPanNumber() , new BCDencodeField60(), IsoType.LLBIN, 19); // pan number
				iso.setValue(14, paymentBean.getExpiryDate(), IsoType.NUMERIC, 4); // expiry date in YYMM
			}
			if(paymentBean.getPinBlock() != null){
				iso.setValue(52, paymentBean.getPinBlock(), IsoType.BINARY, 8);
			}	
			iso.setValue(41, paymentBean.getTerminalId(), IsoType.ALPHA, 8); // terminal id		
			iso.setValue(42, paymentBean.getMerchantId(), IsoType.ALPHA, 15); // acquiring id or merchant id
			if(paymentBean.getField55() != null && !paymentBean.getField55().isEmpty()){
				iso.setValue(55, paymentBean.getField55(),new BCDEncodedField55() , IsoType.LLLBIN, 1);
			}
			iso.setValue(60, field60 ,new BCDencodeField60() , IsoType.LLLVAR, 1); // AuthorizationSource Code
			iso.setValue(62, field62,new BCDencodeField60(), IsoType.LLLVAR, 6); //  invoice number new
			
			iso.setValue(63, field63iso,new BCDencodeField60(), IsoType.LLLVAR, 6); //  currency Code
			
			
			/*
			 * make field 63 for emi sale
			 */
			/*
			 * F. 6 Digit Program Code left padded with zeros + 
			 * 15 digit Product Code alpha numeric right padded with spaces +
			 * 2 Digit user input Tenure left padded with zeros +
			 * 15 digit IMEI/serial number.
			 * 
			 */
			/*
			 * public String brandEmiManufacture ="05805";
				public String brandEmivarient ="05915";
				public String brandEmiMobileNumber ="06010";
				public String brandEmiserialNumber ="06115";
				public String brandEmiskuCode ="06220";
				public String brandEmiVelocity ="06301";
				public String brandEmiVelocitydays ="06403";
				public String brandEmiVelocityDWMY ="06501";
			 */
			if(Constants.TCH_EMI_SALE.equals(paymentBean.getDecisionFlag()) || Constants.TCH_BRDSALE.equals(paymentBean.getDecisionFlag())){
				SuperMenuObject superObj = AbstractBrandEmiAdaptor.brandEmiFinalCache.get(paymentBean.getTerminalSerialNumber());
				Map<String,TenureObject> tenureMap = superObj.getTenureMap();
				iso.setValue(2, paymentBean.getPanNumber() , new BCDencodeField60(), IsoType.LLBIN, 19); // pan number
				iso.setValue(14, paymentBean.getExpiryDate(), IsoType.NUMERIC, 4); // expiry date   in YYMM
				String tenure = "0";
				if(paymentBean.getEmiObject().getTenureObject() != null){
					logger.debug("tenure obejct :" + paymentBean.getEmiObject().getTenureObject());
					if(paymentBean.getEmiObject().getTenureObject()[0] != null){
						tenure= paymentBean.getEmiObject().getTenureObject()[0].getTenure();
					}
				}
				logger.debug("tenure value after setting :" + tenure);
				StringBuilder field63 = new StringBuilder("");
				field63.append(Tags.hardwareserialnumberTagLength
						+ String.format("%015d", Integer.parseInt(paymentBean.getTempSerialNumber()))+Tags.currencyCodeTagLength + paymentBean.getCurrencyCode());
				
				if(superObj.getManifactureCode() != null){
					logger.debug("Manufacturer code :" + superObj.getManifactureCode());
					field63.append(Tags.brandEmiManufacture + String.format("%1$-5s",superObj.getManifactureCode()));
				}
				
				if(superObj.getMenuObject() != null){
					field63.append(Tags.brandEmischeme + String.format("%02d", BrandEmiAdaptorImpl.getSchemeMappingInInt(superObj.getMenuObject()[2].getDisplayName())));
					field63.append(Tags.brandEmiproductCategory 
							+ String.format("%02d", BrandEmiAdaptorImpl.getProducCategoryMappingInInt(superObj.getMenuObject()[0].getDisplayName())));
				}
				if(superObj.getValidationParameter() != null){
					logger.debug("validation parameter : "+superObj.getValidationParameter().toString());
					if(superObj.getValidationParameter().getVelocityType() != null){
						field63.append(Tags.brandEmiVelocityDWMY + superObj.getValidationParameter().getVelocityType());
					}
					if(superObj.getValidationParameter().getVelocityCount() != null){
						field63.append(Tags.brandEmiVelocity+ String.format("%02d",Integer.parseInt(superObj.getValidationParameter().getVelocityCount())));
					}
					if(superObj.getValidationParameter().getNumberOfDays() != null){
						field63.append(Tags.brandEmiVelocitydays+ String.format("%03d",Integer.parseInt(superObj.getValidationParameter().getNumberOfDays())));
					}
				}
				
				//emiPrefixTagLength is F for brandemi and E for bank emi
				if(Constants.TCH_BRDSALE.equals(paymentBean.getDecisionFlag()))
				{
					logger.debug("emiPrefixTagLength :F");	
					field63.append(Tags.emiPrefixTagLength + "F");
					logger.debug("discount flag value : " + superObj.getDiscountFlag());
					field63.append(Tags.emidiscflagtypeTagLength + superObj.getDiscountFlag());
					paymentBean.setEmiIndicator("F");
				}
				if(Constants.TCH_EMI_SALE.equals(paymentBean.getDecisionFlag()))
				{
					logger.debug("emiPrefixTagLength : E");	
					field63.append(Tags.emiPrefixTagLength + "E");
					paymentBean.setEmiIndicator("E");
					if(paymentBean.getEmiObject().getTenureObject()[0].getCashBackFlag()!= null)
					field63.append(Tags.emidiscflagtypeTagLength + paymentBean.getEmiObject().getTenureObject()[0].getCashBackFlag()); // FIXED or Percentage
					else field63.append(Tags.emidiscflagtypeTagLength + "F"); // FIXED or Percentage
				}
				if(paymentBean.getEmiObject().getProgramObject() != null)
					field63.append(Tags.emiProgramCodeTagLength + String.format("%1$-6s",paymentBean.getEmiObject().getProgramObject()[0].getProgramCode()));
				
				if(superObj.getProductDetails() != null){
					logger.debug("product code :" + superObj.getProductDetails()[0]);
					if(superObj.getProductDetails()[0] != null){
						field63.append(Tags.emiProductCodeTagLength + String.format("%1$-15s", superObj.getProductDetails()[0]));
					}else{
						field63.append(Tags.emiProductCodeTagLength + String.format("%1$-15s", "000000"));
					}
				}else{
				if(paymentBean.getEmiObject().getPrductCode() != null){
						logger.debug("product code  : " + paymentBean.getEmiObject().getPrductCode());
						field63.append(Tags.emiProductCodeTagLength + String.format("%1$-15s", paymentBean.getEmiObject().getPrductCode()));
					}else{
						logger.debug("product code :00000" );
						field63.append(Tags.emiProductCodeTagLength + String.format("%1$-15s", "000000"));
					}
				}
				//}
				field63.append(Tags.emiTenureTagLength + String.format("%02d", Integer.parseInt(tenure)));
				
				String emiAmount = paymentBean.getEmiObject().getTenureObject()[0].getEmiAmount();
					if(emiAmount != null && emiAmount.contains(".")){
						emiAmount = new BigDecimal(emiAmount).movePointRight(2).toPlainString();
					}
				logger.debug("EMI amount :" + emiAmount);
				field63.append(Tags.emiAmountTagLength + String.format("%012d", Integer.parseInt(emiAmount))); //EMI amount
				
				logger.debug("Emi rate of interest : " + superObj.getTenureMap().get(tenure).getRoi());
				
				if(superObj.getTenureMap().get(tenure).getRoi() != null){
					String roi = pointConverter(superObj.getTenureMap().get(tenure).getRoi());
					if(roi.length() == 4){
						field63.append(Tags.emiROITagLength +roi);
					}else{
						field63.append(Tags.emiROITagLength + String.format("%1$4s", roi).replace(' ', '0'));	
					}
				}
				
				/*if(paymentBean.getEmiObject().getTenureObject()[0].getCashbackPercent() != null)
					field63.append(Tags.emiPercentageTagLength + "0000".substring(paymentBean.getEmiObject().getTenureObject()[0].getCashbackPercent().length()) + paymentBean.getEmiObject().getTenureObject()[0].getCashbackPercent()); // discount percentage
				else
					field63.append(Tags.emiPercentageTagLength + "0000"); // discount percentage
*/				 
				logger.debug("cash back %  :" + tenureMap.get(tenure).getCashBackFinalPer()); 
				
				if(tenureMap.get(tenure).getCashBackFinalPer() != null){
					String cashback = pointConverter(tenureMap.get(tenure).getCashBackFinalPer());
					logger.debug("Cash back after conversion : "+ cashback);
					if(cashback.length() == 4)
						field63.append(Tags.emiPercentageTagLength + cashback); // discount percentage
					else if(cashback.length() < 4){
						field63.append(Tags.emiPercentageTagLength + String.format("%1$4s", cashback).replace(' ', '0')); // discount percentage
					}
				else
					field63.append(Tags.emiPercentageTagLength + "0000");
				}
				//if("P".equals(paymentBean.getEmiObject().getTenureObject()[0].getCashBackFlag())){
				logger.debug("Cash back flag :" + tenureMap.get(tenure).getCashBackFlag());
				
				logger.info("discount amount :  percetange" + "["+ tenureMap.get(tenure).getCashBackFinalAmt() +"] :  [" +tenureMap.get(tenure).getCashBackFinalPer()+"]" );
				if("F".equals(tenureMap.get(tenure).getCashBackFlag())){
					logger.info("discount amount :  percetange" + "["+ tenureMap.get(tenure).getCashBackFinalAmt() +"] :  [" +tenureMap.get(tenure).getCashBackFinalPer()+"]" );
					field63.append(Tags.emiFixedAmountTagLength + String.format("%012d", Integer.parseInt(tenureMap.get(tenure).getCashBackFinalAmt())));
					field63.append(Tags.emiDiscAmountTagLength + String.format("%012d",0));
				}else{
					if(tenureMap.get(tenure).getCashBackFinalAmt() != null){
						String cashback = pointConverter(tenureMap.get(tenure).getCashBackFinalAmt());
						logger.debug("Cash back after conversion : "+ cashback);
						if(cashback.length() == 12)
							field63.append(Tags.emiDiscAmountTagLength + cashback); // discount percentage
						else if(cashback.length() < 12){
							field63.append(Tags.emiDiscAmountTagLength + String.format("%1$12s", cashback).replace(' ', '0')); // discount percentage
						}
						else
							field63.append(Tags.emiDiscAmountTagLength + "000000000000");
						field63.append(Tags.emiFixedAmountTagLength + String.format("%012d",0));
					}
					
					
					/*if(tenureMap.get(tenure).getCashBackFinalAmt() != null){
						if(tenureMap.get(tenure).getCashBackFinalAmt().contains(".")){
							field63.append(Tags.emiFixedAmountTagLength + 
									String.format("%012d", new BigDecimal(tenureMap.get(tenure).getCashBackFinalAmt()).movePointRight(2).toBigInteger()));
						}else{
							field63.append(Tags.emiFixedAmountTagLength + 
									String.format("%012d", new BigDecimal(tenureMap.get(tenure).getCashBackFinalAmt()).toBigInteger()));
						}
					}
					else
						field63.append(Tags.emiFixedAmountTagLength + String.format("%012d",0));*/
				}
				//logger.debug("cap :" + paymentBean.getEmiObject().getTenureObject()[0].getCapAmount());
				logger.debug("cap :" + tenureMap.get(tenure).getCapAmount());
				if(tenureMap.get(tenure).getCapAmount() != null)
					if(tenureMap.get(tenure).getCashBackFinalAmt().contains(".")){
						field63.append(Tags.emiCapAmountTagLength + String.format("%012d", new BigDecimal(tenureMap.get(tenure).getCapAmount()).movePointRight(2).toBigInteger()));
					}else
						field63.append(Tags.emiCapAmountTagLength + String.format("%012d", new BigDecimal(tenureMap.get(tenure).getCapAmount()).toBigInteger()));
				else
					field63.append(Tags.emiCapAmountTagLength + String.format("%012d",0));
				
				
				
				
				String procfee = pointConverter(paymentBean.getEmiObject().getTenureObject()[0].getPerFeePercentage());
				logger.debug("Procesing fee % : "+ procfee);
				if(procfee.length() == 4)
					field63.append(Tags.emiprocfeepercentageTagLength + procfee); // discount percentage
				else if(procfee.length() < 4){
					field63.append(Tags.emiprocfeepercentageTagLength + String.format("%1$4s", procfee).replace(' ', '0')); // discount percentage
				}
				else
					field63.append(Tags.emiprocfeepercentageTagLength + "0000");
				
				
				
				 /*emiAmount = paymentBean.getEmiObject().getTenureObject()[0].getPerFeePercentage();
				if(emiAmount != null && emiAmount.contains(".")){
					emiAmount = new BigDecimal(emiAmount).movePointRight(2).toPlainString();
				}
				 field63.append(Tags.emiprocfeepercentageTagLength + "0000".substring(emiAmount.length()) + emiAmount);
				emiAmount = paymentBean.getEmiObject().getTenureObject()[0].getEmiTotalAmount();
				if(emiAmount != null && emiAmount.contains(".")){
					emiAmount = new BigDecimal(emiAmount).movePointRight(2).toPlainString();
				}*/
				
				
				logger.debug("EMI total amount : " + emiAmount);
				if(emiAmount != null)
				field63.append(Tags.emitotalamountTagLength + String.format("%012d", Integer.parseInt(emiAmount)))
				.append(Tags.emieffcostcustomerTagLength +  String.format("%012d", Integer.parseInt(emiAmount)));
				paymentBean.getEmiObject().getTenureObject()[0].setEffectiveCostToCustomer(emiAmount);
				logger.debug("Field 63 :[" + field63.toString() +"]");
				iso.setValue(63, field63.toString(),new BCDencodeField60(), IsoType.LLLVAR, 6); //  field 63 as per specification
			}else if(Constants.TCH_DCC_SALE.equals(paymentBean.getIsoDCCFlag())){
				
				iso.setValue(2, paymentBean.getPanNumber() , new BCDencodeField60(), IsoType.LLBIN, 19); // pan number
				iso.setValue(14, paymentBean.getExpiryDate(), IsoType.NUMERIC, 4); // expiry date in YYMM
				iso.setValue(4, paymentBean.getDccAmount(), IsoType.NUMERIC, 12);
				StringBuilder field63 = new StringBuilder("");
				field63.append(field63iso);
				if(Constants.TCH_PROMT_N_PROC.equals(paymentBean.getDecisionFlag())){
					field63.append(Tags.optoutVoidTransactionTagLength + "01"); // opt out void transaction
				}
				logger.debug("Field 63 :[" + field63.toString()+"]");
				iso.setValue(63,field63.toString(), IsoType.LLLVAR, 6); //  field 63 as per specification
				
			}
			
			
			/*
			 * end of make field 63
			 */
			
			//iso.setValue(62, paymentBean.getInvoiceNumber() , IsoType.LLBIN, 6); //  invoice number
			return iso;
		}
		
		private static IsoMessage createIso220tip(final Payment paymentBean, final IsoMessage iso){
			
			logger.info("ISO REQUEST FOR 0220 TIP");
			logger.info("Field 2 :["+ paymentBean.getPanNumber() +"]"); //  pan number
			logger.info("Field 3 :["+"02"+paymentBean.getAccountType()+"]"); // processing code
			logger.info("Field 4 :["+paymentBean.getOriginalAmount() +"]"); // amount 
			logger.info("Field 11 :["+paymentBean.getStanNumber()+"]"); // stan number
			logger.info("Field 12 :["+paymentBean.getTime()+"]"); // time
			logger.info("Field 13 :["+UtilityHelper.dateInMMddYYYY().substring(0, 4)+"]"); // date
			logger.info("Field 14 :["+paymentBean.getExpiryDate()+"]"); // expiry date
			logger.info("Field 22 :["+paymentBean.getCardEntryMode()+"]"); // pos entry mode
			logger.info("Field 24 :["+112+"]"); //nii
			logger.info("Field 25 :[00]");// pos condition code
			logger.info("Field 37 :["+paymentBean.getRetrivalRefNumber()+"]"); // rrn number
			logger.info("Field 38 :["+paymentBean.getAuthorizationId()+"]"); // auth id
			logger.info("Field 39 :["+paymentBean.getResponseCode()+"]"); // Response code
			logger.info("Field 41 :["+paymentBean.getTerminalId()+"]");
			logger.info("Field 42 :["+paymentBean.getMerchantId()+"]");
			logger.info("Field 54 :["+paymentBean.getAdditionalAmount()+"]");
			logger.info("Field 55 :["+paymentBean.getField55()+"]");
			
			String field60 = getField60(paymentBean,"");
			String field62 = getField62(paymentBean);
			String field63 = getField63(paymentBean);
			
			logger.info("Field 60 :["+field60+"]");
			logger.info("Field 62 :["+field62+"]");
			logger.info("Field 63 :["+field63+"]");
			
			iso.setValue(2, paymentBean.getPanNumber() , new BCDencodeField60(), IsoType.LLBIN, 19); // pan number
			iso.setValue(3, "02"+paymentBean.getAccountType() , IsoType.NUMERIC, 6);
			iso.setValue(4, paymentBean.getOriginalAmount() , IsoType.NUMERIC, 12); 
			iso.setValue(11, paymentBean.getStanNumber(), IsoType.NUMERIC, 6); // stan number
			iso.setValue(12, paymentBean.getTime(), IsoType.NUMERIC, 6); // time
			iso.setValue(13, UtilityHelper.dateInMMddYYYY().substring(0, 4), IsoType.NUMERIC, 4); // date
			iso.setValue(14, paymentBean.getExpiryDate(), IsoType.NUMERIC, 4); // date expiry
			iso.setValue(22, Integer.valueOf(paymentBean.getCardEntryMode()), IsoType.NUMERIC, 3); // pos entry mode
			iso.setValue(24, 112 , IsoType.NUMERIC, 3); // NII
			iso.setValue(25, 00 , IsoType.NUMERIC, 2);// pos condition code
			iso.setValue(14, paymentBean.getExpiryDate(), IsoType.NUMERIC, 4); // expiry date in YYMM
			iso.setValue(37, paymentBean.getRetrivalRefNumber() , IsoType.ALPHA, 12);// RRN
			iso.setValue(38, paymentBean.getAuthorizationId() , IsoType.ALPHA, 6);// Auth Code
			iso.setValue(39, paymentBean.getResponseCode() , IsoType.ALPHA, 2);// Response code
			iso.setValue(41, paymentBean.getTerminalId(), IsoType.ALPHA, 8); // terminal id
			iso.setValue(42, paymentBean.getMerchantId(), IsoType.ALPHA, 15); // acquiring id or merchant id
			iso.setValue(54, String.format("%012d", Integer.parseInt(paymentBean.getAdditionalAmount())), new BCDencodeField60(), IsoType.LLLVAR, 6);
			if(paymentBean.getField55() != null && !paymentBean.getField55().isEmpty()){
				iso.setValue(55, paymentBean.getField55(),new BCDEncodedField55() , IsoType.LLLBIN, 1);
			}
			if(paymentBean.getAdditionalAmount() != null){
				iso.setValue(60,field60, new BCDencodeField60(), IsoType.LLLVAR, 1); 	
			}
			iso.setValue(62,field62, new BCDencodeField60(), IsoType.LLLVAR, 6); //  invoice number
			iso.setValue(63,field63,new BCDencodeField60(), IsoType.LLLVAR, 6); //  currency Code
			return iso;
		}
		private static IsoMessage createIso200SaleCashBack(final Payment paymentBean, final IsoMessage iso){
			
			logger.info("ISO REQUEST FOR 0200 ");
			logger.info("Field 2 :["+ paymentBean.getPanNumber() +"]"); //  pan number
			logger.info("Field 3 :["+ "09" +paymentBean.getAccountType() +"]");
			logger.info("Field 4 :["+paymentBean.getOriginalAmount()+"]");
			logger.info("Field 11 :["+paymentBean.getStanNumber()+"]");
			logger.info("Field 14 :["+paymentBean.getExpiryDate()+"]");
			logger.info("Field 22 :["+ paymentBean.getCardEntryMode()+"]");
			logger.info("Field 24 :["+paymentBean.getNii().trim()+"]");
			logger.info("Field 25 :[00]");
			logger.info("Field 35 :["+paymentBean.getTrack2()+"]");
			logger.info("Field 41 :["+paymentBean.getTerminalId()+"]");
			logger.info("Field 42 :["+paymentBean.getMerchantId()+"]");
			logger.info("Field 52 :["+paymentBean.getPinBlock()+"]");
			logger.info("Field 54 :["+paymentBean.getAdditionalAmount()+"]");
			
			String field55 = String.format("%012d", Integer.parseInt(paymentBean.getAdditionalAmount()));
			String field60 = getField60(paymentBean);
			String field62 = getField62(paymentBean);
			String field63 = getField63(paymentBean);
			
			logger.info("Field 55 :["+field55+"]");
			logger.info("Field 60 :["+field60+"]");
			logger.info("Field 62 :["+field62+"]");
			logger.info("Field 63 :["+field63+"]");
			
			iso.setValue(3, "09"+ paymentBean.getAccountType() , IsoType.NUMERIC, 6);
			iso.setValue(4, paymentBean.getOriginalAmount(), IsoType.NUMERIC, 12);
			iso.setValue(11, Integer.valueOf(paymentBean.getStanNumber()), IsoType.NUMERIC, 6); // stan number
			iso.setValue(22, Integer.valueOf(paymentBean.getCardEntryMode()), IsoType.NUMERIC, 3); 
			iso.setValue(24, paymentBean.getNii().trim() , IsoType.NUMERIC, 3); // NII
			iso.setValue(25, 00 , IsoType.NUMERIC, 2);// pos condition code
			if(paymentBean.getTrack2() != null)
				iso.setValue(35,paymentBean.getTrack2(), new BcdEncodedField35(), IsoType.LLBIN, 37); // track 2 data
			else{
				iso.setValue(2, paymentBean.getPanNumber() , new BCDencodeField60(), IsoType.LLBIN, 19); // pan number
				iso.setValue(14, paymentBean.getExpiryDate(), IsoType.NUMERIC, 4); // expiry date in YYMM
			}
			iso.setValue(41, paymentBean.getTerminalId(), IsoType.ALPHA, 8); // terminal id		
			iso.setValue(42, paymentBean.getMerchantId(), IsoType.ALPHA, 15); // acquiring id or merchant id
			if(paymentBean.getPinBlock()!=null){
				iso.setValue(52, paymentBean.getPinBlock(), IsoType.BINARY, 8);
			}
			if(paymentBean.getField55() != null && !paymentBean.getField55().isEmpty())
				iso.setValue(55, paymentBean.getField55(),new BCDEncodedField55() , IsoType.LLLBIN, 1);
			iso.setValue(54, field55, new BCDencodeField60(), IsoType.LLLVAR, 6);
			iso.setValue(60,field60,new BCDencodeField60() , IsoType.LLLVAR, 1); // new
			iso.setValue(62,field62,new BCDencodeField60(), IsoType.LLLVAR, 6); //  invoice number new
			iso.setValue(63,field63,new BCDencodeField60(), IsoType.LLLVAR, 6); //  currency Code
			return iso;
		}
		
		
		/**
		 * preauth iso packet creation
		 * @param paymentBean
		 * @param iso
		 * @return ISOMessage return iso packet
		 * 
		 */
		private static IsoMessage createIso100(final Payment paymentBean, final IsoMessage iso){

			
			logger.info("ISO REQUEST FOR 0100 PREAUTH");
			logger.info("Field 2 :["+ paymentBean.getPanNumber() +"]"); //  pan number
			logger.info("Field 3 :["+paymentBean.getAccountType() +"]");
			logger.info("Field 4 :["+paymentBean.getOriginalAmount()+"]");
			logger.info("Field 11 :["+paymentBean.getStanNumber()+"]");
			logger.info("Field 14 :["+paymentBean.getExpiryDate()+"]");
			logger.info("Field 22 :["+ paymentBean.getCardEntryMode()+"]");
			logger.info("Field 24 :["+paymentBean.getNii().trim()+"]");
			logger.info("Field 25 :[06]");
			logger.info("Field 35 :["+paymentBean.getTrack2()+"]");
			logger.info("Field 41 :["+paymentBean.getTerminalId()+"]");
			logger.info("Field 42 :["+paymentBean.getMerchantId()+"]");
			logger.info("Field 52 :["+paymentBean.getPinBlock()+"]");
			logger.info("Field 55 :["+paymentBean.getField55()+"]");
			
			String field60 = getField60(paymentBean);
			String field62 = getField62(paymentBean);
			String field63 = getField63(paymentBean);
			
			logger.info("Field 60 :["+field60+"]");
			logger.info("Field 62 :["+field62+"]");
			logger.info("Field 63 :["+field63+"]");
			
			iso.setValue(3, paymentBean.getAccountType() , IsoType.NUMERIC, 6);
			iso.setValue(4, paymentBean.getOriginalAmount(), IsoType.NUMERIC, 12);
			iso.setValue(11, Integer.valueOf(paymentBean.getStanNumber()), IsoType.NUMERIC, 6); // stan number
			iso.setValue(22, Integer.valueOf(paymentBean.getCardEntryMode()), IsoType.NUMERIC, 3); 
			iso.setValue(24, paymentBean.getNii().trim() , IsoType.NUMERIC, 3); // NII
			iso.setValue(25, 06 , IsoType.NUMERIC, 2);// pos condition code for Pre authorization
			if(paymentBean.getTrack2() != null){
				iso.setValue(35,paymentBean.getTrack2(), new BcdEncodedField35(), IsoType.LLBIN, 37); // track 2 data
			} else{
				iso.setValue(2, paymentBean.getPanNumber() , new BCDencodeField60(), IsoType.LLBIN, 19); // pan number
				iso.setValue(14, paymentBean.getExpiryDate(), IsoType.NUMERIC, 4); // expiry date in YYMM
			}
			if(paymentBean.getPinBlock() != null){
				iso.setValue(52, paymentBean.getPinBlock(), IsoType.BINARY, 8);
			}
			iso.setValue(41, paymentBean.getTerminalId(), IsoType.ALPHA, 8); // terminal id		
			iso.setValue(42, paymentBean.getMerchantId(), IsoType.ALPHA, 15); // acquiring id or merchant id
			if(paymentBean.getField55() != null){
				iso.setValue(55, paymentBean.getField55(),new BCDEncodedField55() , IsoType.LLLBIN, 1);
			}
			iso.setValue(60, field60,new BCDencodeField60() , IsoType.LLLVAR, 1); // new
			iso.setValue(62, field62,new BCDencodeField60(), IsoType.LLLVAR, 6); //  invoice number new
			iso.setValue(63, field63,new BCDencodeField60(), IsoType.LLLVAR, 6); //  currency Code
			return iso;
		}
		
		/**
		 * create iso for MTI 0x220 
		 * @param paymentBean
		 * @param iso
		 * @return
		 */
		private static IsoMessage createIso220(final Payment paymentBean, final IsoMessage iso){
			
			logger.info("ISO REQUEST FOR 0220 VOID");
			logger.info("Field 2 :["+ paymentBean.getPanNumber() +"]"); //  pan number
			if("1".equals(paymentBean.getRefundApproved())){
				logger.info("Field 3 :["+"22"+paymentBean.getAccountType()+"]"); // processing code	
			}else{
				logger.info("Field 3 :["+"02"+paymentBean.getAccountType()+"]"); // processing code
			}
			
			logger.info("Field 4 :["+0 +"]"); // amount 
			logger.info("Field 11 :["+paymentBean.getStanNumber()+"]"); // stan number
			logger.info("Field 12 :["+paymentBean.getTime()+"]"); // time
			logger.info("Field 13 :["+UtilityHelper.dateInMMddYYYY().substring(0, 4)+"]"); // date
			logger.info("Field 14 :["+paymentBean.getExpiryDate()+"]"); // expiry date
			logger.info("Field 22 :["+paymentBean.getCardEntryMode()+"]"); // pos entry mode
			logger.info("Field 24 :["+112+"]"); //nii
			logger.info("Field 25 :[00]");// pos condition code
			logger.info("Field 35 :["+paymentBean.getTrack2()+"]");// track2
			logger.info("Field 37 :["+paymentBean.getRetrivalRefNumber()+"]"); // rrn number
			logger.info("Field 38 :["+paymentBean.getAuthorizationId()+"]"); // auth id
			logger.info("Field 39 :["+paymentBean.getResponseCode()+"]"); // Response code
			logger.info("Field 41 :["+paymentBean.getTerminalId()+"]");
			logger.info("Field 42 :["+paymentBean.getMerchantId()+"]");
			logger.info("Field 55 :["+paymentBean.getField55()+"]");
			
			String field60 = getField60(paymentBean,"");
			String field62 = getField62(paymentBean);
			String field63 = getField63(paymentBean);
			
			logger.info("Field 60 :["+field60+"]");
			logger.info("Field 62 :["+field62+"]");
			logger.info("Field 63 :["+field63+"]");
			
			iso.setValue(2, paymentBean.getPanNumber() , new BCDencodeField60(), IsoType.LLBIN, 19); // pan number
			if("1".equals(paymentBean.getRefundApproved())){
				iso.setValue(3, "22"+paymentBean.getAccountType() , IsoType.NUMERIC, 6);
			}else{
				iso.setValue(3, "02"+paymentBean.getAccountType() , IsoType.NUMERIC, 6);
			}
			iso.setValue(4, 0, IsoType.NUMERIC, 12); 
			iso.setValue(11, paymentBean.getStanNumber(), IsoType.NUMERIC, 6); // stan number
			iso.setValue(12, paymentBean.getTime(), IsoType.NUMERIC, 6); // time
			iso.setValue(13, UtilityHelper.dateInMMddYYYY().substring(0, 4), IsoType.NUMERIC, 4); // date
			iso.setValue(14, paymentBean.getExpiryDate(), IsoType.NUMERIC, 4); // date expiry
			iso.setValue(22, Integer.valueOf(paymentBean.getCardEntryMode()), IsoType.NUMERIC, 3); // pos entry mode
			iso.setValue(24, 112 , IsoType.NUMERIC, 3); // NII
			iso.setValue(25, 00 , IsoType.NUMERIC, 2);// pos condition code
			iso.setValue(37, paymentBean.getRetrivalRefNumber() , IsoType.ALPHA, 12);// RRN
			iso.setValue(38, paymentBean.getAuthorizationId() , IsoType.ALPHA, 6);// Auth Code
			iso.setValue(39, paymentBean.getResponseCode() , IsoType.ALPHA, 2);// Response code
			iso.setValue(41, paymentBean.getTerminalId(), IsoType.ALPHA, 8); // terminal id
			iso.setValue(42, paymentBean.getMerchantId(), IsoType.ALPHA, 15); // acquiring id or merchant id
			if(paymentBean.getField55() != null)
				iso.setValue(55, paymentBean.getField55(),new BCDEncodedField55() , IsoType.LLLBIN, 1);
			iso.setValue(60, field60, new BCDencodeField60(), IsoType.LLLVAR, 1); // auth source code
			iso.setValue(62, field62, new BCDencodeField60(), IsoType.LLLVAR, 6); //  invoice number
			iso.setValue(63, field63,new BCDencodeField60(), IsoType.LLLVAR, 6); //  currency Code
			return iso;
		}
		
		/**
		 * 
		 */
		private static IsoMessage createIso220SaleOffline(final Payment paymentBean, final IsoMessage iso){
			
			logger.info("ISO REQUEST FOR 0220 SALE OFFLINE");
			logger.info("Field 2 :["+ paymentBean.getPanNumber() +"]"); //  pan number
			logger.info("Field 3 :[00a000]"); // processing code
			logger.info("Field 4 :["+paymentBean.getOriginalAmount() +"]"); // amount 
			logger.info("Field 11 :["+paymentBean.getStanNumber()+"]"); // stan number
			logger.info("Field 12 :["+paymentBean.getTime()+"]"); // time
			logger.info("Field 13 :["+UtilityHelper.dateInMMddYYYY().substring(0, 4)+"]"); // date
			logger.info("Field 14 :["+paymentBean.getExpiryDate()+"]"); // expiry date
			logger.info("Field 22 :["+paymentBean.getCardEntryMode()+"]"); // pos entry mode
			logger.info("Field 24 :["+112+"]"); //nii
			logger.info("Field 25 :[00]");// pos condition code
			logger.info("Field 35 :["+paymentBean.getTrack2()+"]");// track2
			logger.info("Field 38 :["+paymentBean.getAuthorizationId()+"]"); // auth id
			logger.info("Field 39 :["+paymentBean.getResponseCode()+"]"); // Response code
			logger.info("Field 41 :["+paymentBean.getTerminalId()+"]");
			logger.info("Field 42 :["+paymentBean.getMerchantId()+"]");
			logger.info("Field 55 :["+paymentBean.getField55()+"]");
			
			String field62 = getField62(paymentBean);
			String field63 = getField63(paymentBean);
			
			logger.info("Field 62 :["+field62+"]");
			logger.info("Field 63 :["+field63+"]");
			
			iso.setValue(2, paymentBean.getPanNumber() , new BCDencodeField60(), IsoType.LLBIN, 19); // pan number
			iso.setValue(3, "00a000" , IsoType.NUMERIC, 6);
			iso.setValue(4, paymentBean.getOriginalAmount(), IsoType.NUMERIC, 12); 
			iso.setValue(11, paymentBean.getStanNumber(), IsoType.NUMERIC, 6); // stan number
			iso.setValue(12, paymentBean.getTime(), IsoType.NUMERIC, 6); // time
			iso.setValue(13, UtilityHelper.dateInMMddYYYY().substring(0, 4), IsoType.NUMERIC, 4); // date
			iso.setValue(14, paymentBean.getExpiryDate(), IsoType.NUMERIC, 4); // date expiry
			iso.setValue(22, Integer.valueOf(paymentBean.getCardEntryMode()), IsoType.NUMERIC, 3); // pos entry mode
			iso.setValue(24, 112 , IsoType.NUMERIC, 3); // NII
			iso.setValue(25, 00 , IsoType.NUMERIC, 2);// pos condition code
			iso.setValue(38, paymentBean.getAuthorizationId() , IsoType.ALPHA, 6);// Auth Code
			iso.setValue(41, paymentBean.getTerminalId(), IsoType.ALPHA, 8); // terminal id
			iso.setValue(42, paymentBean.getMerchantId(), IsoType.ALPHA, 15); // acquiring id or merchant id
			if(paymentBean.getField55() != null)
				iso.setValue(55, paymentBean.getField55(),new BCDEncodedField55() , IsoType.LLLBIN, 1);
			iso.setValue(62, field62, new BCDencodeField60(), IsoType.LLLVAR, 6); //  invoice number
			iso.setValue(63, field63,new BCDencodeField60(), IsoType.LLLVAR, 6); //  currency Code
			
			return iso;
		}
		
		/**
		 * create ISO220 refund packet
		 * @param paymentBean
		 * @param iso
		 * @return IsoMessage iso packet format
		 */
		private static IsoMessage createIso220refund(final Payment paymentBean, final IsoMessage iso){
			
			logger.info("ISO REQUEST FOR 0220 REFUND");
			logger.info("Field 2 :["+paymentBean.getPanNumber() +"]"); //  pan number
			logger.info("Field 3 :["+"20"+paymentBean.getAccountType()+"]"); // processing code
			String amount = null;
			if(paymentBean.getCurrencyCode() != null && !paymentBean.getCurrencyCode().equals("0") && !Constants.INR_CURR_CODE.equals(paymentBean.getCurrencyCode())){
				amount= paymentBean.getDccAmount();
			}else{
				amount= paymentBean.getOriginalAmount(); 
			}
			logger.info("Field 4 :["+amount +"]"); // amount 
			logger.info("Field 11 :["+paymentBean.getStanNumber()+"]"); // stan number
			logger.info("Field 12 :["+paymentBean.getTime()+"]"); // time
			logger.info("Field 13 :["+UtilityHelper.dateInMMddYYYY().substring(0, 4)+"]"); // date
			logger.info("Field 14 :["+paymentBean.getExpiryDate()+"]"); // expiry date
			logger.info("Field 22 :["+paymentBean.getCardEntryMode()+"]"); // pos entry mode
			logger.info("Field 24 :["+112+"]"); //nii
			logger.info("Field 25 :[00]");// pos condition code
			logger.info("Field 35 :["+paymentBean.getTrack2()+"]");// track2
			logger.info("Field 37 :["+paymentBean.getRetrivalRefNumber()+"]"); // rrn number
			logger.info("Field 38 :["+paymentBean.getAuthorizationId()+"]"); // auth id
			logger.info("Field 39 :["+paymentBean.getResponseCode()+"]"); // Response code
			logger.info("Field 41 :["+paymentBean.getTerminalId()+"]");
			logger.info("Field 42 :["+paymentBean.getMerchantId()+"]");
			logger.info("Field 55 :["+paymentBean.getField55()+"]");
			
			String field60 = getField60(paymentBean);
			String field62 = getField62(paymentBean);
			String field63 = getField63(paymentBean);
			
			logger.info("Field 62 :["+field62+"]");
			logger.info("Field 63 :["+field63+"]");
			
			iso.setValue(2, paymentBean.getPanNumber() , new BCDencodeField60(), IsoType.LLBIN, 19); // pan number
			iso.setValue(3, "20"+ paymentBean.getAccountType(), IsoType.NUMERIC, 6);
			iso.setValue(4, amount, IsoType.NUMERIC, 12);
			iso.setValue(11, paymentBean.getStanNumber(), IsoType.NUMERIC, 6); // stan number
			iso.setValue(12, paymentBean.getTime(), IsoType.NUMERIC, 6); // time
			iso.setValue(13, UtilityHelper.dateInMMddYYYY().substring(0, 4), IsoType.NUMERIC, 4); // date
			iso.setValue(14, paymentBean.getExpiryDate(), IsoType.NUMERIC, 4); // date expiry
			iso.setValue(22, Integer.valueOf(paymentBean.getCardEntryMode()), IsoType.NUMERIC, 3); // pos entry mode
			iso.setValue(24, 112 , IsoType.NUMERIC, 3); // NII
			iso.setValue(25, 00 , IsoType.NUMERIC, 2);// pos condition code
			if (null != paymentBean.getCardEntryMode() && !paymentBean.getCardEntryMode().isEmpty()){
				if(paymentBean.getTrack2() != null)
					iso.setValue(35,paymentBean.getTrack2(), new BcdEncodedField35(), IsoType.LLBIN, 37); // track 2 data
				else{
					iso.setValue(2, paymentBean.getPanNumber() , new BCDencodeField60(), IsoType.LLBIN, 19); // pan number
					iso.setValue(14, paymentBean.getExpiryDate(), IsoType.NUMERIC, 4); // expiry date in YYMM
				}
			}
			if(paymentBean.getRetrivalRefNumber() != null)
				iso.setValue(37, paymentBean.getRetrivalRefNumber() , IsoType.ALPHA, 12);// RRN
			if(paymentBean.getAuthorizationId() == null) iso.setValue(38, 0 , IsoType.ALPHA, 6);// Auth Code
			else iso.setValue(38,paymentBean.getAuthorizationId(), IsoType.ALPHA, 6);
			if(paymentBean.getResponseCode() != null) iso.setValue(39, paymentBean.getResponseCode() , IsoType.ALPHA, 2);// Response code
			iso.setValue(41, paymentBean.getTerminalId(), IsoType.ALPHA, 8); // terminal id
			iso.setValue(42, paymentBean.getMerchantId(), IsoType.ALPHA, 15); // acquiring id or merchant id
			if(paymentBean.getField55() != null && !Constants.TCH_NULL.equalsIgnoreCase(paymentBean.getField55()))
				iso.setValue(55, paymentBean.getField55(),new BCDEncodedField55() , IsoType.LLLBIN, 1);
			iso.setValue(62,field62, new BCDencodeField60(), IsoType.LLLVAR, 6); //  invoice number
			iso.setValue(63,field63,new BCDencodeField60(), IsoType.LLLVAR, 6); //  currency Code
			return iso;
		}
		
		private static IsoMessage createIso220PreAuth(final Payment paymentBean, final IsoMessage iso){
			
			logger.info("ISO REQUEST FOR 0220 PREAUTH COMPLETION");
			logger.info("Field 2 :["+paymentBean.getPanNumber() +"]"); //  pan number
			logger.info("Field 3 :["+"00"+paymentBean.getAccountType()+"]"); // processing code
			logger.info("Field 4 :["+paymentBean.getOriginalAmount() +"]"); // amount 
			logger.info("Field 11 :["+paymentBean.getStanNumber()+"]"); // stan number
			logger.info("Field 12 :["+paymentBean.getTime()+"]"); // time
			logger.info("Field 13 :["+UtilityHelper.dateInMMddYYYY().substring(0, 4)+"]"); // date
			logger.info("Field 14 :["+paymentBean.getExpiryDate()+"]"); // expiry date
			logger.info("Field 22 :["+paymentBean.getCardEntryMode()+"]"); // pos entry mode
			logger.info("Field 24 :["+112+"]"); //nii
			logger.info("Field 25 :[06]");// pos condition code
			logger.info("Field 37 :["+paymentBean.getRetrivalRefNumber()+"]"); // rrn number
			logger.info("Field 39 :["+paymentBean.getResponseCode()+"]"); // rrn number
			logger.info("Field 38 :["+paymentBean.getAuthorizationId()+"]"); // auth id
			logger.info("Field 41 :["+paymentBean.getTerminalId()+"]");
			logger.info("Field 42 :["+paymentBean.getMerchantId()+"]");
			logger.info("Field 55 :["+paymentBean.getField55()+"]");
			logger.info("Field 61 :["+paymentBean.getAuthorizationId()+"]");
			logger.info("Field 62 :["+paymentBean.getInvoiceNumber()+"]");
			
			
			String field60 = getField60(paymentBean);
			String field62 = getField62(paymentBean);
			String field63 = getField63(paymentBean);
			
			//logger.info("Field 60 :["+field60+"]");
			logger.info("Field 62 :["+field62+"]");
			logger.info("Field 63 :["+field63+"]");
			
			iso.setValue(3, "00"+ paymentBean.getAccountType(), IsoType.NUMERIC, 6);
			iso.setValue(4, paymentBean.getOriginalAmount(), IsoType.NUMERIC, 12); 
			iso.setValue(11, paymentBean.getStanNumber(), IsoType.NUMERIC, 6); // stan number
			iso.setValue(12, paymentBean.getTime(), IsoType.NUMERIC, 6); // time
			iso.setValue(13, UtilityHelper.dateInMMddYYYY().substring(0, 4), IsoType.NUMERIC, 4); // date
			iso.setValue(22, Integer.valueOf(paymentBean.getCardEntryMode()), IsoType.NUMERIC, 3); // pos entry mode
			iso.setValue(24, 112 , IsoType.NUMERIC, 3); // NII
			iso.setValue(25, 06 , IsoType.NUMERIC, 2);// pos condition code
			iso.setValue(2, paymentBean.getPanNumber() , new BCDencodeField60(), IsoType.LLBIN, 19); // pan number
			iso.setValue(14, paymentBean.getExpiryDate(), IsoType.NUMERIC, 4); // expiry date in YYMM
			if(paymentBean.getRetrivalRefNumber() != null)
				iso.setValue(37, paymentBean.getRetrivalRefNumber() , IsoType.ALPHA, 12);// RRN
			if(paymentBean.getAuthorizationId() != null){
				iso.setValue(38, paymentBean.getAuthorizationId() , IsoType.ALPHA, 6);// Auth Code
			}else{
				iso.setValue(38, 0 , IsoType.ALPHA, 6);// Auth Code
			}
			iso.setValue(41, paymentBean.getTerminalId(), IsoType.ALPHA, 8); // terminal id
			iso.setValue(42, paymentBean.getMerchantId(), IsoType.ALPHA, 15); // acquiring id or merchant id
			if(paymentBean.getField55() != null )
				iso.setValue(55, paymentBean.getField55(),new BCDEncodedField55() , IsoType.LLLBIN, 1);
			iso.setValue(39, paymentBean.getResponseCode(), IsoType.ALPHA, 2);
			if(paymentBean.getAuthorizationId() != null)
				iso.setValue(61,paymentBean.getAuthorizationId(), new BCDencodeField60(), IsoType.LLLVAR, 6);
			else{
				iso.setValue(61,"0", new BCDencodeField60(), IsoType.LLLVAR, 6);
			}
			iso.setValue(62,field62, new BCDencodeField60(), IsoType.LLLVAR, 6); //  invoice number
			iso.setValue(63,field63,new BCDencodeField60(), IsoType.LLLVAR, 6); //  currency Code
			return iso;
		}
		
		/**
		 * create ISO 400 
		 * @param paymentBean
		 * @param iso
		 * @return IsoMessage ISO packet for 400 MTI
		 */
		private static IsoMessage createIso400(final Payment paymentBean, final IsoMessage iso){

			
			logger.info("ISO REQUEST FOR 0400");
			logger.info("Field 2 :["+ paymentBean.getPanNumber() +"]"); //  pan number
			logger.info("Field 3 :["+paymentBean.getProcessingCode() != null ? paymentBean.getProcessingCode() : "000000" +"]"); // processing code
			logger.info("Field 4 :["+paymentBean.getOriginalAmount()+"]"); // amount 
			logger.info("Field 11 :["+paymentBean.getStanNumber()+"]"); // stan number
			logger.info("Field 14 :["+paymentBean.getExpiryDate()+"]"); // expiry date
			logger.info("Field 22 :["+paymentBean.getCardEntryMode()+"]"); // pos entry mode
			logger.info("Field 24 :["+112+"]"); //nii
			logger.info("Field 25 :[00]");// pos condition code
			logger.info("Field 52 :["+paymentBean.getPinBlock()+"]");
			logger.info("Field 35 :["+paymentBean.getTrack2()+"]");// track2
			logger.info("Field 39 :["+paymentBean.getDecisionFlag() +"]");
			logger.info("Field 41 :["+paymentBean.getTerminalId()+"]");
			logger.info("Field 42 :["+paymentBean.getMerchantId()+"]");
			logger.info("Field 54 :["+paymentBean.getAdditionalAmount()+"]");
			logger.info("Field 55 :["+paymentBean.getField55()+"]");
			
			String field60 = getField60(paymentBean);
			String field62 = getField62(paymentBean);
			String field63 = getField63(paymentBean);
			
			//logger.info("Field 60 :["+field60+"]");
			logger.info("Field 62 :["+field62+"]");
			logger.info("Field 63 :["+field63+"]");
			
			iso.setValue(3,  paymentBean.getProcessingCode() != null ? paymentBean.getProcessingCode() : "000000" , IsoType.NUMERIC, 6);
			iso.setValue(4, paymentBean.getOriginalAmount(), IsoType.NUMERIC, 12);
			iso.setValue(11, paymentBean.getStanNumber(), IsoType.NUMERIC, 6); // stan number
			iso.setValue(22, Integer.valueOf(paymentBean.getCardEntryMode()), IsoType.NUMERIC, 3); // pos entry mode
			iso.setValue(24, 112 , IsoType.NUMERIC, 3); // NII
			iso.setValue(25, 00 , IsoType.NUMERIC, 2);// pos condition code
			if(Constants.TCH_E1.equals(paymentBean.getDecisionFlag()) || Constants.TCH_E2.equals(paymentBean.getDecisionFlag()) || Constants.TCH_Z3.equals(paymentBean.getDecisionFlag()))
				iso.setValue(39, paymentBean.getDecisionFlag() , IsoType.ALPHA, 2);// Response code
			iso.setValue(41, paymentBean.getTerminalId(), IsoType.ALPHA, 8); // terminal id
			iso.setValue(42, paymentBean.getMerchantId(), IsoType.ALPHA, 15); // acquiring id or merchant id
			iso.setValue(54, paymentBean.getAdditionalAmount(), IsoType.NUMERIC, 12);
			if(paymentBean.getField55() != null)
			iso.setValue(55, paymentBean.getField55(),new BCDEncodedField55() , IsoType.LLLBIN, 1);
			iso.setValue(2, paymentBean.getPanNumber() , new BCDencodeField60(), IsoType.LLBIN, 19); // pan number
			iso.setValue(14, paymentBean.getExpiryDate(), IsoType.NUMERIC, 4); // expiry date in YYMM
			if(paymentBean.getPinBlock() != null){
				iso.setValue(52, paymentBean.getPinBlock(), IsoType.BINARY, 8);
			}	
			iso.setValue(62,field62, new BCDencodeField60(), IsoType.LLLVAR, 6); //  invoice number
			iso.setValue(63,field63,new BCDencodeField60(), IsoType.LLLVAR, 6); //  currency Code
			
			if(Constants.TCH_EMI_SALE.equals(paymentBean.getDecisionFlag())){
				/*iso.setValue(2, "4629864339046007" , new BCDencodeField60(), IsoType.LLBIN, 19); // pan number
				iso.setValue(14, "1510", IsoType.NUMERIC, 4); // expiry date in YYMM
				iso.setValue(4, 111  , IsoType.AMOUNT, 12);
				iso.setValue(3, 000000, IsoType.NUMERIC, 6);
				iso.setValue(11, "334" , IsoType.NUMERIC, 6); // stan number
				iso.setValue(22, 51, IsoType.NUMERIC, 3); //pos entry mode //emv 51
				iso.setValue(24, 112 , IsoType.NUMERIC, 3); // NII
				iso.setValue(25, 00 , IsoType.NUMERIC, 2);// pos condition code  
				iso.setValue(41, "00001288", IsoType.ALPHA, 8); // terminal id		
				iso.setValue(42, "075126027804067  ", IsoType.ALPHA, 15); // acquiring id or merchant id
				iso.setValue(60, "A"+"0",new BCDencodeField60() , IsoType.LLLVAR, 1);
				iso.setValue(62, "000007",new BCDencodeField60(), IsoType.LLLVAR, 6); //  invoice number
				
*/				iso.setValue(2, paymentBean.getPanNumber() , new BCDencodeField60(), IsoType.LLBIN, 19); // pan number
				iso.setValue(14, paymentBean.getExpiryDate(), IsoType.NUMERIC, 4); // expiry date in YYMM
				String progCode =  String.format("%1$-6s",paymentBean.getEmiObject().getProgramObject()[0].getProgramCode());
				//String progCode = String.format("%06d",Integer.parseInt("123111"));
				String productCode = String.format("%1$-15s", paymentBean.getEmiObject().getPrductCode().trim());
				//String productCode = String.format("%1$-15s", "21312");
				String tenure = String.format("%02d", Integer.parseInt(paymentBean.getEmiObject().getTenureObject()[0].getTenure()));
				//String tenure = String.format("%02d", Integer.parseInt("3"));
				String imie = "";
				if(paymentBean.getEmiObject().getImieNumber() == null){
					imie = String.format("%02d", Integer.parseInt("0"));
				}else{
					
					//imie = String.format("%02d", Integer.parseInt(paymentBean.getEmiObject().getImieNumber()));
					imie = paymentBean.getEmiObject().getImieNumber(); //changed for number format exception
				}
				paymentBean.setField63("F" +progCode + productCode + tenure + imie);
				logger.debug("Field 63 :[" + paymentBean.getField63() +"]");
				iso.setValue(63, paymentBean.getField63().trim(),new BCDencodeField60(), IsoType.LLLVAR, 6); //  field 63 as per specification
			}else if(Constants.TCH_DCC_SALE.equals(paymentBean.getDecisionFlag())){
				iso.setValue(2, paymentBean.getPanNumber() , new BCDencodeField60(), IsoType.LLBIN, 19); // pan number
				iso.setValue(14, paymentBean.getExpiryDate(), IsoType.NUMERIC, 4); // expiry date in YYMM
				iso.setValue(4, paymentBean.getDccAmount(), IsoType.NUMERIC, 12);
				logger.info("DCC Field 4 :["+paymentBean.getDccAmount()+"]");
				logger.debug("original amount : " + paymentBean.getOriginalAmount());
				logger.debug("currency code : " + paymentBean.getCurrencyCode());
				logger.debug("Exchnage rate : " + paymentBean.getExchangeRate());
				logger.debug("Markup  : " + paymentBean.getMarkup());
				logger.debug("currency name : " + paymentBean.getCurrencyName());
				
				String exchangeRate =  new BigDecimal(paymentBean.getExchangeRate()).movePointRight(4).toPlainString();
				String markup =  new BigDecimal(paymentBean.getMarkup()).movePointRight(4).toPlainString();
				logger.debug("Exchange rate after converting" + exchangeRate);
				logger.debug("Markup rate after converting" + markup);
				
				StringBuilder field631 = new StringBuilder("")
				.append(Tags.hardwareserialnumberTagLength + String.format("%015d", Integer.parseInt(paymentBean.getTempSerialNumber())))
				.append(Tags.DCCtransactionIndicatorTagLength + "D")
				.append(Tags.currencyCodeTagLength + paymentBean.getCurrencyCode())
				.append(Tags.amountInLocalCurrencyTagLength + String.format("%012d", Long.parseLong(paymentBean.getOriginalAmount())))
				.append(Tags.localCurrencyCodeTagLength + String.format("%03d", 356))
				.append(Tags.exchangeRateTagLength + String.format("%08d", Integer.parseInt(exchangeRate)))
				.append(Tags.markupPercentTagLength +String.format("%04d", Integer.parseInt(markup)))
				//.append(Tags.DCCTransactioncurrencycodeTagLength + paymentBean.getCurrencyCode())
				.append(Tags.transactionCurrencyName + paymentBean.getCurrencyName())
				.append(Tags.numberOfDecimalForDccAmount + "2"); // number of decimal digit
				if(Constants.TCH_PROMT_N_PROC.equals(paymentBean.getDecisionFlag())){
					field631.append(Tags.optoutVoidTransactionTagLength + "01"); // opt out void transaction
				}
				logger.debug("Field 63 :[" + field631.toString()+"]");
				iso.setValue(63,field631.toString(), IsoType.LLLVAR, 6); //  field 63 as per specification
				
			}
			return iso;
		}
		
		/**
		 * @param paymentBean
		 * @param iso
		 * @return
		 */
		private static IsoMessage createIso100BalEnq(final Payment paymentBean, final IsoMessage iso){
			
			logger.info("ISO REQUEST FOR 0100 balance enquiry");
			logger.info("Field 2 :["+paymentBean.getPanNumber() +"]");
			logger.info("Field 3 :["+"31"+paymentBean.getAccountType() +"]");
			logger.info("Field 11 :["+paymentBean.getStanNumber()+"]");
			logger.info("Field 14 :["+paymentBean.getExpiryDate()+"]");
			logger.info("Field 22 :["+"0"+ paymentBean.getCardEntryMode()+"]");
			logger.info("Field 24 :["+paymentBean.getNii().trim()+"]");
			logger.info("Field 25 :[00]");
			logger.info("Field 35 :["+paymentBean.getTrack2()+"]");
			logger.info("Field 41 :["+paymentBean.getTerminalId()+"]");
			logger.info("Field 42 :["+paymentBean.getMerchantId()+"]");
			logger.info("Field 52 :["+paymentBean.getPinBlock()+"]");
			logger.info("Field 55 :["+paymentBean.getField55()+"]");
			
			String field60 = getField60(paymentBean);
			String field62 = getField62(paymentBean);
			String field63 = getField63(paymentBean);
			
			logger.info("Field 60 :["+field60+"]");
			logger.info("Field 62 :["+field62+"]");
			logger.info("Field 63 :["+field63+"]");
			
			iso.setValue(3,"31"+ paymentBean.getAccountType() , IsoType.NUMERIC, 6);
			iso.setValue(11, Integer.valueOf(paymentBean.getStanNumber()), IsoType.NUMERIC, 6); // stan number
			iso.setValue(22, Integer.valueOf(paymentBean.getCardEntryMode()), IsoType.NUMERIC, 3); 
			iso.setValue(24, paymentBean.getNii().trim() , IsoType.NUMERIC, 3); // NII
			iso.setValue(25, 00 , IsoType.NUMERIC, 2);// pos condition code for Pre authorization
			if(paymentBean.getTrack2() != null){
				iso.setValue(35,paymentBean.getTrack2(), new BcdEncodedField35(), IsoType.LLBIN, 37); // track 2 data
			}else{
				
				iso.setValue(2, paymentBean.getPanNumber() , new BCDencodeField60(), IsoType.LLBIN, 19); // pan number
				iso.setValue(14, paymentBean.getExpiryDate(), IsoType.NUMERIC, 4); // expiry date in YYMM
			}
			if(!Constants.TCH_NULL.equals(paymentBean.getPinBlock())){
				
				iso.setValue(52, paymentBean.getPinBlock(), IsoType.BINARY, 8);
			}
			
			iso.setValue(41, paymentBean.getTerminalId(), IsoType.ALPHA, 8); // terminal id		
			iso.setValue(42, paymentBean.getMerchantId(), IsoType.ALPHA, 15); // acquiring id or merchant id
			//iso.setValue(60, "A"+"F",new BcdEncodedField35(), IsoType.LLBIN, 1); // auth source code
			if(paymentBean.getField55() != null && !paymentBean.getField55().isEmpty()){
				iso.setValue(55, paymentBean.getField55(),new BCDEncodedField55() , IsoType.LLLBIN, 1);
				
			}
			iso.setValue(60,field60,new BCDencodeField60() , IsoType.LLLVAR, 1); // new
			iso.setValue(62,field62,new BCDencodeField60(), IsoType.LLLVAR, 6); //  invoice number new
			iso.setValue(63,field63,new BCDencodeField60(), IsoType.LLLVAR, 6); //  currency Code
			return iso;
		}
		
		private static void setEMIValues(final Payment input){
			/*listLabel.add("EMI DETAILS");
			listLabel.add("Tenure(in months)"); //0						
			listLabel.add("Rate of Interest(%)");//1				
			listLabel.add("Proc fee(%)");	//2 	
			listLabel.add("Cashback/Payback/Discount(%)");//3
			listLabel.add("Cashback/Payback/Discount Amt");//4
			listLabel.add("EMI Booking Amount");//5 //Effective Cost to the Customer Amount
			listLabel.add("EMI Amt");//6
			listLabel.add("Total Amt (with Int)");//7
			listLabel.add("PRODUCT DETAILS");//8
			listLabel.add("MFG NAME");//9
			listLabel.add("Product Code");//10
			listLabel.add("Product Desc");//11
			listLabel.add("Product IMEI/Sr");//12*/
			
			List<DataPrintObject> listdpo = new ArrayList<DataPrintObject>();
			List<String> listLabel = new ArrayList<String>();
			SuperMenuObject superObj = AbstractBrandEmiAdaptor.brandEmiFinalCache.get(input.getTerminalSerialNumber());
			
			String tenureCode = input.getEmiObject().getTenureObject()[0].getTenure();
			Map<String, TenureObject> tenureMap = superObj.getTenureMap();
			for(int i = 0; i < EMIMaster.getMap().size() ; i++){
				listLabel.add(EMIMaster.get(String.valueOf(i)));
			}
			int index = 0;
			for(String str : listLabel){
				DataPrintObject d = new DataPrintObject();
				d.setPrintLabel(str);
				boolean repeatRestrict = false;
				switch(index){
				case 0:
					d.setPrintVal("NULL");
					break;
				case 1: // tenure
					try{
						logger.info("Tenure vale:" + input.getEmiObject().getTenureObject()[0].getTenure());
						d.setPrintVal(input.getEmiObject().getTenureObject()[0].getTenure());
						tenureCode = input.getEmiObject().getTenureObject()[0].getTenure();
						superObj.setTenure(tenureCode);
					}catch(Exception e){
						logger.debug("Exception in 1 :",e);
					}
					break;
				case 2: // rate of interest
					try{
						logger.info("Rate of interest : in print :" + input.getEmiObject().getTenureObject()[0].getRoi());
						d.setPrintVal(String.format("%.02f",new BigDecimal(input.getEmiObject().getTenureObject()[0].getRoi())));
					}catch(Exception e){
						logger.debug("Exception in 2 :",e);
					}
					//d.setPrintVal(field63.substring(70,74).trim());
					break;
				case 3: // Proc fee per
					try{
						if(input.getEmiObject().getTenureObject()[0].getPerFeePercentage() != null && !input.getEmiObject().getTenureObject()[0].getPerFeePercentage().isEmpty()
								&& !input.getEmiObject().getTenureObject()[0].getPerFeePercentage().equals("0")){
							logger.info("Proc fee per : in print :" + new BigDecimal(input.getEmiObject().getTenureObject()[0].getPerFeePercentage()));
							d.setPrintVal(String.format("%.02f",new BigDecimal(input.getEmiObject().getTenureObject()[0].getPerFeePercentage())));
						}else{
							d.setPrintVal("0.00");
						}
						listdpo.add(d);
						d = new DataPrintObject();
						if(input.getEmiObject().getTenureObject()[0].getPerFeeAmount() != null){
							d.setPrintLabel("Proc Amt(Rs) :");
							BigDecimal amount = new BigDecimal(input.getEmiObject().getTenureObject()[0].getPerFeeAmount());
							logger.debug("Proc Amt(Rs) :" + amount + " after adding decimal precision :" + String.format("%.02f", amount));
							d.setPrintVal(String.format("%.02f", amount));
						}else{
							d.setPrintVal("0.00");
						}
					}catch(Exception e){
						logger.debug("Exception in 3 :",e);
					}
					break;
				case 4: // Cashback/Payback/Discount(%)
					//logger.info("Cashback disc per : in print :" + (String) AbstractBrandEmiAdaptor.generalCacheForBRDEMI.get(input.getTerminalSerialNumber()+Constants.TCH_BRD_CASHBACK_PER+AbstractBrandEmiAdaptor.brdEmiMapping.get(input.getEmiObject().getTenureObject()[0].getTenure())));
					//d.setPrintVal((String) AbstractBrandEmiAdaptor.generalCacheForBRDEMI.get(input.getTerminalSerialNumber()+Constants.TCH_BRD_CASHBACK_PER+AbstractBrandEmiAdaptor.brdEmiMapping.get(input.getEmiObject().getTenureObject()[0].getTenure())));
					try{
						tenureMap =  AbstractBrandEmiAdaptor.brandEmiFinalCache.get(input.getTerminalSerialNumber()).getTenureMap();
						logger.debug("Cashback % :" + tenureMap.get(tenureCode).getSubventionFinalPer()); 
						d.setPrintVal(tenureMap.get(tenureCode).getSubventionFinalPer());
						
						/*if(input.getEmiObject().getTenureObject()[0].getCashbackPercent() != null){
							//d.setPrintVal(input.getEmiObject().getTenureObject()[0].getCashbackPercent().contains(".") ? input.getEmiObject().getTenureObject()[0].getCashbackPercent() : 
								//new BigDecimal(input.getEmiObject().getTenureObject()[0].getCashbackPercent()).movePointRight(2).toPlainString());
							d.setPrintVal((String) AbstractBrandEmiAdaptor.generalCacheForBRDEMI.get(input.getTerminalSerialNumber()+Constants.TCH_BRD_CASHBACK_PER));
						}
						else{
							d.setPrintLabel(null);
							d.setPrintVal(null);
						}*/
						//d.setPrintVal(field63.substring(77,80).trim());
					}catch(Exception e){
						logger.debug("Exception in 4 :",e);
					}
					
					break;
				case 5:
					try{
						// Cashback/Payback/Discount Amt
						/*Integer tenure = AbstractBrandEmiAdaptor.brdEmiMapping.get(input.getEmiObject().getTenureObject()[0].getTenure());
						String key = input.getTerminalSerialNumber()+Constants.TCH_BRD_CASHBACK_AMOUNT+tenure;
						logger.info("cashback amount : in print :" + (String) AbstractBrandEmiAdaptor.generalCacheForBRDEMI.get(key));
						BigDecimal cashBackAmount = new BigDecimal((String) AbstractBrandEmiAdaptor.generalCacheForBRDEMI.get(key));
						d.setPrintVal(String.format("%.02f", cashBackAmount));*/
						logger.debug("Cashbach amount :" + AbstractBrandEmiAdaptor.brandEmiFinalCache.get(input.getTerminalSerialNumber()));
						tenureMap = AbstractBrandEmiAdaptor.brandEmiFinalCache.get(input.getTerminalSerialNumber()).getTenureMap();
						logger.debug("Cashbach amount :" + tenureMap.get(tenureCode).getSubventionFinalAmt());
						d.setPrintVal(tenureMap.get(tenureCode).getSubventionFinalAmt());
					}catch(Exception e){
						logger.debug("Exception in 5 :",e);
					}
					break;
				case 6: // booking amount
					try{
						logger.info("EMI booking amount: in print :" +input.getOriginalAmount());
						d.setPrintVal(new BigDecimal(input.getOriginalAmount()).movePointLeft(2).toPlainString());
					}catch(Exception e){
						logger.debug("Exception in 6 :",e);
					}
					break;
				case 7: // EMI amount
					try{
						logger.info("Emi amount : in print :" + input.getEmiObject().getTenureObject()[0].getEmiAmount());
						d.setPrintVal(input.getEmiObject().getTenureObject()[0].getEmiAmount());
					}catch(Exception e){
						logger.debug("Exception in 7 :",e);
					}
					break;
				case 8: //Total amount (with int)
					try{
						logger.info("Total amount with int : in print :" + input.getEmiObject().getTenureObject()[0].getEmiTotalAmount());
						if(input.getEmiObject().getTenureObject()[0].getEmiTotalAmount() != null	&& 
								input.getEmiObject().getTenureObject()[0].getEmiTotalAmount().contains("."))
							d.setPrintVal(input.getEmiObject().getTenureObject()[0].getEmiTotalAmount());
						else
							d.setPrintVal(addDecimalToAmt(input.getEmiObject().getTenureObject()[0].getEmiTotalAmount()));
					}catch(Exception e){
						logger.debug("Exception in 8 :",e);
					}
					break;
				case 9: //dash line
					d.setPrintVal("----------------------------------------------");
					break;
				case 10://issuer diclaimer
					/*logger.info("Issuer disc :" + AbstractBrandEmiAdaptor.generalCacheForBRDEMI.get(input.getTerminalSerialNumber()+Constants.TCH_BRD_ISSUER_BANK_DISC));
					if(AbstractBrandEmiAdaptor.generalCacheForBRDEMI.get(input.getTerminalSerialNumber()+Constants.TCH_BRD_ISSUER_BANK_DISC) !=null)
						d.setPrintVal((String) AbstractBrandEmiAdaptor.generalCacheForBRDEMI.get(input.getTerminalSerialNumber()+Constants.TCH_BRD_ISSUER_BANK_DISC));
					else
						d.setPrintVal("");*/
					try{
						String issuerBank = getIssuerBankName(superObj.getIssuerBankName());
						if (issuerBank !=null){
							d = new DataPrintObject();
							d.setPrintLabel("ISSUER BANK ");
							d.setPrintVal(issuerBank);
							listdpo.add(d);
						}
					}
					catch(Exception e){
						logger.debug("Exception in 10 :",e);
					}
					try{
						logger.debug("Issuer disc :" + AbstractBrandEmiAdaptor.brandEmiFinalCache.get(input.getTerminalSerialNumber()).getIssuerDisclaimer());
						if(AbstractBrandEmiAdaptor.brandEmiFinalCache.get(input.getTerminalSerialNumber()).getIssuerDisclaimer() !=null)
						{
							List<String> disc = HelperUtil.toGetByteString(AbstractBrandEmiAdaptor.brandEmiFinalCache.get(input.getTerminalSerialNumber()).getIssuerDisclaimer());
							repeatRestrict = true;
							d = new DataPrintObject();
							d.setPrintLabel("Issuer Disclaimer :");
							d.setPrintVal("");
							listdpo.add(d);
							for(String dis : disc){
								d = new DataPrintObject();
								d.setPrintVal(dis);
								listdpo.add(d);
							}
						}

						else
							d.setPrintVal("");
					}catch(Exception e){
						logger.debug("Exception in 10 :",e);
					}
					break;
				case 11 : // dash line
					if(AbstractBrandEmiAdaptor.brandEmiFinalCache.get(input.getTerminalSerialNumber()).getProductDetails() != null || 
					input.getEmiObject().getImieNumber() != null || input.getEmiObject().getPrductCode() != null || input.getReferenceValue()!= null
					||input.getReferenceValue()!= null){
					d.setPrintVal("----------------------------------------------");
					}
					else
						d=null;
					//d.setPrintVal("");
					break;
				case 12:
					try{
						logger.debug("Product details :" +AbstractBrandEmiAdaptor.brandEmiFinalCache.get(input.getTerminalSerialNumber()).getProductDetails()
								+ " IMIE number :"+input.getEmiObject().getImieNumber()  + "product code :"+ input.getEmiObject().getPrductCode()
								+" Invoice number " +input.getReferenceValue());
						if(AbstractBrandEmiAdaptor.brandEmiFinalCache.get(input.getTerminalSerialNumber()).getProductDetails() != null ||
								input.getEmiObject().getImieNumber() != null || input.getEmiObject().getPrductCode() != null ||input.getReferenceValue()!= null){
							d.setPrintVal("NULL");
						}else
							d=null;
					}catch(Exception e){
						logger.debug("Exception in 12 :",e);
					}
					break;
				case 13: // Manufature name
					/*logger.info("Man name : inprint : "+ AbstractBrandEmiAdaptor.generalCacheForBRDEMI.get(input.getTerminalSerialNumber()+Constants.TCH_BRD_MANUFACTURE_NAME));
					if(AbstractBrandEmiAdaptor.generalCacheForBRDEMI.get(input.getTerminalSerialNumber()+Constants.TCH_BRD_MANUFACTURE_NAME) != null)
						d.setPrintVal((String) AbstractBrandEmiAdaptor.generalCacheForBRDEMI.get(input.getTerminalSerialNumber()+Constants.TCH_BRD_MANUFACTURE_NAME));
					else 
						d.setPrintVal("");*/
					try{
						if(AbstractBrandEmiAdaptor.brandEmiFinalCache.get(input.getTerminalSerialNumber()).getManuFactureName() != null){
							logger.debug("manufacture name :" + AbstractBrandEmiAdaptor.brandEmiFinalCache.get(input.getTerminalSerialNumber()).getManuFactureName());
							if(AbstractBrandEmiAdaptor.brandEmiFinalCache.get(input.getTerminalSerialNumber()).getManuFactureName() != null)
								d.setPrintVal(AbstractBrandEmiAdaptor.brandEmiFinalCache.get(input.getTerminalSerialNumber()).getManuFactureName());
							else
								d.setPrintVal("");
						}else
							d=null;
					}catch(Exception e){
						logger.debug("Exception in 2 :",e);
					}
					break;
				case 14: 
						// adding code for invoice number
						try{
							if (null != input.getReferenceValue()){
								logger.debug("Invoice no :" + input.getReferenceValue());
								DataPrintObject d1 = new DataPrintObject();
								d1.setPrintLabel("Invoice No  :");
								d1.setPrintVal(input.getReferenceValue());
								listdpo.add(d1);
							}else{
								logger.debug("Invoice no is null");
							}
							
							// Product code
							if(null != AbstractBrandEmiAdaptor.brandEmiFinalCache.get(input.getTerminalSerialNumber()).getProductDetails()){
								logger.info("Product code : in print :" + AbstractBrandEmiAdaptor.brandEmiFinalCache.get(input.getTerminalSerialNumber()).getProductDetails()[0]);
								d.setPrintVal(AbstractBrandEmiAdaptor.brandEmiFinalCache.get(input.getTerminalSerialNumber()).getProductDetails()[0]);
							}else if(input.getEmiObject().getPrductCode() != null){
								logger.debug("Product code :" + input.getEmiObject().getPrductCode());
								d.setPrintVal(input.getEmiObject().getPrductCode());
								
							}
							else 
								d=null;
						}catch(Exception e){
							logger.debug("Exception in 2 :",e);
						}
					break;
				case 15: // Product Desc
					/*logger.info("Product desc : in print :" + AbstractBrandEmiAdaptor.generalCacheForBRDEMI.get(input.getTerminalSerialNumber()+Constants.TCH_PRODUCT_DESC));
					if(AbstractBrandEmiAdaptor.generalCacheForBRDEMI.get(input.getTerminalSerialNumber()+Constants.TCH_PRODUCT_DESC) != null)
						d.setPrintVal((String) AbstractBrandEmiAdaptor.generalCacheForBRDEMI.get(input.getTerminalSerialNumber()+Constants.TCH_PRODUCT_DESC));
					else
						d.setPrintVal("");*/
					try{
						logger.debug("prodcut disc : " + AbstractBrandEmiAdaptor.brandEmiFinalCache.get(input.getTerminalSerialNumber()).getProductDesc());
						if(AbstractBrandEmiAdaptor.brandEmiFinalCache.get(input.getTerminalSerialNumber()).getProductDesc() != null){
								d.setPrintVal(AbstractBrandEmiAdaptor.brandEmiFinalCache.get(input.getTerminalSerialNumber()).getProductDesc());
						} else
							d = null;
					}catch(Exception e){
						logger.debug("Exception in 2 :",e);
					}
					break;
				case 16: // prodcut IMIE/Sr no 
					try{
						logger.info("IMIE number : in print :" + input.getEmiObject().getImieNumber());
						if(input.getEmiObject().getImieNumber() != null)
							d.setPrintVal(input.getEmiObject().getImieNumber());
						else
							d = null;
					}catch(Exception e){
						logger.debug("Exception in 2 :",e);
					}
					
					break;
				case 17: // manufacturer disclaimer
					/*logger.info("Manufacture discalaimer :" + AbstractBrandEmiAdaptor.generalCacheForBRDEMI.get(input.getTerminalSerialNumber()+Constants.TCH_BRD_MANUFACTURE_DISC));
					if(AbstractBrandEmiAdaptor.generalCacheForBRDEMI.get(input.getTerminalSerialNumber()+Constants.TCH_BRD_MANUFACTURE_DISC) != null)
						d.setPrintVal((String) AbstractBrandEmiAdaptor.generalCacheForBRDEMI.get(input.getTerminalSerialNumber()+Constants.TCH_BRD_MANUFACTURE_DISC));
					else
						d.setPrintVal("");*/
					try{
					logger.info(" discalaimer :" + AbstractBrandEmiAdaptor.brandEmiFinalCache.get(input.getTerminalSerialNumber()).getManufatureDisclaimer());
					if(AbstractBrandEmiAdaptor.brandEmiFinalCache.get(input.getTerminalSerialNumber()).getManufatureDisclaimer() != null)
					{
						d.setPrintVal("----------------------------------------------");
						listdpo.add(d);
						d = new DataPrintObject();
						List<String> disc = HelperUtil.toGetByteString(AbstractBrandEmiAdaptor.brandEmiFinalCache.get(input.getTerminalSerialNumber()).getManufatureDisclaimer());
						repeatRestrict = true;
						d = new DataPrintObject();
						d.setPrintLabel("Manufacture Disclaimer :");
						d.setPrintVal("");
						listdpo.add(d);
						for(String dis : disc){
							d = new DataPrintObject();
							d.setPrintVal(dis);
							listdpo.add(d);
						}
					}
					else
						d.setPrintVal("");
					}catch(Exception e){
						logger.debug("Excpeto :" ,e);
					}
					break;
				case 18:
					try{
						logger.debug("disclaimer....");
						
						String cashBackPur = AbstractBrandEmiAdaptor.brandEmiFinalCache.get(input.getTerminalSerialNumber()).getTenureMap().get(tenureCode).getCashBackFinalPer();
						String cashBackAmt = AbstractBrandEmiAdaptor.brandEmiFinalCache.get(input.getTerminalSerialNumber()).getTenureMap().get(tenureCode).getCashBackFinalAmt();
						String  cashBackVeloDays = "0"; 
						if(AbstractBrandEmiAdaptor.brandEmiFinalCache.get(input.getTerminalSerialNumber()).getValidationParameter() != null ){
							cashBackVeloDays = AbstractBrandEmiAdaptor.brandEmiFinalCache.get(input.getTerminalSerialNumber()).getValidationParameter().getVelocityCount();
						}
						if(cashBackAmt != null && !"0.00".equals(cashBackAmt) && !"0".equals(cashBackAmt)){
							String message = AppConfigMaster.getConfigValue(Constants.TCH_DISCLAIMER_UTILITY, Constants.TCH_BRD_DESC);
							logger.debug("Message from DB :" +message);
							String[] value_split = null;
							if(message != null){
								value_split = message.split("\\|");
							}
							String finalMsg = null;
							logger.debug("cashBackPur : "+ cashBackPur);
							logger.debug("cashBackAmt :"+ cashBackAmt);
							logger.debug("cashBackVeloDays :"+ cashBackVeloDays);
							logger.debug("message :"+ message);
							if(value_split != null){
								if (null == cashBackPur){
									cashBackPur ="";
								}
								if (null == cashBackVeloDays){
									cashBackVeloDays ="";
								}
								d.setPrintVal("----------------------------------------------");
								listdpo.add(d);
								d = new DataPrintObject();
								StringBuilder strBuilder = new StringBuilder();
								strBuilder.append(value_split[0]);
								strBuilder.append(cashBackPur);
								strBuilder.append(value_split[1]);
								strBuilder.append(cashBackAmt);
								strBuilder.append(value_split[2]);
								strBuilder.append(cashBackVeloDays);
								strBuilder.append(value_split[3]);
								finalMsg = strBuilder.toString();
								logger.debug("Final message :" + finalMsg);
							}
							logger.debug("Final message :" + finalMsg);
							try{
								List<String> finalString = HelperUtil.toGetByteString(finalMsg);
								repeatRestrict = true;
								for(String msg : finalString){
									d = new DataPrintObject();
									d.setPrintVal(msg);
									listdpo.add(d);
								}
							}catch(Exception e){
								logger.debug("Exception in parsing data : " ,e);
							}
						}
					}catch(Exception e){
						logger.debug("Exception :",e);
					}
					break;
					
				default:
					break;
				}
				index++;
				if(!repeatRestrict)
					listdpo.add(d);
			}
			DataPrintObject[] darr = new DataPrintObject[listdpo.size()];
			input.setDataPrintObject(listdpo.toArray(darr));
			input.setEmiObject(null); // set null as value does not needed at the end
		}


		private static String getIssuerBankName(String issuerBank) {
			if (issuerBank != null){
				logger.debug("ISSUER BANKNAME "+issuerBank);
				for(String key : bankName.keySet()){
					if(issuerBank.toUpperCase().contains(key)){
						issuerBank = bankName.get(key);
						break;
					}
				}
			}
			return issuerBank;
		}


		
		public static void main(String[] args) {
			System.out.println(getIssuerBankName("SBI GE WL EMI"));
		}
		
		/**
		 * 
		 * @param paymentBean
		 * @return
		 */
		
		private static String getField63(Payment paymentBean){
			String currencyCode = paymentBean.getCurrencyCode() != null ? paymentBean.getCurrencyCode() : "356";
			String field63 = Tags.hardwareserialnumberTagLength + String.format("%015d", Integer.parseInt(paymentBean.getTempSerialNumber())) + Tags.currencyCodeTagLength + currencyCode;
			if(paymentBean.getTableId() != null && !paymentBean.getTableId().contains(Constants.TCH_T1)){
				if(paymentBean.getPinBypass() != null)
					field63 = field63 + Tags.upiIndicator + Tags.pinBypassFlag + Constants.TCH_1;
				else
					field63 = field63 + Tags.upiIndicator + Tags.pinBypassFlag + Constants.TCH_0;
			}
			
			if(paymentBean.getEmiIndicator()!= null && !paymentBean.getEmiIndicator().isEmpty()){
				logger.debug("Setting emi indicator : " + paymentBean.getEmiIndicator());
				field63 = field63 + Tags.emiPrefixTagLength + paymentBean.getEmiIndicator();
			}
			
			if (!Constants.INR_CURR_CODE.equals(currencyCode)){
				logger.info("DCC Field 4 :["+paymentBean.getDccAmount()+"]");
				logger.debug("original amount : " + paymentBean.getOriginalAmount());
				logger.debug("currency code : " + paymentBean.getCurrencyCode());
				logger.debug("Exchnage rate : " + paymentBean.getExchangeRate());
				logger.debug("Markup  : " + paymentBean.getMarkup());
				logger.debug("currency name : " + paymentBean.getCurrencyName());
				
				String exchangeRate =  new BigDecimal(paymentBean.getExchangeRate()).movePointRight(4).toPlainString();
				String markup =  new BigDecimal(paymentBean.getMarkup()).movePointRight(4).toPlainString();
				logger.debug("Exchange rate after converting" + exchangeRate);
				logger.debug("Markup rate after converting" + markup);
				StringBuilder field63Builder = new StringBuilder("")
				.append(Tags.hardwareserialnumberTagLength + String.format("%015d", Integer.parseInt(paymentBean.getTempSerialNumber())))
				.append(Tags.DCCtransactionIndicatorTagLength + "D")
				.append(Tags.currencyCodeTagLength + paymentBean.getCurrencyCode())
				.append(Tags.amountInLocalCurrencyTagLength + String.format("%012d", Long.parseLong(paymentBean.getOriginalAmount())))
				.append(Tags.localCurrencyCodeTagLength + String.format("%03d", 356))
				.append(Tags.exchangeRateTagLength + String.format("%08d", Integer.parseInt(exchangeRate)))
				.append(Tags.markupPercentTagLength +String.format("%04d", Integer.parseInt(markup)))
				//.append(Tags.DCCTransactioncurrencycodeTagLength + paymentBean.getCurrencyCode())
				.append(Tags.transactionCurrencyName + paymentBean.getCurrencyName())
				.append(Tags.numberOfDecimalForDccAmount + "2"); // number of decimal digit
				field63 += field63Builder.toString();
			}
			
			return field63;
		}
		
		/**
		 * 
		 * @param paymentBean
		 * @return
		 */
		private static String getField60(Payment paymentBean){
			String field60 = Tags.authorizationSourceTagLength+ "A" + Constants.TCH_0;
			return field60;
		}
		
		/**
		 * 
		 * @param paymentBean
		 * @param type
		 * @return
		 */
		private static String getField60(Payment paymentBean, String type){
			String field60 = Tags.authorizationSourceTagLength+ "A" + Constants.TCH_0 + Tags.originalAmountTagLength + String.format("%012d", Long.parseLong(paymentBean.getOriginalAmount()));
			//need to add for TIP
			//Tags.hardwareserialnumberTagLength + String.format("%015d", Integer.parseInt(paymentBean.getTempSerialNumber())) + Tags.originalAmountTagLength +  String.format("%012d", Long.valueOf(paymentBean.getOriginalAmount()))
			//need to add for void
			//Tags.originalAmountTagLength + String.format("%012d", Long.parseInt(paymentBean.getOriginalAmount()))
			return field60;
		}
		/**
		 * 
		 * @param paymentBean
		 * @return
		 */
		private static String getField62(Payment paymentBean){
			String field62 = Tags.invoiceNumberTagLength+paymentBean.getInvoiceNumber();
			return field62;
		}
		
		private static String addDecimalToAmt(String amount){
			if(amount != null){
				return (new BigDecimal(amount).movePointLeft(2)).toString();
			}
			return amount;
		}
		
		
		/**
		 * Set the values from field 63 based on the tag present in specification
		 * @param input
		 * 
		 */
		private static void getInfoFromTLV(final Payment input){
			
			String field63 = input.getField63();
			if(field63 != null && !field63.isEmpty() && field63.length() > 6) {
				String tag = field63.substring(0,4);
				int length = Integer.parseInt(field63.substring(4,6));
				String value = field63.substring(6,6+length);
				switch(tag){
				case "0035":
						input.setSchemaTransactionId(value);
					break;
				case "0067":
					List<DataPrintObject> listPrntObj = new ArrayList<DataPrintObject>();
					DataPrintObject prntObj = new DataPrintObject();  
					prntObj.setPrintLabel("SE NUMBER :");
					prntObj.setPrintVal(value);
					listPrntObj.add(prntObj);
					input.setDataPrintObject(listPrntObj.toArray(new DataPrintObject[listPrntObj.size()]));
					prntObj = null;
					break;
				case "0068":
					if(value != null){
						//BASED ON THE SCHEME ID SEND BY MAGNUS SET CARD LABEL
						if(input.getSchemaTransactionId() != null)
							input.setCardLabel(schema.get(input.getSchemaTransactionId().toUpperCase())); // changes by ashish for PCPOS requirement 17/01/2019
						
						if(value.equalsIgnoreCase("D")){
							input.setCardLabel(input.getCardLabel() + Constants.TCH_DOMESTIC);
							input.setInternationalFlag(Constants.TCH_DOMESTIC);
						} else if(value.equalsIgnoreCase("I")){
							input.setCardLabel(input.getCardLabel() + Constants.TCH_INTERNATIONAL);
							input.setInternationalFlag(Constants.TCH_INTERNATIONAL);
						}
					}
					break;
				default:
					break;
				}
				input.setField63(field63.substring(value.length()+6));
				getInfoFromTLV(input);
			}
		}
		
		/**
		 * Convert point values in without point values
		 * @param str
		 * @return
		 */
		private static String pointConverter(String str){
			if(str != null){
				return new BigDecimal(str).multiply(ONE_HUNDRED).toBigInteger().toString();
			}	else{
				return str;
			}
		}
}
