/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Dec 14 15:02:15 CET 2006
 */
package jacob.entrypoint.soap;

import jacob.common.AppLogger;
import jacob.common.GradeManager;
import jacob.common.StateManager;
import jacob.model.Application;
import jacob.model.Bug;
import jacob.model.Creator;
import jacob.model.Owner;

import javax.xml.namespace.QName;

import org.apache.axis.Constants;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.IRelationSet;
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
 * The WSDL for this class: http://localhost:8080/{applicationName}/services/{applicationName}/BugService?wsdl
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
public class BugService extends SOAPEntryPoint
{
	static public final transient String RCS_ID = "$Id: BugService.java,v 1.1 2007/11/25 22:10:33 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * Required for connection test of Eclipse bugtracker plugin.
	 * @return always true
	 */
	public boolean test()
	{
		return true;
	}
	
	/**
	 *
	 *	result[0] = Bug.pkey
	 *	result[1] = Bug.application
   *	result[2] = Bug.description
	 *	result[3] = Bug.grade
	 *	result[4] = Bug.reproduceabitlity
	 *	result[5] = Bug.state
	 *	result[6] = Bug.title
	 *	result[7] = FILLER
	 *	result[8] = FILLER
	 *	result[9] = FILLER
	 *	result[10]= FILLER
	 *	result[11]= FILLER
	 *	result[12]= FILLER
	 *  
	 * @param value
	 * @return
	 */
	public String[] findByPkey(String pkey)
	{
		try {
			SOAPContext   context  = getContext();
			IDataAccessor accessor = context.getDataAccessor();

			IDataTable bugTable = accessor.getTable(Bug.NAME);
			bugTable.qbeSetKeyValue(Bug.pkey, pkey);
			int count = bugTable.search();
			if(count==1)
			{
				IDataTableRecord bug = bugTable.getSelectedRecord();
				IDataTableRecord owner   = bug.getLinkedRecord(Owner.NAME);
				IDataTableRecord creator = bug.getLinkedRecord(Creator.NAME);
				IDataTableRecord app     = bug.getLinkedRecord(Application.NAME);

				String[] result = new String[13];
				result[0] = bug.getSaveStringValue(Bug.pkey);
				result[1] = app.getSaveStringValue(Application.name);
				result[2] = bug.getSaveStringValue(Bug.description);
				result[3] = bug.getSaveStringValue(Bug.grade);
				result[4] = bug.getSaveStringValue(Bug.reproduceabitlity);
				result[5] = bug.getSaveStringValue(Bug.state);
				result[6] = bug.getSaveStringValue(Bug.title);
				result[7] = owner!=null?owner.getSaveStringValue(Owner.fullname):"<unset>";
				result[8] = creator!=null?creator.getSaveStringValue(Creator.fullname):"<unset>";
				result[9] = "FILLER";
				result[10] = "FILLER";
				result[11] = "FILLER";
				result[12] = "FILLER";
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
	 * 
	 * @return
	 */
	public String[] create(String applicationName)
	{
		SOAPContext   context  = getContext();
		IDataAccessor accessor = context.getDataAccessor();
		IDataTransaction trans = accessor.newTransaction();
		try 
		{

			IDataTable bugTable = accessor.getTable(Bug.NAME);
			IDataTableRecord bug = bugTable.newRecord(trans);

			String[] app = new ApplicationService().findByName(applicationName);
			bug.setValue(trans, Bug.application_key, app[0]);
			bug.setValue(trans, Bug.title, "subject");
			trans.commit();
			
			String[] result = new String[13];
			result[0] = bug.getSaveStringValue(Bug.pkey);
			result[1] = applicationName;
			result[2] = bug.getSaveStringValue(Bug.description);
			result[3] = bug.getSaveStringValue(Bug.grade);
			result[4] = bug.getSaveStringValue(Bug.reproduceabitlity);
			result[5] = bug.getSaveStringValue(Bug.state);
			result[6] = bug.getSaveStringValue(Bug.title);
			result[7] = "<unset>";
			result[8] = "<unset>";
			result[9] = "FILLER";
			result[10] = "FILLER";
			result[11] = "FILLER";
			result[12] = "FILLER";
			return result;
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
	
	public boolean commit(String pkey, String state, String grade, String subject, String description)
	{
		SOAPContext   context  = getContext();
		IDataAccessor accessor = context.getDataAccessor();
		IDataTransaction trans = accessor.newTransaction();
		try 
		{
			IDataTable bugTable = accessor.getTable(Bug.NAME);
			bugTable.qbeSetKeyValue(Bug.pkey, pkey);
			int count = bugTable.search();
			if(count==1)
			{
				IDataTableRecord bug = bugTable.getSelectedRecord();
				bug.setValue(trans, Bug.grade, grade);
				bug.setValue(trans, Bug.state, state);
				bug.setValue(trans, Bug.title, subject);
				bug.setValue(trans, Bug.description, description);
				trans.commit();
			}
		} 
		catch(Exception exc)
		{
			throw new RuntimeException(exc.getMessage(),exc);
		}
		finally
		{
			trans.close();
		}
		return true;
	}
	
	/**
	 *
	 *	result[0] = Bug.pkey
	 *	result[1] = Bug.application
   *	result[2] = Bug.description
	 *	result[3] = Bug.grade
	 *	result[4] = Bug.reproduceabitlity
	 *	result[5] = Bug.state
	 *	result[6] = Bug.title
	 *	result[7] = FILLER
	 *	result[8] = FILLER
	 *	result[9] = FILLER
	 *	result[10]= FILLER
	 *	result[11]= FILLER
	 *	result[12]= FILLER
	 *  
	 * @param value
	 * @return
	 */
	public String[] findByApplicationName(String applicationName)
	{
		try {
			SOAPContext   context  = getContext();
			IDataAccessor accessor = context.getDataAccessor();

			IDataTable bugTable         = accessor.getTable(Bug.NAME);
			IDataTable applicationTable = accessor.getTable(Application.NAME);
			
			applicationTable.qbeSetKeyValue(Application.name, applicationName);
			
			int count = bugTable.search(IRelationSet.DEFAULT_NAME);
			
			String[] result = new String[count*13];
			for(int i=0; i<count;i++)
			{
				IDataTableRecord bug     = bugTable.getRecord(i);
				IDataTableRecord owner   = bug.getLinkedRecord(Owner.NAME);
				IDataTableRecord creator = bug.getLinkedRecord(Creator.NAME);
				IDataTableRecord app     = bug.getLinkedRecord(Application.NAME);
				
				result[i*13+0] = bug.getSaveStringValue(Bug.pkey);
				result[i*13+1] = app.getSaveStringValue(Application.name);
				result[i*13+2] = bug.getSaveStringValue(Bug.description);
				result[i*13+3] = bug.getSaveStringValue(Bug.grade);
				result[i*13+4] = bug.getSaveStringValue(Bug.reproduceabitlity);
				result[i*13+5] = bug.getSaveStringValue(Bug.state);
				result[i*13+6] = bug.getSaveStringValue(Bug.title);
				result[i*13+7] = owner!=null?owner.getSaveStringValue(Owner.fullname):"<unset>";
				result[i*13+8] = creator!=null?creator.getSaveStringValue(Creator.fullname):"<unset>";
				result[i*13+9] = "FILLER";
				result[i*13+10] = "FILLER";
				result[i*13+11] = "FILLER";
				result[i*13+12] = "FILLER";
			}
			return result;
		} 
		catch(Exception exc)
		{
			throw new RuntimeException(exc.getMessage(),exc);
		}
	}
	
	
	public String[] getValidStates(String state)
	{
			return StateManager.getValidStates(state);
	}
	
	public String[] getValidGrades(String grade)
	{
			return GradeManager.getValidGrades(grade);
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
			call.setTargetEndpointAddress("http://localhost:8080/{applicationName}/services/{applicationName}/BugService");
			
			// Enter a valid user/password for the '{applicationName}' application
			//
			call.setUsername("admin"); // <= Default user - replace with your user accout
			call.setPassword("");

			// we want call the 'echo' method
			//
			call.setOperationName(new QName("findByPkey"));
			
			// the method has one input parameter
			//
			call.addParameter("pkey", Constants.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
		  
			// ...and a String type as return value
			//
			call.setReturnType(Constants.XSD_STRING, String[].class);
		
			// invoke the SOAP object with the required parameters
			//
			Object result = call.invoke(new Object[]{"1"});
			
			// and print out the return value from the server
			// ....thats all
			//

			System.out.println( "pkey: " + ((String[])result)[0]);
			System.out.println( "application: " + ((String[])result)[1]);
			System.out.println( "description: " + ((String[])result)[2]);
			System.out.println( "grade: " + ((String[])result)[3]);
			System.out.println( "reproduceabitlity: " + ((String[])result)[4]);
			System.out.println( "state: " + ((String[])result)[5]);
			System.out.println( "title: " + ((String[])result)[6]);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			System.err.println(e.toString());
		}
	}
	
}
