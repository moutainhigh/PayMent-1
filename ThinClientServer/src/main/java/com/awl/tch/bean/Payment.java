package com.awl.tch.bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.awl.tch.annotation.Mandate;
import com.awl.tch.annotation.Transient;
import com.awl.tch.brandemi.model.ValidationParameter;
import com.awl.tch.constant.Constants;
import com.awl.tch.model.PaymentAmexDTO;
import com.awl.tch.model.PaymentAuthDTO;
import com.awl.tch.model.PaymentDTO;
import com.awl.tch.model.QRRequestDTO;
import com.google.gson.annotations.SerializedName;

/*
 * @Mandate annotation used to mention mandatory field , so that Default value will be got set for mandatory field if it is String
 *  
 */
/**
 * @author ashish.bhavsar
 *
 */
public class Payment {

	@SerializedName("binNum")
	@Mandate
	private String binNumber;

	@SerializedName("orgAmt")
	@Mandate
	private String originalAmount;

	@SerializedName("exgRt")
	@Mandate
	private String exchangeRate;

	@SerializedName("crdEntMd")
	private String cardEntryMode;

	@SerializedName("curCd")
	@Mandate
	private String currencyCode;

	@SerializedName("curNm")
	@Mandate
	private String currencyName;

	@SerializedName("mrkup")
	@Mandate
	private String markup;

	@SerializedName("mid")
	@Mandate
	private String merchantId;

	@SerializedName("tid")
	@Mandate
	private String terminalId;

	/**
	 * AAB related json value
	 */
	@SerializedName("appNm")
	private String appName;

	@SerializedName("cltId")
	@Mandate
	private String clientId;

	@SerializedName("invNum")
	@Mandate
	private String invoiceNumber;

	@SerializedName("trk2")
	private String track2;

	@SerializedName("fld55")
	@Mandate
	private String field55;

	@SerializedName("addAmt")
	@Mandate
	private String additionalAmount;

	@SerializedName("accTyp")
	private String accountType;

	@SerializedName("tSerNum")
	@Mandate
	private String terminalSerialNumber;

	@SerializedName("crdLbl")
	@Mandate
	private String cardLabel;

	@SerializedName("dt")
	@Mandate
	private String date;

	@SerializedName("tm")
	@Mandate
	private String time;

	@SerializedName("sessKey")
	private String sessionKey;

	@SerializedName("desflg")
	@Mandate
	private String decisionFlag;

	@SerializedName("refVl")
	private String referenceValue;

	@SerializedName("dccAmt")
	@Mandate
	private String dccAmount;

	@SerializedName("rrn")
	@Mandate
	private String retrivalRefNumber;

	@SerializedName("bthNum")
	@Mandate
	private String batchNumber;

	@SerializedName("tnxCnt")
	private String noOfCount;

	@SerializedName("tnxSchId")
	@Mandate
	private String schemaTransactionId;

	@SerializedName("panNum")
	private String panNumber;

	@SerializedName("expDt")
	private String expiryDate;

	@SerializedName("lst4DgtVl")
	private String lastFourDigitValue;

	@SerializedName("isFld55")
	private String issuerField55;

	@SerializedName("pnBlk")
	private String pinBlock;

	@SerializedName("authCd")
	private String authorizationId;

	@SerializedName("stNum")
	private String stanNumber;
	/*
	 * AAB related json value
	 */
	@SerializedName("unqId")
	private String branchCode;

	@SerializedName("totDcc")
	private String totalDcc;

	@SerializedName("totInr")
	private String totalInr;

	@SerializedName("pinBypas")
	private String pinBypass;
	
	/*
	 * UPI related changes
	 */
	@SerializedName("qrStr")
	private String qrString;
	
	@SerializedName("qrTyp")
	private String qrType;
	
	@SerializedName("custNm")
	private String customerName;
	
	@SerializedName("hndTx")
	private String hundredTxn;
	
	
	
	private transient String internationalFlag;
	
	
	private transient String hostId;
	
	
	/**
	 * @return the internationalFlag
	 */
	public String getInternationalFlag() {
		return internationalFlag;
	}

	/**
	 * @param internationalFlag the internationalFlag to set
	 */
	public void setInternationalFlag(String internationalFlag) {
		this.internationalFlag = internationalFlag;
	}

	/**
	 * @return the hostId
	 */
	public String getHostId() {
		return hostId;
	}

	/**
	 * @param hostId the hostId to set
	 */
	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	/**
	 * @return the hundredTxn
	 */
	public String getHundredTxn() {
		return hundredTxn;
	}

	/**
	 * @param hundredTxn the hundredTxn to set
	 */
	public void setHundredTxn(String hundredTxn) {
		this.hundredTxn = hundredTxn;
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

	/*
	 * End of UPI related changes
	 */
	/**
	 * @return the qrString
	 */
	public String getQrString() {
		return qrString;
	}

	/**
	 * @param qrString the qrString to set
	 */
	public void setQrString(String qrString) {
		this.qrString = qrString;
	}

	/**
	 * @return the qrType
	 */
	public String getQrType() {
		return qrType;
	}

	/**
	 * @param qrType the qrType to set
	 */
	public void setQrType(String qrType) {
		this.qrType = qrType;
	}

	/**
	 * @return the pinBypass
	 */
	public String getPinBypass() {
		return pinBypass;
	}

	/**
	 * @param pinBypass
	 *            the pinBypass to set
	 */
	public void setPinBypass(String pinBypass) {
		this.pinBypass = pinBypass;
	}

	/*
	 * AAB related json value
	 */
	@SerializedName("brnNm")
	private String branchName;

	@SerializedName("dspMsg")
	private String displayMessage;

	@SerializedName("applicatioId")
	private String applicationId;

	@SerializedName("xtrDtObj")
	private ExtraObject[] extraDataObject;

	@SerializedName("emiObj")
	private EMIObject emiObject;

	@SerializedName("prtObj")
	DataPrintObject[] dataPrintObject;

	@SerializedName(value = "hltObj")
	private HealthObject healthObject;

	@SerializedName(value = "imeiNum")
	private String IMIENumber;

	@SerializedName(value = "mrMbNum")
	private String mobileNumber;

	@SerializedName("billObj")
	private BillingObj[] billingObject;
	
	@SerializedName("menuObj")
	private FirstMenu[] menuobj;
	
	@Transient
	private String extraInfo;
	
	/*
	 * @SerializedName("extraDataName") private String extraDataName;
	 * 
	 * @SerializedName("extraDataVal") private String extraDataValue;
	 * 
	 * @SerializedName("cPrintValObj") private String cprintValObject;
	 * 
	 * @SerializedName("cPrintValName") private String cprintValName;
	 * 
	 * @SerializedName("cPrintValData") private String cprintValData;
	 * 
	 * @SerializedName("cPrintPromoObj") private String cprintPromoObject;
	 * 
	 * @SerializedName("cPrintPromoMsg") private String cprintPromoMessage;
	 * 
	 * @SerializedName("cPrintBMP") private String cprintBMP;
	 */
	

	public String getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}

	/**
	 * @return the iMIENumber
	 */
	public String getIMIENumber() {
		return IMIENumber;
	}

	/**
	 * @return the menuobj
	 */
	public FirstMenu[] getMenuobj() {
		return menuobj;
	}

	/**
	 * @param menuobj the menuobj to set
	 */
	public void setMenuobj(FirstMenu[] menuobj) {
		this.menuobj = menuobj;
	}

	/**
	 * @return the billingObject
	 */
	public BillingObj[] getBillingObject() {
		return billingObject;
	}

	/**
	 * @param billingObject the billingObject to set
	 */
	public void setBillingObject(BillingObj[] billingObject) {
		this.billingObject = billingObject;
	}

