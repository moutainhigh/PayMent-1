package com.awl.tch.axisepay.model;

import com.awl.tch.axisepay.encryption.ChecksumHash;

public class AXISepayRequest {

	private String url;
	private String tid;
	private String urn;
	private String datetime;
	private String mid;
	private String rrn;
	private String cid;
	private String rid;
	private String amount;

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
	public String getUrn() {
		return urn;
	}
	public void setUrn(String urn) {
		this.urn = urn;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	
	public String getRrn() {
		return rrn;
	}
	public void setRrn(String rrn) {
		this.rrn = rrn;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getRid() {
		return rid;
	}
	public void setRid(String rid) {
		this.rid = rid;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	
	private String getTrnxDtlsCheckSum() {
		ChecksumHash ckHash = new ChecksumHash(getMid(), getTid(), getUrn());
		return ckHash.getHashedCheckSum();
	}
	
	private String getTrnxEnqCheckSum() {
		ChecksumHash ckHash = new ChecksumHash(getMid(), getTid(), getUrn(),getRrn());
		return ckHash.getHashedCheckSum();
	}

	private String getSaleCheckSum() {
		ChecksumHash ckHash = new ChecksumHash(getMid(), getTid(), getUrn(),getCid(),getRid(), getRrn());
		return ckHash.getHashedCheckSum();
	}
	public String getTransactionDetailsRequest() {
		return new StringBuilder("MID=").append(getMid()).append("&")
				.append("TID=").append(getTid()).append("&")
				.append("URN=").append(getUrn()).append("&")
				.append("DT_TIME=").append(getDatetime()).append("&")
				.append("CKS=").append(this.getTrnxDtlsCheckSum()).toString();
	}
	
	public String getTransactionEnqRequest() {
		return new StringBuilder("MID=").append(getMid()).append("&")
				.append("TID=").append(getTid()).append("&")
				.append("URN=").append(getUrn()).append("&")
				.append("CID=").append(getCid()).append("&")
				.append("RID=").append(getRid()).append("&")
				.append("POS_TRAN_ID=").append(getRrn()).append("&")
				.append("AMOUNT=").append(getAmount()).append("&")
				.append("CKS=").append(this.getTrnxEnqCheckSum()).toString();
	}
	
	public String getSaleRequest() {
		return new StringBuilder("MID=").append(getMid()).append("&")
				.append("TID=").append(getTid()).append("&")
				.append("URN=").append(getUrn()).append("&")
				.append("CID=").append(getCid()).append("&")
				.append("RID=").append(getRid()).append("&")
				.append("POS_TRAN_ID=").append(getRrn()).append("&")
				.append("AMOUNT=").append(getAmount()).append("&")
				.append("CKS=").append(this.getSaleCheckSum()).toString();
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
