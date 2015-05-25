/**
 * DSHWebservice_SMCServiceTestCase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC3 Feb 28, 2005 (10:15:14 EST) WSDL2Java emitter.
 */

package jacob.common.bms;

public class DSHWebservice_SMCServiceTestCase extends junit.framework.TestCase {
    public DSHWebservice_SMCServiceTestCase(java.lang.String name) {
        super(name);
    }

    public void testsmc2bmsWSDL() throws Exception {
        javax.xml.rpc.ServiceFactory serviceFactory = javax.xml.rpc.ServiceFactory.newInstance();
        java.net.URL url = new java.net.URL(new jacob.common.bms.DSHWebservice_SMCServiceLocator().getsmc2bmsAddress() + "?WSDL");
        javax.xml.rpc.Service service = serviceFactory.createService(url, new jacob.common.bms.DSHWebservice_SMCServiceLocator().getServiceName());
        assertTrue(service != null);
    }

    public void test1smc2bmsInsertOrder() throws Exception {
        jacob.common.bms.Smc2BmsSoapBindingStub binding;
        try {
            binding = (jacob.common.bms.Smc2BmsSoapBindingStub)
                          new jacob.common.bms.DSHWebservice_SMCServiceLocator().getsmc2bms();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        java.lang.String value = null;
        value = binding.insertOrder(new java.lang.String[0], new java.lang.String[0]);
        // TBD - validate results
    }

    public void test2smc2bmsGetOrderList() throws Exception {
        jacob.common.bms.Smc2BmsSoapBindingStub binding;
        try {
            binding = (jacob.common.bms.Smc2BmsSoapBindingStub)
                          new jacob.common.bms.DSHWebservice_SMCServiceLocator().getsmc2bms();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        java.lang.String value = null;
        value = binding.getOrderList();
        // TBD - validate results
    }

    public void test3smc2bmsSetOrderStatus() throws Exception {
        jacob.common.bms.Smc2BmsSoapBindingStub binding;
        try {
            binding = (jacob.common.bms.Smc2BmsSoapBindingStub)
                          new jacob.common.bms.DSHWebservice_SMCServiceLocator().getsmc2bms();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        boolean value = false;
        value = binding.setOrderStatus(new java.lang.String(), new java.lang.String(), new java.lang.String());
        // TBD - validate results
    }

}
