package com.awl.tch.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import com.awl.tch.bean.DataPrintObject;
import com.awl.tch.bean.Payment;
import com.awl.tch.bean.SummaryReport;
import com.awl.tch.bean.TerminalParameter;
import com.awl.tch.constant.Constants;
import com.awl.tch.constant.ErrorConstants;
import com.awl.tch.exceptions.TCHQueryException;
import com.awl.tch.model.TerminalHardwareParameterDTO;
import com.awl.tch.model.UtilityDTO;
import com.awl.tch.server.TcpServer;
import com.awl.tch.util.ErrorMaster;
import com.awl.tch.util.Property;
import com.awl.tch.util.TerminalParameterCacheMaster;
import com.awl.tch.util.UtilityHelper;
/**
 * Consists of all useful methods which are going to use through out the application
 * @author ashish.bhavsar
 *
 * @param <T>
 */
public abstract class AbstractGenericDao<T> implements GenericDao<T>{
	public static final BigDecimal ONE_HUNDRED_AB = new BigDecimal(100);
	private static final Logger logger  = LoggerFactory.getLogger(AbstractGenericDao.class);
	public static final ConcurrentHashMap<String, LinkedHashSet<String>> midTidsFrDetail = new ConcurrentHashMap<String, LinkedHashSet<String>>();
	public static final ConcurrentHashMap<String, LinkedHashSet<String>> midTidsFrBatch = new ConcurrentHashMap<String, LinkedHashSet<String>>();
	public static final ConcurrentHashMap<String, LinkedHashSet<String>> midTidsFrSummaryBatch = new ConcurrentHashMap<String, LinkedHashSet<String>>();
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcCall simpleJdbcCall;
	
    @Autowired
    public void setDataSource(DataSource dataSource) {
       this.simpleJdbcCall = new SimpleJdbcCall(dataSource);
    }
    
	public SimpleJdbcCall getSimpleJdbcCall() {
		return simpleJdbcCall;
	}


	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public String getISOHeader(String terminalSerialNumber) throws TCHQueryException{
		
		String sql = "select distinct(b.tpdu) from tc_tid_hwsr_mapping a, tc_term_parameter b where a.mid = b.mid and a.tid = b.tid and a.hwsrno= ?";
		Integer tpdu  = null;
		
		logger.debug("Terminal serial number :" + terminalSerialNumber);
		if(Property.isShowSql()){
			logger.debug("SQL for getting tpdu :[" + sql +"]");
		}
		
		try {
			tpdu = jdbcTemplate.queryForObject(sql, Integer.class, new Object[]{terminalSerialNumber});
		}catch(DataAccessException dae){
			logger.debug("Exception while fetching data :" + dae
					.getMessage());
			throw new TCHQueryException(ErrorConstants.TCH_S009, ErrorMaster.get(ErrorConstants.TCH_S009));
		}catch (Exception e){
			logger.debug("Exception while fetching data :" + e.getMessage());
			throw new TCHQueryException(ErrorConstants.TCH_S009, ErrorMaster.get(ErrorConstants.TCH_S009));
		}
		
		if(tpdu != null)
			return tpdu.toString();
		else 
			throw new TCHQueryException("Require tpdu is not present for  termminal serial number : "  + terminalSerialNumber);
	}
	
