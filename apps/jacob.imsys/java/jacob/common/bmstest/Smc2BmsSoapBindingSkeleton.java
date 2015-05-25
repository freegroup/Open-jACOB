/**
 * Smc2BmsSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC3 Feb 28, 2005 (10:15:14 EST) WSDL2Java emitter.
 */

package jacob.common.bmstest;

public class Smc2BmsSoapBindingSkeleton implements jacob.common.bmstest.Smc2Bms_PortType, org.apache.axis.wsdl.Skeleton {
    private jacob.common.bmstest.Smc2Bms_PortType impl;
    private static java.util.Map _myOperations = new java.util.Hashtable();
    private static java.util.Collection _myOperationsList = new java.util.ArrayList();

    /**
    * Returns List of OperationDesc objects with this name
    */
    public static java.util.List getOperationDescByName(java.lang.String methodName) {
        return (java.util.List)_myOperations.get(methodName);
    }

    /**
    * Returns Collection of OperationDescs
    */
    public static java.util.Collection getOperationDescs() {
        return _myOperationsList;
    }

    static {
        org.apache.axis.description.OperationDesc _oper;
        org.apache.axis.description.FaultDesc _fault;
        org.apache.axis.description.ParameterDesc [] _params;
        _params = new org.apache.axis.description.ParameterDesc [] {
        };
        _oper = new org.apache.axis.description.OperationDesc("getOrderList", _params, new javax.xml.namespace.QName("", "getOrderListReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://soap.entrypoint.jacob", "getOrderList"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getOrderList") == null) {
            _myOperations.put("getOrderList", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getOrderList")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "taskkey"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "bmsid"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "value"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("setOrderStatus", _params, new javax.xml.namespace.QName("", "setOrderStatusReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://soap.entrypoint.jacob", "setOrderStatus"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("setOrderStatus") == null) {
            _myOperations.put("setOrderStatus", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("setOrderStatus")).add(_oper);
    }

    public Smc2BmsSoapBindingSkeleton() {
        this.impl = new jacob.common.bmstest.Smc2BmsSoapBindingImpl();
    }

    public Smc2BmsSoapBindingSkeleton(jacob.common.bmstest.Smc2Bms_PortType impl) {
        this.impl = impl;
    }
    public java.lang.String getOrderList() throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.getOrderList();
        return ret;
    }

    public boolean setOrderStatus(java.lang.String taskkey, java.lang.String bmsid, java.lang.String value) throws java.rmi.RemoteException
    {
        boolean ret = impl.setOrderStatus(taskkey, bmsid, value);
        return ret;
    }

}
