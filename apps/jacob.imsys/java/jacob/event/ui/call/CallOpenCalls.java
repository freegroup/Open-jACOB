/*
 * Created on 23.07.2004
 *
 */
package jacob.event.ui.call;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IActionButtonEventHandler;
import de.tif.jacob.security.IUser;

/**
 * @author achim
 *
 */
public class CallOpenCalls extends IActionButtonEventHandler {
static public final transient String RCS_ID = "$Id: CallOpenCalls.java,v 1.1 2005/06/03 15:18:54 mike Exp $";
static public final transient String RCS_REV = "$Revision: 1.1 $";
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IActionButtonEventHandler#beforeAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IActionEmitter)
	 */
	public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception 
	{
		IDataTable contexttable = context.getDataTable();
		contexttable.qbeSetValue("callstatus","Durchgestellt|AK zugewiesen|Fehlgeroutet|Angenommen");
		IUser user = context.getUser();
		String searchstring = user.getKey();
		if (user.hasRole("CQ_PM")|user.hasRole("CQ_ADMIN")|user.hasRole("CQ_AGENT")|user.hasRole("CQ_SUPERAK")|user.hasRole("CQ_SDADMIN")) 
		{
			searchstring = "";
		}
		IDataTable groupmember = context.getDataTable("groupmember");
		groupmember.qbeSetValue("employeegroup",searchstring);
		
		return true;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IActionButtonEventHandler#onSuccess(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	public void onSuccess(IClientContext context, IGuiElement button) {


	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState status,
			IGuiElement emitter) throws Exception {


	}

}
