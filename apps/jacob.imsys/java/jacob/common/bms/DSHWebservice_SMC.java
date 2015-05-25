/**
 * DSHWebservice_SMC.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC3 Feb 28, 2005 (10:15:14 EST) WSDL2Java emitter.
 */

package jacob.common.bms;

public interface DSHWebservice_SMC extends java.rmi.Remote {
    public java.lang.String insertOrder(java.lang.String[] in0, java.lang.String[] in1) throws java.rmi.RemoteException;
    public java.lang.String getOrderList() throws java.rmi.RemoteException;
    public boolean setOrderStatus(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
}
