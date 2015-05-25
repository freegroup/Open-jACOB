/*
 * Created on 22.03.2004
 * by mike
 *
 */
package jacob.common;
import jacob.common.data.DataUtils;
import jacob.exception.BusinessException;

import java.util.Date;
import java.util.Vector;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.security.IUser;
import electric.uddi.GetRegistered;


/**
 * 
 * @author mike
 *
 */

public class Call 
{
  static public final transient String RCS_ID = "$Id: Call.java,v 1.6 2005/12/14 16:52:44 mike Exp $";
  static public final transient String RCS_REV = "$Revision: 1.6 $";
  
	static protected final transient Log logger = AppLogger.getLogger();
  static private final Vector globalAccessRoles = new Vector();
  
  static
  {
    globalAccessRoles.add("CQ_PM");
    globalAccessRoles.add("CQ_ADMIN");
    globalAccessRoles.add("CQ_AGENT");
    globalAccessRoles.add("CQ_SDADMIN");
  }
  
  /**
	 * Search calls of the current user
	 * @param context ClientContext
	 * @param statusConstraint 
	 * @param relationSet
	 */
	public static void findByUserAndState(IClientContext context, IUser user, String statusConstraint, String relationSet) throws Exception 
  {
	  IDataAccessor accessor         = context.getDataAccessor();  // current data connection
	  IDataBrowser  browser          = context.getDataBrowser();   // the current browser
	  IDataTable    callTable        = context.getDataTable();     // the table in which the actions performes
    IDataTable	   groupmemberTable	= accessor.getTable("groupmember"); //	the groupmember table to perform a constraint
      
// Meister wollen einen Clear Focus vorab!
    context.clearDomain();

    
    String userKey = user.getKey();
    
	 // set  search criteria
     callTable.qbeSetValue("callstatus",statusConstraint );

	 groupmemberTable.qbeSetValue("employeegroup",userKey );
     // auch nur lesenden Zugriff dies erlauben
	// groupmemberTable.qbeSetValue("accessallowed", "lesen/schreiben");

	
	 // do the search itself
	 //
	 browser.search(relationSet,Filldirection.BOTH);

	 // display the result set
	 //
	 context.getGUIBrowser().setData(context, browser);
	}
	
	
	/**
	 * Set calls.callstatus = newStatus
	 * @param context
	 * @param newStatus
	 */
	public static void setStatus(IClientContext context,  IDataTableRecord call, String newStatus)throws Exception
	{
		IDataTransaction currentTransaction = context.getDataAccessor().newTransaction();
		try
		{
			call.setValue(currentTransaction, "callstatus", newStatus);
			currentTransaction.commit();
		}
		finally
		{
			currentTransaction.close();
		}
		logger.debug("commit done");
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public static boolean accessallowed(IDataTableRecord record) throws Exception
	{
	  Context context=Context.getCurrent();
		IUser user = context.getUser();
		boolean access;
		if(user.hasOneRoleOf(globalAccessRoles)) 
		{
			access = true;
		}
		else 
		{
			IDataTable currentcall = context.getDataTable("call");
			IDataTable groupmember = context.getDataTable("groupmember");
			groupmember.qbeClear();
			groupmember.qbeSetValue("employeegroup", context.getUser().getKey());
			groupmember.qbeSetValue("workgroupgroup", record.getValue("workgroupcall"));
			groupmember.qbeSetValue("accessallowed", "lesen/schreiben");
			groupmember.search();
			access= groupmember.recordCount() > 0 ;
		}
		
		return access;
	}
	
	/**
	 * wenn der User nur Rolle AK hat, dann muss die Tabelle groupmember <br>
	 * mit dem User.key eingeschränkt werden
	 * @param context
	 */
	public static void setGroubmemberConstraint(IClientContext context) throws Exception
	{
		IUser user = context.getUser();
		if(!user.hasOneRoleOf(globalAccessRoles) ) 
		{
			IDataTable grpmember = context.getDataTable("groupmember");
			grpmember.qbeClear();
			grpmember.qbeSetValue("employeegroup",user.getKey());
		}

	}
	public static void setDefault(IClientContext context,IDataTableRecord newcall,IDataTransaction newtrans) throws Exception 
	 
	{

		newcall.setValue(newtrans,"autoclosed", "Ja");
		newcall.setValue(newtrans,"cclist", null);
		newcall.setValue(newtrans,"closedby_sd", "0");
	    newcall.setValue(newtrans,"closedtaskcount", "0");
	    newcall.setValue(newtrans,"coordinationtime", null);
	   	newcall.setValue(newtrans,"cti_phone", null);
	   	newcall.setValue(newtrans,"dateassigned", null);
	   	newcall.setValue(newtrans,"datecallbackreq", null);
	   	newcall.setValue(newtrans,"datecallconnected", null);
	   	newcall.setValue(newtrans,"dateclosed", null);
	   	newcall.setValue(newtrans,"datedocumented", null);
	   	newcall.setValue(newtrans,"datefirstcontact", null);
	   	newcall.setValue(newtrans,"dateowned", null);
	   	newcall.setValue(newtrans,"datereported", null);
	    newcall.setValue(newtrans,"dateresolved", null);
	    newcall.setValue(newtrans,"defaultcontract", "0");
	    newcall.setValue(newtrans,"forwardbyphone", "0");
	    newcall.setValue(newtrans,"ksl", null);
	    newcall.setValue(newtrans,"opentaskcount", "0");
	    newcall.setValue(newtrans,"origin", null);
     
	    newcall.setValue(newtrans,"sd_time", null);
	    newcall.setValue(newtrans,"sl", null);
	    newcall.setValue(newtrans,"totaltaskdoc", null);
	    newcall.setValue(newtrans,"totaltasktimespent", null);
        
	    newcall.setValue(newtrans,"callstatus", "Durchgestellt");
	    
	    //setzen von datecallconnected 
	    newcall.setValue(newtrans,"datecallconnected", new Date() );
	    
		//setlinkedrecord für Agent
		IDataTable agent = context.getDataTable("agent");
		agent.clear();
		agent.qbeClear();
		agent.qbeSetValue("pkey", context.getUser().getKey());
		agent.search();
        if(agent.recordCount()!=1)
            throw new BusinessException("Sie sind nicht als Erfasser registriert. \n Wenden Sie sich bitte an den Administrator.");
		newcall.setLinkedRecord(newtrans,agent.getRecord(0));
		
		// Setzten von betroffener Person: 
		// Wenn Gruppe Melder ausgefüllt und betroffene Person leer, 
		// dann kopiere Melder in betroffene Person
		
		IDataTableRecord customer = context.getDataTable("customerint").getSelectedRecord();
		if (customer != null)
		{
            if (newcall.getValue("accountingcodetext")== null)
                newcall.setValue(newtrans,"accountingcodetext", customer.getValue("accountingcodecorr"));
            
			if (newcall.getValue("affectedperson") == null)
			{
				String value = customer.getStringValue("fullname") + " Tel: " + customer.getSaveStringValue("phonecorr");
				newcall.setValue(newtrans, "affectedperson", value);
			}
            
            if (context.getDataTable("affectedperson").recordCount()!=1)
            {
                IDataTable affectedperson = context.getDataTable("affectedperson");
                affectedperson.qbeClear();
                affectedperson.qbeSetKeyValue("pkey",customer.getSaveStringValue("pkey"));
                affectedperson.search();
                if (affectedperson.recordCount()==1)
                    newcall.setLinkedRecord(newtrans,affectedperson.getRecord(0));
            }

		}
        if (context.getDataTable("process").recordCount()!=1)
        {
            IDataTable process = context.getDataTable("process");
            process.qbeClear();
            process.qbeSetKeyValue("pkey",DataUtils.getAppprofileValue(context,"process_key"));
            process.search();
            newcall.setLinkedRecord(newtrans,process.getRecord(0));
            
        }
        // AK oder Owner löschen
        context.getDataTable("callworkgroup").clear();
        newcall.setValue(newtrans, "workgroupcall", null);
	}
}
