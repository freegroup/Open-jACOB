/**
 * Smc2BmsServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC3 Feb 28, 2005 (10:15:14 EST) WSDL2Java emitter.
 */

package jacob.common.bmstest;

public class Smc2BmsServiceLocator extends org.apache.axis.client.Service implements jacob.common.bmstest.Smc2BmsService {

    public Smc2BmsServiceLocator() {
    }


    public Smc2BmsServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public Smc2BmsServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for smc2bms
    private java.lang.String smc2bms_address = "http://localhost:8100/jacob/services/imsys/smc2bms";

    public java.lang.String getsmc2bmsAddress() {
        return smc2bms_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String smc2bmsWSDDServiceName = "smc2bms";

    public java.lang.String getsmc2bmsWSDDServiceName() {
        return smc2bmsWSDDServiceName;
    }

    public void setsmc2bmsWSDDServiceName(java.lang.String name) {
        smc2bmsWSDDServiceName = name;
    }

    public jacob.common.bmstest.Smc2Bms_PortType getsmc2bms() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(smc2bms_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getsmc2bms(endpoint);
    }

    public jacob.common.bmstest.Smc2Bms_PortType getsmc2bms(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            jacob.common.bmstest.Smc2BmsSoapBindingStub _stub = new jacob.common.bmstest.Smc2BmsSoapBindingStub(portAddress, this);
            _stub.setPortName(getsmc2bmsWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setsmc2bmsEndpointAddress(java.lang.String address) {
        smc2bms_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (jacob.common.bmstest.Smc2Bms_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                jacob.common.bmstest.Smc2BmsSoapBindingStub _stub = new jacob.common.bmstest.Smc2BmsSoapBindingStub(new java.net.URL(smc2bms_address), this);
                _stub.setPortName(getsmc2bmsWSDDServiceName());
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
        if ("smc2bms".equals(inputPortName)) {
            return getsmc2bms();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://localhost:8100/jacob/services/imsys/smc2bms", "smc2bmsService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://localhost:8100/jacob/services/imsys/smc2bms", "smc2bms"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("smc2bms".equals(portName)) {
            setsmc2bmsEndpointAddress(address);
        }
        else { // Unknown Port Name
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
