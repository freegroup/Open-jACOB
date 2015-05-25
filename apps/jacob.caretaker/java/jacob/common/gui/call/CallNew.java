/*
 * Created on 27.07.2004
 *
 */
package jacob.common.gui.call;

import jacob.common.Call;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IActionButtonEventHandler;

/**
 * Script, welches bei anlegen einer Meldung ausgeführt wird
 * Haupsächlich Feldinitialisierungen finden statt.
 * 
 * @author achim
 *
 */
public class CallNew extends IActionButtonEventHandler {
  static public final transient String RCS_ID = "$Id: CallNew.java,v 1.9 2008/09/25 16:17:45 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.9 $";


	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IActionButtonEventHandler#beforeAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IActionEmitter)
	 */
	public boolean beforeAction(IClientContext context, IActionEmitter button)
	throws Exception {

		return true;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IActionButtonEventHandler#onSuccess(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	public void onSuccess(IClientContext context, IGuiElement button) throws Exception 
	 
	{
		IDataTableRecord newcall = context.getSelectedRecord();
		IDataTransaction newtrans= newcall.getCurrentTransaction();
		Call.setDefault(context,newcall,newtrans);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState status,IGuiElement emitter) throws Exception 
	{


	}

}
