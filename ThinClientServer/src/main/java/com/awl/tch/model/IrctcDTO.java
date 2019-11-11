package com.awl.tch.model;

import com.awl.tch.annotation.Column;
import com.awl.tch.annotation.ICurrentTimestamp;
import com.awl.tch.annotation.Id;
import com.awl.tch.annotation.Table;
import com.awl.tch.annotation.UCurrentTimestamp;

@Table(name="TCH_IRCTC_REPORT")
public class IrctcDTO {
	
	
	@Column(name = "I_TID")
	private String tid;
	
	@Column(name = "I_ASSET_SR_NO")
	private String asstSrNo;
	
	@Column(name = "I_SERVICE_PROVIDER")
	private String serviceProvider;
	
	@Column(name = "I_TIME_STAMP")
	private String timeStamp;
	
	@Column(name = "I_BANK_CODE")
	private String bankCode;
	
	@Column(name = "I_STATUS")
	private String status;
	
	@Column(name = "I_MESSAGE")
	private String message;
	
	@Id
	@Column(name = "I_APP_TXN_ID")
	private String appTxnId;
	
	
	@Column(name = "I_AMOUNT")
	private String amount;
	
	@Column(name = "I_INVOICE_NO")
	private String invoiceNo;
	
	@Column(name = "I_RRN")
	private String rrn;
	
	@Column(name = "I_CARD_DIGIT")
	private String cardigit;

	@Column(name = "I_CARD_TYPE")
	private String cardType;
	
	@Column(name = "I_CARD_PROVIDER")
	private String cardProvider;
	
	@Column(name = "I_REQ_TYPE")
	private String reqType;
	
	@Column(name="I_CREATED")
	@ICurrentTimestamp
	private String created;
		
	@Column(name="I_UPDATED")
	@ICurrentTimestamp
	@UCurrentTimestamp
	private String updated;
	
	@Column(name = "I_MID")
	private String mid;
	
	@Column(name = "I_CPGTXNID")
	private String cpgTxnId;
	
	@Column(name = "I_POSMID")
	private String posMID;
	
	@Column(name = "I_AUTHCODE")
    private String authCode;
	
	@Column(name = "I_PINSTATUS")
    private String pinStatus;
	
	@Column(name = "I_BATCH_NO")
    private String batchNo;
	
	@Column(name = "I_STNCODE")
    private String stnCode;
	
	@Column(name = "I_DIVCODE")
    private String divCode;
	
	@Column(name = "I_ZONECODE")
    private String zoneCode;
	
	@Column(name = "I_RLYTID")
    private String rlyTid;
	
	@Column(name = "I_CLIENT_APP_CODE")
    private String clientAppCode;
	

	/**
	 * @return the posMID
	 */
	public String getPosMID() {
		return posMID;
	}


	/**
	 * @param posMID the posMID to set
	 */
	public void setPosMID(String posMID) {
		this.posMID = posMID;
	}


	/**
	 * @return the authCode
	 */
	public String getAuthCode() {
		return authCode;
	}


