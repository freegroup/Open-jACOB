/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Sun Dec 17 20:14:55 CET 2006
 */
package jacob.entrypoint.soap;

import jacob.common.AppLogger;
import jacob.model.Application;

import javax.xml.namespace.QName;

import org.apache.axis.Constants;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.soap.SOAPContext;
import de.tif.jacob.soap.SOAPEntryPoint;


/**
 * This is a SOAP adapter class .<br>
 * SOAP is an XML-based communication protocol and encoding format for inter-application communication. 
 * Originally conceived by Microsoft and Userland software, it has evolved through several generations; 
 * the current spec is version, SOAP 1.2, though version 1.1 is more widespread. The W3C's XML Protocol 
 * working group is in charge of the specification.
 * <p>
 * Default encoding:        SOAP 1.1 "http://schemas.xmlsoap.org/soap/encoding/"
 * The WSDL for this class: http://localhost:8080/{applicationName}/services/{applicationName}/ApplicationService?wsdl
 *                          (Please replace <b>localhost:8080</b> with your real server and port!)
 * 
 * Note: All <b>public</b> methods with generic data types like "String, String[], int, int[], byte, byte[]...." 
 *       will be exported as SOAP methods.
 *       
 * You <b>must</b> have a valid user account to call this SOAP service. The WSDL can be retrieve without
 * a valid user. 
 * 
 * @author andherz
 */
public class ApplicationService extends SOAPEntryPoint
{
	static public final transient String RCS_ID = "$Id: ApplicationService.java,v 1.1 2007/11/25 22:10:33 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * This is only an example method.
	 *  
	 * @param value
	 * @return
	 */
	public String[] getAll()
	{
		try 
		{
			SOAPContext   context  = getContext();
			IDataAccessor accessor = context.getDataAccessor();

			IDataTable applicationTable = accessor.getTable(Application.NAME);
			int count = applicationTable.search();
			String[] result = new String[count*2];
			for(int i=0; i<count; i++)
			{
				IDataTableRecord bug = applicationTable.getRecord(i);
				result[i*2]   = bug.getSaveStringValue(Application.pkey);
				result[(i*2)+1] = bug.getSaveStringValue(Application.name);
			}
			return result;
		} 
		catch(Exception exc)
		{
			throw new RuntimeException(exc.getMessage(),exc);
		}
	}

	/**
	 * result[0] = Application.pkey
	 * result[1] = Application.name
	 * result[2] = FILLER
	 * result[3] = FILLER
	 * result[4] = FILLER
	 * result[5] = FILLER
	 * result[6] = FILLER
	 * result[7] = FILLER
	 * @return
	 */
	public String[] findByName(String name)
	{
		try 
		{
			SOAPContext   context  = getContext();
			IDataAccessor accessor = context.getDataAccessor();

			IDataTable applicationTable = accessor.getTable(Application.NAME);
			applicationTable.qbeSetKeyValue(Application.name, name);
			int count = applicationTable.search();
			if(count==1)
			{
				String[] result = new String[8];
				IDataTableRecord bug = applicationTable.getSelectedRecord();
				result[0] = bug.getSaveStringValue(Application.pkey);
				result[1] = bug.getSaveStringValue(Application.name);
				result[2] = "FILLER";
				result[3] = "FILLER";
				result[4] = "FILLER";
				result[5] = "FILLER";
				result[6] = "FILLER";
				result[7] = "FILLER";
				return result;
			}
		} 
		catch(Exception exc)
		{
			throw new RuntimeException(exc.getMessage(),exc);
		}
		return null;
	}
	
	/**
	 * Create a new application entry in the bugtracker. 
	 * Nothing happens if the application name already exists.
	 * 
	 * @param name
	 */
	public void create(String name)
	{
		SOAPContext   context  = getContext();
		IDataAccessor accessor = context.getDataAccessor();
		IDataTransaction trans = accessor.newTransaction();
		try 
		{
			IDataTable applicationTable = accessor.getTable(Application.NAME);
			
			applicationTable.qbeSetKeyValue(Application.name, name);
			if(applicationTable.search()!=0)
				return;
			applicationTable.qbeClear();
			IDataTableRecord appRecord= applicationTable.newRecord(trans);
			appRecord.setStringValue(trans, Application.name, name);
			trans.commit();
		} 
		catch(Exception exc)
		{
			throw new RuntimeException(exc.getMessage(),exc);
		}
		finally
		{
			trans.close();
		}
	}
 	
	/**
	 * ONLY TO TEST THIS CLASS!!!
	 * 
	 * We use Apache Axis to test the SOAP service. You can use any SOAP1.1 / SOAP1.2 
	 * client. 
	 * 
	 */
	
	public static void main(String[] args) 
	{
		try 
		{
			Service service = new org.apache.axis.client.Service();
			Call call = (Call) service.createCall();

			// set the endpoint of the SOAP service
			//
			call.setTargetEndpointAddress("http://localhost:8080/{applicationName}/services/{applicationName}/ApplicationService");
			
			// Enter a valid user/password for the '{applicationName}' application
			//
			call.setUsername("admin"); // <= Default user - replace with your user accout
			call.setPassword("");

			// we want call the 'echo' method
			//
			call.setOperationName(new QName("getApplications"));
			
			// ...and a String type as return value
			//
			call.setReturnType(Constants.XSD_STRING, String[].class);
		
			// invoke the SOAP object with the required parameters
			//
		  String[] result = (String[])call.invoke(new Object[0]);
			
			// and print out the return value from the server
			// ....thats all
			//
		  for (int i=0; i < result.length; i+=2) 
		  {
				System.out.println("pkey: " + result[i]);
				System.out.println("name: " + result[i+1]);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			System.err.println(e.toString());
		}
	}
}
