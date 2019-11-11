
package com.aabsweets.aaberp.webservices;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "PlaceMyOrder", targetNamespace = "http://aaberp.aabsweets.com/webservices/", wsdlLocation = "file:/C:/Users/ashish.bhavsar/Desktop/ashish/aab_wsdl/aab.wsdl")
public class PlaceMyOrder
    extends Service
{

    private final static URL PLACEMYORDER_WSDL_LOCATION;
    private final static WebServiceException PLACEMYORDER_EXCEPTION;
    private final static QName PLACEMYORDER_QNAME = new QName("http://aaberp.aabsweets.com/webservices/", "PlaceMyOrder");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("file:/C:/Users/ashish.bhavsar/Desktop/ashish/aab_wsdl/aab.wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        PLACEMYORDER_WSDL_LOCATION = url;
        PLACEMYORDER_EXCEPTION = e;
    }

    public PlaceMyOrder() {
        super(__getWsdlLocation(), PLACEMYORDER_QNAME);
    }

    public PlaceMyOrder(WebServiceFeature... features) {
        super(__getWsdlLocation(), PLACEMYORDER_QNAME, features);
    }

    public PlaceMyOrder(URL wsdlLocation) {
        super(wsdlLocation, PLACEMYORDER_QNAME);
    }

    public PlaceMyOrder(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, PLACEMYORDER_QNAME, features);
    }

    public PlaceMyOrder(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public PlaceMyOrder(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns PlaceMyOrderSoap
     */
    @WebEndpoint(name = "PlaceMyOrderSoap")
    public PlaceMyOrderSoap getPlaceMyOrderSoap() {
        return super.getPort(new QName("http://aaberp.aabsweets.com/webservices/", "PlaceMyOrderSoap"), PlaceMyOrderSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns PlaceMyOrderSoap
     */
    @WebEndpoint(name = "PlaceMyOrderSoap")
    public PlaceMyOrderSoap getPlaceMyOrderSoap(WebServiceFeature... features) {
        return super.getPort(new QName("http://aaberp.aabsweets.com/webservices/", "PlaceMyOrderSoap"), PlaceMyOrderSoap.class, features);
    }

    private static URL __getWsdlLocation() {
        if (PLACEMYORDER_EXCEPTION!= null) {
            throw PLACEMYORDER_EXCEPTION;
        }
        return PLACEMYORDER_WSDL_LOCATION;
    }

}