package com.tch.sbiepay.exception;

public class TCHSbiepayException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4430495239907369426L;
	
	private String errorCode;
	private String erroMessage;
	private String responseCode;

	public TCHSbiepayException(String message) {
		super(message);
	}
	public TCHSbiepayException(String errorCode, String errorMessage, String responseCode) {
		this(errorMessage);
		this.errorCode = errorCode;
		this.erroMessage = errorMessage;
		this.responseCode = responseCode;
	}
	public TCHSbiepayException(String errorCode,String errorMessage){
		this(errorCode, errorMessage,null);
	}
	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}
	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	/**
	 * @return the erroMessage
	 */
	public String getErroMessage() {
		return erroMessage;
	}
	/**
	 * @param erroMessage the erroMessage to set
	 */
	public void setErroMessage(String erroMessage) {
		this.erroMessage = erroMessage;
	}
	/**
	 * @return the responseCode
	 */
	public String getResponseCode() {
		return responseCode;
	}
	/**
	 * @param responseCode the responseCode to set
	 */
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	
	 
}