	/**
	 * @param authCode the authCode to set
	 */
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}


	/**
	 * @return the pinStatus
	 */
	public String getPinStatus() {
		return pinStatus;
	}


	/**
	 * @param pinStatus the pinStatus to set
	 */
	public void setPinStatus(String pinStatus) {
		this.pinStatus = pinStatus;
	}


	/**
	 * @return the batchNo
	 */
	public String getBatchNo() {
		return batchNo;
	}


	/**
	 * @param batchNo the batchNo to set
	 */
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}


	/**
	 * @return the stnCode
	 */
	public String getStnCode() {
		return stnCode;
	}


	/**
	 * @param stnCode the stnCode to set
	 */
	public void setStnCode(String stnCode) {
		this.stnCode = stnCode;
	}


	/**
	 * @return the divCode
	 */
	public String getDivCode() {
		return divCode;
	}


	/**
	 * @param divCode the divCode to set
	 */
	public void setDivCode(String divCode) {
		this.divCode = divCode;
	}


	/**
	 * @return the zoneCode
	 */
	public String getZoneCode() {
		return zoneCode;
	}


	/**
	 * @param zoneCode the zoneCode to set
	 */
	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}


	/**
	 * @return the rlyTid
	 */
	public String getRlyTid() {
		return rlyTid;
	}


	/**
	 * @param rlyTid the rlyTid to set
	 */
	public void setRlyTid(String rlyTid) {
		this.rlyTid = rlyTid;
	}


	/**
	 * @return the clientAppCode
	 */
	public String getClientAppCode() {
		return clientAppCode;
	}


	/**
	 * @param clientAppCode the clientAppCode to set
	 */
	public void setClientAppCode(String clientAppCode) {
		this.clientAppCode = clientAppCode;
	}


	/**
	 * @return the mid
	 */
	public String getMid() {
		return mid;
	}


	/**
	 * @param mid the mid to set
	 */
	public void setMid(String mid) {
		this.mid = mid;
	}


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
	 * @return the asstSrNo
	 */
	public String getAsstSrNo() {
		return asstSrNo;
	}

	
	/**
	 * @param asstSrNo the asstSrNo to set
	 */
	public void setAsstSrNo(String asstSrNo) {
		this.asstSrNo = asstSrNo;
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
	 * @return the timeStamp
	 */
	public String getTimeStamp() {
		return timeStamp;
	}

	
	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	
	/**
	 * @return the bankCode
	 */
	public String getBankCode() {
		return bankCode;
	}

	
	/**
	 * @param bankCode the bankCode to set
	 */
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
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
	 * @return the invoiceNo
	 */
	public String getInvoiceNo() {
		return invoiceNo;
	}

	
	/**
	 * @param invoiceNo the invoiceNo to set
	 */
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
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
	 * @return the cardigit
	 */
	public String getCardigit() {
		return cardigit;
	}

	
	/**
	 * @param cardigit the cardigit to set
	 */
	public void setCardigit(String cardigit) {
		this.cardigit = cardigit;
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
	 * @return the reqType
	 */
	public String getReqType() {
		return reqType;
	}

	/**
	 * @param reqType the reqType to set
	 */
	public void setReqType(String reqType) {
		this.reqType = reqType;
	}

	
	/**
	 * @return the created
	 */
	public String getCreated() {
		return created;
	}


	/**
	 * @param created the created to set
	 */
	public void setCreated(String created) {
		this.created = created;
	}

	
	/**
	 * @return the updated
	 */
	public String getUpdated() {
		return updated;
	}


	/**
	 * @param updated the updated to set
	 */
	public void setUpdated(String updated) {
		this.updated = updated;
	}


	public String getCpgTxnId() {
		return cpgTxnId;
	}


	public void setCpgTxnId(String cpgTxnId) {
		this.cpgTxnId = cpgTxnId;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "IrctcDTO [tid=" + tid + ", asstSrNo=" + asstSrNo + ", serviceProvider=" + serviceProvider
				+ ", timeStamp=" + timeStamp + ", bankCode=" + bankCode + ", status=" + status + ", message=" + message
				+ ", appTxnId=" + appTxnId + ",  amount=" + amount
				+ ", invoiceNo=" + invoiceNo + ", rrn=" + rrn + ", cardigit=" + cardigit + ", cardType=" + cardType
				+ ", cardProvider=" + cardProvider + ", reqType=" + reqType + ", created=" + created + ", updated="
				+ updated + ", mid=" + mid + ", cpgTxnId=" + cpgTxnId + ", posMID=" + posMID + ", authCode=" + authCode
				+ ", pinStatus=" + pinStatus + ", batchNo=" + batchNo + ", stnCode=" + stnCode + ", divCode=" + divCode
				+ ", zoneCode=" + zoneCode + ", rlyTid=" + rlyTid + ", clientAppCode=" + clientAppCode + "]";
	}


}