	public TerminalHardwareParameterDTO getPCRFlag(String terminalSerialNumber) throws TCHQueryException{
		
		String sql = "select a.OPENPCRFLAG,a.ISACTIVE from tc_tid_hwsr_mapping a, TC_ME_PARAMETER b where a.mid = b.mid and a.hwsrno= ? and a.status = 'A'";
		
		
		if(Property.isShowSql())
			logger.debug("Query for getting pcr flag value :" + sql + "[" + terminalSerialNumber +"]");
		
		
		List<Map<String,Object>> rows = getJdbcTemplate().queryForList(sql, terminalSerialNumber);
		TerminalHardwareParameterDTO t = null;
		if(rows	!= null){
			for(Map<String,Object> row : rows){
				t = new TerminalHardwareParameterDTO();
				
				t.setIsActive((String)row.get("ISACTIVE"));
				t.setOpenPcrFlag((String)row.get("OPENPCRFLAG"));
			}
		}else{
			throw new TCHQueryException(ErrorConstants.TCH_S009, ErrorMaster.get(ErrorConstants.TCH_S009));
		}
		
		/*try {
			openPCR = jdbcTemplate.queryForObject(sql, String.class, new Object[]{terminalSerialNumber});
		}catch(DataAccessException dae){
			dae.getStackTrace();
			logger.debug("Exception in fetching data : " +dae.getMessage());
			throw new TCHQueryException(ErrorConstants.TCH_S009, ErrorMaster.get(ErrorConstants.TCH_S009));
		}catch (Exception e){
			logger.debug("Exception while fetching data :" + e.getMessage());
			throw new TCHQueryException(ErrorConstants.TCH_S009, ErrorMaster.get(ErrorConstants.TCH_S009));
		}*/
		
		return t;
	}
	
	
	

	
	public String checkSU99(String terminalSerialNumber) throws TCHQueryException{

		
		String sql = "select H_FIRST_TXN_CHK from TCH_HANDSHAKES WHERE H_TERMINAL_SERIAL_NUMBER = ?";
		String message  = "";
		
		
		if(Property.isShowSql())
			logger.debug("Query :" + sql + "[" + terminalSerialNumber +"]");
		
		try {
			message = jdbcTemplate.queryForObject(sql, String.class, new Object[]{terminalSerialNumber});
		}catch(DataAccessException dae){
			dae.getStackTrace();
			logger.debug("Exception in fetching data : " +dae.getMessage());
			throw new TCHQueryException(ErrorConstants.TCH_S009, ErrorMaster.get(ErrorConstants.TCH_S009));
		}catch (Exception e){
			logger.debug("Exception while fetching data :" + e.getMessage());
			throw new TCHQueryException(ErrorConstants.TCH_S009, ErrorMaster.get(ErrorConstants.TCH_S009));
		}
		
		return message;
	
	}
	
	// update is Active flag in hwsrnomapping table
	public void updateHwsrNo(String terminalSerialNumber){
		logger.debug("Updating terminal active status." );
		String sql = "update TC_TID_HWSR_MAPPING set ISACTIVE ='Y' where HWSRNO = ?";
		if(Property.isShowSql())
			logger.debug("Query :" + sql + "[" + terminalSerialNumber +"]");
		try {
			jdbcTemplate.update(sql, new Object[]{terminalSerialNumber});
		}catch(DataAccessException dae){
			dae.getStackTrace();
			logger.debug("Exception while updating data in TC_TID_HWSR_MAPPING : " +dae.getMessage());
		}catch (Exception e){
			logger.debug("Exception while updating data in TC_TID_HWSR_MAPPING :" + e.getMessage());
		}
		
	
	}
	
	public Map<String,String> getEMIDCCFlag(String terminalSerialNumber) throws TCHQueryException{
		
		String sql = "select a.EMIFLAG,a.DCCFALG,a.PROMPT_N_PRCD from tc_tid_hwsr_mapping a where a.hwsrno= ?";
		
		
		Map<String,String> tempMap = new HashMap<String, String>();
		if(Property.isShowSql())
			logger.debug("Query for getting emi dcc flag value :" + sql + "[" + terminalSerialNumber +"]");
		
		try {
			List<Map<String, Object>> map  = jdbcTemplate.queryForList(sql,terminalSerialNumber);
			for(Map<String, Object> m : map){
				tempMap.put(Constants.TCH_EMI_FLAG, (String) m.get("EMIFLAG"));
				tempMap.put(Constants.TCH_DCC_FLAG, (String) m.get("DCCFALG"));
				tempMap.put(Constants.TCH_PROMT_N_PROC, (String) m.get("PROMPT_N_PRCD"));
			}
		}catch(DataAccessException dae){
			dae.getStackTrace();
			logger.debug("Exception in fetching data : " +dae.getMessage());
			throw new TCHQueryException(ErrorConstants.TCH_S009, ErrorMaster.get(ErrorConstants.TCH_S009));
		}catch (Exception e){
			logger.debug("Exception while fetching data :" + e.getMessage());
			throw new TCHQueryException(ErrorConstants.TCH_S009, ErrorMaster.get(ErrorConstants.TCH_S009));
		}
		
		return tempMap;
	}
	
