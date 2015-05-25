/*
 * Created on Wed Jul 20 10:35:20 CEST 2005
 *
 */
package jacob.entrypoint.soap;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.soap.SOAPContext;
import de.tif.jacob.soap.SOAPEntryPoint;

/**
 * This is a SOAP Adapter class .<br>
 * SOAP is an XML-based communication protocol and encoding format for inter-application communication. 
 * Originally conceived by Microsoft and Userland software, it has evolved through several generations; 
 * the current spec is version, SOAP 1.2, though version 1.1 is more widespread. The W3C's XML Protocol 
 * working group is in charge of the specification.
 * 
 * Default encoding:        SOAP 1.1 "http://schemas.xmlsoap.org/soap/encoding/"
 * The WSDL for this class: http://localhost:8080/jacob/services/imsys/smc2bms?wsdl
 *                          (Please replace <b>localhost:8080</b> with your real server and port!)
 * 
 * Note: All <b>public</b> methods with generic data types like "String, String[], int, int[], byte, byte[]...." 
 *       will be exported as SOAP methods.
 *       
 * You <b>must</b> have a valid user account to call this SOAP service. The WSDL can be retrieve without
 * a valid user. 
 * 
 */
public class smc2bms extends SOAPEntryPoint
{
  /**
   * This is only a example method.
   *  
   * @param value
   * @return
   */

  public String echo(String value)
  {
    SOAPContext   context  = getContext();
    IDataAccessor accessor = context.getDataAccessor();

    return value;
  }
    public String insertOrder( String[] arg1, String[] arg2 )
    {
        return "1";
    }

public String getOrderList()
    {
      return "Hello";
    }

public boolean setOrderStatus( String taskkey, String bmsid, String value )
    {
     return true;
    }


  /**
   * ONLY TO TEST THIS CLASS!!!
   * 
   * We use Apache Axis to test the SOAP service. You can use any SOAP1.1 / SOAP1.2 
   * client. 
   * 
   */
  /*
	public static void main(String[] args) 
	{
		try 
		{
			Service service = new org.apache.axis.client.Service();
			Call call = (Call) service.createCall();

			// set the endpoint of the SOAP service
			//
			call.setTargetEndpointAddress("http://localhost:8080/jacob/services/imsys/smc2bms");
			
			// Enter a valid user/password for the 'imsys' application
			//
			call.setUsername("jACOB USER!!!");
			call.setPassword("jACOB USER PASSWORD!!");

			// we want call the 'echo' method
			//
			call.setOperationName(new QName("echo"));
			
			// the method has one input parameter
			//
		  call.addParameter("value", Constants.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
		  
		  // ...and a String type as return value
		  //
			call.setReturnType(Constants.XSD_STRING);
		
			// invoke the SOAP object with the required parameters
			//
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
	*/
}
