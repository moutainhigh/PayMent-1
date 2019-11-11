package com.awl.tch.axisepay.model;

import java.util.StringTokenizer;

public class AXISepayResponse {

	private String customerName;
	private String urn;
	private String mid;
	private String tid;
	private String crn;
	private String amount;
	private String trxnDatetime;
	private String dateTime;
	private String rid;
	private String errorCode;
	private String errorDesc;
	private String cid;
	private String rrn;
	private String status;
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getUrn() {
		return urn;
	}
	public void setUrn(String urn) {
		this.urn = urn;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getCrn() {
		return crn;
	}
	public void setCrn(String crn) {
		this.crn = crn;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getTrxnDatetime() {
		return trxnDatetime;
	}
	public void setTrxnDatetime(String trxnDatetime) {
		this.trxnDatetime = trxnDatetime;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getRid() {
		return rid;
	}
	public void setRid(String rid) {
		this.rid = rid;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorDesc() {
		return errorDesc;
	}
	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getRrn() {
		return rrn;
	}
	public void setRrn(String postTrxnId) {
		this.rrn = postTrxnId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	public static AXISepayResponse getObjectFromResponse(String response){
		StringTokenizer st = new StringTokenizer(response, "&");
		AXISepayResponse responseObject = new AXISepayResponse();
		while(st.hasMoreTokens()){
			String keyValue = st.nextToken();
			String[] splitedString = keyValue.split("=");
			String key= splitedString[0];
			String value = null;
			if (splitedString.length>1){
			 value = splitedString[1];
			}
			switch(key){
			case "CUST_NAME":
				responseObject.setCustomerName(value);
				break;
				
			case "URN":
				responseObject.setUrn(value);
				break;
				
			case "TID":
				responseObject.setTid(value);
				break;
				
			case "MID":
				responseObject.setMid(value);
				break;
				
				
			case "CID":
				responseObject.setCid(value);
				break;
				
			case "CRN":
				responseObject.setCrn(value);
				break;
				
			case "AMOUNT":
				responseObject.setAmount(value);
				break;
				
			case "TRAN_DT":
				responseObject.setTrxnDatetime(value);
				break;
				
			case "DT_TIME":
				responseObject.setDateTime(value);
				break;
				
			case "RID":
				responseObject.setRid(value);
				break;
				
			case "ERR_CODE":
				responseObject.setErrorCode(value);
				break;
				
			case "ERR_DES":
				responseObject.setErrorDesc(value);
				break;
				
			case "POS_TRAN_ID":
				responseObject.setRrn(value);
				break;
				
			case "STATUS":
				responseObject.setStatus(value);
				break;
			}
			
		}
		
		return responseObject;
		
	}
}