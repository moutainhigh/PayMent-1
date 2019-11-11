
package com.awl.tch.brandemi.samsung;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;

import com.awl.tch.brandemi.constants.Constants;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "ValidateSerialService", targetNamespace = Constants.TCH_BRDEMI_URL_PINELAB, wsdlLocation = Constants.TCH_BRDEMI_URL_VALIDATESERVICE)
public class ValidateSerialService
    extends Service
{

    private final static URL VALIDATESERIALSERVICE_WSDL_LOCATION;
    private final static WebServiceException VALIDATESERIALSERVICE_EXCEPTION;
    private final static QName VALIDATESERIALSERVICE_QNAME = new QName(Constants.TCH_BRDEMI_URL_PINELAB, "ValidateSerialService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL(Constants.TCH_BRDEMI_URL_VALIDATESERVICE);
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        VALIDATESERIALSERVICE_WSDL_LOCATION = url;
        VALIDATESERIALSERVICE_EXCEPTION = e;
    }

    public ValidateSerialService() {
        super(__getWsdlLocation(), VALIDATESERIALSERVICE_QNAME);
    }

    public ValidateSerialService(WebServiceFeature... features) {
        super(__getWsdlLocation(), VALIDATESERIALSERVICE_QNAME, features);
    }

    public ValidateSerialService(URL wsdlLocation) {
        super(wsdlLocation, VALIDATESERIALSERVICE_QNAME);
    }

    public ValidateSerialService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, VALIDATESERIALSERVICE_QNAME, features);
    }

    public ValidateSerialService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ValidateSerialService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns ValidateSerialServiceSoap
     */
    @WebEndpoint(name = "ValidateSerialServiceSoap")
    public ValidateSerialServiceSoap getValidateSerialServiceSoap() {
        return super.getPort(new QName(Constants.TCH_BRDEMI_URL_PINELAB, "ValidateSerialServiceSoap"), ValidateSerialServiceSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ValidateSerialServiceSoap
     */
    @WebEndpoint(name = "ValidateSerialServiceSoap")
    public ValidateSerialServiceSoap getValidateSerialServiceSoap(WebServiceFeature... features) {
        return super.getPort(new QName(Constants.TCH_BRDEMI_URL_PINELAB, "ValidateSerialServiceSoap"), ValidateSerialServiceSoap.class, features);
    }

    private static URL __getWsdlLocation() {
        if (VALIDATESERIALSERVICE_EXCEPTION!= null) {
            throw VALIDATESERIALSERVICE_EXCEPTION;
        }
        return VALIDATESERIALSERVICE_WSDL_LOCATION;
    }

}