	/**
	 * @param iMIENumber
	 *            the iMIENumber to set
	 */
	public void setIMIENumber(String iMIENumber) {
		IMIENumber = iMIENumber;
	}

	/**
	 * @return the mobileNumber
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * @param mobileNumber
	 *            the mobileNumber to set
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * @return the branchCode
	 */
	public String getBranchCode() {
		return branchCode;
	}

	/**
	 * @return the totalDcc
	 */
	public String getTotalDcc() {
		return totalDcc;
	}

	/**
	 * @param totalDcc
	 *            the totalDcc to set
	 */
	public void setTotalDcc(String totalDcc) {
		this.totalDcc = totalDcc;
	}

	/**
	 * @return the totalInr
	 */
	public String getTotalInr() {
		return totalInr;
	}

	/**
	 * @param totalInr
	 *            the totalInr to set
	 */
	public void setTotalInr(String totalInr) {
		this.totalInr = totalInr;
	}

	/**
	 * @return the healthObject
	 */
	public HealthObject getHealthObject() {
		return healthObject;
	}

	/**
	 * @param healthObject
	 *            the healthObject to set
	 */
	public void setHealthObject(HealthObject healthObject) {
		this.healthObject = healthObject;
	}

	/**
	 * @return the displayMessage
	 */
	public String getDisplayMessage() {
		return displayMessage;
	}

	/**
	 * @param displayMessage
	 *            the displayMessage to set
	 */
	public void setDisplayMessage(String displayMessage) {
		this.displayMessage = displayMessage;
	}

	/**
	 * @param branchCode
	 *            the branchCode to set
	 */
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	/**
	 * @return the branchName
	 */
	public String getBranchName() {
		return branchName;
	}

	/**
	 * @param branchName
	 *            the branchName to set
	 */
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	private transient String nii;
	private transient Integer cardType;
	private transient String tpdu;

	private transient String responseCode;
	private transient String refundNewFlag;
	private transient String cashBackFlag;
	private transient String preAuthFlag;
	private transient String tipAdjustFlag;
	private transient String tipPercent;
	private transient String autoSettlement;
	private transient String cashbackPur;
	private transient Long paymentId;
	private transient String tipApproved;
	private transient String refundApproved;
	private transient String field63;
	private transient String processingCode;
	private transient String MTI;
	private transient String newStanForSettlement;
	private transient String emiTransactionFlag;
	private transient String dccTransactionFlag;
	private transient Integer binRangeValue;
	private transient String tempSerialNumber;
	private transient String balEnqAllowed;
	private transient Integer noOfDecimal;
	private transient String seNumber;
	private transient String isoDCCFlag;
	private transient String tableId;
	private transient String saleOfflineFlag;
	private transient String bankCode;
	private transient String status;
	private transient Integer tempBatchNumber;
	
	/*
	 * Brand emi value
	 */
	private transient String velocityFlag;
	private transient String velocityCount;
	private transient String velocityNoOfDays;
	private transient String velocityDHMY;
	private transient String dealerCode;
	private transient String manufacturerCode;
	private transient String productCode;
	private transient String skuCode;
	private transient String schemeValue;
	private transient String emiIndicator;         //Added by Saloni Jindal on 7-09-2017
	private transient String amexIndicator;
	private transient String saleAckWrong;
	private transient String normaltxn;
	private transient String preauthPer;
	
	
	/*
	 * end of Brand emi value
	 */
	
	/*
	 * Change in code by Saloni Jindal on 7-09-2017
	 */
	private transient String cardTypeValue; // added by ashish for irctc
	private transient String cardProviderValue; // added by ashish for irctc
	private transient String irctcRefValue;
	
	private transient String binType;
	
	private transient String cgsttax;
	private transient String igsttax;
	private transient String feePer;
	private transient String feeValue;
	private transient String totalValue;
	private transient String feeFlag; 
	private transient String cgstPer;
	private transient String sgstPer;
	private transient String fixed;
	
	
	/**
	 * @return the fixed
	 */
	public String getFixed() {
		return fixed;
	}

	/**
	 * @param fixed the fixed to set
	 */
	public void setFixed(String fixed) {
		this.fixed = fixed;
	}

	/**
	 * @return the totalValue
	 */
	public String getTotalValue() {
		return totalValue;
	}

	/**
	 * @param totalValue the totalValue to set
	 */
	public void setTotalValue(String totalValue) {
		this.totalValue = totalValue;
	}

	/**
	 * @return the cgstPer
	 */
	public String getCgstPer() {
		return cgstPer;
	}

	/**
	 * @param cgstPer the cgstPer to set
	 */
	public void setCgstPer(String cgstPer) {
		this.cgstPer = cgstPer;
	}

	/**
	 * @return the sgstPer
	 */
	public String getSgstPer() {
		return sgstPer;
	}

	/**
	 * @param sgstPer the sgstPer to set
	 */
	public void setSgstPer(String sgstPer) {
		this.sgstPer = sgstPer;
	}

	/**
	 * @return the feePer
	 */
	public String getFeePer() {
		return feePer;
	}

	/**
	 * @param feePer the feePer to set
	 */
	public void setFeePer(String feePer) {
		this.feePer = feePer;
	}

	/**
	 * @return the feeFlag
	 */
	public String getFeeFlag() {
		return feeFlag;
	}

	/**
	 * @param feeFlag the feeFlag to set
	 */
	public void setFeeFlag(String feeFlag) {
		this.feeFlag = feeFlag;
	}

	/**
	 * @return the igsttax
	 */
	public String getIgsttax() {
		return igsttax;
	}

	/**
	 * @param igsttax the igsttax to set
	 */
	public void setIgsttax(String igsttax) {
		this.igsttax = igsttax;
	}

	/**
	 * @return the feeValue
	 */
	public String getFeeValue() {
		return feeValue;
	}

	/**
	 * @param feeValue the feeValue to set
	 */
	public void setFeeValue(String feeValue) {
		this.feeValue = feeValue;
	}

	/**
	 * @return the cgsttax
	 */
	public String getCgsttax() {
		return cgsttax;
	}

	/**
	 * @param cgsttax the cgsttax to set
	 */
	public void setCgsttax(String cgsttax) {
		this.cgsttax = cgsttax;
	}

	/**
	 * @return the binType
	 */
	public String getBinType() {
		return binType;
	}

	/**
	 * @param binType the binType to set
	 */
	public void setBinType(String binType) {
		this.binType = binType;
	}

	public String getIrctcRefValue() {
		return irctcRefValue;
	}

	public void setIrctcRefValue(String irctcRefValue) {
		this.irctcRefValue = irctcRefValue;
	}

	/**
	 * @return the cardTypeValue
	 */
	public String getCardTypeValue() {
		return cardTypeValue;
	}

	/**
	 * @param cardTypeValue the cardTypeValue to set
	 */
	public void setCardTypeValue(String cardTypeValue) {
		this.cardTypeValue = cardTypeValue;
	}

	/**
	 * @return the cardProviderValue
	 */
	public String getCardProviderValue() {
		return cardProviderValue;
	}

	/**
	 * @param cardProviderValue the cardProviderValue to set
	 */
	public void setCardProviderValue(String cardProviderValue) {
		this.cardProviderValue = cardProviderValue;
	}

	/**
	 * @return the preauthPer
	 */
	public String getPreauthPer() {
		return preauthPer;
	}

	/**
	 * @param preauthPer the preauthPer to set
	 */
	public void setPreauthPer(String preauthPer) {
		this.preauthPer = preauthPer;
	}

	//BRAND EMI CHANGES
	private transient ValidationParameter validationParameter;
	private transient String[] productDetails;
	private transient String[] manufactureDetails;
	//END OF BRAND EMI CHANGES
	
	
	/**
	 * @return the emiIndicator
	 */
	public String getEmiIndicator() {
		return emiIndicator;
	}
	
