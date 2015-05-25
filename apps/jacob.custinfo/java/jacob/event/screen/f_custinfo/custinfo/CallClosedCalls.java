/*
 * Created on 08.04.2004
 * by mike
 *
 */
package jacob.event.screen.f_custinfo.custinfo;


import java.util.HashMap;
import java.util.Map;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * 
 * @author mike
 *
 */
public class CallClosedCalls  extends IButtonEventHandler
{
	
	static Map actions = new HashMap();
	static {
		actions.put("offene Aufträge",new CallActionShowOpen("Erledigte Aufträge"));
		actions.put("Erledigte Aufträge",new CallActionShowClosed("offene Aufträge"));
	}
	
	
	/*
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.ClientContext,
	 *      de.tif.jacob.screen.Button) 
	 */
	
	public void onAction(IClientContext context, IGuiElement button)throws Exception
	{
		context.clearDomain();
		IDataAccessor accessor = context.getDataAccessor(); // current data connection
		IDataTable callTable = accessor.getTable("call"); // any function related
		IDataBrowser browser = context.getDataBrowser(); // the current browser
		IDataTable attachmentTable = context.getDataTable(); // the table in which
		//accessor.qbeClearAll();
		
		ICallAction action = (ICallAction)actions.get(button.getLabel());
		action.performe(context, callTable,button);
		
		// do the search itself
		//
		browser.search("r_call",Filldirection.BOTH);
		if (browser.recordCount() >= 1)
		{
		  browser.setSelectedRecordIndex(0);
		  browser.propagateSelections();
		}
		
    // display the result set
		//
		context.getGUIBrowser().setData(context, browser);																											 // table
	}

	/* 
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, int, de.tif.jacob.screen.IGuiElement)
	 */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button)
	{
	}
}
