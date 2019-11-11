package com.awl.tch.model;

import com.awl.tch.annotation.Column;
import com.awl.tch.annotation.ICurrentTimestamp;
import com.awl.tch.annotation.Id;
import com.awl.tch.annotation.Table;
import com.awl.tch.annotation.UCurrentTimestamp;

@Table(name = "TCH_UPI_QR_CODE")
public class QRRequestDTO{
	 
	   @Id
	   @Column(name = "UPI_TRID")
	   private String trId;
	   
	   @Column(name = "UPI_MID")
	   private String mid;
	   
	   @Column(name = "UPI_TID")
	   private String tid;
	   
	   @Column(name = "UPI_BANK_CODE")
	   private String bankCode;
	   
	   @Column(name = "UPI_STATUS")
	   private String status;
	   
	   @Column(name = "UPI_RRN")
	   private String rrn;
	   
	   @Column(name = "UPI_PRGRAM_TYPE")
	   private String prgType;
	   
	   @Column(name = "UPI_AUTHCODE")
	   private String authcd;
	   
	   @Column(name = "UPI_MESSAGE")
	   private String message;
	   
	   @Column(name = "UPI_UNIQUE_REFVALUE")
	   private String unqRefValue;
	   
	   @Column(name = "UPI_QR_RESPONSE_STRING")
	   private String qrResponseString;
	   
	   @Column(name ="UPI_CREATED")
	   @ICurrentTimestamp
	   private String upiCreated;
	   
	   @Column(name ="UPI_UPDATED")
	   @UCurrentTimestamp
	   private String upiUpdated;

	/**
	 * @return the trId
	 */
	public String getTrId() {
		return trId;
	}

	/**
	 * @param trId the trId to set
	 */
	public void setTrId(String trId) {
		this.trId = trId;
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
	 * @return the prgType
	 */
	public String getPrgType() {
		return prgType;
	}

	/**
	 * @param prgType the prgType to set
	 */
	public void setPrgType(String prgType) {
		this.prgType = prgType;
	}

	/**
	 * @return the authcd
	 */
	public String getAuthcd() {
		return authcd;
	}

	/**
	 * @param authcd the authcd to set
	 */
	public void setAuthcd(String authcd) {
		this.authcd = authcd;
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
	 * @return the qrResponseString
	 */
	public String getQrResponseString() {
		return qrResponseString;
	}

	/**
	 * @param qrResponseString the qrResponseString to set
	 */
	public void setQrResponseString(String qrResponseString) {
		this.qrResponseString = qrResponseString;
	}

	/**
	 * @return the upiCreated
	 */
	public String getUpiCreated() {
		return upiCreated;
	}

	/**
	 * @param upiCreated the upiCreated to set
	 */
	public void setUpiCreated(String upiCreated) {
		this.upiCreated = upiCreated;
	}

	/**
	 * @return the upiUpdated
	 */
	public String getUpiUpdated() {
		return upiUpdated;
	}

	/**
	 * @param upiUpdated the upiUpdated to set
	 */
	public void setUpiUpdated(String upiUpdated) {
		this.upiUpdated = upiUpdated;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "QRRequestDTO [trId=" + trId + ", mid=" + mid + ", tid=" + tid
				+ ", bankCode=" + bankCode + ", status=" + status + ", rrn="
				+ rrn + ", prgType=" + prgType + ", authcd=" + authcd
				+ ", message=" + message + ", qrResponseString="
				+ qrResponseString + "]";
	}

	public String getUnqRefValue() {
		return unqRefValue;
	}

	public void setUnqRefValue(String unqRefValue) {
		this.unqRefValue = unqRefValue;
	}
}