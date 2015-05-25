/**
 * Smc2BmsSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC3 Feb 28, 2005 (10:15:14 EST) WSDL2Java emitter.
 */

package jacob.common.bms;

public class Smc2BmsSoapBindingSkeleton implements jacob.common.bms.DSHWebservice_SMC, org.apache.axis.wsdl.Skeleton {
    private jacob.common.bms.DSHWebservice_SMC impl;
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
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://53.79.185.239:8080/axis/services/smc2bms", "ArrayOf_xsd_string"), java.lang.String[].class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://53.79.185.239:8080/axis/services/smc2bms", "ArrayOf_xsd_string"), java.lang.String[].class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("insertOrder", _params, new javax.xml.namespace.QName("", "insertOrderReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://smc.webservice.bms.app.p.edm.debis.com", "insertOrder"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("insertOrder") == null) {
            _myOperations.put("insertOrder", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("insertOrder")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
        };
        _oper = new org.apache.axis.description.OperationDesc("getOrderList", _params, new javax.xml.namespace.QName("", "getOrderListReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://smc.webservice.bms.app.p.edm.debis.com", "getOrderList"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getOrderList") == null) {
            _myOperations.put("getOrderList", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getOrderList")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("setOrderStatus", _params, new javax.xml.namespace.QName("", "setOrderStatusReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://smc.webservice.bms.app.p.edm.debis.com", "setOrderStatus"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("setOrderStatus") == null) {
            _myOperations.put("setOrderStatus", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("setOrderStatus")).add(_oper);
    }

    public Smc2BmsSoapBindingSkeleton() {
        this.impl = new jacob.common.bms.Smc2BmsSoapBindingImpl();
    }

    public Smc2BmsSoapBindingSkeleton(jacob.common.bms.DSHWebservice_SMC impl) {
        this.impl = impl;
    }
    public java.lang.String insertOrder(java.lang.String[] in0, java.lang.String[] in1) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.insertOrder(in0, in1);
        return ret;
    }

    public java.lang.String getOrderList() throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.getOrderList();
        return ret;
    }

    public boolean setOrderStatus(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException
    {
        boolean ret = impl.setOrderStatus(in0, in1, in2);
        return ret;
    }

}
