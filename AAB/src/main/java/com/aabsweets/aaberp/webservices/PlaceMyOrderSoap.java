
package com.aabsweets.aaberp.webservices;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebService(name = "PlaceMyOrderSoap", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface PlaceMyOrderSoap {


    /**
     * 
     * @param cardNo
     * @param mySId
     * @param fromShopCode
     * @param trnsId
     * @param acknowledgementId
     * @param requestType
     * @param extTransactionID
     * @param trnDate
     * @param cardBank
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "CardPaymentRequest", action = "http://aaberp.aabsweets.com/webservices/CardPaymentRequest")
    @WebResult(name = "CardPaymentRequestResult", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
    @RequestWrapper(localName = "CardPaymentRequest", targetNamespace = "http://aaberp.aabsweets.com/webservices/", className = "com.aabsweets.aaberp.webservices.CardPaymentRequest")
    @ResponseWrapper(localName = "CardPaymentRequestResponse", targetNamespace = "http://aaberp.aabsweets.com/webservices/", className = "com.aabsweets.aaberp.webservices.CardPaymentRequestResponse")
    public String cardPaymentRequest(
        @WebParam(name = "MySId", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String mySId,
        @WebParam(name = "RequestType", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String requestType,
        @WebParam(name = "FromShopCode", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String fromShopCode,
        @WebParam(name = "TrnsId", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String trnsId,
        @WebParam(name = "ExtTransactionID", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String extTransactionID,
        @WebParam(name = "AcknowledgementId", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String acknowledgementId,
        @WebParam(name = "TrnDate", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String trnDate,
        @WebParam(name = "CardBank", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String cardBank,
        @WebParam(name = "CardNo", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String cardNo);

    /**
     * 
     * @param mySId
     * @param toShopCode
     * @param itemArg
     * @param others
     * @param paymentRefno
     * @param dlyboyName
     * @param dlyboyPassCode
     * @param dlymode
     * @param deliverytime
     * @param onlineOrderNo
     * @param deliverydate
     * @param customer
     * @param ordrThruWebOrApp
     * @param emailid
     * @param pincode
     * @param landmark
     * @param custmobileNo
     * @param invAmt
     * @param dlyboyMobileNo
     * @param locality
     * @param fulladdress
     * @param instruction
     * @param sublocality
     * @param paymentOption
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "MyOrderArgs", action = "http://aaberp.aabsweets.com/webservices/MyOrderArgs")
    @WebResult(name = "MyOrderArgsResult", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
    @RequestWrapper(localName = "MyOrderArgs", targetNamespace = "http://aaberp.aabsweets.com/webservices/", className = "com.aabsweets.aaberp.webservices.MyOrderArgs")
    @ResponseWrapper(localName = "MyOrderArgsResponse", targetNamespace = "http://aaberp.aabsweets.com/webservices/", className = "com.aabsweets.aaberp.webservices.MyOrderArgsResponse")
    public String myOrderArgs(
        @WebParam(name = "MySId", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String mySId,
        @WebParam(name = "ToShopCode", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String toShopCode,
        @WebParam(name = "customer", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String customer,
        @WebParam(name = "ItemArg", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String itemArg,
        @WebParam(name = "DlyboyMobileNo", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String dlyboyMobileNo,
        @WebParam(name = "DlyboyName", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String dlyboyName,
        @WebParam(name = "DlyboyPassCode", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String dlyboyPassCode,
        @WebParam(name = "locality", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String locality,
        @WebParam(name = "sublocality", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String sublocality,
        @WebParam(name = "fulladdress", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String fulladdress,
        @WebParam(name = "landmark", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String landmark,
        @WebParam(name = "pincode", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String pincode,
        @WebParam(name = "emailid", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String emailid,
        @WebParam(name = "PaymentOption", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String paymentOption,
        @WebParam(name = "PaymentRefno", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String paymentRefno,
        @WebParam(name = "deliverydate", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String deliverydate,
        @WebParam(name = "deliverytime", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String deliverytime,
        @WebParam(name = "CustmobileNo", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String custmobileNo,
        @WebParam(name = "OnlineOrderNo", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String onlineOrderNo,
        @WebParam(name = "instruction", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String instruction,
        @WebParam(name = "OrdrThru_Web_Or_App", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String ordrThruWebOrApp,
        @WebParam(name = "Dlymode", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String dlymode,
        @WebParam(name = "InvAmt", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String invAmt,
        @WebParam(name = "Others", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String others);

    /**
     * 
     * @param mySId
     * @param itemArg
     * @param toShopCode
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "ItemAvailabilityCheck", action = "http://aaberp.aabsweets.com/webservices/ItemAvailabilityCheck")
    @WebResult(name = "ItemAvailabilityCheckResult", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
    @RequestWrapper(localName = "ItemAvailabilityCheck", targetNamespace = "http://aaberp.aabsweets.com/webservices/", className = "com.aabsweets.aaberp.webservices.ItemAvailabilityCheck")
    @ResponseWrapper(localName = "ItemAvailabilityCheckResponse", targetNamespace = "http://aaberp.aabsweets.com/webservices/", className = "com.aabsweets.aaberp.webservices.ItemAvailabilityCheckResponse")
    public String itemAvailabilityCheck(
        @WebParam(name = "MySId", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String mySId,
        @WebParam(name = "ToShopCode", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String toShopCode,
        @WebParam(name = "ItemArg", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String itemArg);

    /**
     * 
     * @param cancelledRefNo
     * @param mySId
     * @param toShopCode
     * @param orderNo
     * @param payment
     * @param reason
     * @param orderdate
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "CancelOrderConfirmed", action = "http://aaberp.aabsweets.com/webservices/CancelOrderConfirmed")
    @WebResult(name = "CancelOrderConfirmedResult", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
    @RequestWrapper(localName = "CancelOrderConfirmed", targetNamespace = "http://aaberp.aabsweets.com/webservices/", className = "com.aabsweets.aaberp.webservices.CancelOrderConfirmed")
    @ResponseWrapper(localName = "CancelOrderConfirmedResponse", targetNamespace = "http://aaberp.aabsweets.com/webservices/", className = "com.aabsweets.aaberp.webservices.CancelOrderConfirmedResponse")
    public String cancelOrderConfirmed(
        @WebParam(name = "MySId", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String mySId,
        @WebParam(name = "ToShopCode", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String toShopCode,
        @WebParam(name = "orderdate", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String orderdate,
        @WebParam(name = "OrderNo", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String orderNo,
        @WebParam(name = "reason", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String reason,
        @WebParam(name = "Payment", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String payment,
        @WebParam(name = "CancelledRefNo", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String cancelledRefNo);

    /**
     * 
     * @param mySId
     * @param toShopCode
     * @param orderNo
     * @param reason
     * @param orderdate
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "CancelOrder", action = "http://aaberp.aabsweets.com/webservices/CancelOrder")
    @WebResult(name = "CancelOrderResult", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
    @RequestWrapper(localName = "CancelOrder", targetNamespace = "http://aaberp.aabsweets.com/webservices/", className = "com.aabsweets.aaberp.webservices.CancelOrder")
    @ResponseWrapper(localName = "CancelOrderResponse", targetNamespace = "http://aaberp.aabsweets.com/webservices/", className = "com.aabsweets.aaberp.webservices.CancelOrderResponse")
    public String cancelOrder(
        @WebParam(name = "MySId", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String mySId,
        @WebParam(name = "ToShopCode", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String toShopCode,
        @WebParam(name = "orderdate", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String orderdate,
        @WebParam(name = "OrderNo", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String orderNo,
        @WebParam(name = "reason", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String reason);

    /**
     * 
     * @param mySId
     * @param toShopCode
     * @param orderNo
     * @param orderdate
     * @param newDriverMob
     * @param newDriverName
     * @param driverPasscode
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "ChangeDriver", action = "http://aaberp.aabsweets.com/webservices/ChangeDriver")
    @WebResult(name = "ChangeDriverResult", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
    @RequestWrapper(localName = "ChangeDriver", targetNamespace = "http://aaberp.aabsweets.com/webservices/", className = "com.aabsweets.aaberp.webservices.ChangeDriver")
    @ResponseWrapper(localName = "ChangeDriverResponse", targetNamespace = "http://aaberp.aabsweets.com/webservices/", className = "com.aabsweets.aaberp.webservices.ChangeDriverResponse")
    public String changeDriver(
        @WebParam(name = "MySId", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String mySId,
        @WebParam(name = "ToShopCode", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String toShopCode,
        @WebParam(name = "orderdate", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String orderdate,
        @WebParam(name = "OrderNo", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String orderNo,
        @WebParam(name = "NewDriverMob", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String newDriverMob,
        @WebParam(name = "NewDriverName", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String newDriverName,
        @WebParam(name = "DriverPasscode", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String driverPasscode);

    /**
     * 
     * @param region
     * @param mySId
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "ListofLocationsForDoorDly", action = "http://aaberp.aabsweets.com/webservices/ListofLocationsForDoorDly")
    @WebResult(name = "ListofLocationsForDoorDlyResult", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
    @RequestWrapper(localName = "ListofLocationsForDoorDly", targetNamespace = "http://aaberp.aabsweets.com/webservices/", className = "com.aabsweets.aaberp.webservices.ListofLocationsForDoorDly")
    @ResponseWrapper(localName = "ListofLocationsForDoorDlyResponse", targetNamespace = "http://aaberp.aabsweets.com/webservices/", className = "com.aabsweets.aaberp.webservices.ListofLocationsForDoorDlyResponse")
    public String listofLocationsForDoorDly(
        @WebParam(name = "MySId", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String mySId,
        @WebParam(name = "Region", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String region);

    /**
     * 
     * @param region
     * @param mySId
     * @param subcat
     * @param maincat
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "ListofItemsForDoorDly", action = "http://aaberp.aabsweets.com/webservices/ListofItemsForDoorDly")
    @WebResult(name = "ListofItemsForDoorDlyResult", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
    @RequestWrapper(localName = "ListofItemsForDoorDly", targetNamespace = "http://aaberp.aabsweets.com/webservices/", className = "com.aabsweets.aaberp.webservices.ListofItemsForDoorDly")
    @ResponseWrapper(localName = "ListofItemsForDoorDlyResponse", targetNamespace = "http://aaberp.aabsweets.com/webservices/", className = "com.aabsweets.aaberp.webservices.ListofItemsForDoorDlyResponse")
    public String listofItemsForDoorDly(
        @WebParam(name = "MySId", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String mySId,
        @WebParam(name = "Region", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String region,
        @WebParam(name = "Maincat", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String maincat,
        @WebParam(name = "Subcat", targetNamespace = "http://aaberp.aabsweets.com/webservices/")
        String subcat);

}