	public void getTerminalDetails(final Payment input) throws TCHQueryException{
		
		//String query = "select th.H_CARD_ACQUIRING_ID, th.H_TID from TCH_HANDSHAKES th where th.H_TERMINAL_SERIAL_NUMBER = ? ";
		String query = "select th.H_CARD_ACQUIRING_ID, th.H_TID,thm.BANKCODE from TCH_HANDSHAKES th JOIN tc_tid_hwsr_mapping THM ON THM.HWSRNO  = th.H_TERMINAL_SERIAL_NUMBER where th.H_TERMINAL_SERIAL_NUMBER = ?";
		
		
		if(Property.isShowSql()){
			logger.debug("Query : [" + query + "] : ["+input.getTerminalSerialNumber().trim()+"]");
		}
		try{
		List<Map<String,Object>> listTerminal = getJdbcTemplate().queryForList(query,new Object[]{input.getTerminalSerialNumber().trim()});
		//Object(query,,new RowMapper<HandshakeDTO>(){
			//@Override
			/*public HandshakeDTO mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				HandshakeDTO hsk = new HandshakeDTO();
				hsk.setCardAcquiringId(rs.getString("H_CARD_ACQUIRING_ID"));
				hsk.setTid(rs.getString("H_TID"));
				return hsk;
			}
		});*/
		if(listTerminal != null){
			for(Map<String,Object> row : listTerminal){
				input.setMerchantId((String) row.get("H_CARD_ACQUIRING_ID"));
				input.setTerminalId((String) row.get("H_TID"));
				input.setBankCode((String) row.get("BANKCODE"));
			}
		}else{
			logger.debug("Exception in fetching terminal parameters");
			throw new TCHQueryException(ErrorConstants.TCH_S009,ErrorMaster.get(ErrorConstants.TCH_S999));
		}
		logger.debug("MID :" +input.getMerchantId());
		logger.debug("TID :" +input.getTerminalId());
		logger.debug("BANKCode :" + input.getBankCode());
		}catch(Exception e){
			logger.debug("Exception in fetching MID and TID :" + e.getMessage());
			throw new TCHQueryException(ErrorConstants.TCH_S009,ErrorMaster.get(ErrorConstants.TCH_S999));
		}
	}
	
	
	public String getBatchNumberBasedOnTerminalSerialNumber(String terminalSerialNumber) throws TCHQueryException{
		String batchNumber = "";
		
		String sql = "SELECT DISTINCT(TPT.P_BATCH_NUMBER) AS BATCH_NUMBER FROM TCH_PAYMENT_TXN TPT WHERE TPT.P_TERMINAL_SERIAL_NUMBER = ? AND TPT.P_SETTLEMENT_FLAG='N'";
		if(Property.isShowSql())
			logger.debug("Query for getting pcr flag value :" + sql + "[" + terminalSerialNumber +"]");
		
		try {
			batchNumber = jdbcTemplate.queryForObject(sql, String.class, new Object[]{terminalSerialNumber});
			if(batchNumber == null){
				batchNumber = "00001";
			}else{
				batchNumber = String.format("%06d", Integer.parseInt(batchNumber));
			}
		}catch(DataAccessException dae){
			dae.getStackTrace();
			logger.debug("Exception in fetching data : " +dae.getMessage());
			throw new TCHQueryException(ErrorConstants.TCH_S009, ErrorMaster.get(ErrorConstants.TCH_S009));
		}catch (Exception e){
			logger.debug("Exception while fetching data :" + e.getMessage());
			throw new TCHQueryException(ErrorConstants.TCH_S009, ErrorMaster.get(ErrorConstants.TCH_S009));
		}
		return batchNumber;
	}
	
	/**
	 * Get the mid and tid based on terminal serial number from current payment transaction
	 * @param input
	 * @return 
	 * @throws TCHQueryException
	 */
	public static String[] getMidTids(String termSerialNumber, String hostId) throws TCHQueryException {
		
		TerminalParameter terminalParameter = TerminalParameterCacheMaster.terminalParameterCache.get(termSerialNumber);
		String arr[] = new String[2];
		if(terminalParameter != null){
			if (hostId == null) {
				hostId = Constants.TCH_CR_DB;
			}
			switch(hostId){
			case Constants.TCH_AMEX :
				arr = new String[]{terminalParameter.getAmexMid(),terminalParameter.getAmexTid()};
				break;
			case Constants.TCH_CR_DB:
			case Constants.TCH_WALLET:
				arr = new String[]{terminalParameter.getNormalMid(),terminalParameter.getNormalTid()};
				break;
			case Constants.TCH_DCC :
				arr = new String[]{terminalParameter.getDccMid(),terminalParameter.getDccTid()};
				break;
			}
		}
		return arr;
	}
	