	/**
	 * @return the tempBatchNumber
	 */
	public Integer getTempBatchNumber() {
		return tempBatchNumber;
	}

	/**
	 * @param tempBatchNumber the tempBatchNumber to set
	 */
	public void setTempBatchNumber(Integer tempBatchNumber) {
		this.tempBatchNumber = tempBatchNumber;
	}

	/**
	 * @return the saleAckWrong
	 */
	public String getSaleAckWrong() {
		return saleAckWrong;
	}

	/**
	 * @param saleAckWrong the saleAckWrong to set
	 */
	public void setSaleAckWrong(String saleAckWrong) {
		this.saleAckWrong = saleAckWrong;
	}

	/**
	 * @return the normaltxn
	 */
	public String getNormaltxn() {
		return normaltxn;
	}

	/**
	 * @param normaltxn the normaltxn to set
	 */
	public void setNormaltxn(String normaltxn) {
		this.normaltxn = normaltxn;
	}

	/**
	 * @return the amexIndicator
	 */
	public String getAmexIndicator() {
		return amexIndicator;
	}

	/**
	 * @param amexIndicator the amexIndicator to set
	 */
	public void setAmexIndicator(String amexIndicator) {
		this.amexIndicator = amexIndicator;
	}

	/**
	 * @return the validationParameter
	 */
	public ValidationParameter getValidationParameter() {
		return validationParameter;
	}

	/**
	 * @param validationParameter the validationParameter to set
	 */
	public void setValidationParameter(ValidationParameter validationParameter) {
		this.validationParameter = validationParameter;
	}

	/**
	 * @return the productDetails
	 */
	public String[] getProductDetails() {
		return productDetails;
	}

	/**
	 * @param productDetails the productDetails to set
	 */
	public void setProductDetails(String[] productDetails) {
		this.productDetails = productDetails;
	}

	/**
	 * @return the manufactureDetails
	 */
	public String[] getManufactureDetails() {
		return manufactureDetails;
	}

	/**
	 * @param manufactureDetails the manufactureDetails to set
	 */
	public void setManufactureDetails(String[] manufactureDetails) {
		this.manufactureDetails = manufactureDetails;
	}

	/**
	 * @param emiIndicator the emiIndicator to set
	 */

	public void setEmiIndicator(String emiIndicator) {
		this.emiIndicator = emiIndicator;
	}
	
	// End of changes

	/**
	 * @return the skuCode
	 */
	
	public String getSkuCode() {
		return skuCode;
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
	 * @return the velocityDHMY
	 */
	public String getVelocityDHMY() {
		return velocityDHMY;
	}

	/**
	 * @param velocityDHMY the velocityDHMY to set
	 */
	public void setVelocityDHMY(String velocityDHMY) {
		this.velocityDHMY = velocityDHMY;
	}

	/**
	 * @return the productCode
	 */
	public String getProductCode() {
		return productCode;
	}

	/**
	 * @param productCode the productCode to set
	 */
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	/**
	 * @return the schemeValue
	 */
	public String getSchemeValue() {
		return schemeValue;
	}

	/**
	 * @param schemeValue the schemeValue to set
	 */
	public void setSchemeValue(String schemeValue) {
		this.schemeValue = schemeValue;
	}

	/**
	 * @param skuCode the skuCode to set
	 */
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	/**
	 * @return the velocityNoOfDays
	 */
	public String getVelocityNoOfDays() {
		return velocityNoOfDays;
	}

	/**
	 * @param velocityNoOfDays the velocityNoOfDays to set
	 */
	public void setVelocityNoOfDays(String velocityNoOfDays) {
		this.velocityNoOfDays = velocityNoOfDays;
	}

	/**
	 * @return the dealerCode
	 */
	public String getDealerCode() {
		return dealerCode;
	}

	/**
	 * @param dealerCode the dealerCode to set
	 */
	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}

	/**
	 * @return the manufacturerCode
	 */
	public String getManufacturerCode() {
		return manufacturerCode;
	}

	/**
	 * @param manufacturerCode the manufacturerCode to set
	 */
	public void setManufacturerCode(String manufacturerCode) {
		this.manufacturerCode = manufacturerCode;
	}

	/**
	 * @return the velocityCount
	 */
	public String getVelocityCount() {
		return velocityCount;
	}

	/**
	 * @param velocityCount
	 *            the velocityCount to set
	 */
	public void setVelocityCount(String velocityCount) {
		this.velocityCount = velocityCount;
	}

	/**
	 * @return the velocityFlag
	 */
	public String getVelocityFlag() {
		return velocityFlag;
	}

	/**
	 * @param velocityFlag the velocityFlag to set
	 */
	public void setVelocityFlag(String velocityFlag) {
		this.velocityFlag = velocityFlag;
	}

	// Changes as per discussion with stak holder for reversal 16/05/2017 by
	// ashish
	private transient Integer reversalCount;

	/**
	 * @return the reversalCount
	 */
	public Integer getReversalCount() {
		return reversalCount;
	}

	/**
	 * @param reversalCount
	 *            the reversalCount to set
	 */
	public void setReversalCount(Integer reversalCount) {
		this.reversalCount = reversalCount;
	}

	/**
	 * @return the saleOfflineFlag
	 */
	public String getSaleOfflineFlag() {
		return saleOfflineFlag;
	}

	/**
	 * @param saleOfflineFlag
	 *            the saleOfflineFlag to set
	 */
	public void setSaleOfflineFlag(String saleOfflineFlag) {
		this.saleOfflineFlag = saleOfflineFlag;
	}

	/**
	 * @return the tableId
	 */
	public String getTableId() {
		return tableId;
	}

	/**
	 * @param tableId
	 *            the tableId to set
	 */
	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	/**
	 * @return the isoDCCFlag
	 */
	public String getIsoDCCFlag() {
		return isoDCCFlag;
	}

	/**
	 * @param isoDCCFlag
	 *            the isoDCCFlag to set
	 */
	public void setIsoDCCFlag(String isoDCCFlag) {
		this.isoDCCFlag = isoDCCFlag;
	}

	/**
	 * @return the seNumber
	 */
	public String getSeNumber() {
		return seNumber;
	}

	/**
	 * @param seNumber
	 *            the seNumber to set
	 */
	public void setSeNumber(String seNumber) {
		this.seNumber = seNumber;
	}

	// AAB related variable
	private transient String url;

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the noOfDecimal
	 */
	public Integer getNoOfDecimal() {
		return noOfDecimal;
	}

	/**
	 * @param noOfDecimal
	 *            the noOfDecimal to set
	 */
	public void setNoOfDecimal(Integer noOfDecimal) {
		this.noOfDecimal = noOfDecimal;
	}

	/**
	 * @return the appName
	 */
	public String getAppName() {
		return appName;
	}

	/**
	 * @param appName
	 *            the appName to set
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}

	/**
	 * @return the balEnqAllowed
	 */
	public String getBalEnqAllowed() {
		return balEnqAllowed;
	}

	/**
	 * @param balEnqAllowed
	 *            the balEnqAllowed to set
	 */
	public void setBalEnqAllowed(String balEnqAllowed) {
		this.balEnqAllowed = balEnqAllowed;
	}

	/**
	 * @return the noOfCount
	 */
	public String getNoOfCount() {
		return noOfCount;
	}

	/**
	 * @param noOfCount
	 *            the noOfCount to set
	 */
	public void setNoOfCount(String noOfCount) {
		this.noOfCount = noOfCount;
	}

	/**
	 * @return the tempSerialNumber
	 */
	public String getTempSerialNumber() {
		return tempSerialNumber;
	}

