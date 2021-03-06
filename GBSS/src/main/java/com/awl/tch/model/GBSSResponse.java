package com.awl.tch.model;

import com.google.gson.annotations.SerializedName;

public class GBSSResponse {

	@SerializedName("Response_Status")
	private String responseStatus;
	
	@SerializedName("Error_Description")
	private String errorDescription;
	
	@SerializedName("MID")
	private String merchantId;
	
	@SerializedName("TID")
	private String terminalId;
	
	@SerializedName("CHLN_NO")
	private String challanNumber;
	
	@SerializedName("CUST_NAME")
	private String customerName;
	
	@SerializedName("Txn_Amount")
	private String amount;
	
	@SerializedName("Pay_Mode")
	private String pyamentMode;
	
	@SerializedName("Txn_Date")
	private String transactionDate;
	
	@SerializedName("Txn_Time")
	private String transactionTime;
	
	@SerializedName("Txn_Status")
	private String transactionStatus;
	
	@SerializedName("Tran_ID")
	private String transactionId;

	/**
	 * @return the responseStatus
	 */
	public String getResponseStatus() {
		return responseStatus;
	}

	/**
	 * @param responseStatus the responseStatus to set
	 */
	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}

	/**
	 * @return the errorDescription
	 */
	public String getErrorDescription() {
		return errorDescription;
	}

	/**
	 * @param errorDescription the errorDescription to set
	 */
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	/**
	 * @return the merchantId
	 */
	public String getMerchantId() {
		return merchantId;
	}

	/**
	 * @param merchantId the merchantId to set
	 */
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	/**
	 * @return the terminalId
	 */
	public String getTerminalId() {
		return terminalId;
	}

	/**
	 * @param terminalId the terminalId to set
	 */
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	/**
	 * @return the challanNumber
	 */
	public String getChallanNumber() {
		return challanNumber;
	}

	/**
	 * @param challanNumber the challanNumber to set
	 */
	public void setChallanNumber(String challanNumber) {
		this.challanNumber = challanNumber;
	}

	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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
	 * @return the pyamentMode
	 */
	public String getPyamentMode() {
		return pyamentMode;
	}

	/**
	 * @param pyamentMode the pyamentMode to set
	 */
	public void setPyamentMode(String pyamentMode) {
		this.pyamentMode = pyamentMode;
	}

	/**
	 * @return the transactionDate
	 */
	public String getTransactionDate() {
		return transactionDate;
	}

	/**
	 * @param transactionDate the transactionDate to set
	 */
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	/**
	 * @return the transactionTime
	 */
	public String getTransactionTime() {
		return transactionTime;
	}

	/**
	 * @param transactionTime the transactionTime to set
	 */
	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}

	/**
	 * @return the transactionStatus
	 */
	public String getTransactionStatus() {
		return transactionStatus;
	}

	/**
	 * @param transactionStatus the transactionStatus to set
	 */
	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	/**
	 * @return the transactionId
	 */
	public String getTransactionId() {
		return transactionId;
	}

	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GBSSResponse [responseStatus=" + responseStatus
				+ ", errorDescription=" + errorDescription + ", merchantId="
				+ merchantId + ", terminalId=" + terminalId
				+ ", challanNumber=" + challanNumber + ", customerName="
				+ customerName + ", amount=" + amount + ", pyamentMode="
				+ pyamentMode + ", transactionDate=" + transactionDate
				+ ", transactionTime=" + transactionTime
				+ ", transactionStatus=" + transactionStatus
				+ ", transactionId=" + transactionId + "]";
	}
}
