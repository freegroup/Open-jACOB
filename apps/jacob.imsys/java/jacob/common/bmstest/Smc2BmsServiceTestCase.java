/**
 * Smc2BmsServiceTestCase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC3 Feb 28, 2005 (10:15:14 EST) WSDL2Java emitter.
 */

package jacob.common.bmstest;

import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

import com.ibm.wsdl.Constants;

public class Smc2BmsServiceTestCase extends junit.framework.TestCase {
    public Smc2BmsServiceTestCase(java.lang.String name) {
        super(name);
    }

    public void testsmc2bmsWSDL() throws Exception {
        javax.xml.rpc.ServiceFactory serviceFactory = javax.xml.rpc.ServiceFactory.newInstance();
        java.net.URL url = new java.net.URL(new jacob.common.bmstest.Smc2BmsServiceLocator().getsmc2bmsAddress() + "?WSDL");
        javax.xml.rpc.Service service = serviceFactory.createService(url, new jacob.common.bmstest.Smc2BmsServiceLocator().getServiceName());
        assertTrue(service != null);
    }

    public void test1smc2bmsGetOrderList() throws Exception {
        jacob.common.bmstest.Smc2BmsSoapBindingStub binding;
        try {
            binding = (jacob.common.bmstest.Smc2BmsSoapBindingStub)
                          new jacob.common.bmstest.Smc2BmsServiceLocator().getsmc2bms();
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

    public void test2smc2bmsSetOrderStatus() throws Exception {
        jacob.common.bmstest.Smc2BmsSoapBindingStub binding;
        try {
            binding = (jacob.common.bmstest.Smc2BmsSoapBindingStub)
                          new jacob.common.bmstest.Smc2BmsServiceLocator().getsmc2bms();
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
    public static void main(String[] args) 
    {
        try 
        {
            Service service = new org.apache.axis.client.Service();
            Call call = (Call) service.createCall();

            // set the endpoint of the SOAP service
            //
            call.setTargetEndpointAddress("http://localhost:8100/jacob/services/imsys/smc2bms");
            
            // Enter a valid user/password for the 'imsys' application
            //
            call.setUsername("q");
            call.setPassword("q");

            // we want call the 'echo' method
            //
            call.setOperationName(new QName("echo"));
            
            // the method has one input parameter
            //
          call.addParameter("value", org.apache.axis.Constants.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
          
          // ...and a String type as return value
          //
            call.setReturnType(org.apache.axis.Constants.XSD_STRING);
        
            // invoke the SOAP object with the required parameters
            //
            //Object result = call.invoke(new Object[]{"echo this string"});
            Object result = call.invoke(new Object[]{"echo this string"});
            
            // and print out the return value from the server
            // ....thats all
            //
            System.out.println( "Server reply: " + result);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            System.err.println(e.toString());
        }
    }
}
