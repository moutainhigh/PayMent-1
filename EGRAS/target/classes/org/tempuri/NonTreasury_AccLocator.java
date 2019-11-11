/**
 * NonTreasury_AccLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tempuri;

public class NonTreasury_AccLocator extends org.apache.axis.client.Service implements org.tempuri.NonTreasury_Acc {

    public NonTreasury_AccLocator() {
    }


    public NonTreasury_AccLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public NonTreasury_AccLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for NonTreasury_AccSoap
    private java.lang.String NonTreasury_AccSoap_address = "http://164.100.137.245:8080/egras/NonTreasury_Acc.asmx";

    public java.lang.String getNonTreasury_AccSoapAddress() {
        return NonTreasury_AccSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String NonTreasury_AccSoapWSDDServiceName = "NonTreasury_AccSoap";

    public java.lang.String getNonTreasury_AccSoapWSDDServiceName() {
        return NonTreasury_AccSoapWSDDServiceName;
    }

    public void setNonTreasury_AccSoapWSDDServiceName(java.lang.String name) {
        NonTreasury_AccSoapWSDDServiceName = name;
    }

    public org.tempuri.NonTreasury_AccSoap getNonTreasury_AccSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(NonTreasury_AccSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getNonTreasury_AccSoap(endpoint);
    }

    public org.tempuri.NonTreasury_AccSoap getNonTreasury_AccSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.tempuri.NonTreasury_AccSoapStub _stub = new org.tempuri.NonTreasury_AccSoapStub(portAddress, this);
            _stub.setPortName(getNonTreasury_AccSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setNonTreasury_AccSoapEndpointAddress(java.lang.String address) {
        NonTreasury_AccSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.tempuri.NonTreasury_AccSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                org.tempuri.NonTreasury_AccSoapStub _stub = new org.tempuri.NonTreasury_AccSoapStub(new java.net.URL(NonTreasury_AccSoap_address), this);
                _stub.setPortName(getNonTreasury_AccSoapWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("NonTreasury_AccSoap".equals(inputPortName)) {
            return getNonTreasury_AccSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org/", "NonTreasury_Acc");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "NonTreasury_AccSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("NonTreasury_AccSoap".equals(portName)) {
            setNonTreasury_AccSoapEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