	public void getMidTidsFrBatchSummary(String terminalSerialNumber) throws TCHQueryException {
		String sql = "SELECT DISTINCT CASE WHEN P_TRANSACTION_ID = 'A' THEN P_TRANSACTION_ID "
				+"WHEN P_TRANSACTION_ID NOT IN ('A') THEN 'Z' WHEN P_TRANSACTION_ID IS NULL THEN 'Z' END AS AUTHSOURCE ,"
				+ "P_MERCHANTID,P_TERMINALID,P_BATCH_NUMBER,P_TRANSACTION_ID FROM TCH_PAYMENT_TXN "
				+ "AND TTP.P_TERMINALID = TH.H_TID AND TTP.P_CLIENTID = TH.H_CLIENT_ID "
				+ "WHERE TTP.P_RESPONSE_CODE='00' AND TTP.P_BATCH_NUMBER = O_BATCH_NUMBER "
				+ "AND TTP.P_TERMINALID = I_TID AND TTP.P_TERMINAL_SERIAL_NUMBER = I_TERMINAL_SERIAL_NUMBER ORDER BY P_TRANSACTION_ID DESC";
		
		if(Property.isShowSql()){
			logger.info(sql + "["+terminalSerialNumber +"]");
		}
		if(midTidsFrBatch.get(terminalSerialNumber) == null){
			List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql,terminalSerialNumber);
			if(rows != null && rows.size() != 0){
				LinkedHashSet<String> listMidTid = new LinkedHashSet<String>();
				for(Map<String,Object> map : rows){
					String midTid = "";
					midTid = ((String)  map.get("P_MERCHANTID")) +","+ ((String) map.get("P_TERMINALID")) + "," +((String) map.get("AUTHSOURCE"));
					listMidTid.add(midTid);
				}
				midTidsFrBatch.put(terminalSerialNumber, listMidTid);
			}else{
				throw new TCHQueryException(ErrorConstants.TCH_D402, ErrorMaster.get(ErrorConstants.TCH_D402));
			}
		}
	}
	
	/**
	 * Set start date and end date of transaction for the current batch
	 * @param input
	 * @param startDate
	 * @param endDate
	 */
	public void setStartAndEndDate(final SummaryReport input,String startDate, String endDate){
		try{
			List<DataPrintObject> listdataObject = new ArrayList<DataPrintObject>();
			DataPrintObject b1 = new DataPrintObject();
			b1.setPrintLabel(Constants.TCH_BATCH_START);
			b1.setPrintVal(UtilityHelper.timestampInddMMyyyy_HHmmss(startDate));
			listdataObject.add(b1);
			DataPrintObject b2 = new DataPrintObject();
			b2.setPrintLabel(Constants.TCH_BATCH_END);
			b2.setPrintVal(UtilityHelper.timestampInddMMyyyy_HHmmss(endDate));
			listdataObject.add(b2);
			input.setDataPrintObj(listdataObject.toArray(new DataPrintObject[listdataObject.size()]));
		}catch(Exception e){
			logger.debug("Exception in parsing :" , e);
		}
	}
	
	/**
	 * Set decision flag as per amex transaction present or not
	 * @param input
	 */
	public void setDecisionFlagForSummaryReport(final SummaryReport input){
		if(TerminalParameterCacheMaster.terminalParameterCache.get(input.getTerminalSerialNumber()) != null){
			if(TerminalParameterCacheMaster.terminalParameterCache.get(input.getTerminalSerialNumber()).isAmexFlag() && input.getHostId() == null){			// check condition for amex 
				input.setHostId(Constants.TCH_AMEX);
				input.setDecisionFlag(Constants.TCH_SUMMARY_REPORT);
			}else if(TerminalParameterCacheMaster.terminalParameterCache.get(input.getTerminalSerialNumber()).isDccFlag() 
					&& (input.getHostId().equals(Constants.TCH_AMEX) || input.getHostId() == null)){ // check condition for dcc
				input.setHostId(Constants.TCH_DCC);
				input.setDecisionFlag(Constants.TCH_END);
			}else{
				input.setDecisionFlag(null);
			}
		}else{
			input.setDecisionFlag(null);
		}
	}

	/**
	 * Get version number based on the terminal serial number
	 * @param termNumber
	 * @return
	 */
	public String getVersionNumber(String termNumber){
		String query  = "SELECT DISTINCT(H_EDC_APP_VERSION) FROM TCH_HANDSHAKES WHERE H_TERMINAL_SERIAL_NUMBER = ?";
		if(Property.isShowSql()){
			logger.info(query + "["+termNumber +"]");
		}
		String message = getJdbcTemplate().queryForObject(query, String.class, new Object[]{termNumber});
		logger.debug("Version number used :[" + message + "]");
		return message;
	}
	
	
	public boolean isNewVersionPresent(String termSerialNumber){
		String versionNumber = getVersionNumber(termSerialNumber);
		if(versionNumber != null){
			try {
				if(UtilityHelper.isVersionGreaterThan14Nov(versionNumber)){
					logger.debug("Older version before 141117");
					return true;
				}
			} catch (ParseException e) {
				logger.debug("Exception inparsing",e);
			}
		}else{
			logger.debug("Version number absent");
		}
		return false;
	}
	
	
	public UtilityDTO getUtilityDetailInfo(String appName) throws TCHQueryException{
		logger.debug("Inside dao impl enquiry");
		
		String sql = "SELECT * FROM TCH_UTILITY_INFO WHERE U_APP_NAME='"+appName+"'";
		
		if(Property.isShowSql()){
			logger.debug("Fetching utility info sql ["+sql+"]");
		}
		List<Map<String,Object>> rows = getJdbcTemplate().queryForList(sql);
		UtilityDTO u = null;
		if(rows	!= null){
			for(Map<String,Object> row : rows){
				u = new UtilityDTO();
				u.setAppName((String) row.get("U_APP_NAME"));
				u.setIpAddress((String) row.get("U_IP_ADDRESS"));
				u.setPortNumber((String) row.get("U_PORT_NUMBER"));
				u.setUrl((String) row.get("U_URL"));
				u.setPrintLabels((String) row.get("U_PRINT_LABELS"));
			}
		}else{
			throw new TCHQueryException(ErrorConstants.TCH_U001,ErrorMaster.get(ErrorConstants.TCH_U001));
		}
		logger.debug("Exiting dao impl enquiry");
		return u;
	}
	
	
	/**
	 * Apply convenience fee value for GBSS
	 * @param input
	 * @param feeCursor
	 * @return payment object
	 */
	public static Payment applyCovenienceFee(final Payment input,List<HashMap<String,Object>> feeCursor){
		
		logger.debug("Card type : " + input.getCardTypeValue() + " | " + "BinType :" + input.getBinType());
		
		if(feeCursor != null){
			logger.debug("setting fee values");
			String per;
			String fixed;
			String maxAmt;
			String minAmt;
			String mid;
			String calMethod;
			String cardType;
			String binType;
			String cgstTax;
			String igstTax;
			String onusFlag;
			BigInteger finalamount;
			input.setFeeFlag("C"); // default setting fee type as C - convenience fee
			for(Map<String, Object> row : feeCursor){
				per = (String)row.get("F_PER");
				fixed = (String)row.get("F_FIXED");
				maxAmt = (String)row.get("F_MAX_AMT");
				minAmt = (String)row.get("F_MIN_AMT");
				mid = (String)row.get("F_MID");
				calMethod = (String)row.get("F_CAL_METHOD");
				cardType=(String)row.get("F_CARD_TYPE");
				onusFlag = (String)row.get("F_ONUS");
				binType = (String)row.get("F_BINTYPE");
				cgstTax = (String)row.get("F_CTAX1");
				igstTax = (String)row.get("F_ITAX1");
				
				//calculation of convenience fee
				if(Integer.parseInt(maxAmt) >= Integer.parseInt(input.getOriginalAmount()) && Integer.parseInt(minAmt) <= Integer.parseInt(input.getOriginalAmount())
						&& cardType.equalsIgnoreCase(input.getCardTypeValue()) && binType.equalsIgnoreCase(input.getBinType())){
					logger.debug("maxAmt :" + maxAmt);
					logger.debug("minAmt :" + minAmt);
					logger.debug("Original AMt :" + input.getOriginalAmount());
					logger.debug("calMethod :" + calMethod);
					logger.debug("cardType :" + cardType);
					logger.debug("binType :" + binType); 
					logger.debug("Percetage value :" + per);
					logger.debug("Fixed value :" + fixed);
					try{
						if(Constants.TCH_P.equals(calMethod)){
							BigDecimal p = new BigDecimal(per);
							BigDecimal perAmount = new BigDecimal(input.getOriginalAmount()).multiply(p).divide(ONE_HUNDRED_AB).setScale(0,RoundingMode.CEILING);
							Map<String,BigDecimal> taxValues = applyTax(perAmount,cgstTax,igstTax);
							BigDecimal cgstValue =null;
							BigDecimal igstValue  = null;
							if(taxValues != null){
								cgstValue  = taxValues.get(Constants.TCH_CGST).stripTrailingZeros().setScale(0,RoundingMode.HALF_UP);
								igstValue  = taxValues.get(Constants.TCH_IGST).stripTrailingZeros().setScale(0,RoundingMode.HALF_DOWN);
								input.setCgsttax(cgstValue.toPlainString());
								input.setIgsttax(igstValue.toPlainString());
								logger.debug("Tax percentage cgstTax:" +cgstTax + "| igstTax : "+igstTax);
								logger.debug("Tax percentage value cgstTax :"+input.getCgsttax());
								logger.debug("Tax percentage value igstTax :"+input.getIgsttax());
							}
							input.setFeePer(p.toPlainString());
							input.setFeeValue(perAmount.toPlainString());
							if(cgstValue != null && igstValue != null){
								finalamount = perAmount.add(new BigDecimal(input.getOriginalAmount())).add(cgstValue).add(igstValue).toBigInteger();
							}else{
								finalamount = perAmount.add(new BigDecimal(input.getOriginalAmount())).toBigInteger();
							}
							logger.debug("Total :"+finalamount);
							input.setFixed(null);
							input.setTotalValue(finalamount.toString());
							input.setCgstPer(cgstTax);
							input.setSgstPer(igstTax);
						}else if(Constants.TCH_F.equals(calMethod)){
							BigDecimal p = new BigDecimal(fixed); // convenience fee amount
							Map<String,BigDecimal> taxValues = applyTax(p,cgstTax,igstTax);
							BigDecimal cgstValue =null;
							BigDecimal igstValue  = null;
							if(taxValues != null){
								cgstValue = taxValues.get(Constants.TCH_CGST).stripTrailingZeros().setScale(0,RoundingMode.HALF_UP);
								igstValue = taxValues.get(Constants.TCH_IGST).stripTrailingZeros().setScale(0,RoundingMode.HALF_DOWN);
								input.setCgsttax(cgstValue.toPlainString());
								input.setIgsttax(igstValue.toPlainString());
								logger.debug("Tax percentage cgstTax:" +cgstTax + "| igstTax : "+igstTax);
								logger.debug("Tax percentage value cgstTax :"+input.getCgsttax());
								logger.debug("Tax percentage value igstTax :"+input.getIgsttax());
							}
							//BigInteger perAmount = new BigDecimal(input.getOriginalAmount()).multiply(p).divide(ONE_HUNDRED).toBigInteger();
							input.setFeeValue(p.toPlainString());
							if(cgstValue != null && igstValue != null){
							finalamount = p.add(new BigDecimal(input.getOriginalAmount())).add(cgstValue).add(igstValue).toBigInteger();
							}else{
								finalamount = p.add(new BigDecimal(input.getOriginalAmount())).toBigInteger();
							}
							input.setFeePer(null);
							logger.debug("setting fixed value" + p.toPlainString());
							input.setFixed(p.toPlainString());
							logger.debug("setting fixed value" + input.getFixed());
							input.setTotalValue(finalamount.toString());
							input.setCgstPer(cgstTax);
							input.setSgstPer(igstTax);
						}else if(Constants.TCH_B.equals(calMethod)){
							BigDecimal p = new BigDecimal(per);
							BigDecimal perAmount = new BigDecimal(input.getOriginalAmount()).multiply(p).divide(ONE_HUNDRED_AB);
							Map<String,BigDecimal> taxValues = applyTax(perAmount,cgstTax,igstTax);
							input.setCgsttax(taxValues.get(Constants.TCH_CGST).toPlainString());
							input.setIgsttax(taxValues.get(Constants.TCH_IGST).toPlainString());
							finalamount = perAmount.add(new BigDecimal(fixed)).add(new BigDecimal(input.getOriginalAmount())).toBigInteger();
							input.setTotalValue(finalamount.toString());
						}
					}catch(Exception e){
						logger.debug("Exception occured ,",e);
					}
				}
			}
		}
		return input;
	}
	
	/**
	 * Calculate gst tax value
	 * @param feeAmt
	 * @param cgstTax
	 * @param igstTax
	 * @return
	 */
	public static Map<String,BigDecimal> applyTax(BigDecimal feeAmt, String cgstTax, String igstTax){
		logger.debug("IGTAX :" + igstTax);
		logger.debug("cgstTax :" + cgstTax);
		Map<String,BigDecimal> taxMap = null;
		if(cgstTax != null && !cgstTax.equals("0")	&& igstTax != null && !igstTax.equals("0")){
			taxMap = new HashMap<String, BigDecimal>();
			logger.debug("Fee amount after calculation :" + feeAmt);
			BigDecimal igstTaxValue = feeAmt.multiply(new BigDecimal(igstTax)).divide(ONE_HUNDRED_AB);
			BigDecimal cgstTaxValue = feeAmt.multiply(new BigDecimal(cgstTax)).divide(ONE_HUNDRED_AB);
			logger.debug("ctax1 amount after calculation :" + igstTaxValue);
			logger.debug("itax2 amount after calculation :" + cgstTaxValue);
			taxMap.put(Constants.TCH_IGST, igstTaxValue);
			taxMap.put(Constants.TCH_CGST, cgstTaxValue);
		}
		return  taxMap;
	}
	
	
	public String calculateConvFee(Payment input) throws TCHQueryException{
		String orgAmount = "";
		logger.debug("calling function to get bin validation");
		
		SqlParameter inTerminalSerialNumber = new SqlParameter("I_TERMINAL_SERIAL_NUMBER", Types.VARCHAR);
		SqlParameter inBinVal = new SqlParameter("I_BINVAL", Types.VARCHAR); 
		
		DataSource ds = (DataSource) TcpServer.getContext().getBean("dataSource");
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(ds);
		simpleJdbcCall.withProcedureName("TCH_CONV_FEE_PROC").declareParameters(inTerminalSerialNumber,inBinVal).compile();
		
		logger.debug("Input for function :" + input.getBinNumber());
		SqlParameterSource parameter = new MapSqlParameterSource()
		.addValue("I_TERMINAL_SERIAL_NUMBER", input.getTerminalSerialNumber())
		.addValue("I_BINVAL", input.getBinNumber());
		Map<String,Object> map = simpleJdbcCall.execute(parameter);
		
		String binInfo = (String)map.get("O_BINTYPE");
		logger.debug("card value :" + binInfo); //B-MAST-CC-D
		
		String amount = input.getOriginalAmount();
		logger.debug("original amount : " + amount);
		if(binInfo != null){
			String arr[] = binInfo.split("-");
			input.setCardTypeValue(UtilityHelper.reverseCardEntryMode(arr[arr.length-2]));
			input.setBinType(arr[arr.length-1]); // set international flag
			List<HashMap<String,Object>> feeCursor = (List<HashMap<String, Object>>) map.get("C_FEE");
			
			if(feeCursor != null && feeCursor.size() > 0)
				input = applyCovenienceFee(input, feeCursor);				// calculation for convenience fee
			else
				throw new TCHQueryException("GB-13","NO DATA FOR FEE");
		}else{
			throw new TCHQueryException("GB-12","INVALID BIN");
		}
		logger.debug("Fee value :" + input.getFeePer());
		logger.debug("Igst value :" + input.getIgsttax());
		logger.debug("Cgst value :" + input.getCgsttax());
		input.setOriginalAmount(input.getTotalValue());
		logger.debug("Amount after calculation"+ input.getOriginalAmount());
		return orgAmount;
	}
	
	public static void main(String[] args) {
		Payment input = new Payment();
		input.setMerchantId("152");
		input.setCardTypeValue("C");
		input.setBinType("I");
		input.setOriginalAmount("5235400");
		List<HashMap<String,Object>> feeCursor = new LinkedList<HashMap<String,Object>>();
		HashMap<String, Object> m1 = new HashMap<String, Object>();
		m1.put("F_PER","2.5");
		m1.put("F_MAX_AMT","300000000");
		m1.put("F_MIN_AMT","3000");
		m1.put("F_CAL_METHOD","P");
		m1.put("F_MID","152");
		m1.put("F_CARD_TYPE","C");
		m1.put("F_BINTYPE","I");
		m1.put("F_CTAX1","9");
		m1.put("F_ITAX1","9");
		feeCursor.add(m1);
		
		AbstractGenericDao.applyCovenienceFee(input, feeCursor);
	}
}
