/**
 * Smc2Bms_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC3 Feb 28, 2005 (10:15:14 EST) WSDL2Java emitter.
 */

package jacob.common.bmstest;

public interface Smc2Bms_PortType extends java.rmi.Remote {
    public java.lang.String getOrderList() throws java.rmi.RemoteException;
    public boolean setOrderStatus(java.lang.String taskkey, java.lang.String bmsid, java.lang.String value) throws java.rmi.RemoteException;
}
