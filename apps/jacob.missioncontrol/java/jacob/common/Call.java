/*
 * Created on 22.03.2004
 * by mike
 *
 */
package jacob.common;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IClientContext;


/**
 * 
 * @author mike
 *
 */

public class Call 
{
  static public final transient String RCS_ID = "$Id: Call.java,v 1.3 2005/09/06 13:29:20 mike Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";
  

  /**
	 * Search calls of the current user
	 * @param context ClientContext
	 * @param statusConstraint 
	 * @param relationSet
	 */
	public static void findByGroupAndState(IClientContext context, String statusConstraint, String relationSet) throws Exception 
  {
	  IDataAccessor accessor         = context.getDataAccessor();  // current data connection
	  IDataBrowser  browser          = context.getDataBrowser();   // the current browser
	  IDataTable    callTable        = context.getDataTable();     // the table in which the actions performes
    IDataTable	   groupmemberTable	= accessor.getTable("groupmember"); //	the groupmember table to perform a constraint
      
// Meister wollen einen Clear Focus vorab!
    context.clearDomain();

       
	 // set  search criteria
     callTable.qbeSetValue("status",statusConstraint );

	 groupmemberTable.qbeSetKeyValue("member_key",context.getUser().getKey() );


	
	 // do the search itself
	 //
	 browser.search(relationSet,Filldirection.BOTH);

	 // display the result set
	 //
	 context.getGUIBrowser().setData(context, browser);
	}
	
    public static void findByState(IClientContext context, String statusConstraint, String relationSet) throws Exception 
      {
          IDataBrowser  browser          = context.getDataBrowser();   // the current browser
          IDataTable    callTable        = context.getDataTable();     // the table in which the actions performes
          
        context.clearDomain();
        
         // set  search criteria
         callTable.qbeSetValue("status",statusConstraint );

         
         // do the search itself
         //
         browser.search(relationSet,Filldirection.BOTH);

         // display the result set
         //
         context.getGUIBrowser().setData(context, browser);
        }

    public static void findByOwnerAndState(IClientContext context, String statusConstraint, String relationSet) throws Exception 
    {
        IDataBrowser  browser          = context.getDataBrowser();   // the current browser
        IDataTable    callTable        = context.getDataTable();     // the table in which the actions performes
        
      context.clearDomain();
      
       // set  search criteria
       callTable.qbeSetValue("status",statusConstraint );
       callTable.qbeSetKeyValue("owner_key",context.getUser().getKey() );
       
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
			call.setValue(currentTransaction, "status", newStatus);
			currentTransaction.commit();
		}
		finally
		{
			currentTransaction.close();
		}
		
	}


	public static void setDefault(IClientContext context,IDataTableRecord newcall,IDataTransaction newtrans) throws Exception 
	 
	{

	   	newcall.setValue(newtrans,"dateassigned", null);
	   	newcall.setValue(newtrans,"dateowned", null);
	   	newcall.setValue(newtrans,"date_qa", null);
	   	newcall.setValue(newtrans,"datedone", null);


		//setlinkedrecord für Agent
		IDataTable agent = context.getDataAccessor().getTable("agent");
		agent.clear();
		agent.qbeClear();
		agent.qbeSetValue("pkey", context.getUser().getKey());
		agent.search();
        if (agent.recordCount()==1)
            newcall.setLinkedRecord(newtrans,agent.getRecord(0));
		
	}
}
