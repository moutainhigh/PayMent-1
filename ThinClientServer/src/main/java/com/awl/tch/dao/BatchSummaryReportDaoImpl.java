package com.awl.tch.dao;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

import com.awl.tch.bean.PrintObject;
import com.awl.tch.bean.SummaryReport;
import com.awl.tch.constant.ErrorConstants;
import com.awl.tch.exceptions.TCHQueryException;
import com.awl.tch.server.TcpServer;
import com.awl.tch.util.ErrorMaster;


@Repository("batchSummaryReportDao")

public class BatchSummaryReportDaoImpl extends GenericDaoImpl<SummaryReport> implements BatchSummaryReportDao{
	private static Logger logger = LoggerFactory.getLogger(BatchSummaryReportDaoImpl.class);

	@Override
	public void getBatchWiseReport(SummaryReport input)
			throws TCHQueryException {
		logger.info("Inside batch summary service");
		
		SqlParameter inTerminalSerialNumber = new SqlParameter("I_TERMINAL_SERIAL_NUMBER", Types.VARCHAR);
		SqlParameter inBatchNumber = new SqlParameter("I_BATCH_NUMBER", Types.VARCHAR);
		SqlParameter outSqlCode = new SqlOutParameter("O_SQL_CODE", Types.VARCHAR);
		SqlParameter outStartDate= new SqlOutParameter("O_START_DATE", Types.VARCHAR);
		SqlParameter outEndDate = new SqlOutParameter("O_END_DATE", Types.VARCHAR);
		SqlParameter outAppErrorCode = new SqlOutParameter("O_APP_ERROR_CODE", Types.VARCHAR);
		SqlParameter outDebugPoint = new SqlOutParameter("O_DEBUG_POINT", Types.VARCHAR);
		SqlParameter outBatchNumber = new SqlOutParameter("O_BATCH_NUMBER", Types.VARCHAR);
		SqlParameter outMerchantId= new SqlOutParameter("O_MERCHANT_ID", Types.VARCHAR);
		SqlParameter outTerminalId = new SqlOutParameter("O_TERMINAL_ID", Types.VARCHAR);
		SqlParameter outTipAmount = new SqlOutParameter("O_TIP_AMOUNT", Types.VARCHAR);
		SqlParameter outTipCount = new SqlOutParameter("O_TIP_COUNT", Types.VARCHAR);
		
		SqlParameter cursorSettlementRequest = new SqlOutParameter("C_SETTLEMENT_DATA_REQUEST", OracleTypes.CURSOR,new ColumnMapRowMapper());
		SqlParameter cursorSettlementScheme = new SqlOutParameter("C_SETTLEMENT_DATA_SCHEME", OracleTypes.CURSOR,new ColumnMapRowMapper());
		
		DataSource ds = (DataSource) TcpServer.getContext().getBean("dataSource");
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(ds);
		
		simpleJdbcCall.withProcedureName("TCH_SUMARY_REPORT_BTCH_PROC")
		.declareParameters(inTerminalSerialNumber,inBatchNumber,outMerchantId,outTerminalId, 
				outSqlCode,outStartDate,outEndDate, outAppErrorCode, outBatchNumber, outDebugPoint, outTipAmount, outTipCount, cursorSettlementRequest,cursorSettlementScheme)
		.withoutProcedureColumnMetaDataAccess()
		.compile();
		
		String batchNumber  = null;
		if(input.getBatchNumber() != null)
			batchNumber = String.valueOf(Integer.parseInt(input.getBatchNumber()));
		
		logger.info("I_TERMINAL_SERIAL_NUMBER: [" + input.getTerminalSerialNumber()  +"]" );
		logger.info("I_BATCH_NUMBER: [" +  batchNumber +"]" );
		
		SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("I_TERMINAL_SERIAL_NUMBER",input.getTerminalSerialNumber().trim())
				.addValue("I_BATCH_NUMBER", batchNumber);
		
		logger.info("Calling store procedure for sale TCH_SUMARY_REPORT_BTCH_PROC");
		
		Map<String,Object> output = simpleJdbcCall.execute(parameterSource);
		
		String sqlCode = (String) output.get("O_SQL_CODE");
		String appErrorCode = (String) output.get("O_APP_ERROR_CODE");
		String oDebugPoint = (String) output.get("O_DEBUG_POINT");
		String oTipAmount = (String) output.get("O_TIP_AMOUNT");
		String oTipCount = (String) output.get("O_TIP_COUNT");
		String oStartDate = (String) output.get("O_START_DATE");
		String oEndDate = (String) output.get("O_END_DATE");
		
		logger.info("SqlCode from Sp :" + sqlCode);
		logger.info("App Error Code from SP :" + appErrorCode);
		logger.info("Debug Point from SP :" + oDebugPoint);
		logger.info("Tip amount from SP :" + oTipAmount);
		logger.info("Tip count from SP :" + oTipCount);
		logger.info(" Start date:" + oStartDate);
		logger.info(" End date:" + oEndDate);
		
		
		if(oStartDate != null && oEndDate != null){
			setStartAndEndDate(input,oStartDate, oEndDate);
		}
		
		if(appErrorCode != null){
			throw new TCHQueryException(appErrorCode, ErrorMaster.get(appErrorCode));
		}
		if(!"3".equalsIgnoreCase(oDebugPoint)){
			throw new TCHQueryException(ErrorConstants.TCH_S616, ErrorMaster.get(ErrorConstants.TCH_S616));
		}
		input.setBatchNumber( (String) output.get("O_BATCH_NUMBER"));
		input.setMerchantId( (String) output.get("O_MERCHANT_ID"));
		input.setTerminalId( (String) output.get("O_TERMINAL_ID"));
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("SALE", null);
		map.put("VOID", null);
		map.put("REFUND", null);
		map.put("VOIDREFUND", null);
		//map.put("TOTAL", null);
		
		
		@SuppressWarnings("unchecked")
		List<HashMap<String,Object>> rows = (List<HashMap<String,Object>>) output.get("C_SETTLEMENT_DATA_REQUEST");
		@SuppressWarnings("unchecked")
		List<HashMap<String,Object>> rows1 = (List<HashMap<String,Object>>) output.get("C_SETTLEMENT_DATA_SCHEME");
		
		
		List<PrintObject> listPrintObject = new ArrayList<PrintObject>();
		PrintObject printObject  = new PrintObject();
		// set default value for request wise
		printObject.setPrintValueName("BATCH REPORT");
		printObject.setPrintValueCount("NULL");
		printObject.setPrintValueData("NULL");
		listPrintObject.add(printObject);
		
		for(Map<String, Object> row : rows){
			String value= (String.valueOf(((BigDecimal) row.get("COUNT_VAL")).toPlainString())) + ","+
					(String.valueOf(((BigDecimal) row.get("TOTAL_AMOUNT")).toPlainString()));
			map.put((String) row.get("REQUEST_TYPE"), value);
		}
		Long amount = 0L;
		Long countTotal =  0l;
		for(String str : map.keySet()){
			printObject  = new PrintObject();
			printObject.setPrintValueName(str);
			if(map.get(str) != null){
				String printValueCount = map.get(str).split(",")[0];
				String printValueData  = map.get(str).split(",")[1];
				
				if(printValueCount != null){
					/*if("SALE".equals(str)){
						saleAmount = saleAmount + Long.valueOf(printValueData); // storing sale amount need to be display on batch summary charge slip
					}*/
					printObject.setPrintValueCount(printValueCount);
					
					if(!"VOID".equals(str) && !"VOIDREFUND".equals(str) && !"TIP".equals(str))
						countTotal = countTotal + Long.valueOf(printValueCount);
				}else{
					printObject.setPrintValueCount("0");
				}

				if(printValueData != null){
					if("REFUND".equals(str)){
						amount = amount - Long.valueOf(printValueData); 
					} else if("SALE".equals(str)){
						amount = amount + Long.valueOf(printValueData);
					}
					printObject.setPrintValueData((new BigDecimal(printValueData).movePointLeft(2)).toString());
				}else{
					printObject.setPrintValueData("0.00");
				}
				listPrintObject.add(printObject);
			}else{
				printObject.setPrintValueCount("0");
				printObject.setPrintValueData("0.00");
				listPrintObject.add(printObject);
			}
		}
		
		if(oTipAmount != null && oTipCount != null){
			printObject  = new PrintObject();
			printObject.setPrintValueName("TIP");
			printObject.setPrintValueCount(oTipCount);
			printObject.setPrintValueData(new BigDecimal(oTipAmount).movePointLeft(2).toPlainString());
			listPrintObject.add(printObject);
		}
		
		printObject  = new PrintObject();
		printObject.setPrintValueName("TOTAL");
		printObject.setPrintValueCount(countTotal.toString());
		printObject.setPrintValueData((new BigDecimal(amount.toString()).movePointLeft(2)).toString());
		listPrintObject.add(printObject);
		countTotal = 0l;	
		
		// SET SCHEME WISE VALUES
		printObject  = new PrintObject();
		printObject.setPrintValueName("SCHEME REPORT");
		printObject.setPrintValueCount("NULL");
		printObject.setPrintValueData("NULL");
		listPrintObject.add(printObject);
		
		if(rows != null){
			for(Map<String, Object> row : rows1){
				amount = 0l;
				printObject  = new PrintObject();
				printObject.setPrintValueName((String) row.get("SHEME_NAME"));
				printObject.setPrintValueCount((String.valueOf(((BigDecimal) row.get("COUNT_VAL")).toPlainString())));
				amount  = amount + Long.valueOf(((BigDecimal) row.get("TOTAL_AMOUNT")).toPlainString());
				if((BigDecimal) row.get("TOTAL_REFUND_AMOUNT") != null){
					Long refundAmount = Long.valueOf(((BigDecimal) row.get("TOTAL_REFUND_AMOUNT")).toPlainString());
					//amount  = amount - (2 * refundAmount); // delete 2 times as result got refund amount add in it
					amount  = amount - refundAmount; // correct value printed
				}
				printObject.setPrintValueData((new BigDecimal(amount.toString()).movePointLeft(2)).toString());
					listPrintObject.add(printObject);
			}
		}
		/*printObject  = new PrintObject();
		printObject.setPrintValueName("TOTAL");
		printObject.setPrintValueCount(countTotal.toString());
		printObject.setPrintValueData(amount.toString());
		listPrintObject.add(printObject);*/
		int arrSize = rows.size()+rows1.size();
		PrintObject[] printObjArr = new PrintObject[arrSize];
		input.setPrintObject(listPrintObject.toArray(printObjArr));
		
		logger.info("Exiting batch summary report service");
	}

