
package com.awl.tch.iso8583;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.awl.tch.bean.DataPrintObject;
import com.awl.tch.bean.ExtraObject;
import com.awl.tch.bean.Payment;
import com.awl.tch.constant.Constants;
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

public final class ISOPayment{
	
	private static final Logger logger  = LoggerFactory.getLogger(ISOPayment.class);
	
		/**Create iso message based on request type.
		 * set MTI and Header globally for ISO message.
		 * @param paymentBean
		 * @param requestType
		 * @return String ISO converted message in string format
		 */
		@SuppressWarnings("unused")
		public static String createISOMessage(Payment paymentBean, String requestType){
			
			IsoMessage iso = getMTI(requestType);
			logger.debug("TPDU from table :" + paymentBean.getTpdu());
			logger.debug("NII from table :" + paymentBean.getNii());
			logger.debug("TPDU : [" + "6001120000" +"]");
			iso.setIsoHeader(UtilityHelper.unHex("6001120000")); // commented as for PG interface
			//013749534F383538332D31393837000000010000000500
			//iso.setIsoHeader(UtilityHelper.unHex("49534F383538332D31393837000000010000000500"));
			iso.setBinary(true);
			iso.setCharacterEncoding("ISO-8859-1");
			
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
			default :
				break;
			}
			
			
			byte[] isoMsg = iso.writeData();
			ByteArrayOutputStream byteOuts = new ByteArrayOutputStream();
			
			try {
				iso.write(byteOuts, 2);
				isoMsg = byteOuts.toByteArray();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("Exception in Sale transaction :",e);
			}finally{	
				try {
					byteOuts.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block				
					e.printStackTrace();
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
			
			try{
				byte responseByteArray[] = responseData.getBytes("ISO-8859-1");
				String hexDumpResponse = UtilityHelper.byteArrayToHexString(responseByteArray);
				
				logger.info("Response packet :"+hexDumpResponse);
				final MessageFactory<IsoMessage> mfact = UtilityHelper.config("config.xml");
				
				byte binArray[]  = Arrays.copyOfRange(responseByteArray, 2, responseByteArray.length);
				
				mfact.setUseBinaryMessages(true);
				mfact.setUseBinaryBitmap(true);;
				//mfact.setCharacterEncoding("ISO-8859-1");
				
				IsoMessage m = mfact.parseMessage(binArray, 5) ;
			
				switch (requestType){
				case Constants.TCH_REQUEST_SALE: 
						parseIso200(m, paymentBean);
					break;
				case Constants.TCH_REQUEST_VOID:
						parseIso220(m, paymentBean);
					break;
				case Constants.TCH_REQUEST_REFUND:
						parseIso220(m, paymentBean);
					break;
				case Constants.TCH_REQUEST_REVERSAL:
						parseIso400(m, paymentBean);
					break;
				case Constants.TCH_REQUEST_PREAUTH:
					parseIso100(m, paymentBean);
					break;
				case Constants.TCH_REQUEST_PREAUTHCOMP:
					parseIso220(m, paymentBean);
					break;
				case Constants.TCH_REQUEST_SALECSHBK:
					parseIso200(m, paymentBean);
					break;
				case Constants.TCH_REQUEST_TIPADJUST:
					parseIso220(m, paymentBean);
					break;
				case Constants.TCH_BALANCE_ENQ:
					parseIso100(m, paymentBean);
					break;
				default :
				}
				
				
				
			} catch (IOException e) {
				logger.debug("Exception in parsing ISO :"+e.getMessage());
				e.printStackTrace();
			} catch (ParseException e) {
				logger.debug("Exception in parsing ISO :"+e.getMessage());
				e.printStackTrace();
			}
			
			
				
				//System.out.println("Response data in hex : "  + responseData);				
				
		}
		
		/**parse iso 210 message for sale transaction
		 * @param responseData
		 * @param paymentBean
		 */
		private static void parseIso200(IsoMessage m210, Payment paymentBean){
			
			//try{
				/*byte responseByteArray[] = responseData.getBytes("ISO-8859-1");
				String hexDumpResponse = UtilityHelper.byteArrayToHexString(responseByteArray);
				
				logger.info("Sale Response packet :"+hexDumpResponse);
				final MessageFactory<IsoMessage> mfact = UtilityHelper.config("config.xml");
				
				
				
				byte binArray[]  = Arrays.copyOfRange(responseByteArray, 2, responseByteArray.length);
				
				mfact.setUseBinaryMessages(true);
				mfact.setUseBinaryBitmap(true);;
				//mfact.setCharacterEncoding("ISO-8859-1");
				
				IsoMessage m210 = mfact.parseMessage(binArray, 5) ;*/
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
				if(m210.getField(13) != null)
					paymentBean.setDate(m210.getField(13).toString());
				if(m210.getField(12) != null)
					paymentBean.setTime(m210.getField(12).toString());
				if(m210.getField(63) != null){
					logger.debug("Get field 63 : " + m210.getField(63));
					if(Constants.TCH_EMI_SALE.equals(paymentBean.getDecisionFlag())){
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
						String field63 = m210.getField(63).toString();
						
						paymentBean.setSchemaTransactionId(field63.substring(1,16));
						paymentBean.getEmiObject().getTenureObject()[0].setEmiAmount(field63.substring(field63.indexOf('F'),field63.indexOf('F')+12));
						
						List<DataPrintObject> listdpo = new ArrayList<DataPrintObject>();
						List<String> listLabel = new ArrayList<String>();
						for(int i = 0; i < EMIMaster.getMap().size() ; i++){
							listLabel.add(EMIMaster.get(String.valueOf(i)));
						}
						
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
						listLabel.add("Product IMEI/Sr");//12
*/						
						int index = 0;
						for(String str : listLabel){
							DataPrintObject d = new DataPrintObject();
							d.setPrintLabel(str);
							
							switch(index){
							case 0:
								d.setPrintVal("NULL");
								break;
							case 1:
								d.setPrintVal(field63.substring(75,76).trim());
								break;
							case 2:
								d.setPrintVal(field63.substring(70,74).trim());
								break;
							case 3:
								d.setPrintVal(new BigDecimal(field63.substring(93,96).trim()).movePointLeft(2).toPlainString()); 
								break;
							case 4:
								d.setPrintVal(field63.substring(77,80).trim());
								break;
							case 5:
								d.setPrintVal(new BigDecimal(field63.substring(81,92).trim()).movePointLeft(2).toPlainString());
								break;
							case 6:
								d.setPrintVal(new BigDecimal(field63.substring(43,55).trim()).movePointLeft(2).toPlainString()); 
								break;
							case 7:
								d.setPrintVal(new BigDecimal(field63.substring(19,31).trim()).movePointLeft(2).toPlainString());
								break;
							case 8:
								d.setPrintVal(new BigDecimal(field63.substring(31,43).trim()).movePointLeft(2).toPlainString());
								break;
							case 9:
								d.setPrintVal("NULL");
								break;
							case 10:
								d.setPrintVal(field63.substring(107,124).trim());
								break;
							case 11:
								d.setPrintVal(paymentBean.getEmiObject().getPrductCode());
								break;
							case 12:
								d.setPrintVal(field63.substring(124,field63.length()).trim());
								break;
							case 13:
								d.setPrintVal(paymentBean.getEmiObject().getImieNumber());
								break;
							}
							index++;
							listdpo.add(d);
						}
						
						DataPrintObject[] darr = new DataPrintObject[listdpo.size()];
						paymentBean.setDataPrintObject(listdpo.toArray(darr));
						paymentBean.setEmiObject(null); // set null as value does not needed at the end
					}else
						paymentBean.setSchemaTransactionId(m210.getField(63).toString());
				}
				if(m210.getField(55) != null){
					String field55String = m210.getField(55).toString();
					logger.debug("Get field 55 : " + field55String);
					//paymentBean.setField55(field55String);
					paymentBean.setIssuerField55(field55String);
				}
				
			/*} catch (IOException e) {
				logger.debug("Exception in parsing ISO :"+e.getMessage());
				e.printStackTrace();
			} catch (ParseException e) {
				logger.debug("Exception in parsing ISO :"+e.getMessage());
				e.printStackTrace();
			}*/
		}
		
		private static void parseIso100(IsoMessage m110, Payment paymentBean){
			
			/*byte responseByteArray[] = responseData.getBytes("ISO-8859-1");
			String hexDumpResponse = UtilityHelper.byteArrayToHexString(responseByteArray);
			
			logger.info("Sale Response packet :"+hexDumpResponse);
			final MessageFactory<IsoMessage> mfact = UtilityHelper.config("config.xml");
			
			
			
			byte binArray[]  = Arrays.copyOfRange(responseByteArray, 2, responseByteArray.length);
			
			mfact.setUseBinaryMessages(true);
			mfact.setUseBinaryBitmap(true);;
			//mfact.setCharacterEncoding("ISO-8859-1");
			
			IsoMessage m110 = mfact.parseMessage(binArray, 5) ;*/
			//m110.setBinaryBitmap(true);
			logger.info("ISO RESPONSE FOR 0110");
			logger.info("Field 3 :["+m110.getField(3) +"]");
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
			if(m110.getField(13) != null)
				paymentBean.setDate(m110.getField(13).toString());
			if(m110.getField(12) != null)
				paymentBean.setTime(m110.getField(12).toString());
			if(m110.getField(63) != null){
				logger.debug("Get field 63 : " + m110.getField(63));
				paymentBean.setSchemaTransactionId(m110.getField(63).toString());
			}
			if(m110.getField(55) != null){
				String field55String = m110.getField(55).toString();
				logger.debug("Get field 55 : " + field55String);
				//paymentBean.setField55(field55String);
				paymentBean.setIssuerField55(field55String);
			}
		}
		
		/**parse iso message with MTI 0x410
		 * @param responseData
		 * @param paymentBean
		 */
		private static void parseIso400(IsoMessage m410, Payment paymentBean){
			
			/*try{
				byte responseByteArray[] = responseData.getBytes("ISO-8859-1");
				String hexDumpResponse = UtilityHelper.byteArrayToHexString(responseByteArray);
				
				logger.info("Reversal Response packet :"+hexDumpResponse);
				final MessageFactory<IsoMessage> mfact = UtilityHelper.config("config.xml");
				
				
				
				byte binArray[]  = Arrays.copyOfRange(responseByteArray, 2, responseByteArray.length);
				
				mfact.setUseBinaryMessages(true);
				mfact.setUseBinaryBitmap(true);;
				//mfact.setCharacterEncoding("ISO-8859-1");
				
				IsoMessage m410 = mfact.parseMessage(binArray, 5) ;*/
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
					paymentBean.setSchemaTransactionId(m410.getField(63).toString());
				}
				if(m410.getField(55) != null){
					logger.debug("Get field 55 : " + m410.getField(55));
					paymentBean.setField55(m410.getField(55).toString());
				}
				
			/*} catch (IOException e) {
				logger.debug("Exception in parsing ISO :"+e.getMessage());
				e.printStackTrace();
			} catch (ParseException e) {
				logger.debug("Exception in parsing ISO :"+e.getMessage());
				e.printStackTrace();
			}*/
		}
		
		
		/**
		 * parse iso message with MTI 0x230
		 * @param responseData
		 * @param paymentBean
		 */
		private static void parseIso220(IsoMessage m230, final Payment paymentBean){

			
			/*try{
				byte responseByteArray[] = responseData.getBytes("ISO-8859-1");
				String hexDumpResponse = UtilityHelper.byteArrayToHexString(responseByteArray);
				
				logger.info("Response packet :"+hexDumpResponse);
				final MessageFactory<IsoMessage> mfact = UtilityHelper.config("config.xml");
				
				byte binArray[]  = Arrays.copyOfRange(responseByteArray, 2, responseByteArray.length);
				
				mfact.setUseBinaryMessages(true);
				mfact.setUseBinaryBitmap(true);;
				//mfact.setCharacterEncoding("ISO-8859-1");
				
				IsoMessage m230 = mfact.parseMessage(binArray, 5) ;*/
				
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
				
				// setting the responses
				paymentBean.setMTI("220");
				paymentBean.setProcessingCode(m230.getField(3).toString());
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
					logger.debug("Extra object value");
					ExtraObject[] e = new ExtraObject[1];
					ExtraObject e1 = new ExtraObject();
					e1.setExtraDataName("SE number");
					e1.setExtraDataValue(m230.getField(63).toString());
					e[0] = e1;
					paymentBean.setExtraDataObject(e);
					paymentBean.setField63(m230.getField(63).toString());
					paymentBean.setSchemaTransactionId(m230.getField(63).toString());
				}
				
