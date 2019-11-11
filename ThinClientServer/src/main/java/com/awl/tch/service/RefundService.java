package com.awl.tch.service;

import com.awl.tch.bean.Payment;
import com.awl.tch.exceptions.TCHServiceException;

public interface RefundService extends TCHService<Payment>{

	public Payment serviceQR(Payment input) throws TCHServiceException;
	
	public Payment serviceFc(Payment input) throws TCHServiceException;
}