	@Override
	public void getBatchWiseReportNew(SummaryReport input)
			throws TCHQueryException {

logger.debug("Get details report dao, get mid and tid list");
		
		
		//getMidTids(input.getTerminalSerialNumber());
		
		String midtid[] = getMidTids(input.getTerminalSerialNumber(),input.getHostId());
		if(midtid != null && midtid.length > 0){
			input.setMerchantId(midtid[0]);
			input.setTerminalId(midtid[1]);
		}
		
		
		logger.info("Inside batch summary service");
		
		SqlParameter inTerminalSerialNumber = new SqlParameter("I_TERMINAL_SERIAL_NUMBER", Types.VARCHAR);
		SqlParameter inBatchNumber = new SqlParameter("I_BATCH_NUMBER", Types.VARCHAR);
		SqlParameter inMid = new SqlParameter("I_MID", Types.VARCHAR);
		SqlParameter inTid = new SqlParameter("I_TID", Types.VARCHAR);
		SqlParameter outSqlCode = new SqlOutParameter("O_SQL_CODE", Types.VARCHAR);
		SqlParameter outStartDate= new SqlOutParameter("O_START_DATE", Types.VARCHAR);
		SqlParameter outEndDate = new SqlOutParameter("O_END_DATE", Types.VARCHAR);
		SqlParameter outAppErrorCode = new SqlOutParameter("O_APP_ERROR_CODE", Types.VARCHAR);
		SqlParameter outDebugPoint = new SqlOutParameter("O_DEBUG_POINT", Types.VARCHAR);
		SqlParameter outBatchNumber = new SqlOutParameter("O_BATCH_NUMBER", Types.VARCHAR);
		SqlParameter outTipAmount = new SqlOutParameter("O_TIP_AMOUNT", Types.VARCHAR);
		SqlParameter outTipCount = new SqlOutParameter("O_TIP_COUNT", Types.VARCHAR);
		
		SqlParameter cursorSettlementRequest = new SqlOutParameter("C_SETTLEMENT_DATA_REQUEST", OracleTypes.CURSOR,new ColumnMapRowMapper());
		SqlParameter cursorSettlementScheme = new SqlOutParameter("C_SETTLEMENT_DATA_SCHEME", OracleTypes.CURSOR,new ColumnMapRowMapper());
		
		DataSource ds = (DataSource) TcpServer.getContext().getBean("dataSource");
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(ds);
		
		simpleJdbcCall.withProcedureName("TCH_SUMARY_REPORT_BTCH_PROC1")
		.declareParameters(inTerminalSerialNumber,inBatchNumber,inMid,inTid, outSqlCode,outStartDate,outEndDate, outAppErrorCode, outBatchNumber, outDebugPoint, outTipAmount, 
				outTipCount, cursorSettlementRequest,cursorSettlementScheme)
		.withoutProcedureColumnMetaDataAccess()
		.compile();
		
		String batchNumber  = null;
		if(input.getBatchNumber() != null)
			batchNumber = String.valueOf(Integer.parseInt(input.getBatchNumber()));
		
		logger.info("I_TERMINAL_SERIAL_NUMBER: [" + input.getTerminalSerialNumber()  +"]" );
		logger.info("I_BATCH_NUMBER: [" +  batchNumber +"]" );
		logger.info("I_MID: [" +  input.getMerchantId()+"]" );
		logger.info("I_TID: [" +  input.getTerminalId() +"]" );
		
		
		SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("I_TERMINAL_SERIAL_NUMBER",input.getTerminalSerialNumber().trim())
				.addValue("I_BATCH_NUMBER", batchNumber)
				.addValue("I_MID", input.getMerchantId())
				.addValue("I_TID", input.getTerminalId());
		
		logger.info("Calling store procedure for sale TCH_SUMARY_REPORT_BTCH_PROC1");
		
		Map<String,Object> output = simpleJdbcCall.execute(parameterSource);
		
		String sqlCode = (String) output.get("O_SQL_CODE");
		String appErrorCode = (String) output.get("O_APP_ERROR_CODE");
		String oDebugPoint = (String) output.get("O_DEBUG_POINT");
		String oTipAmount = (String) output.get("O_TIP_AMOUNT");
		String oTipCount = (String) output.get("O_TIP_COUNT");
		String oStartDate = (String) output.get("O_START_DATE");
		String oEndDate = (String) output.get("O_END_DATE");
		
		logger.info("SqlCode from Sp :" + sqlCode);
		logger.info("App Error Code from SP :" + appErrorCode);
		logger.info("Debug Point from SP :" + oDebugPoint);
		logger.info("Tip amount from SP :" + oTipAmount);
		logger.info("Tip count from SP :" + oTipCount);
		logger.info(" Start date:" + oStartDate);
		logger.info(" End date:" + oEndDate);
		
		
		if(oStartDate != null && oEndDate != null){
			setStartAndEndDate(input,oStartDate, oEndDate);
		}
		
		
		if(appErrorCode != null){
			if(input.getHostId() != null){
				throw new TCHQueryException(appErrorCode, input.getHostId()+" "+ErrorMaster.get(appErrorCode));
			}else{
				throw new TCHQueryException(appErrorCode, ErrorMaster.get(appErrorCode));
			}
		}
		if(!"3".equalsIgnoreCase(oDebugPoint)){
			throw new TCHQueryException(ErrorConstants.TCH_S616, ErrorMaster.get(ErrorConstants.TCH_S616));
		}
		input.setBatchNumber((String) output.get("O_BATCH_NUMBER"));
		
		// check condition for setting continuity flag and host id
		if(input.getHostId() == null){
			setDecisionFlagForSummaryReport(input);
		}
		
		@SuppressWarnings("unchecked")
		List<HashMap<String,Object>> rows = (List<HashMap<String,Object>>) output.get("C_SETTLEMENT_DATA_REQUEST");
		@SuppressWarnings("unchecked")
		List<HashMap<String,Object>> rows1 = (List<HashMap<String,Object>>) output.get("C_SETTLEMENT_DATA_SCHEME");
		
		if(rows != null && rows.size() > 0){
			logger.debug("Size of rows :" + rows.size());
			LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
			map.put("SALE", null);
			map.put("VOID", null);
			map.put("REFUND", null);
			map.put("VOIDREFUND", null);
			
			List<PrintObject> listPrintObject = new ArrayList<PrintObject>();
			PrintObject printObject  = new PrintObject();
			// set default value for request wise
			printObject.setPrintValueName("BATCH REPORT");
			printObject.setPrintValueCount("NULL");
			printObject.setPrintValueData("NULL");
			listPrintObject.add(printObject);
			
			for(Map<String, Object> row : rows){
				String value= (String.valueOf(((BigDecimal) row.get("COUNT_VAL")).toPlainString())) + ","+
						(String.valueOf(((BigDecimal) row.get("TOTAL_AMOUNT")).toPlainString()));
				map.put((String) row.get("REQUEST_TYPE"), value);
			}
			Long amount = 0L;
			Long countTotal =  0l;
			for(String str : map.keySet()){
				printObject  = new PrintObject();
				printObject.setPrintValueName(str);
				if(map.get(str) != null){
					String printValueCount = map.get(str).split(",")[0];
					String printValueData  = map.get(str).split(",")[1];
					
					if(printValueCount != null){
						/*if("SALE".equals(str)){
							saleAmount = saleAmount + Long.valueOf(printValueData); // storing sale amount need to be display on batch summary charge slip
						}*/
						printObject.setPrintValueCount(printValueCount);
						
						if(!"VOID".equals(str) && !"VOIDREFUND".equals(str) && !"TIP".equals(str))
							countTotal = countTotal + Long.valueOf(printValueCount);
					}else{
						printObject.setPrintValueCount("0");
					}

					if(printValueData != null){
						if("REFUND".equals(str)){
							amount = amount - Long.valueOf(printValueData); 
						} else if("SALE".equals(str)){
							amount = amount + Long.valueOf(printValueData);
						}
						printObject.setPrintValueData((new BigDecimal(printValueData).movePointLeft(2)).toString());
					}else{
						printObject.setPrintValueData("0.00");
					}
					listPrintObject.add(printObject);
				}else{
					printObject.setPrintValueCount("0");
					printObject.setPrintValueData("0.00");
					listPrintObject.add(printObject);
				}
			}
			
			if(oTipAmount != null && oTipCount != null){
				printObject  = new PrintObject();
				printObject.setPrintValueName("TIP");
				printObject.setPrintValueCount(oTipCount);
				printObject.setPrintValueData(new BigDecimal(oTipAmount).movePointLeft(2).toPlainString());
				listPrintObject.add(printObject);
			}
			
			printObject  = new PrintObject();
			printObject.setPrintValueName("TOTAL");
			printObject.setPrintValueCount(countTotal.toString());
			printObject.setPrintValueData((new BigDecimal(amount.toString()).movePointLeft(2)).toString());
			listPrintObject.add(printObject);
			countTotal = 0l;	
			
			// SET SCHEME WISE VALUES
			printObject  = new PrintObject();
			printObject.setPrintValueName("SCHEME REPORT");
			printObject.setPrintValueCount("NULL");
			printObject.setPrintValueData("NULL");
			listPrintObject.add(printObject);
			
			if(rows != null){
				for(Map<String, Object> row : rows1){
					amount = 0l;
					printObject  = new PrintObject();
					printObject.setPrintValueName((String) row.get("SHEME_NAME"));
					printObject.setPrintValueCount((String.valueOf(((BigDecimal) row.get("COUNT_VAL")).toPlainString())));
					amount  = amount + Long.valueOf(((BigDecimal) row.get("TOTAL_AMOUNT")).toPlainString());
					if((BigDecimal) row.get("TOTAL_REFUND_AMOUNT") != null){
						Long refundAmount = Long.valueOf(((BigDecimal) row.get("TOTAL_REFUND_AMOUNT")).toPlainString());
						//amount  = amount - (2 * refundAmount); // delete 2 times as result got refund amount add in it
						amount  = amount - refundAmount; // correct value printed
					}
					printObject.setPrintValueData((new BigDecimal(amount.toString()).movePointLeft(2)).toString());
						listPrintObject.add(printObject);
				}
			}
			int arrSize = rows.size()+rows1.size();
			PrintObject[] printObjArr = new PrintObject[arrSize];
			input.setPrintObject(listPrintObject.toArray(printObjArr));
		}else{
			throw new TCHQueryException(ErrorConstants.TCH_R202, ErrorMaster.get(ErrorConstants.TCH_R202));
		}
		logger.info("Exiting batch summary report service");
	}
}