			/*} catch (IOException e) {
				logger.debug("Exception in parsing ISO :"+e.getMessage());
				e.printStackTrace();
			} catch (ParseException e) {
				logger.debug("Exception in parsing ISO :"+e.getMessage());
				e.printStackTrace();
			}*/
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
			else if(Constants.TCH_BALANCE_ENQ.equalsIgnoreCase(reqType))
				return msgFact.newMessage(0x100);
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
			
			logger.info("Field 3 :["+"00"+paymentBean.getAccountType() +"]");
			iso.setValue(3, paymentBean.getAccountType() , IsoType.NUMERIC, 6);
			logger.info("Field 4 :["+paymentBean.getOriginalAmount()+"]");
			iso.setValue(4, paymentBean.getOriginalAmount(), IsoType.NUMERIC, 12);
			logger.info("Field 11 :["+paymentBean.getStanNumber()+"]");
			iso.setValue(11, Integer.valueOf(paymentBean.getStanNumber()), IsoType.NUMERIC, 6); // stan number
			logger.info("Field 22 :["+ "0" +paymentBean.getCardEntryMode()+"]");
			iso.setValue(22, Integer.valueOf(paymentBean.getCardEntryMode()), IsoType.NUMERIC, 3); 
			logger.info("Field 24 :["+paymentBean.getNii().trim()+"]");
			iso.setValue(24, paymentBean.getNii().trim() , IsoType.NUMERIC, 3); // NII
			logger.info("Field 25 :[00]");
			iso.setValue(25, 00 , IsoType.NUMERIC, 2);// pos condition code
			if(paymentBean.getTrack2() != null){
				//if((paymentBean.getTrack2().length() % 2) == 0) 
					logger.info("Field 35 :["+paymentBean.getTrack2()+"]");
					iso.setValue(35,paymentBean.getTrack2(), new BcdEncodedField35(), IsoType.LLBIN, 37); // track 2 data
				//else
				//	iso.setValue(35,paymentBean.getTrack2() +"F", new BcdEncodedField35(), IsoType.LLBIN, 37); // track 2 data
			}else{
				logger.info("Field 2 :["+paymentBean.getPanNumber() +"]");
				logger.info("Field 14 :["+paymentBean.getExpiryDate()+"]");
				iso.setValue(2, paymentBean.getPanNumber() , new BCDencodeField60(), IsoType.LLBIN, 19); // pan number
				iso.setValue(14, paymentBean.getExpiryDate(), IsoType.NUMERIC, 4); // expiry date in YYMM
			}
			if(paymentBean.getPinBlock() != null){
				logger.info("Field 52 :["+paymentBean.getPinBlock()+"]");
				iso.setValue(52, paymentBean.getPinBlock(), IsoType.BINARY, 8);
			}	
			logger.info("Field 41 :["+paymentBean.getTerminalId()+"]");
			logger.info("Field 42 :["+paymentBean.getMerchantId()+"]");
			iso.setValue(41, paymentBean.getTerminalId(), IsoType.ALPHA, 8); // terminal id		
			iso.setValue(42, paymentBean.getMerchantId(), IsoType.ALPHA, 15); // acquiring id or merchant id
			if(paymentBean.getField55() != null && !paymentBean.getField55().isEmpty()){
				logger.info("Field 55 :["+paymentBean.getField55()+"]");
				iso.setValue(55, paymentBean.getField55(),new BCDEncodedField55() , IsoType.LLLBIN, 1);
			}
			logger.info("Field 60	 :["+"A"+"]");
			iso.setValue(60, "A0",new BCDencodeField60() , IsoType.LLLVAR, 1); // new
			logger.info("Field 62 :["+paymentBean.getInvoiceNumber()+"]");
			iso.setValue(62, paymentBean.getInvoiceNumber(),new BCDencodeField60(), IsoType.LLLVAR, 6); //  invoice number new
			
			
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
				String progCode = String.format("%06d",Integer.parseInt(paymentBean.getEmiObject().getProgramObject()[0].getProgramCode()));
				//String progCode = String.format("%06d",Integer.parseInt("123111"));
				String productCode = String.format("%1$-15s", paymentBean.getEmiObject().getPrductCode().trim());
				//String productCode = String.format("%1$-15s", "21312");
				String tenure = String.format("%02d", Integer.parseInt(paymentBean.getEmiObject().getTenureObject()[0].getTenure()));
				//String tenure = String.format("%02d", Integer.parseInt("3"));
				String imie = "";
				if(paymentBean.getEmiObject().getImieNumber() == null){
					imie = String.format("%015d", Integer.parseInt("0"));
				}else{
					
					imie = String.format("%015d", new BigInteger(paymentBean.getEmiObject().getImieNumber()));
				}
				paymentBean.setField63("F" +progCode + productCode + tenure + imie);
				logger.debug("Field 63 :[" + paymentBean.getField63() +"]");
				iso.setValue(63, paymentBean.getField63().trim(),new BCDencodeField60(), IsoType.LLLVAR, 6); //  field 63 as per specification
			}else if(Constants.TCH_DCC_SALE.equals(paymentBean.getDecisionFlag())){
				iso.setValue(2, paymentBean.getPanNumber() , new BCDencodeField60(), IsoType.LLBIN, 19); // pan number
				iso.setValue(14, paymentBean.getExpiryDate(), IsoType.NUMERIC, 4); // expiry date in YYMM
				StringBuilder field63 = new StringBuilder("")
				.append("D")
				.append(String.format("%012d", Integer.parseInt(paymentBean.getOriginalAmount())))
				.append(String.format("%03d", Integer.parseInt(paymentBean.getCurrencyCode())))
				.append(String.format("%08d", Integer.parseInt(paymentBean.getExchangeRate())))
				.append(String.format("%04d", Integer.parseInt(paymentBean.getMarkup())))
				.append("2") // number of decimal digit
				.append(paymentBean.getCurrencyName());
				//.append("01") // opt out void transaction
				
				logger.debug("Field 63 :[" + field63.toString()+"]");
				iso.setValue(63,field63.toString(), IsoType.LLLVAR, 6); //  field 63 as per specification
				
			}
			
			
			/*
			 * end of make field 63
			 */
			
			//iso.setValue(62, paymentBean.getInvoiceNumber() , IsoType.LLBIN, 6); //  invoice number
			
			
			/*iso.setValue(3, 000000, IsoType.NUMERIC, 6);
		//	if(paymentBean.getOriginalAmount() != null)
				iso.setValue(4, 111  , IsoType.AMOUNT, 12);
			
			iso.setValue(11, "647" , IsoType.NUMERIC, 6); // stan number
			iso.setValue(22, 51, IsoType.NUMERIC, 3); //pos entry mode //emv 51
			iso.setValue(24, 112 , IsoType.NUMERIC, 3); // NII
			iso.setValue(25, 00 , IsoType.NUMERIC, 2);// pos condition code  
			//iso.setValue(35,"6071690000000314=21025208180000000000F", new BcdEncodedField35() , IsoType.LLBIN,37); // track 2 data
			//if(!Constants.TCH_NULL.equals(paymentBean.getTrack2()))
				iso.setValue(35,"5413330057004112=2512201095461915F", new BcdEncodedField35() , IsoType.LLBIN,37); // track 2 data
			//else{
				//iso.setValue(2, "6071690000000314" , new BCDencodeField60(), IsoType.LLBIN, 19); // pan number
				//iso.setValue(14, "2102", IsoType.NUMERIC, 4); // expiry date in YYMM
			//}
			//iso.setField(35, new IsoValue<String>(IsoType.LLBIN,new String(buf)));
			
			iso.setValue(41, "00001287", IsoType.ALPHA, 8); // terminal id
			//iso.setValue(41, "39270585", IsoType.ALPHA, 8);
			iso.setValue(42, "075126027804067", IsoType.ALPHA, 15); // acquiring id or merchant id
			//iso.setValue(42, "037022000042128", IsoType.ALPHA, 15);
			// iso.setValue(52, "037022000042128", IsoType.ALPHA, 15);
			iso.setValue(55, "820258008400950502000080009A031609269C01005F2A0203565F3401019F02060000000078009F03060000000000009F090200129F10120110A5000F040000DAC000000000000000FF9F1A0203569F1E08534349464D5836359F26087AC6EBB7249557F09F2701809F3303E0F0C89F34034103029F3501229F3602007A9F37047626234C9F4104000000259F530152",new BCDEncodedField55() , IsoType.LLLBIN, 1);
			iso.setValue(60, "A"+"0",new BCDencodeField60() , IsoType.LLLVAR, 1);
			iso.setValue(62, "000007",new BCDencodeField60(), IsoType.LLLVAR, 6); //  invoice number
*/			
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
			logger.info("Field 22 :["+"0"+paymentBean.getCardEntryMode()+"]"); // pos entry mode
			logger.info("Field 24 :["+112+"]"); //nii
			logger.info("Field 25 :[00]");// pos condition code
			logger.info("Field 37 :["+paymentBean.getRetrivalRefNumber()+"]"); // rrn number
			logger.info("Field 38 :["+paymentBean.getAuthorizationId()+"]"); // auth id
			logger.info("Field 39 :["+paymentBean.getResponseCode()+"]"); // Response code
			logger.info("Field 41 :["+paymentBean.getTerminalId()+"]");
			logger.info("Field 42 :["+paymentBean.getMerchantId()+"]");
			logger.info("Field 54 :["+paymentBean.getAdditionalAmount()+"]");

			iso.setValue(2, paymentBean.getPanNumber() , new BCDencodeField60(), IsoType.LLBIN, 19); // pan number
			iso.setValue(3, "02"+paymentBean.getAccountType() , IsoType.NUMERIC, 6);
			// always be zero for void
			iso.setValue(4, paymentBean.getOriginalAmount(), IsoType.NUMERIC, 12); 
			iso.setValue(11, paymentBean.getStanNumber(), IsoType.NUMERIC, 6); // stan number
			iso.setValue(12, paymentBean.getTime(), IsoType.NUMERIC, 6); // time
			iso.setValue(13, UtilityHelper.dateInMMddYYYY().substring(0, 4), IsoType.NUMERIC, 4); // date
			iso.setValue(14, paymentBean.getExpiryDate(), IsoType.NUMERIC, 4); // expiry date in YYMM
			iso.setValue(22, Integer.valueOf(paymentBean.getCardEntryMode()), IsoType.NUMERIC, 3); // pos entry mode
			iso.setValue(24, 112 , IsoType.NUMERIC, 3); // NII
			iso.setValue(25, 00 , IsoType.NUMERIC, 2);// pos condition code
			iso.setValue(37, paymentBean.getRetrivalRefNumber() , IsoType.ALPHA, 12);// RRN
			iso.setValue(38, Integer.valueOf(paymentBean.getAuthorizationId()) , IsoType.ALPHA, 6);// Auth Code
			iso.setValue(39, paymentBean.getResponseCode() , IsoType.ALPHA, 2);// Response code
			iso.setValue(41, paymentBean.getTerminalId(), IsoType.ALPHA, 8); // terminal id
			iso.setValue(42, paymentBean.getMerchantId(), IsoType.ALPHA, 15); // acquiring id or merchant id
			//iso.setValue(54, paymentBean.getAdditionalAmount(), IsoType.ALPHA, 12);
			iso.setValue(54, String.format("%012d", Integer.parseInt(paymentBean.getAdditionalAmount())), new BCDencodeField60(), IsoType.LLLVAR, 6);
			
			if(paymentBean.getAdditionalAmount() != null){
				Long addAmount = Long.valueOf(paymentBean.getAdditionalAmount());
				Long orgPlusTipAmount = Long.valueOf(paymentBean.getOriginalAmount());
				Long origAmount = orgPlusTipAmount - addAmount;
				String field60 = String.format("%012d", origAmount);
				iso.setValue(60, field60, new BCDencodeField60(), IsoType.LLLVAR, 1); 	
				logger.info("Field 60 :["+field60+"]");
			}
			logger.info("Field 62 :["+paymentBean.getInvoiceNumber()+"]");
			iso.setValue(62, paymentBean.getInvoiceNumber(), new BCDencodeField60(), IsoType.LLLVAR, 6); //  invoice number
			
			/*iso.setValue(2, "DDA39E74833743E16228CEC918C78B33" , new BCDencodeField60(), IsoType.LLBIN, 19); // pan number
			iso.setValue(3, 020000 , IsoType.NUMERIC, 6);
			iso.setValue(4, 3896, IsoType.NUMERIC, 12);
			iso.setValue(11, 123, IsoType.NUMERIC, 6); // stan number
			iso.setValue(12, 151648, IsoType.NUMERIC, 6); // time
			iso.setValue(13, 0527, IsoType.NUMERIC, 4); // date
			iso.setValue(14, 5011, IsoType.NUMERIC, 4); // date expiry
			iso.setValue(22, 21, IsoType.NUMERIC, 3); // pos entry mode
			iso.setValue(24, 112 , IsoType.NUMERIC, 3); // NII
			iso.setValue(25, 00 , IsoType.NUMERIC, 2);// pos condition code
			iso.setValue(37, "631913120001" , IsoType.ALPHA, 12);// RRN
			iso.setValue(38, 231878 , IsoType.ALPHA, 6);// Auth Code
			iso.setValue(39, "00" , IsoType.ALPHA, 2);// Response code
			iso.setValue(41, 00001475, IsoType.ALPHA, 8); // terminal id
			iso.setValue(42, "075126027804096", IsoType.ALPHA, 15); // acquiring id or merchant id
			iso.setValue(54, String.format("%012d", Integer.parseInt("1151")), new BCDencodeField60(), IsoType.LLLVAR, 6);
			iso.setValue(60, "5465", new BCDencodeField60(), IsoType.LLBIN, 1); // auth source code
			iso.setValue(62, "000001",new BCDencodeField60(), IsoType.LLLVAR, 6); //  invoice number
*/				
			return iso;
		}
		private static IsoMessage createIso200SaleCashBack(final Payment paymentBean, final IsoMessage iso){
			
			logger.info("ISO REQUEST FOR 0200");
			logger.info("Field 3 :["+ "09" +paymentBean.getAccountType() +"]");
			logger.info("Field 4 :["+paymentBean.getOriginalAmount()+"]");
			logger.info("Field 11 :["+paymentBean.getStanNumber()+"]");
			logger.info("Field 22 :["+"0"+ paymentBean.getCardEntryMode()+"]");
			logger.info("Field 24 :["+paymentBean.getNii().trim()+"]");
			logger.info("Field 25 :[00]");
			
			
			logger.info("Field 54 :["+paymentBean.getAdditionalAmount()+"]");
			
			
			
			iso.setValue(3, "09"+ paymentBean.getAccountType() , IsoType.NUMERIC, 6);
			iso.setValue(4, paymentBean.getOriginalAmount(), IsoType.NUMERIC, 12);
			iso.setValue(11, Integer.valueOf(paymentBean.getStanNumber()), IsoType.NUMERIC, 6); // stan number
			iso.setValue(22, Integer.valueOf(paymentBean.getCardEntryMode()), IsoType.NUMERIC, 3); 
			iso.setValue(24, paymentBean.getNii().trim() , IsoType.NUMERIC, 3); // NII
			iso.setValue(25, 00 , IsoType.NUMERIC, 2);// pos condition code
			if(paymentBean.getTrack2() != null){
				logger.info("Field 35 :["+paymentBean.getTrack2()+"]");
				iso.setValue(35,paymentBean.getTrack2(), new BcdEncodedField35(), IsoType.LLBIN, 37); // track 2 data
			}else{
				logger.info("Field 2 :["+paymentBean.getPanNumber() +"]");
				logger.info("Field 14 :["+paymentBean.getExpiryDate()+"]");
				iso.setValue(2, paymentBean.getPanNumber() , new BCDencodeField60(), IsoType.LLBIN, 19); // pan number
				iso.setValue(14, paymentBean.getExpiryDate(), IsoType.NUMERIC, 4); // expiry date in YYMM
			}
			logger.info("Field 41 :["+paymentBean.getTerminalId()+"]");
			logger.info("Field 42 :["+paymentBean.getMerchantId()+"]");
			iso.setValue(41, paymentBean.getTerminalId(), IsoType.ALPHA, 8); // terminal id		
			iso.setValue(42, paymentBean.getMerchantId(), IsoType.ALPHA, 15); // acquiring id or merchant id
			//iso.setValue(60, "A"+"F",new BcdEncodedField35(), IsoType.LLBIN, 1); // auth source code
			if(!Constants.TCH_NULL.equals(paymentBean.getPinBlock())){
				logger.info("Field 52 :["+paymentBean.getPinBlock()+"]");
				iso.setValue(52, paymentBean.getPinBlock(), IsoType.BINARY, 8);
			}
			if(paymentBean.getField55() != null && !paymentBean.getField55().isEmpty()){
				logger.info("Field 55 :["+paymentBean.getField55()+"]");
				iso.setValue(55, paymentBean.getField55(),new BCDEncodedField55() , IsoType.LLLBIN, 1);
			}
			//iso.setValue(54, paymentBean.getAdditionalAmount(), IsoType.NUMERIC, 12);
			iso.setValue(54, String.format("%012d", Long.valueOf(paymentBean.getAdditionalAmount())),new BCDencodeField60(), IsoType.LLLVAR, 6);
			logger.info("Field 60	 :["+"A"+"]");
			logger.info("Field 62 :["+paymentBean.getInvoiceNumber()+"]");
			iso.setValue(60, "A",new BCDencodeField60() , IsoType.LLLVAR, 1); // new
			iso.setValue(62, paymentBean.getInvoiceNumber(),new BCDencodeField60(), IsoType.LLLVAR, 6); //  invoice number new
			//iso.setValue(62, paymentBean.getInvoiceNumber() , IsoType.LLBIN, 6); //  invoice number
			
			
			/*iso.setValue(3, 000000, IsoType.NUMERIC, 6);
		//	if(paymentBean.getOriginalAmount() != null)
				iso.setValue(4, 111  , IsoType.AMOUNT, 12);
			
			iso.setValue(11, "647" , IsoType.NUMERIC, 6); // stan number
			iso.setValue(22, 51, IsoType.NUMERIC, 3); //pos entry mode //emv 51
			iso.setValue(24, 112 , IsoType.NUMERIC, 3); // NII
			iso.setValue(25, 00 , IsoType.NUMERIC, 2);// pos condition code  
			//iso.setValue(35,"6071690000000314=21025208180000000000F", new BcdEncodedField35() , IsoType.LLBIN,37); // track 2 data
			//if(!Constants.TCH_NULL.equals(paymentBean.getTrack2()))
				iso.setValue(35,"5413330057004112=2512201095461915F", new BcdEncodedField35() , IsoType.LLBIN,37); // track 2 data
			//else{
				//iso.setValue(2, "6071690000000314" , new BCDencodeField60(), IsoType.LLBIN, 19); // pan number
				//iso.setValue(14, "2102", IsoType.NUMERIC, 4); // expiry date in YYMM
			//}
			//iso.setField(35, new IsoValue<String>(IsoType.LLBIN,new String(buf)));
			
			iso.setValue(41, "00001287", IsoType.ALPHA, 8); // terminal id
			//iso.setValue(41, "39270585", IsoType.ALPHA, 8);
			iso.setValue(42, "075126027804067", IsoType.ALPHA, 15); // acquiring id or merchant id
			//iso.setValue(42, "037022000042128", IsoType.ALPHA, 15);
			// iso.setValue(52, "037022000042128", IsoType.ALPHA, 15);
			iso.setValue(55, "820258008400950502000080009A031609269C01005F2A0203565F3401019F02060000000078009F03060000000000009F090200129F10120110A5000F040000DAC000000000000000FF9F1A0203569F1E08534349464D5836359F26087AC6EBB7249557F09F2701809F3303E0F0C89F34034103029F3501229F3602007A9F37047626234C9F4104000000259F530152",new BCDEncodedField55() , IsoType.LLLBIN, 1);
			iso.setValue(60, "A"+"0",new BCDencodeField60() , IsoType.LLLVAR, 1);
			iso.setValue(62, "000007",new BCDencodeField60(), IsoType.LLLVAR, 6); //  invoice number
*/			
			return iso;
		}
		
		private static IsoMessage createIso100BalEnq(final Payment paymentBean, final IsoMessage iso){
			
			logger.info("ISO REQUEST FOR 0100 balance enquiry");
			logger.info("Field 3 :["+"31"+paymentBean.getAccountType() +"]");
			logger.info("Field 11 :["+paymentBean.getStanNumber()+"]");
			logger.info("Field 14 :["+paymentBean.getExpiryDate()+"]");
			logger.info("Field 22 :["+"0"+ paymentBean.getCardEntryMode()+"]");
			logger.info("Field 24 :["+paymentBean.getNii().trim()+"]");
			logger.info("Field 25 :[00]");
			
			iso.setValue(3,"31"+ paymentBean.getAccountType() , IsoType.NUMERIC, 6);
			iso.setValue(11, Integer.valueOf(paymentBean.getStanNumber()), IsoType.NUMERIC, 6); // stan number
			iso.setValue(22, Integer.valueOf(paymentBean.getCardEntryMode()), IsoType.NUMERIC, 3); 
			iso.setValue(24, paymentBean.getNii().trim() , IsoType.NUMERIC, 3); // NII
			iso.setValue(25, 00 , IsoType.NUMERIC, 2);// pos condition code for Pre authorization
			if(paymentBean.getTrack2() != null){
				logger.info("Field 35 :["+paymentBean.getTrack2()+"]");
				iso.setValue(35,paymentBean.getTrack2(), new BcdEncodedField35(), IsoType.LLBIN, 37); // track 2 data
			}else{
				logger.info("Field 2 :["+paymentBean.getPanNumber() +"]");
				logger.info("Field 14 :["+paymentBean.getExpiryDate()+"]");
				iso.setValue(2, paymentBean.getPanNumber() , new BCDencodeField60(), IsoType.LLBIN, 19); // pan number
				iso.setValue(14, paymentBean.getExpiryDate(), IsoType.NUMERIC, 4); // expiry date in YYMM
			}
			if(!Constants.TCH_NULL.equals(paymentBean.getPinBlock())){
				logger.info("Field 52 :["+paymentBean.getPinBlock()+"]");
				iso.setValue(52, paymentBean.getPinBlock(), IsoType.BINARY, 8);
			}
			logger.info("Field 41 :["+paymentBean.getTerminalId()+"]");
			logger.info("Field 42 :["+paymentBean.getMerchantId()+"]");
			iso.setValue(41, paymentBean.getTerminalId(), IsoType.ALPHA, 8); // terminal id		
			iso.setValue(42, paymentBean.getMerchantId(), IsoType.ALPHA, 15); // acquiring id or merchant id
			//iso.setValue(60, "A"+"F",new BcdEncodedField35(), IsoType.LLBIN, 1); // auth source code
			if(paymentBean.getField55() != null && !paymentBean.getField55().isEmpty()){
				iso.setValue(55, paymentBean.getField55(),new BCDEncodedField55() , IsoType.LLLBIN, 1);
				logger.info("Field 55 :["+paymentBean.getField55()+"]");
			}
			logger.info("Field 60	 :["+"A"+"]");
			logger.info("Field 62 :["+paymentBean.getInvoiceNumber()+"]");
			iso.setValue(60, "A"+ "0",new BCDencodeField60() , IsoType.LLLVAR, 1); // new
			iso.setValue(62, paymentBean.getInvoiceNumber(),new BCDencodeField60(), IsoType.LLLVAR, 6); //  invoice number new
			//iso.setValue(62, paymentBean.getInvoiceNumber() , IsoType.LLBIN, 6); //  invoice number
			
			
			/*iso.setValue(3, 000000, IsoType.NUMERIC, 6);
		//	if(paymentBean.getOriginalAmount() != null)
				iso.setValue(4, 111  , IsoType.AMOUNT, 12);
			
			iso.setValue(11, "647" , IsoType.NUMERIC, 6); // stan number
			iso.setValue(22, 51, IsoType.NUMERIC, 3); //pos entry mode //emv 51
			iso.setValue(24, 112 , IsoType.NUMERIC, 3); // NII
			iso.setValue(25, 00 , IsoType.NUMERIC, 2);// pos condition code  
			//iso.setValue(35,"6071690000000314=21025208180000000000F", new BcdEncodedField35() , IsoType.LLBIN,37); // track 2 data
			//if(!Constants.TCH_NULL.equals(paymentBean.getTrack2()))
				iso.setValue(35,"5413330057004112=2512201095461915F", new BcdEncodedField35() , IsoType.LLBIN,37); // track 2 data
			//else{
				//iso.setValue(2, "6071690000000314" , new BCDencodeField60(), IsoType.LLBIN, 19); // pan number
				//iso.setValue(14, "2102", IsoType.NUMERIC, 4); // expiry date in YYMM
			//}
			//iso.setField(35, new IsoValue<String>(IsoType.LLBIN,new String(buf)));
			
			iso.setValue(41, "00001287", IsoType.ALPHA, 8); // terminal id
			//iso.setValue(41, "39270585", IsoType.ALPHA, 8);
			iso.setValue(42, "075126027804067", IsoType.ALPHA, 15); // acquiring id or merchant id
			//iso.setValue(42, "037022000042128", IsoType.ALPHA, 15);
			// iso.setValue(52, "037022000042128", IsoType.ALPHA, 15);
			iso.setValue(55, "820258008400950502000080009A031609269C01005F2A0203565F3401019F02060000000078009F03060000000000009F090200129F10120110A5000F040000DAC000000000000000FF9F1A0203569F1E08534349464D5836359F26087AC6EBB7249557F09F2701809F3303E0F0C89F34034103029F3501229F3602007A9F37047626234C9F4104000000259F530152",new BCDEncodedField55() , IsoType.LLLBIN, 1);
			iso.setValue(60, "A"+"0",new BCDencodeField60() , IsoType.LLLVAR, 1);
			iso.setValue(62, "000007",new BCDencodeField60(), IsoType.LLLVAR, 6); //  invoice number
*/			
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

			
			logger.info("ISO REQUEST FOR 0100");
			logger.info("Field 3 :["+paymentBean.getAccountType() +"]");
			logger.info("Field 4 :["+paymentBean.getOriginalAmount()+"]");
			logger.info("Field 11 :["+paymentBean.getStanNumber()+"]");
			logger.info("Field 14 :["+paymentBean.getExpiryDate()+"]");
			logger.info("Field 22 :["+"0"+ paymentBean.getCardEntryMode()+"]");
			logger.info("Field 24 :["+paymentBean.getNii().trim()+"]");
			logger.info("Field 25 :[06]");
			
			
			
			
			
			iso.setValue(3, paymentBean.getAccountType() , IsoType.NUMERIC, 6);
			iso.setValue(4, paymentBean.getOriginalAmount(), IsoType.NUMERIC, 12);
			iso.setValue(11, Integer.valueOf(paymentBean.getStanNumber()), IsoType.NUMERIC, 6); // stan number
			iso.setValue(22, Integer.valueOf(paymentBean.getCardEntryMode()), IsoType.NUMERIC, 3); 
			iso.setValue(24, paymentBean.getNii().trim() , IsoType.NUMERIC, 3); // NII
			iso.setValue(25, 06 , IsoType.NUMERIC, 2);// pos condition code for Pre authorization
			if(paymentBean.getTrack2() != null){
				logger.info("Field 35 :["+paymentBean.getTrack2()+"]");
				iso.setValue(35,paymentBean.getTrack2(), new BcdEncodedField35(), IsoType.LLBIN, 37); // track 2 data
			}else{
				logger.info("Field 2 :["+paymentBean.getPanNumber() +"]");
				logger.info("Field 14 :["+paymentBean.getExpiryDate()+"]");
				iso.setValue(2, paymentBean.getPanNumber() , new BCDencodeField60(), IsoType.LLBIN, 19); // pan number
				iso.setValue(14, paymentBean.getExpiryDate(), IsoType.NUMERIC, 4); // expiry date in YYMM
			}
			if(!Constants.TCH_NULL.equals(paymentBean.getPinBlock())){
				logger.info("Field 52 :["+paymentBean.getPinBlock()+"]");
				iso.setValue(52, paymentBean.getPinBlock(), IsoType.BINARY, 8);
			}
			logger.info("Field 41 :["+paymentBean.getTerminalId()+"]");
			logger.info("Field 42 :["+paymentBean.getMerchantId()+"]");
			iso.setValue(41, paymentBean.getTerminalId(), IsoType.ALPHA, 8); // terminal id		
			iso.setValue(42, paymentBean.getMerchantId(), IsoType.ALPHA, 15); // acquiring id or merchant id
			//iso.setValue(60, "A"+"F",new BcdEncodedField35(), IsoType.LLBIN, 1); // auth source code
			if(paymentBean.getField55() != null && !paymentBean.getField55().isEmpty()){
				iso.setValue(55, paymentBean.getField55(),new BCDEncodedField55() , IsoType.LLLBIN, 1);
				logger.info("Field 55 :["+paymentBean.getField55()+"]");
			}
			logger.info("Field 60	 :["+"A"+"]");
			logger.info("Field 62 :["+paymentBean.getInvoiceNumber()+"]");
			iso.setValue(60, "A"+ "0",new BCDencodeField60() , IsoType.LLLVAR, 1); // new
			iso.setValue(62, paymentBean.getInvoiceNumber(),new BCDencodeField60(), IsoType.LLLVAR, 6); //  invoice number new
			//iso.setValue(62, paymentBean.getInvoiceNumber() , IsoType.LLBIN, 6); //  invoice number
			
			
			/*iso.setValue(3, 000000, IsoType.NUMERIC, 6);
		//	if(paymentBean.getOriginalAmount() != null)
				iso.setValue(4, 111  , IsoType.AMOUNT, 12);
			
			iso.setValue(11, "647" , IsoType.NUMERIC, 6); // stan number
			iso.setValue(22, 51, IsoType.NUMERIC, 3); //pos entry mode //emv 51
			iso.setValue(24, 112 , IsoType.NUMERIC, 3); // NII
			iso.setValue(25, 00 , IsoType.NUMERIC, 2);// pos condition code  
			//iso.setValue(35,"6071690000000314=21025208180000000000F", new BcdEncodedField35() , IsoType.LLBIN,37); // track 2 data
			//if(!Constants.TCH_NULL.equals(paymentBean.getTrack2()))
				iso.setValue(35,"5413330057004112=2512201095461915F", new BcdEncodedField35() , IsoType.LLBIN,37); // track 2 data
			//else{
				//iso.setValue(2, "6071690000000314" , new BCDencodeField60(), IsoType.LLBIN, 19); // pan number
				//iso.setValue(14, "2102", IsoType.NUMERIC, 4); // expiry date in YYMM
			//}
			//iso.setField(35, new IsoValue<String>(IsoType.LLBIN,new String(buf)));
			
			iso.setValue(41, "00001287", IsoType.ALPHA, 8); // terminal id
			//iso.setValue(41, "39270585", IsoType.ALPHA, 8);
			iso.setValue(42, "075126027804067", IsoType.ALPHA, 15); // acquiring id or merchant id
			//iso.setValue(42, "037022000042128", IsoType.ALPHA, 15);
			// iso.setValue(52, "037022000042128", IsoType.ALPHA, 15);
			iso.setValue(55, "820258008400950502000080009A031609269C01005F2A0203565F3401019F02060000000078009F03060000000000009F090200129F10120110A5000F040000DAC000000000000000FF9F1A0203569F1E08534349464D5836359F26087AC6EBB7249557F09F2701809F3303E0F0C89F34034103029F3501229F3602007A9F37047626234C9F4104000000259F530152",new BCDEncodedField55() , IsoType.LLLBIN, 1);
			iso.setValue(60, "A"+"0",new BCDencodeField60() , IsoType.LLLVAR, 1);
			iso.setValue(62, "000007",new BCDencodeField60(), IsoType.LLLVAR, 6); //  invoice number
*/			
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
			logger.info("Field 3 :["+"02"+paymentBean.getAccountType()+"]"); // processing code
			logger.info("Field 4 :["+0 +"]"); // amount 
			logger.info("Field 11 :["+paymentBean.getStanNumber()+"]"); // stan number
			logger.info("Field 12 :["+paymentBean.getTime()+"]"); // time
			logger.info("Field 13 :["+UtilityHelper.dateInMMddYYYY().substring(0, 4)+"]"); // date
			logger.info("Field 14 :["+paymentBean.getExpiryDate()+"]"); // expiry date
			logger.info("Field 22 :["+"0"+paymentBean.getCardEntryMode()+"]"); // pos entry mode
			logger.info("Field 24 :["+112+"]"); //nii
			logger.info("Field 25 :[00]");// pos condition code
			logger.info("Field 35 :["+paymentBean.getTrack2()+"]");// track2
			logger.info("Field 37 :["+paymentBean.getRetrivalRefNumber()+"]"); // rrn number
			logger.info("Field 38 :["+paymentBean.getAuthorizationId()+"]"); // auth id
			logger.info("Field 39 :["+paymentBean.getResponseCode()+"]"); // Response code
			logger.info("Field 41 :["+paymentBean.getTerminalId()+"]");
			logger.info("Field 42 :["+paymentBean.getMerchantId()+"]");
			logger.info("Field 60 :["+paymentBean.getOriginalAmount()+"]");
			logger.info("Field 62 :["+paymentBean.getInvoiceNumber()+"]");
			
			
			iso.setValue(2, paymentBean.getPanNumber() , new BCDencodeField60(), IsoType.LLBIN, 19); // pan number
			iso.setValue(3, "02"+paymentBean.getAccountType() , IsoType.NUMERIC, 6);
			// always be zero for void
			iso.setValue(4, 0, IsoType.NUMERIC, 12); 
			iso.setValue(11, paymentBean.getStanNumber(), IsoType.NUMERIC, 6); // stan number
			iso.setValue(12, paymentBean.getTime(), IsoType.NUMERIC, 6); // time
			iso.setValue(13, UtilityHelper.dateInMMddYYYY().substring(0, 4), IsoType.NUMERIC, 4); // date
			iso.setValue(14, paymentBean.getExpiryDate(), IsoType.NUMERIC, 4); // date expiry
			iso.setValue(22, Integer.valueOf(paymentBean.getCardEntryMode()), IsoType.NUMERIC, 3); // pos entry mode
			iso.setValue(24, 112 , IsoType.NUMERIC, 3); // NII
			iso.setValue(25, 00 , IsoType.NUMERIC, 2);// pos condition code
			iso.setValue(37, paymentBean.getRetrivalRefNumber() , IsoType.ALPHA, 12);// RRN
			iso.setValue(38, Integer.valueOf(paymentBean.getAuthorizationId()) , IsoType.ALPHA, 6);// Auth Code
			iso.setValue(39, paymentBean.getResponseCode() , IsoType.ALPHA, 2);// Response code
			iso.setValue(41, paymentBean.getTerminalId(), IsoType.ALPHA, 8); // terminal id
			iso.setValue(42, paymentBean.getMerchantId(), IsoType.ALPHA, 15); // acquiring id or merchant id
			iso.setValue(60, String.format("%012d", Long.valueOf(paymentBean.getOriginalAmount())), new BCDencodeField60(), IsoType.LLLVAR, 1); 
			iso.setValue(62, paymentBean.getInvoiceNumber(), new BCDencodeField60(), IsoType.LLLVAR, 6); //  invoice number
			
			
			
			
			/*iso.setValue(2, "6071690000000314" , new BCDencodeField60(), IsoType.LLBIN, 19); // pan number
			iso.setValue(3, 000000 , IsoType.NUMERIC, 6);
			iso.setValue(4, 0, IsoType.NUMERIC, 12);
			iso.setValue(11, 123, IsoType.NUMERIC, 6); // stan number
			iso.setValue(12, 151648, IsoType.NUMERIC, 6); // time
			iso.setValue(13, 0527, IsoType.NUMERIC, 4); // date
			iso.setValue(14, 2212, IsoType.NUMERIC, 4); // date expiry
			iso.setValue(22, 21, IsoType.NUMERIC, 3); // pos entry mode
			iso.setValue(24, 112 , IsoType.NUMERIC, 3); // NII
			iso.setValue(25, 00 , IsoType.NUMERIC, 2);// pos condition code
			iso.setValue(37, "625612380001" , IsoType.ALPHA, 12);// RRN
			iso.setValue(38, 211321 , IsoType.ALPHA, 6);// Auth Code
			iso.setValue(39, "00" , IsoType.ALPHA, 2);// Response code
			iso.setValue(41, 98000001, IsoType.ALPHA, 8); // terminal id
			iso.setValue(42, "000310003101004", IsoType.ALPHA, 15); // acquiring id or merchant id
			//iso.setValue(60, paymentBean.getOriginalAmount(), new BCDencodeField60(), IsoType.LLBIN, 1); // auth source code
			iso.setValue(62, "000001",new BCDencodeField60(), IsoType.LLLVAR, 6); //  invoice number
*/				
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
			logger.info("Field 4 :["+paymentBean.getOriginalAmount() +"]"); // amount 
			logger.info("Field 11 :["+paymentBean.getStanNumber()+"]"); // stan number
			logger.info("Field 12 :["+paymentBean.getTime()+"]"); // time
			logger.info("Field 13 :["+UtilityHelper.dateInMMddYYYY().substring(0, 4)+"]"); // date
			logger.info("Field 14 :["+paymentBean.getExpiryDate()+"]"); // expiry date
			logger.info("Field 22 :["+"0"+paymentBean.getCardEntryMode()+"]"); // pos entry mode
			logger.info("Field 24 :["+112+"]"); //nii
			logger.info("Field 25 :[00]");// pos condition code
			
			
			
			
			logger.info("Field 41 :["+paymentBean.getTerminalId()+"]");
			logger.info("Field 42 :["+paymentBean.getMerchantId()+"]");
				
			
			
			
			iso.setValue(2, paymentBean.getPanNumber() , new BCDencodeField60(), IsoType.LLBIN, 19); // pan number
			iso.setValue(3, "20"+ paymentBean.getAccountType(), IsoType.NUMERIC, 6);
			iso.setValue(4, paymentBean.getOriginalAmount(), IsoType.NUMERIC, 12); 
				
			iso.setValue(11, paymentBean.getStanNumber(), IsoType.NUMERIC, 6); // stan number
			iso.setValue(12, paymentBean.getTime(), IsoType.NUMERIC, 6); // time
			iso.setValue(13, UtilityHelper.dateInMMddYYYY().substring(0, 4), IsoType.NUMERIC, 4); // date
			iso.setValue(14, paymentBean.getExpiryDate(), IsoType.NUMERIC, 4); // date expiry
			iso.setValue(22, Integer.valueOf(paymentBean.getCardEntryMode()), IsoType.NUMERIC, 3); // pos entry mode
			iso.setValue(24, 112 , IsoType.NUMERIC, 3); // NII
			iso.setValue(25, 00 , IsoType.NUMERIC, 2);// pos condition code
			if(paymentBean.getTrack2() != null){
				logger.info("Field 35 :["+paymentBean.getTrack2()+"]");// track2
				iso.setValue(35,paymentBean.getTrack2(), new BcdEncodedField35(), IsoType.LLBIN, 37); // track 2 data
			}else{
				iso.setValue(2, paymentBean.getPanNumber() , new BCDencodeField60(), IsoType.LLBIN, 19); // pan number
				iso.setValue(14, paymentBean.getExpiryDate(), IsoType.NUMERIC, 4); // expiry date in YYMM
			}
			if(!Constants.TCH_NULL.equals(paymentBean.getRetrivalRefNumber())){
				logger.info("Field 37 :["+paymentBean.getRetrivalRefNumber()+"]"); // rrn number
				iso.setValue(37, paymentBean.getRetrivalRefNumber() , IsoType.ALPHA, 12);// RRN
			}
			logger.info("Field 38 :["+0+"]");
			iso.setValue(38, 0 , IsoType.ALPHA, 6);// Auth Code
			if(paymentBean.getResponseCode() != null){
				logger.info("Field 39 :["+paymentBean.getResponseCode()+"]"); // Response code
				iso.setValue(39, paymentBean.getResponseCode() , IsoType.ALPHA, 2);// Response code
			}
			iso.setValue(41, paymentBean.getTerminalId(), IsoType.ALPHA, 8); // terminal id
			iso.setValue(42, paymentBean.getMerchantId(), IsoType.ALPHA, 15); // acquiring id or merchant id
			if(paymentBean.getField55() != null){
				logger.info("Field 55 :["+paymentBean.getField55()+"]");
				iso.setValue(55, paymentBean.getField55(),new BCDEncodedField55() , IsoType.LLLBIN, 1);
			}
			logger.info("Field 62 :["+paymentBean.getInvoiceNumber()+"]");
			iso.setValue(62, paymentBean.getInvoiceNumber(), new BCDencodeField60(), IsoType.LLLVAR, 6); //  invoice number
			
			/*iso.setValue(2, "6071690000000314" , new BCDencodeField60(), IsoType.LLBIN, 19); // pan number
			iso.setValue(3, 000000 , IsoType.NUMERIC, 6);
			iso.setValue(4, 0, IsoType.NUMERIC, 12);
			iso.setValue(11, 123, IsoType.NUMERIC, 6); // stan number
			iso.setValue(12, 151648, IsoType.NUMERIC, 6); // time
			iso.setValue(13, 0527, IsoType.NUMERIC, 4); // date
			iso.setValue(14, 2212, IsoType.NUMERIC, 4); // date expiry
			iso.setValue(22, 21, IsoType.NUMERIC, 3); // pos entry mode
			iso.setValue(24, 112 , IsoType.NUMERIC, 3); // NII
			iso.setValue(25, 00 , IsoType.NUMERIC, 2);// pos condition code
			iso.setValue(37, "625612380001" , IsoType.ALPHA, 12);// RRN
			iso.setValue(38, 211321 , IsoType.ALPHA, 6);// Auth Code
			iso.setValue(39, "00" , IsoType.ALPHA, 2);// Response code
			iso.setValue(41, 98000001, IsoType.ALPHA, 8); // terminal id
			iso.setValue(42, "000310003101004", IsoType.ALPHA, 15); // acquiring id or merchant id
			//iso.setValue(60, paymentBean.getOriginalAmount(), new BCDencodeField60(), IsoType.LLBIN, 1); // auth source code
			iso.setValue(62, "000001",new BCDencodeField60(), IsoType.LLLVAR, 6); //  invoice number
*/				
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
			logger.info("Field 22 :["+"0"+paymentBean.getCardEntryMode()+"]"); // pos entry mode
			logger.info("Field 24 :["+112+"]"); //nii
			logger.info("Field 25 :[06]");// pos condition code
			logger.info("Field 35 :["+paymentBean.getTrack2()+"]");// track2
			logger.info("Field 41 :["+paymentBean.getTerminalId()+"]");
			logger.info("Field 42 :["+paymentBean.getMerchantId()+"]");
			
			iso.setValue(3, "00"+ paymentBean.getAccountType(), IsoType.NUMERIC, 6);
			iso.setValue(4, paymentBean.getOriginalAmount(), IsoType.NUMERIC, 12); 
				
			iso.setValue(11, paymentBean.getStanNumber(), IsoType.NUMERIC, 6); // stan number
			iso.setValue(12, paymentBean.getTime(), IsoType.NUMERIC, 6); // time
			iso.setValue(13, UtilityHelper.dateInMMddYYYY().substring(0, 4), IsoType.NUMERIC, 4); // date
			iso.setValue(22, Integer.valueOf(paymentBean.getCardEntryMode()), IsoType.NUMERIC, 3); // pos entry mode
			iso.setValue(24, 112 , IsoType.NUMERIC, 3); // NII
			iso.setValue(25, 06 , IsoType.NUMERIC, 2);// pos condition code
			if(paymentBean.getTrack2()!=null)
				//if((paymentBean.getTrack2().length() % 2) == 0) {
					iso.setValue(35,paymentBean.getTrack2(), new BcdEncodedField35(), IsoType.LLBIN, 37); // track 2 data
				//}else
					//iso.setValue(35,paymentBean.getTrack2() +"F", new BcdEncodedField35(), IsoType.LLBIN, 37); // track 2 data
			else{
				iso.setValue(2, paymentBean.getPanNumber() , new BCDencodeField60(), IsoType.LLBIN, 19); // pan number
				iso.setValue(14, paymentBean.getExpiryDate(), IsoType.NUMERIC, 4); // expiry date in YYMM
			}
			if(!Constants.TCH_NULL.equals(paymentBean.getRetrivalRefNumber())){
				logger.info("Field 37 :["+paymentBean.getRetrivalRefNumber()+"]"); // rrn number
				iso.setValue(37, paymentBean.getRetrivalRefNumber() , IsoType.ALPHA, 12);// RR
			}
			logger.info("Field 38 :["+0+"]"); // auth id
			iso.setValue(38, 0 , IsoType.ALPHA, 6);// Auth Code
			//iso.setValue(39, paymentBean.getResponseCode() , IsoType.ALPHA, 2);// Response code
			iso.setValue(41, paymentBean.getTerminalId(), IsoType.ALPHA, 8); // terminal id
			iso.setValue(42, paymentBean.getMerchantId(), IsoType.ALPHA, 15); // acquiring id or merchant id
			if(paymentBean.getField55() != null){
				logger.info("Field 55 :["+paymentBean.getField55()+"]");
				iso.setValue(55, paymentBean.getField55(),new BCDEncodedField55() , IsoType.LLLBIN, 1);
			}
			logger.info("Field 62 :["+paymentBean.getInvoiceNumber()+"]");
			iso.setValue(62, paymentBean.getInvoiceNumber(), new BCDencodeField60(), IsoType.LLLVAR, 6); //  invoice number
			
			/*iso.setValue(2, "6071690000000314" , new BCDencodeField60(), IsoType.LLBIN, 19); // pan number
			iso.setValue(3, 000000 , IsoType.NUMERIC, 6);
			iso.setValue(4, 0, IsoType.NUMERIC, 12);
			iso.setValue(11, 123, IsoType.NUMERIC, 6); // stan number
			iso.setValue(12, 151648, IsoType.NUMERIC, 6); // time
			iso.setValue(13, 0527, IsoType.NUMERIC, 4); // date
			iso.setValue(14, 2212, IsoType.NUMERIC, 4); // date expiry
			iso.setValue(22, 21, IsoType.NUMERIC, 3); // pos entry mode
			iso.setValue(24, 112 , IsoType.NUMERIC, 3); // NII
			iso.setValue(25, 00 , IsoType.NUMERIC, 2);// pos condition code
			iso.setValue(37, "625612380001" , IsoType.ALPHA, 12);// RRN
			iso.setValue(38, 211321 , IsoType.ALPHA, 6);// Auth Code
			iso.setValue(39, "00" , IsoType.ALPHA, 2);// Response code
			iso.setValue(41, 98000001, IsoType.ALPHA, 8); // terminal id
			iso.setValue(42, "000310003101004", IsoType.ALPHA, 15); // acquiring id or merchant id
			//iso.setValue(60, paymentBean.getOriginalAmount(), new BCDencodeField60(), IsoType.LLBIN, 1); // auth source code
			iso.setValue(62, "000001",new BCDencodeField60(), IsoType.LLLVAR, 6); //  invoice number
*/				
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
			logger.info("Field 2 :["+paymentBean.getPanNumber() +"]"); //  pan number
			logger.info("Field 3 :["+paymentBean.getAccountType()+"]"); // processing code
			logger.info("Field 4 :["+00 +"]"); // amount 
			logger.info("Field 11 :["+paymentBean.getStanNumber()+"]"); // stan number
			logger.info("Field 14 :["+paymentBean.getExpiryDate()+"]"); // expiry date
			logger.info("Field 22 :["+"0"+paymentBean.getCardEntryMode()+"]"); // pos entry mode
			logger.info("Field 24 :["+112+"]"); //nii
			logger.info("Field 25 :[00]");// pos condition code
			logger.info("Field 41 :["+paymentBean.getTerminalId()+"]");
			logger.info("Field 42 :["+paymentBean.getMerchantId()+"]");
			logger.info("Field 42 :["+paymentBean.getMerchantId()+"]");
			logger.info("Field 54 :["+paymentBean.getAdditionalAmount()+"]");
			logger.info("Field 62 :["+paymentBean.getInvoiceNumber()+"]");
			
			
			iso.setValue(2, paymentBean.getPanNumber() , new BCDencodeField60(), IsoType.LLBIN, 19); // pan number
			iso.setValue(3,  paymentBean.getAccountType() , IsoType.NUMERIC, 6);
			iso.setValue(4, 00, IsoType.NUMERIC, 12);
			iso.setValue(11, paymentBean.getStanNumber(), IsoType.NUMERIC, 6); // stan number
			iso.setValue(14, paymentBean.getExpiryDate(), IsoType.NUMERIC, 4); // date expiry
			iso.setValue(22, Integer.valueOf(paymentBean.getCardEntryMode()), IsoType.NUMERIC, 3); // pos entry mode
			iso.setValue(24, paymentBean.getNii() , IsoType.NUMERIC, 3); // NII
			iso.setValue(25, 00 , IsoType.NUMERIC, 2);// pos condition code
			iso.setValue(41, paymentBean.getTerminalId(), IsoType.ALPHA, 8); // terminal id
			iso.setValue(42, paymentBean.getMerchantId(), IsoType.ALPHA, 15); // acquiring id or merchant id
			iso.setValue(54, paymentBean.getAdditionalAmount(), IsoType.NUMERIC, 12);
			if(paymentBean.getField55() != null){
				logger.info("Field 55 :["+paymentBean.getField55()+"]");
				iso.setValue(55, paymentBean.getField55(),new BCDEncodedField55() , IsoType.LLLBIN, 1);
			}
			iso.setValue(62, paymentBean.getInvoiceNumber(), new BCDencodeField60(), IsoType.LLLVAR, 6); //  invoice number
			
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
				String progCode = String.format("%06d",Integer.parseInt(paymentBean.getEmiObject().getProgramObject()[0].getProgramCode()));
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
				StringBuilder field63 = new StringBuilder("")
				.append("D")
				.append(String.format("%012d", Integer.parseInt(paymentBean.getOriginalAmount())))
				.append(String.format("%03d", Integer.parseInt(paymentBean.getCurrencyCode())))
				.append(String.format("%08d", Integer.parseInt(paymentBean.getExchangeRate())))
				.append(String.format("%04d", Integer.parseInt(paymentBean.getMarkup())))
				.append("2") // number of decimal digit
				.append(paymentBean.getCurrencyName());
				//.append("01") // opt out void transaction
				
				logger.debug("Field 63 :[" + field63.toString()+"]");
				iso.setValue(63,field63.toString(), IsoType.LLLVAR, 6); //  field 63 as per specification
				
			}
			
			
			/*iso.setValue(2, "6071690000000314" , new BCDencodeField60(), IsoType.LLBIN, 19); // pan number
			iso.setValue(3, 000000 , IsoType.NUMERIC, 6);
			iso.setValue(4, 0, IsoType.NUMERIC, 12);
			iso.setValue(11, 123, IsoType.NUMERIC, 6); // stan number
			iso.setValue(12, 151648, IsoType.NUMERIC, 6); // time
			iso.setValue(13, 0527, IsoType.NUMERIC, 4); // date
			iso.setValue(14, 2212, IsoType.NUMERIC, 4); // date expiry
			iso.setValue(22, 21, IsoType.NUMERIC, 3); // pos entry mode
			iso.setValue(24, 112 , IsoType.NUMERIC, 3); // NII
			iso.setValue(25, 00 , IsoType.NUMERIC, 2);// pos condition code
			iso.setValue(37, "625612380001" , IsoType.ALPHA, 12);// RRN
			iso.setValue(38, 211321 , IsoType.ALPHA, 6);// Auth Code
			iso.setValue(39, 00 , IsoType.ALPHA, 2);// Response code
			iso.setValue(41, 98000001, IsoType.ALPHA, 8); // terminal id
			iso.setValue(42, "000310003101004", IsoType.ALPHA, 15); // acquiring id or merchant id
			//iso.setValue(60, paymentBean.getOriginalAmount(), new BCDencodeField60(), IsoType.LLBIN, 1); // auth source code
			iso.setValue(62, "000001",new BCDencodeField60(), IsoType.LLLVAR, 6); //  invoice number
*/				
			return iso;
		}
		
		/**
		 * Convert hex string to byte array
		 * @param s <tt>Hex</tt> format string
		 * @return <tt>Byte[]</tt> <p> byte array of respective hex string
		 */
		public static byte[] hexStringToByteArray(String s) {
		    int len = s.length();
		    byte[] data = new byte[len / 2];
		    for (int i = 0; i < len; i += 2) {
		        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
		                             + Character.digit(s.charAt(i+1), 16));
		    }
		    return data;
		}
		
		
		
		
}
