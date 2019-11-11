package com.awl.tch.model;

import java.util.Date;

import com.awl.tch.annotation.Column;
import com.awl.tch.annotation.Id;
import com.awl.tch.annotation.Table;

@Table(name="TC_TID_HWSR_MAPPING")
public class TerminalHardwareParameterDTO {
	
	@Id
	@Column(name = "HWSRMAPID")
	private Integer id;
	
	@Column(name = "BANKCODE")
	private String bankCode;
	
	@Column(name = "INSTITUTIONID")
	private Integer institutionId;
	
	@Column(name = "MID")
	private String	mid;
	
	@Column(name = "TID")
	private String	tid;
	
	@Column(name = "HWSRNO")
	private String	hardwareSerialNumber;
	
	@Column(name = "MODELNAME")
	private String	modelName;
	
	@Column(name = "VENDOR")
	private String	vendor;
	
	@Column(name = "CLIENTID")
	private Integer clientId;
	
	@Column(name = "HANDSHAKE")
	private String handshake;
	
	@Column(name = "ADDEDON")
	private Date addedOn;
	
	@Column(name = "ADDEDBY")
	private String addedBy;
	
	@Column(name = "ADDIPADDRESS")
	private String	addIpAddress;
	
	@Column(name = "MODIFIEDON")
	private Date	modifiedOn;
	
	@Column(name = "MODIFIEDBY")
	private String	modifiedBy;
	
	@Column(name = "MODIFIEDIPADDRESS")
	private String	modifiedIpAddress;
	
	@Column(name = "ISACTIVE")
	private String isActive;
	
	@Column(name = "OPENPCRFLAG")
	private String openPcrFlag;

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getOpenPcrFlag() {
		return openPcrFlag;
	}

	public void setOpenPcrFlag(String openPcrFlag) {
		this.openPcrFlag = openPcrFlag;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public Integer getInstitutionId() {
		return institutionId;
	}

	public void setInstitutionId(Integer institutionId) {
		this.institutionId = institutionId;
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

	public String getHardwareSerialNumber() {
		return hardwareSerialNumber;
	}

	public void setHardwareSerialNumber(String hardwareSerialNumber) {
		this.hardwareSerialNumber = hardwareSerialNumber;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public String getHandshake() {
		return handshake;
	}

	public void setHandshake(String handshake) {
		this.handshake = handshake;
	}

	public Date getAddedOn() {
		return addedOn;
	}

	public void setAddedOn(Date addedOn) {
		this.addedOn = addedOn;
	}

	public String getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}

	public String getAddIpAddress() {
		return addIpAddress;
	}

	public void setAddIpAddress(String addIpAddress) {
		this.addIpAddress = addIpAddress;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getModifiedIpAddress() {
		return modifiedIpAddress;
	}

	public void setModifiedIpAddress(String modifiedIpAddress) {
		this.modifiedIpAddress = modifiedIpAddress;
	}

	@Override
	public String toString() {
		return "TerminalHardwareParameterDTO [id=" + id + ", bankCode="
				+ bankCode + ", institutionId=" + institutionId + ", mid="
				+ mid + ", tid=" + tid + ", hardwareSerialNumber="
				+ hardwareSerialNumber + ", modelName=" + modelName
				+ ", vendor=" + vendor + ", clientId=" + clientId
				+ ", handshake=" + handshake + ", addedOn=" + addedOn
				+ ", addedBy=" + addedBy + ", addIpAddress=" + addIpAddress
				+ ", modifiedOn=" + modifiedOn + ", modifiedBy=" + modifiedBy
				+ ", modifiedIpAddress=" + modifiedIpAddress + "]";
	}
}