	/**
	 * @param tempSerialNumber
	 *            the tempSerialNumber to set
	 */
	public void setTempSerialNumber(String tempSerialNumber) {
		this.tempSerialNumber = tempSerialNumber;
	}

	/**
	 * @return the binRangeValue
	 */
	public Integer getBinRangeValue() {
		return binRangeValue;
	}

	/**
	 * @param binRangeValue
	 *            the binRangeValue to set
	 */
	public void setBinRangeValue(Integer binRangeValue) {
		this.binRangeValue = binRangeValue;
	}

	/**
	 * @return the dccTransactionFlag
	 */
	public String getDccTransactionFlag() {
		return dccTransactionFlag;
	}

	/**
	 * @param dccTransactionFlag
	 *            the dccTransactionFlag to set
	 */
	public void setDccTransactionFlag(String dccTransactionFlag) {
		this.dccTransactionFlag = dccTransactionFlag;
	}

	/**
	 * @return the dataPrintObject
	 */
	public DataPrintObject[] getDataPrintObject() {
		return dataPrintObject;
	}

	/**
	 * @param dataPrintObject
	 *            the dataPrintObject to set
	 */
	public void setDataPrintObject(DataPrintObject[] dataPrintObject) {
		this.dataPrintObject = dataPrintObject;
	}

	/**
	 * @return the emiTransactionFlag
	 */
	public String getEmiTransactionFlag() {
		return emiTransactionFlag;
	}

	/**
	 * @param emiTransactionFlag
	 *            the emiTransactionFlag to set
	 */
	public void setEmiTransactionFlag(String emiTransactionFlag) {
		this.emiTransactionFlag = emiTransactionFlag;
	}

	/**
	 * @return the emiObject
	 */
	public EMIObject getEmiObject() {
		return emiObject;
	}

	/**
	 * @param emiObject
	 *            the emiObject to set
	 */
	public void setEmiObject(EMIObject emiObject) {
		this.emiObject = emiObject;
	}

	/**
	 * @return the newStanForSettlement
	 */
	public String getNewStanForSettlement() {
		return newStanForSettlement;
	}

	/**
	 * @param newStanForSettlement
	 *            the newStanForSettlement to set
	 */
	public void setNewStanForSettlement(String newStanForSettlement) {
		this.newStanForSettlement = newStanForSettlement;
	}

	/**
	 * @return the mTI
	 */
	public String getMTI() {
		return MTI;
	}

	/**
	 * @param mTI
	 *            the mTI to set
	 */
	public void setMTI(String mTI) {
		MTI = mTI;
	}

	/**
	 * @return the processingCode
	 */
	public String getProcessingCode() {
		return processingCode;
	}

	/**
	 * @param processingCode
	 *            the processingCode to set
	 */
	public void setProcessingCode(String processingCode) {
		this.processingCode = processingCode;
	}

	/**
	 * @return the field63
	 */
	public String getField63() {
		return field63;
	}

	/**
	 * @param field63
	 *            the field63 to set
	 */
	public void setField63(String field63) {
		this.field63 = field63;
	}

	/**
	 * @return the refundApproved
	 */
	public String getRefundApproved() {
		return refundApproved;
	}

	/**
	 * @return the applicationId
	 */
	public String getApplicationId() {
		return applicationId;
	}

	/**
	 * @param applicationId
	 *            the applicationId to set
	 */
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	/**
	 * @param refundApproved
	 *            the refundApproved to set
	 */
	public void setRefundApproved(String refundApproved) {
		this.refundApproved = refundApproved;
	}

	/**
	 * @return the tipApproved
	 */
	public String getTipApproved() {
		return tipApproved;
	}

	/**
	 * @param tipApproved
	 *            the tipApproved to set
	 */
	public void setTipApproved(String tipApproved) {
		this.tipApproved = tipApproved;
	}

	/**
	 * @return the cashbackPur
	 */
	public String getCashbackPur() {
		return cashbackPur;
	}

	/**
	 * @param cashbackPur
	 *            the cashbackPur to set
	 */
	public void setCashbackPur(String cashbackPur) {
		this.cashbackPur = cashbackPur;
	}

	/**
	 * @return the cardType
	 */
	public Integer getCardType() {
		return cardType;
	}

	/**
	 * @param cardType
	 *            the cardType to set
	 */
	public void setCardType(Integer cardType) {
		this.cardType = cardType;
	}

	/**
	 * @return the autoSettlement
	 */
	public String getAutoSettlement() {
		return autoSettlement;
	}

	/**
	 * @param autoSettlement
	 *            the autoSettlement to set
	 */
	public void setAutoSettlement(String autoSettlement) {
		this.autoSettlement = autoSettlement;
	}

	/**
	 * @return the tipAdjustFlag
	 */
	public String getTipAdjustFlag() {
		return tipAdjustFlag;
	}

	/**
	 * @param tipAdjustFlag
	 *            the tipAdjustFlag to set
	 */
	public void setTipAdjustFlag(String tipAdjustFlag) {
		this.tipAdjustFlag = tipAdjustFlag;
	}

	/**
	 * @return the tipPercent
	 */
	public String getTipPercent() {
		return tipPercent;
	}

	/**
	 * @param tipPercent
	 *            the tipPercent to set
	 */
	public void setTipPercent(String tipPercent) {
		this.tipPercent = tipPercent;
	}

	/**
	 * @return the cashBackFlag
	 */
	public String getCashBackFlag() {
		return cashBackFlag;
	}

	/**
	 * @param cashBackFlag
	 *            the cashBackFlag to set
	 */
	public void setCashBackFlag(String cashBackFlag) {
		this.cashBackFlag = cashBackFlag;
	}

	/**
	 * @return the preAuthFlag
	 */
	public String getPreAuthFlag() {
		return preAuthFlag;
	}

	/**
	 * @param preAuthFlag
	 *            the preAuthFlag to set
	 */
	public void setPreAuthFlag(String preAuthFlag) {
		this.preAuthFlag = preAuthFlag;
	}

	/**
	 * @return the refundNewFlag
	 */
	public String getRefundNewFlag() {
		return refundNewFlag;
	}

	/**
	 * @param refundNewFlag
	 *            the refundNewFlag to set
	 */
	public void setRefundNewFlag(String refundNewFlag) {
		this.refundNewFlag = refundNewFlag;
	}

	/**
	 * @return the nii
	 */
	public String getNii() {
		return nii;
	}

	/**
	 * @param nii
	 *            the nii to set
	 */
	public void setNii(String nii) {
		this.nii = nii;
	}

	public String getStanNumber() {
		return stanNumber;
	}

	/**
	 * @return the paymentId
	 */
	public Long getPaymentId() {
		return paymentId;
	}

