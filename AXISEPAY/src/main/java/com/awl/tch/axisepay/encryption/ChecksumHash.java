package com.awl.tch.axisepay.encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.awl.tch.axisepay.constant.AXISepayConstant;

public class ChecksumHash {

	private String mid;
	private String tid;
	private String urn;
	private String rrn;
	private String cid;
	private String rid;
	private String chkSum;
	
	
	private static Logger logger = LoggerFactory.getLogger(ChecksumHash.class);
	
	
	public ChecksumHash(String mid, String tid, String urn) {
		this.mid = mid;
		this.tid = tid;
		this.urn = urn;
		chkSum = new StringBuilder().append(this.getMid())
				.append(getTid()).append(getUrn())
				.append(AXISepayConstant.TCH_AXIS_CHECKSUM).toString();
	}
	
	public ChecksumHash(String mid, String tid, String urn,String rrn) {
		this.mid = mid;
		this.tid = tid;
		this.urn = urn;
		chkSum = new StringBuilder().append(this.getMid())
				.append(getTid()).append(getUrn()).append(getRrn())
				.append(AXISepayConstant.TCH_AXIS_CHECKSUM).toString();
	}
	
	public ChecksumHash(String mid, String tid, String urn, String cid,String rid, String rrn) {
		this.mid = mid;
		this.tid = tid;
		this.urn = urn;
		this.cid = cid;
		this.rid = rid;
		this.rrn = rrn;
		
		chkSum = new StringBuilder().append(this.getMid())
				.append(getTid()).append(getUrn()).append(getCid()).append(getRid()).append(getRrn())
				.append(AXISepayConstant.TCH_AXIS_CHECKSUM).toString();
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
	public String getUrn() {
		return urn;
	}
	public void setUrn(String urn) {
		this.urn = urn;
	}
	
	
	
	public String getHashedCheckSum(){
		String hashCheckedSum= null;
		try {
			logger.debug("Hashing checksum string : "+getChkSum());
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(getChkSum().getBytes());
			byte[] dataBytes = md.digest();
			
			//convert the byte to hex format method 1
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < dataBytes.length; i++) {
	         sb.append(Integer.toString((dataBytes[i] & 0xff) + 0x100, 16).substring(1));
	        }
	        hashCheckedSum=sb.toString();
	        System.out.println("Hex format : " + hashCheckedSum);
	        
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hashCheckedSum;
	}
	
	public static void main(String[] args) {
		
		ChecksumHash ckhash = new ChecksumHash("12345", "12345", "640330");
		ckhash.getHashedCheckSum();
		
		ckhash = new ChecksumHash("12345", "12345", "640330","1125","15616","12345");
		ckhash.getHashedCheckSum();
		
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

	public String getChkSum() {
		return chkSum;
	}

	public void setChkSum(String chkSum) {
		this.chkSum = chkSum;
	}
}
