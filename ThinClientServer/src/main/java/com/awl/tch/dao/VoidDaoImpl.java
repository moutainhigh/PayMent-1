
package com.awl.tch.dao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import oracle.jdbc.OracleTypes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.awl.tch.bean.Payment;
import com.awl.tch.constant.Constants;
import com.awl.tch.constant.ErrorConstants;
import com.awl.tch.exceptions.TCHQueryException;
import com.awl.tch.model.PaymentDTO;
import com.awl.tch.server.TcpServer;
import com.awl.tch.util.ErrorMaster;
import com.awl.tch.util.Property;
import com.awl.tch.util.UtilityHelper;

@Repository("paymentVoidDao")
public class VoidDaoImpl extends GenericDaoImpl<PaymentDTO> implements VoidDao{

	private static final Logger logger = LoggerFactory.getLogger(VoidDaoImpl.class);
	
	/* (non-Javadoc)
	 * @see com.awl.tch.dao.PaymentVoidDao#getOriginalAmount(java.lang.String)
	 */
	
	@Override
	public PaymentDTO getSaleParameter(final Payment input)
			throws TCHQueryException {
		// TODO Auto-generated method stub
		
		String invoiceNumber = input.getReferenceValue().trim();
		String termSerialNumber  = input.getTerminalSerialNumber().trim();
		
		logger.debug("Invoice number : " + invoiceNumber);
		logger.debug("Terminal serial number : " + termSerialNumber);
		
		String query = "SELECT TPT.* from TCH_PAYMENT_TXN TPT JOIN TCH_HANDSHAKES TH ON TPT.P_MERCHANTID = TH.H_CARD_ACQUIRING_ID AND "+
						"TPT.P_TERMINALID  = TH.H_TID AND TPT.P_CLIENTID = TH.H_CLIENT_ID AND TPT.P_TERMINAL_SERIAL_NUMBER = TH.H_TERMINAL_SERIAL_NUMBER "+
						"WHERE TPT.P_INVOICENUMBER = ? AND TH.H_TERMINAL_SERIAL_NUMBER = ? AND TPT.P_REQUEST_TYPE <> 'REVERSAL' AND TPT.P_RESPONSE_CODE = '00'" ;
		//AND P_TIP_APPROVED='0'
		PaymentDTO paymentDto = new PaymentDTO();
		if(Property.isShowSql()){
			logger.info("Query for fetching original amount [" + query  + "]");
		}
		
		List<Map<String, Object>> rows = getJdbcTemplate().queryForList(query,invoiceNumber, termSerialNumber);
		if(rows != null){
			List<PaymentDTO> listpaymentDTO  = UtilityHelper.getPaymentDTO(rows);
			logger.debug("Size of the list :" + listpaymentDTO.size());
			if(listpaymentDTO.size() == 0){
				throw new TCHQueryException(ErrorConstants.TCH_V004,ErrorMaster.get(ErrorConstants.TCH_V004));
			}else if(listpaymentDTO.size() == 1){
				paymentDto = listpaymentDTO.get(0);
			}else{
				throw new TCHQueryException(ErrorConstants.TCH_V007,ErrorMaster.get(ErrorConstants.TCH_V007));
			}
		}else{
			throw new TCHQueryException(ErrorConstants.TCH_V004,ErrorMaster.get(ErrorConstants.TCH_V004));
		}
		return paymentDto;
	}

	@Override
	public PaymentDTO getSaleParameterWithSp(Payment input)
			throws TCHQueryException {
		
		SqlParameter inTerminalSerialNumber = new SqlParameter("I_TERMINAL_SERIAL_NUMBER", Types.VARCHAR);
		SqlParameter inInvNum = new SqlParameter("I_INV_NUMBER", Types.VARCHAR);
		SqlParameter inAppName = new SqlParameter("I_APP_NAME",Types.VARCHAR);
		SqlParameter outAppErrorCode = new SqlOutParameter("O_APP_ERROR_CODE", Types.VARCHAR);
		SqlParameter outDebugPoint = new SqlOutParameter("O_DEBUG_POINT", Types.VARCHAR);
		SqlParameter outSqlCode = new SqlOutParameter("O_SQL_CODE", Types.VARCHAR);
		SqlParameter outHostId= new SqlOutParameter("O_HOST_ID", Types.VARCHAR);
		SqlParameter oSummFlag = new SqlOutParameter("O_SUMM_FLAG", Types.VARCHAR);
		SqlParameter cursorVoidData = new SqlOutParameter("C_VOID_DATA", OracleTypes.CURSOR,new ColumnMapRowMapper());
		
		
		DataSource ds = (DataSource) TcpServer.getContext().getBean("dataSource");
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(ds);
		
		simpleJdbcCall.withProcedureName("TCH_VOID_PROC")
		.declareParameters(inTerminalSerialNumber,inInvNum,inAppName,oSummFlag,outSqlCode,outAppErrorCode,outDebugPoint,outHostId,cursorVoidData)
		.withoutProcedureColumnMetaDataAccess()
		.compile();
		
		logger.info("I_TERMINAL_SERIAL_NUMBER: [" + input.getTerminalSerialNumber().trim()  +"]" );
		logger.info("I_INV_NUMBER: [" + input.getReferenceValue()  +"]" );
		logger.info("I_APP_NAME: [" + input.getAppName()  +"]" );
		
		SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("I_TERMINAL_SERIAL_NUMBER",input.getTerminalSerialNumber().trim())
				.addValue("I_INV_NUMBER", input.getReferenceValue())
				.addValue("I_APP_NAME", input.getAppName());
		
		logger.info("Calling store procedure for TCH_VOID_PROC");
		
		Map<String,Object> output = simpleJdbcCall.execute(parameterSource);
		String sqlCode = (String) output.get("O_SQL_CODE");
		String appErrorCode = (String) output.get("O_APP_ERROR_CODE");
		String oDebugPoint = (String) output.get("O_DEBUG_POINT");
		String oHostId = (String) output.get("O_HOST_ID");
		String summaryFlag = (String) output.get("O_SUMM_FLAG");
		
		
		logger.info("SqlCode from Sp :" + sqlCode);
		logger.info("App Error Code from SP :" + appErrorCode);
		logger.info("Debug Point from SP :" + oDebugPoint);
		logger.info("Host id from SP :" + oHostId);
		logger.debug("Summary flag :" + summaryFlag);
		
		input.setHostId(oHostId);
		
		if(appErrorCode != null){
			throw new TCHQueryException(appErrorCode,ErrorMaster.get(appErrorCode));
		}
		
		if(Constants.TCH_1.equals(summaryFlag)){
			input.setDecisionFlag(Constants.TCH_SUMMARY_REPORT);
			input.setHundredTxn(Constants.TCH_Y);
		}else if(Constants.TCH_2.equals(summaryFlag)){
			if(isNewVersionPresent(input.getTerminalSerialNumber()))
				throw new TCHQueryException(ErrorConstants.TCH_FRSET, ErrorMaster.get(ErrorConstants.TCH_FRSET));
		}
		
		@SuppressWarnings("unchecked")
		List<HashMap<String,Object>> rows = (List<HashMap<String,Object>>) output.get("C_VOID_DATA");
		if(rows != null){
			List<PaymentDTO> p = new ArrayList<PaymentDTO>(1);
			p = UtilityHelper.getPaymentDTO(rows);
			
			PaymentDTO temp = p.get(0);
			return temp;
		}else{
			throw new TCHQueryException(ErrorConstants.TCH_V10,ErrorMaster.get(ErrorConstants.TCH_V10));
		}
		
	}
}
