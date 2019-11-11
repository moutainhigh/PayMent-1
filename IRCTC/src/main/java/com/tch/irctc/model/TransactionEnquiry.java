package com.tch.irctc.model;

public class TransactionEnquiry {
	
	private String tid;
	private String assetSrNo;
	private String serviceProvider;
	private String appTxnId;
	private String amount;
	private String timestamp;
	private String invoiceNumber;
	private String rrn;
	private String cardDigit;
	private String cardType;
	private String cardProvider;
	private String status;
	private String message;
	private String encdata;
	private String appcode;

	
	/**
	 * @return the tid
	 */ 
	public String getTid() {
		return tid;
	}
	
	
	/**
	 * @param tid the tid to set
	 */
	public void setTid(String tid) {
		this.tid = tid;
	}
	
	
	/**
	 * @return the assetSrNo
	 */
	public String getAssetSrNo() {
		return assetSrNo;
	}
	
	
	/**
	 * @param assetSrNo the assetSrNo to set
	 */
	public void setAssetSrNo(String assetSrNo) {
		this.assetSrNo = assetSrNo;
	}
	
	/**
	 * @return the serviceProvider
	 */
	public String getServiceProvider() {
		return serviceProvider;
	}
	
	
	/**
	 * @param serviceProvider the serviceProvider to set
	 */
	public void setServiceProvider(String serviceProvider) {
		this.serviceProvider = serviceProvider;
	}
	
	
	/**
	 * @return the appTxnId
	 */
	public String getAppTxnId() {
		return appTxnId;
	}
	
	
	/**
	 * @param appTxnId the appTxnId to set
	 */
	public void setAppTxnId(String appTxnId) {
		this.appTxnId = appTxnId;
	}
	
	
	/**
	 * @return the amount
	 */
	public String getAmount() {
		return amount;
	}
	
	
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	
	/**
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}
	
	
	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	
	/**
	 * @return the invoiceNumber
	 */
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	
	
	/**
	 * @param invoiceNumber the invoiceNumber to set
	 */
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	
	
	/**
	 * @return the rrn
	 */
	public String getRrn() {
		return rrn;
	}
	
	
	/**
	 * @param rrn the rrn to set
	 */
	public void setRrn(String rrn) {
		this.rrn = rrn;
	}
	
	/**
	 * @return the cardDigit
	 */
	public String getCardDigit() {
		return cardDigit;
	}
	
	
	/**
	 * @param cardDigit the cardDigit to set
	 */
	public void setCardDigit(String cardDigit) {
		this.cardDigit = cardDigit;
	}
	
	
	/**
	 * @return the cardType
	 */
	public String getCardType() {
		return cardType;
	}
	
	
	
	/**
	 * @param cardType the cardType to set
	 */
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	
	
	/**
	 * @return the cardProvider
	 */
	public String getCardProvider() {
		return cardProvider;
	}
	
	
	/**
	 * @param cardProvider the cardProvider to set
	 */
	public void setCardProvider(String cardProvider) {
		this.cardProvider = cardProvider;
	}
	
	
	
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	
	
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
	
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	/**
	 * @return the encdata
	 */
	public String getEncdata() {
		return encdata;
	}
	
	
	/**
	 * @param encdata the encdata to set
	 */
	public void setEncdata(String encdata) {
		this.encdata = encdata;
	}
	
	
	/**
	 * @return the appcode
	 */
	public String getAppcode() {
		return appcode;
	}
	
	
	/**
	 * @param appcode the appcode to set
	 */
	public void setAppcode(String appcode) {
		this.appcode = appcode;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TransactionEnquiry [tid=" + tid + ", assetSrNo=" + assetSrNo
	            +  ", serviceProvider=" + serviceProvider+ ", appTxnId=" + appTxnId 
	            + ", amount=" + amount + ", timestamp="+ timestamp 
	            + ", invoiceNumber=" + invoiceNumber + ", rrn=" + rrn
	            + ", cardDigit=" + cardDigit + ", cardType=" + cardType
	            + ", cardProvider=" + cardProvider + ", status=" + status 
	            + ", message=" + message + ", encdata=" + encdata 
	            + ", appcode=" + appcode + "]";
	}
	
}
