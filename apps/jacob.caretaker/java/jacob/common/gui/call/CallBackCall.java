/*
 * Created on 31.08.2004
 *
 */
package jacob.common.gui.call;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IActionButtonEventHandler;

/**
 * @author achim
 *
 */
public class CallBackCall extends IActionButtonEventHandler {

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IActionButtonEventHandler#beforeAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IActionEmitter)
	 */
	public boolean beforeAction(IClientContext context, IActionEmitter button)
			throws Exception {
	  IDataAccessor accessor         = context.getDataAccessor();  // current data connection
	  IDataBrowser  browser          = context.getDataBrowser();   // the current browser
	  IDataTable    callTable        = context.getDataTable();     // the table in which the actions performes
      
// Meister wollen einen Clear Focus vorab!
    context.clearDomain();

    

    
	 // set  search criteria
     callTable.qbeSetValue("callstatus","Rückruf" );


	
	 // do the search itself
	 //
	 browser.search("r_call_entry",Filldirection.BOTH);

	 // display the result set
	 //
	 context.getGUIBrowser().setData(context, browser);
		return true;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IActionButtonEventHandler#onSuccess(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	public void onSuccess(IClientContext context, IGuiElement button)
			throws Exception {

	}

}