	/**
	 * @param paymentId
	 *            the paymentId to set
	 */
	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}

	public void setStanNumber(String stanNumber) {
		this.stanNumber = stanNumber;
	}

	public String getTpdu() {
		return tpdu;
	}

	public void setTpdu(String tpdu) {
		this.tpdu = tpdu;
	}

	public String getBinNumber() {
		return binNumber;
	}

	public void setBinNumber(String binNumber) {
		this.binNumber = binNumber;
	}

	public String getOriginalAmount() {
		return originalAmount;
	}

	public void setOriginalAmount(String originalAmount) {
		this.originalAmount = originalAmount;
	}

	public String getCardEntryMode() {
		return cardEntryMode;
	}

	public void setCardEntryMode(String cardEntryMode) {
		this.cardEntryMode = cardEntryMode;
	}

	public String getExchangeRate() {
		return exchangeRate;
	}

	/**
	 * @return the issuerField55
	 */
	public String getIssuerField55() {
		return issuerField55;
	}

	/**
	 * @return the extraDataObject
	 */
	public ExtraObject[] getExtraDataObject() {
		return extraDataObject;
	}

	/**
	 * @param extraDataObject
	 *            the extraDataObject to set
	 */
	public void setExtraDataObject(ExtraObject[] extraDataObject) {
		this.extraDataObject = extraDataObject;
	}

	/**
	 * @param issuerField55
	 *            the issuerField55 to set
	 */
	public void setIssuerField55(String issuerField55) {
		this.issuerField55 = issuerField55;
	}

	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public String getMarkup() {
		return markup;
	}

	public void setMarkup(String markup) {
		this.markup = markup;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	/**
	 * @return the track2
	 */
	public String getTrack2() {
		return track2;
	}

	/**
	 * @param track2
	 *            the track2 to set
	 */
	public void setTrack2(String track2) {
		this.track2 = track2;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getField55() {
		return field55;
	}

	public void setField55(String field55) {
		this.field55 = field55;
	}

	public String getAdditionalAmount() {
		return additionalAmount;
	}

	public void setAdditionalAmount(String additionalAmount) {
		this.additionalAmount = additionalAmount;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getTerminalSerialNumber() {
		return terminalSerialNumber;
	}

	public void setTerminalSerialNumber(String terminalSerialNumber) {
		this.terminalSerialNumber = terminalSerialNumber;
	}

	public String getCardLabel() {
		return cardLabel;
	}

	public void setCardLabel(String cardLabel) {
		this.cardLabel = cardLabel;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	/**
	 * @return the pinBlock
	 */
	public String getPinBlock() {
		return pinBlock;
	}

	/**
	 * @param pinBlock
	 *            the pinBlock to set
	 */
	public void setPinBlock(String pinBlock) {
		this.pinBlock = pinBlock;
	}

	public String getDecisionFlag() {
		return decisionFlag;
	}

	public void setDecisionFlag(String decisionFlag) {
		this.decisionFlag = decisionFlag;
	}

	/**
	 * @return the retrivalRefNumber
	 */
	public String getRetrivalRefNumber() {
		return retrivalRefNumber;
	}

	/**
	 * @param retrivalRefNumber
	 *            the retrivalRefNumber to set
	 */
	public void setRetrivalRefNumber(String retrivalRefNumber) {
		this.retrivalRefNumber = retrivalRefNumber;
	}

	public String getReferenceValue() {
		return referenceValue;
	}

	public void setReferenceValue(String referenceValue) {
		this.referenceValue = referenceValue;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		if(batchNumber != null)
		if(batchNumber.length() != 6){
			this.batchNumber = String.format("%06d", Integer.parseInt(batchNumber));
		}else
		this.batchNumber = batchNumber;
	}

	/*
	 * public String getExtraDataName() { return extraDataName; }
	 * 
	 * public void setExtraDataName(String extraDataName) { this.extraDataName =
	 * extraDataName; }
	 * 
	 * public String getExtraDataValue() { return extraDataValue; }
	 * 
	 * public void setExtraDataValue(String extraDataValue) {
	 * this.extraDataValue = extraDataValue; }
	 */

	public String getDccAmount() {
		return dccAmount;
	}

	public void setDccAmount(String dccAmount) {
		this.dccAmount = dccAmount;
	}

	/*
	 * public String getCprintValObject() { return cprintValObject; }
	 * 
	 * public void setCprintValObject(String cprintValObject) {
	 * this.cprintValObject = cprintValObject; }
	 * 
	 * public String getCprintValName() { return cprintValName; }
	 * 
	 * public void setCprintValName(String cprintValName) { this.cprintValName =
	 * cprintValName; }
	 * 
	 * public String getCprintValData() { return cprintValData; }
	 * 
	 * public void setCprintValData(String cprintValData) { this.cprintValData =
	 * cprintValData; }
	 * 
	 * public String getCprintPromoObject() { return cprintPromoObject; }
	 * 
	 * public void setCprintPromoObject(String cprintPromoObject) {
	 * this.cprintPromoObject = cprintPromoObject; }
	 * 
	 * public String getCprintPromoMessage() { return cprintPromoMessage; }
	 * 
	 * public void setCprintPromoMessage(String cprintPromoMessage) {
	 * this.cprintPromoMessage = cprintPromoMessage; }
	 * 
	 * public String getCprintBMP() { return cprintBMP; }
	 * 
	 * public void setCprintBMP(String cprintBMP) { this.cprintBMP = cprintBMP;
	 * }
	 */

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	/**
	 * @return the schemaTransactionId
	 */
	public String getSchemaTransactionId() {
		return schemaTransactionId;
	}

	/**
	 * @param schemaTransactionId
	 *            the schemaTransactionId to set
	 */
	public void setSchemaTransactionId(String schemaTransactionId) {
		this.schemaTransactionId = schemaTransactionId;
	}

	/**
	 * @return the lastFourDigitValue
	 */
	public String getLastFourDigitValue() {
		return lastFourDigitValue;
	}

	/**
	 * @param lastFourDigitValue
	 *            the lastFourDigitValue to set
	 */
	public void setLastFourDigitValue(String lastFourDigitValue) {
		this.lastFourDigitValue = lastFourDigitValue;
	}

	/**
	 * @return the authorizationId
	 */
	public String getAuthorizationId() {
		return authorizationId;
	}

	/**
	 * @param authorizationId
	 *            the authorizationId to set
	 */
	public void setAuthorizationId(String authorizationId) {
		this.authorizationId = authorizationId;
	}

	public static enum AccountType {
		SAVING("1000"),
		// CURRENT("1000"),
		CHECKING("2000"), CREDIT_FACILIT("3000"), UNIVERSAL_ACC("4000");

		private final String name;

		private AccountType(String str) {
			name = str;
		}

		public String toString() {
			return this.name;
		}
	}

	public static enum CardEntryMode {
		KEYENTER(11), SWIPE(21), EMV(51), EMVFALLBK(801), CTLS(71), CTLSSWIPE(
				911);

		private final int code;

		private CardEntryMode(int number) {
			code = number;
		}

		public String toString() {
			return String.valueOf(this.code);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Payment [binNumber=" + binNumber + ", originalAmount="
				+ originalAmount + ", exchangeRate=" + exchangeRate
				+ ", cardEntryMode=" + cardEntryMode + ", lastFourDigitValue="
				+ lastFourDigitValue + ", currencyCode=" + currencyCode
				+ ", currencyName=" + currencyName + ", markup=" + markup
				+ ", merchantId=" + merchantId + ", terminalId=" + terminalId
				+ ", clientId=" + clientId + ", invoiceNumber=" + invoiceNumber
				+ ", track2=" + track2 + ", field55=" + field55
				+ ", additionalAmount=" + additionalAmount + ", accountType="
				+ accountType + ", terminalSerialNumber="
				+ terminalSerialNumber + ", cardLabel=" + cardLabel + ", date="
				+ date + ", time=" + time + ", sessionKey=" + sessionKey
				+ ", decisionFlag=" + decisionFlag + ", pinBlock=" + pinBlock
				+ ", referenceValue=" + referenceValue + ", dccAmount="
				+ dccAmount + ", retrivalRefNumber=" + retrivalRefNumber
				+ ", batchNumber=" + batchNumber + ", schemaTransactionId="
				+ schemaTransactionId + ", panNumber=" + panNumber
				+ ", expiryDate=" + expiryDate + ", extraDataObject=" + nii
				+ ", tpdu=" + tpdu + ", stanNumber=" + stanNumber
				+ ", responseCode=" + responseCode + ", authorizationId="
				+ authorizationId + ", getNii()=" + getNii()
				+ ", getStanNumber()=" + getStanNumber() + ", getTpdu()="
				+ getTpdu() + ", getBinNumber()=" + getBinNumber()
				+ ", getOriginalAmount()=" + getOriginalAmount()
				+ ", getCardEntryMode()=" + getCardEntryMode()
				+ ", getExchangeRate()=" + getExchangeRate()
				+ ", getCurrencyCode()=" + getCurrencyCode()
				+ ", getCurrencyName()=" + getCurrencyName() + ", getMarkup()="
				+ getMarkup() + ", getMerchantId()=" + getMerchantId()
				+ ", getTerminalId()=" + getTerminalId() + ", getClientId()="
				+ getClientId() + ", getInvoiceNumber()=" + getInvoiceNumber()
				+ ", getTrack2()=" + getTrack2() + ", getPanNumber()="
				+ getPanNumber() + ", getExpiryDate()=" + getExpiryDate()
				+ ", getField55()=" + getField55() + ", getAdditionalAmount()="
				+ getAdditionalAmount() + ", getAccountType()="
				+ getAccountType() + ", getTerminalSerialNumber()="
				+ getTerminalSerialNumber() + ", getCardLabel()="
				+ getCardLabel() + ", getDate()=" + getDate() + ", getTime()="
				+ getTime() + ", getSessionKey()=" + getSessionKey()
				+ ", getPinBlock()=" + getPinBlock() + ", getDecisionFlag()="
				+ getDecisionFlag() + ", getRetrivalRefNumber()="
				+ getRetrivalRefNumber() + ", getReferenceValue()="
				+ getReferenceValue() + ", getBatchNumber()="
				+ getBatchNumber() + ", getExtraDataName()="
				+ getResponseCode() + ", getSchemaTransactionId()="
				+ getSchemaTransactionId() + ", getAuthorizationId()="
				+ getAuthorizationId() + ", hashCode()=" + hashCode()
				+ ", getPaymentDTO()=" + getPaymentDTO() + ", getClass()="
				+ getClass() + ", toString()=" + super.toString() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((accountType == null) ? 0 : accountType.hashCode());
		result = prime
				* result
				+ ((additionalAmount == null) ? 0 : additionalAmount.hashCode());
		result = prime * result
				+ ((batchNumber == null) ? 0 : batchNumber.hashCode());
		result = prime * result
				+ ((binNumber == null) ? 0 : binNumber.hashCode());
		result = prime * result
				+ ((cardEntryMode == null) ? 0 : cardEntryMode.hashCode());
		result = prime * result
				+ ((cardLabel == null) ? 0 : cardLabel.hashCode());
		result = prime * result
				+ ((clientId == null) ? 0 : clientId.hashCode());
		result = prime * result
				+ ((currencyCode == null) ? 0 : currencyCode.hashCode());
		result = prime * result
				+ ((currencyName == null) ? 0 : currencyName.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result
				+ ((dccAmount == null) ? 0 : dccAmount.hashCode());
		result = prime * result
				+ ((decisionFlag == null) ? 0 : decisionFlag.hashCode());
		result = prime * result
				+ ((exchangeRate == null) ? 0 : exchangeRate.hashCode());
		result = prime * result
				+ ((expiryDate == null) ? 0 : expiryDate.hashCode());
		result = prime * result + ((field55 == null) ? 0 : field55.hashCode());
		result = prime * result + ((markup == null) ? 0 : markup.hashCode());
		result = prime * result
				+ ((merchantId == null) ? 0 : merchantId.hashCode());
		result = prime * result
				+ ((originalAmount == null) ? 0 : originalAmount.hashCode());
		result = prime * result
				+ ((panNumber == null) ? 0 : panNumber.hashCode());
		result = prime * result
				+ ((referenceValue == null) ? 0 : referenceValue.hashCode());
		result = prime * result
				+ ((sessionKey == null) ? 0 : sessionKey.hashCode());
		result = prime * result
				+ ((terminalId == null) ? 0 : terminalId.hashCode());
		result = prime
				* result
				+ ((terminalSerialNumber == null) ? 0 : terminalSerialNumber
						.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((track2 == null) ? 0 : track2.hashCode());
		return result;
	}

	public PaymentDTO getPaymentDTO() {
		PaymentDTO paymentDto = new PaymentDTO();

		paymentDto.setAccountType(this.getAccountType());
		if (!Constants.TCH_NULL.equalsIgnoreCase(this.additionalAmount)) {
			paymentDto
					.setAdditionalAmount(this.additionalAmount != null ? Double
							.valueOf(this.additionalAmount) : 0);
		} else {
			paymentDto.setAdditionalAmount(new Double(0));
		}
		if (!Constants.TCH_NULL.equalsIgnoreCase(this.batchNumber)) {
			paymentDto.setBatchNumber(this.batchNumber != null ? Integer
					.valueOf(this.batchNumber) : 0);
		} else {
			paymentDto.setBatchNumber(0);
		}
		paymentDto.setStanNumber(this.stanNumber);
		paymentDto.setTime(this.time);
		paymentDto.setCardEntryMode(this.getCardEntryMode());
		if(this.cardLabel != null)
			if(this.cardLabel.lastIndexOf(" ") != -1)
				paymentDto.setCardLabel(this.cardLabel.substring(0,this.cardLabel.lastIndexOf(" ")).trim());
			else
				paymentDto.setCardLabel(this.cardLabel);
			
		paymentDto.setClientId(this.clientId);
		if (!Constants.TCH_NULL.equalsIgnoreCase(this.currencyCode)) {
			paymentDto.setCurrencyCode(this.currencyCode != null ? Integer
					.valueOf(this.currencyCode) : 0);
		} else {
			paymentDto.setCurrencyCode(0);
		}
		paymentDto.setCurrencyName(this.currencyName);
		if (this.date != null && this.date.length() == 6) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd HHmmss");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
			try {
				paymentDto.setDate(sdf1.format(sdf.parse(this.date + " "
						+ this.time)));
			} catch (ParseException e) {
				System.out.println("Exception");
			}
		}
		if (!Constants.TCH_NULL.equalsIgnoreCase(this.dccAmount)) {
			paymentDto.setDccAmount(this.dccAmount != null ? Double
					.valueOf(this.dccAmount) : 0.0);
		} else {
			paymentDto.setDccAmount(new Double(0));
		}
		paymentDto.setDecisionFlag(this.decisionFlag);
		if (!Constants.TCH_NULL.equalsIgnoreCase(this.exchangeRate)) {
			paymentDto.setExchangeRate(this.exchangeRate != null ? Double
					.valueOf(this.exchangeRate) : 0);
		} else {
			paymentDto.setExchangeRate(new Double(0));
		}
		paymentDto.setExpiryDate(this.expiryDate);
		paymentDto.setField55(this.field55);
		paymentDto.setMarkup(this.markup);
		paymentDto.setInvoiceNumber(this.invoiceNumber);
		paymentDto.setMerchantId(this.merchantId);
		if (!Constants.TCH_NULL.equalsIgnoreCase(this.originalAmount)) {
			paymentDto.setOriginalAmount(this.originalAmount != null ? Double
					.valueOf(this.originalAmount) : 0);
		} else {
			paymentDto.setOriginalAmount(new Double(0));
		}
		paymentDto.setPanNumber(this.panNumber);
		paymentDto.setBinNumber(this.binNumber);

		paymentDto.setVoidApprovedFlag(0);
		if ("Y".equalsIgnoreCase(this.refundNewFlag))
			paymentDto.setRefundFlag(1);
		else
			paymentDto.setRefundFlag(0);
		paymentDto.setPinBlock(this.pinBlock);
		paymentDto.setReferenceValue(this.referenceValue);
		paymentDto.setResponseCode(responseCode);
		paymentDto.setTerminalId(terminalId);
		paymentDto.setTerminalSerialNumber(terminalSerialNumber);
		// paymentDto.setTrack2(track2);
		paymentDto.setRetrivalRefNumber(this.retrivalRefNumber);
		paymentDto.setSchemaTransactionId(this.schemaTransactionId);
		paymentDto.setAuthorizationId(this.authorizationId);
		paymentDto.setNii(this.nii);
		paymentDto.setTpdu(this.tpdu);
		paymentDto.setSettlementFlag("N");
		if (!Constants.TCH_NULL.equalsIgnoreCase(this.lastFourDigitValue)) {
			paymentDto
					.setLastFourDigitValue(this.lastFourDigitValue != null ? this.lastFourDigitValue
							: "0");
		} else {
			paymentDto.setLastFourDigitValue("0");
		}
		paymentDto.setIssuerField55(this.issuerField55);
		paymentDto.setPaymentId(this.paymentId);
		paymentDto.setTipPercent(this.tipPercent);
		paymentDto.setCashbackPur(this.cashbackPur);
		paymentDto.setRefundApproved("0");
		paymentDto.setTipApproved("0");
		paymentDto.setSaleCompletionFlag("0");
		paymentDto.setProcessingCode(this.processingCode);
		paymentDto.setField63(this.field63);
		paymentDto.setMTI(this.MTI);
		if(this.getReferenceValue() != null && this.getReferenceValue().length()<=20)
			paymentDto.setBranchCode(this.getReferenceValue() != null ? this.getReferenceValue() : null);
		paymentDto.setAppName(this.appName);
		paymentDto.setCustomerName(this.getCustomerName());
		paymentDto.setBankCode(this.getBankCode());

		paymentDto.setExtraInfo(this.getExtraInfo()); //Changes made by Priyanka Gawas on 13-09-2017
		paymentDto.setEmiIndicator(this.getEmiIndicator());      //Added by Saloni Jindal on 7-09-2017
		paymentDto.setInternationalFlag(this.getInternationalFlag());
		paymentDto.setCardTypeInd(this.getCardTypeValue());
		//new field added to set tax and fees values
		paymentDto.setFeeFlag(this.getFeeFlag());
		paymentDto.setFeeValue(this.getFeeValue());
		paymentDto.setIgsttax(this.getIgsttax());
		paymentDto.setCgsttax(this.getCgsttax());
		return paymentDto;

	}
	
	/**
	 * Get amex dto values
	 * @return
	 * 
	 */
	public PaymentAmexDTO getPaymentAmexDto(){

		PaymentAmexDTO paymentDto = new PaymentAmexDTO();

		paymentDto.setAccountType(this.getAccountType());
		if (!Constants.TCH_NULL.equalsIgnoreCase(this.additionalAmount)) {
			paymentDto
					.setAdditionalAmount(this.additionalAmount != null ? Double
							.valueOf(this.additionalAmount) : 0);
		} else {
			paymentDto.setAdditionalAmount(new Double(0));
		}
		if (!Constants.TCH_NULL.equalsIgnoreCase(this.batchNumber)) {
			paymentDto.setBatchNumber(this.batchNumber != null ? Integer
					.valueOf(this.batchNumber) : 0);
		} else {
			paymentDto.setBatchNumber(0);
		}
		paymentDto.setStanNumber(this.stanNumber);
		paymentDto.setTime(this.time);
		paymentDto.setCardEntryMode(this.getCardEntryMode());
		if(this.cardLabel != null)
			if(this.cardLabel.lastIndexOf(" ") != -1)
				paymentDto.setCardLabel(this.cardLabel.substring(0,this.cardLabel.lastIndexOf(" ")).trim());
			else
				paymentDto.setCardLabel(this.cardLabel);
			
		paymentDto.setClientId(this.clientId);
		if (!Constants.TCH_NULL.equalsIgnoreCase(this.currencyCode)) {
			paymentDto.setCurrencyCode(this.currencyCode != null ? Integer
					.valueOf(this.currencyCode) : 0);
		} else {
			paymentDto.setCurrencyCode(0);
		}
		paymentDto.setCurrencyName(this.currencyName);
		if (this.date != null && this.date.length() == 6) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd HHmmss");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
			try {
				paymentDto.setDate(sdf1.format(sdf.parse(this.date + " "
						+ this.time)));
			} catch (ParseException e) {
				System.out.println("Exception");
			}
		}
		if (!Constants.TCH_NULL.equalsIgnoreCase(this.dccAmount)) {
			paymentDto.setDccAmount(this.dccAmount != null ? Double
					.valueOf(this.dccAmount) : 0.0);
		} else {
			paymentDto.setDccAmount(new Double(0));
		}
		paymentDto.setDecisionFlag(this.decisionFlag);
		if (!Constants.TCH_NULL.equalsIgnoreCase(this.exchangeRate)) {
			paymentDto.setExchangeRate(this.exchangeRate != null ? Double
					.valueOf(this.exchangeRate) : 0);
		} else {
			paymentDto.setExchangeRate(new Double(0));
		}
		paymentDto.setExpiryDate(this.expiryDate);
		paymentDto.setField55(this.field55);
		paymentDto.setMarkup(this.markup);
		paymentDto.setInvoiceNumber(this.invoiceNumber);
		paymentDto.setMerchantId(this.merchantId);
		if (!Constants.TCH_NULL.equalsIgnoreCase(this.originalAmount)) {
			paymentDto.setOriginalAmount(this.originalAmount != null ? Double
					.valueOf(this.originalAmount) : 0);
		} else {
			paymentDto.setOriginalAmount(new Double(0));
		}
		paymentDto.setPanNumber(this.panNumber);
		paymentDto.setBinNumber(this.binNumber);

		paymentDto.setVoidApprovedFlag(0);
		if ("Y".equalsIgnoreCase(this.refundNewFlag))
			paymentDto.setRefundFlag(1);
		else
			paymentDto.setRefundFlag(0);
		paymentDto.setPinBlock(this.pinBlock);
		paymentDto.setReferenceValue(this.referenceValue);
		paymentDto.setResponseCode(responseCode);
		paymentDto.setTerminalId(terminalId);
		paymentDto.setTerminalSerialNumber(terminalSerialNumber);
		// paymentDto.setTrack2(track2);
		paymentDto.setRetrivalRefNumber(this.retrivalRefNumber);
		paymentDto.setSchemaTransactionId(this.schemaTransactionId);
		paymentDto.setAuthorizationId(this.authorizationId);
		paymentDto.setNii(this.nii);
		paymentDto.setTpdu(this.tpdu);
		paymentDto.setSettlementFlag("N");
		if (!Constants.TCH_NULL.equalsIgnoreCase(this.lastFourDigitValue)) {
			paymentDto
					.setLastFourDigitValue(this.lastFourDigitValue != null ? this.lastFourDigitValue
							: "0");
		} else {
			paymentDto.setLastFourDigitValue("0");
		}
		paymentDto.setIssuerField55(this.issuerField55);
		paymentDto.setPaymentId(this.paymentId);
		paymentDto.setTipPercent(this.tipPercent);
		paymentDto.setCashbackPur(this.cashbackPur);
		paymentDto.setRefundApproved("0");
		paymentDto.setTipApproved("0");
		paymentDto.setSaleCompletionFlag("0");
		paymentDto.setProcessingCode(this.processingCode);
		paymentDto.setField63(this.field63);
		paymentDto.setMTI(this.MTI);
		if(this.getReferenceValue() != null && this.getReferenceValue().length()<=20)
			paymentDto.setBranchCode(this.getReferenceValue() != null ? this.getReferenceValue() : null);
		paymentDto.setAppName(this.appName);
		paymentDto.setCustomerName(this.getCustomerName());
		paymentDto.setBankCode(this.getBankCode());

		paymentDto.setExtraInfo(this.getExtraInfo()); //Changes made by Priyanka Gawas on 13-09-2017
		paymentDto.setEmiIndicator(this.getEmiIndicator());      //Added by Saloni Jindal on 7-09-2017
		paymentDto.setInternationalFlag(this.getInternationalFlag());
		paymentDto.setCardTypeInd(this.getCardTypeValue());
		//new field added to set tax and fees values
		paymentDto.setFeeFlag(this.getFeeFlag());
		paymentDto.setFeeValue(this.getFeePer());
		paymentDto.setIgsttax(this.getIgsttax());
		paymentDto.setCgsttax(this.getCgsttax());
		return paymentDto;
	}

	public PaymentAuthDTO getPaymentAuthDTO() {

		PaymentAuthDTO paymentAuthDto = new PaymentAuthDTO();

		paymentAuthDto.setAccountType(this.getAccountType());
		if (!Constants.TCH_NULL.equalsIgnoreCase(this.additionalAmount)) {
			paymentAuthDto
					.setAdditionalAmount(this.additionalAmount != null ? Double
							.valueOf(this.additionalAmount) : 0);
		} else {
			paymentAuthDto.setAdditionalAmount(new Double(0));
		}
		if (!Constants.TCH_NULL.equalsIgnoreCase(this.batchNumber)) {
			paymentAuthDto.setBatchNumber(this.batchNumber != null ? Integer
					.valueOf(this.batchNumber) : 0);
		} else {
			paymentAuthDto.setBatchNumber(0);
		}
		paymentAuthDto.setStanNumber(this.stanNumber);
		paymentAuthDto.setTime(this.time);
		paymentAuthDto.setCardEntryMode(this.getCardEntryMode());
		if(this.cardLabel != null)
			if(this.cardLabel.lastIndexOf(" ") != -1)
				paymentAuthDto.setCardLabel(this.cardLabel.substring(0,this.cardLabel.lastIndexOf(" ")).trim());
			else
				paymentAuthDto.setCardLabel(this.cardLabel);
		paymentAuthDto.setClientId(this.clientId);
		if (!Constants.TCH_NULL.equalsIgnoreCase(this.currencyCode)) {
			paymentAuthDto.setCurrencyCode(this.currencyCode != null ? Integer
					.valueOf(this.currencyCode) : 0);
		} else {
			paymentAuthDto.setCurrencyCode(0);
		}
		paymentAuthDto.setCurrencyName(this.currencyName);
		if (this.date != null && this.date.length() == 6) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd HHmmss");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
			try {
				paymentAuthDto.setDate(sdf1.format(sdf.parse(this.date + " "
						+ this.time)));
			} catch (ParseException e) {
				System.out.println("Exception");
			}
		}

		// paymentAuthDto.setDate(new
		// java.sql.Date(System.currentTimeMillis()));
		if (!Constants.TCH_NULL.equalsIgnoreCase(this.dccAmount)) {
			paymentAuthDto.setDccAmount(this.dccAmount != null ? Double
					.valueOf(this.dccAmount) : 0.0);
		} else {
			paymentAuthDto.setDccAmount(new Double(0));
		}
		paymentAuthDto.setDecisionFlag(this.decisionFlag);
		if (!Constants.TCH_NULL.equalsIgnoreCase(this.exchangeRate)) {
			paymentAuthDto.setExchangeRate(this.exchangeRate != null ? Double
					.valueOf(this.exchangeRate) : 0);
		} else {
			paymentAuthDto.setExchangeRate(new Double(0));
		}
		paymentAuthDto.setExpiryDate(this.expiryDate);
		paymentAuthDto.setField55(this.field55);
		paymentAuthDto.setMarkup(this.markup);
		paymentAuthDto.setInvoiceNumber(this.invoiceNumber);
		paymentAuthDto.setMerchantId(this.merchantId);
		if (!Constants.TCH_NULL.equalsIgnoreCase(this.originalAmount)) {
			paymentAuthDto
					.setOriginalAmount(this.originalAmount != null ? Double
							.valueOf(this.originalAmount) : 0);
		} else {
			paymentAuthDto.setOriginalAmount(new Double(0));
		}
		paymentAuthDto.setPanNumber(this.panNumber);
		paymentAuthDto.setBinNumber(this.binNumber);

		paymentAuthDto.setVoidApprovedFlag(0);
		if ("Y".equalsIgnoreCase(this.refundNewFlag))
			paymentAuthDto.setRefundFlag(1);
		else
			paymentAuthDto.setRefundFlag(0);
		paymentAuthDto.setPinBlock(this.pinBlock);
		paymentAuthDto.setReferenceValue(this.referenceValue);
		paymentAuthDto.setResponseCode(responseCode);
		paymentAuthDto.setTerminalId(terminalId);
		paymentAuthDto.setTerminalSerialNumber(terminalSerialNumber);
		// paymentAuthDto.setTrack2(track2);
		paymentAuthDto.setRetrivalRefNumber(this.retrivalRefNumber);
		paymentAuthDto.setSchemaTransactionId(this.schemaTransactionId);
		paymentAuthDto.setAuthorizationId(this.authorizationId);
		paymentAuthDto.setSettlementFlag("N");

		if (!Constants.TCH_NULL.equalsIgnoreCase(this.lastFourDigitValue)) {
			paymentAuthDto
					.setLastFourDigitValue(this.lastFourDigitValue != null ? this.lastFourDigitValue
							: "0");
		} else {
			paymentAuthDto.setLastFourDigitValue("0");
		}
		paymentAuthDto.setIssuerField55(this.issuerField55);
		paymentAuthDto.setPaymentId(this.paymentId);
		paymentAuthDto.setTipPercent(this.tipPercent);
		paymentAuthDto.setCashbackPur(this.cashbackPur);
		paymentAuthDto.setNii(this.nii);
		paymentAuthDto.setTpdu(this.tpdu);
		paymentAuthDto.setRefundApproved("0");
		paymentAuthDto.setTipApproved("0");
		paymentAuthDto.setSaleCompletionFlag("0");
		paymentAuthDto.setProcessingCode(this.processingCode);
		paymentAuthDto.setMTI(this.MTI);
		paymentAuthDto.setField63(this.field63);
		paymentAuthDto.setBranchCode(this.branchCode);
		paymentAuthDto.setAppName(this.appName);
		paymentAuthDto.setCustomerName(this.getCustomerName());
		paymentAuthDto.setBankCode(this.getBankCode());
		paymentAuthDto.setSessionKey(this.getSessionKey());
		paymentAuthDto.setInternationalFlag(this.getInternationalFlag());
		paymentAuthDto.setCardTypeInd(this.getCardTypeValue());
		//new field added to set tax and fees values
		paymentAuthDto.setFeeFlag(this.getFeeFlag());
		paymentAuthDto.setFeeValue(this.getFeeValue());
		paymentAuthDto.setIgsttax(this.getIgsttax());
		paymentAuthDto.setCgsttax(this.getCgsttax());
		return paymentAuthDto;

	}
	
	public QRRequestDTO paramQRRequestDTO() {
		QRRequestDTO qrDTO = new QRRequestDTO();
		qrDTO.setTrId(this.getReferenceValue());//yes
		qrDTO.setMid(this.getMerchantId());
		qrDTO.setTid(this.getTerminalId());
		qrDTO.setBankCode(this.getBankCode());
		qrDTO.setStatus(this.getStatus());
		qrDTO.setPrgType(this.getQrType()); 
		qrDTO.setRrn(this.getRetrivalRefNumber());
		qrDTO.setAuthcd(this.getAuthorizationId());
		qrDTO.setQrResponseString(this.getQrString());
		return qrDTO;
	}

}