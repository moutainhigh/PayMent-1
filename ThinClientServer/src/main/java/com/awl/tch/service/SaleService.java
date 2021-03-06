package com.awl.tch.service;

import com.awl.tch.bean.Payment;
import com.awl.tch.exceptions.TCHServiceException;
/**
* <h1>Sale service</h1>
* As sale is used to make transaction for purchasing
* item based on the price value and for that this interface make 
* use for setting business logic and traversal data to and from database.
* 
* <b>Note:</b> All the mandatory field must be there
* to get appropriate response from switch.
*
* @author  Ashish Bhavsar
* @version 1.0
* @since   2016-09-21
*/
public interface SaleService extends TCHService<Payment>{
	public Payment serviceQR(Payment input) throws TCHServiceException;

	public Payment serviceFC(Payment saleRequest) throws TCHServiceException;
}
