/*
 * Created on 22.03.2004
 * by mike
 *
 */
package jacob.common;
import jacob.model.CallAgent;
import jacob.model.Groupmember;
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
  static public final transient String RCS_ID = "$Id: Call.java,v 1.2 2006/01/22 17:20:12 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";
  

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
    IDataTable	   groupmemberTable	= accessor.getTable(Groupmember.NAME); //	the groupmember table to perform a constraint
      
// Meister wollen einen Clear Focus vorab!
    context.clearDomain();

       
	 // set  search criteria
     callTable.qbeSetValue(jacob.model.Call.callstatus,statusConstraint );

	 groupmemberTable.qbeSetKeyValue(Groupmember.employee_key,context.getUser().getKey() );


	
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
         callTable.qbeSetValue(jacob.model.Call.callstatus,statusConstraint );

         
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
       callTable.qbeSetValue(jacob.model.Call.callstatus,statusConstraint );
       callTable.qbeSetKeyValue(jacob.model.Call.callowner_key,context.getUser().getKey() );
       
       // do the search itself
       //
       browser.search(relationSet,Filldirection.BOTH);

       // display the result set
       //
       context.getGUIBrowser().setData(context, browser);
      }

	



	public static void setDefault(IClientContext context, IDataTableRecord newcall, IDataTransaction newtrans) throws Exception
  {
    newcall.setValue(newtrans, jacob.model.Call.dateassigned, null);
    newcall.setValue(newtrans, jacob.model.Call.dateowned, null);
    newcall.setValue(newtrans, jacob.model.Call.date_qa, null);
    newcall.setValue(newtrans, jacob.model.Call.dateclosed, null);

    // link record with current agent
    IDataTable agent = context.getDataAccessor().getTable(CallAgent.NAME);
    agent.qbeClear();
    agent.qbeSetValue(CallAgent.pkey, context.getUser().getKey());
    agent.search();
    if (agent.recordCount() == 1)
      newcall.setLinkedRecord(newtrans, agent.getRecord(0));
  }
}
