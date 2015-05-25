/*
 * Created on 02.08.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package jacob.common.gui.call;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IForeignField;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IForeignFieldEventHandler;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class CallAKForeignField extends IForeignFieldEventHandler
{
	/**
	 * es dürfen nur gültige AKs gesucht werden
	 * @author mike
	 *
	 */
	public boolean beforeSearch(IClientContext context, IForeignField foreingField) throws Exception
	{
		IDataTable callworkgroup = context.getDataTable("callworkgroup");
		callworkgroup.qbeSetKeyValue( "groupstatus","gültig");
		return true;
	}
  /* 
   * @see de.tif.jacob.screen.event.IForeignFieldEventHandler#onDeselect(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IForeignField)
   */
  public void onDeselect(IClientContext context, IForeignField foreingField)   throws Exception
  {
    // Wenn wir im Update oder New Modus sind ..
    if (context.getGroup().getDataStatus()==IGuiElement.UPDATE || context.getGroup().getDataStatus()==IGuiElement.NEW )
    {
      // dann auf von Hand geroutet setzen
    	IDataTableRecord callRecord = context.getSelectedRecord();
    	IDataTransaction myTrans = callRecord.getCurrentTransaction();
    	callRecord.setValue(myTrans,"routinginfo", "von Hand geroutet");
    }
  }

  /* 
   * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
   */
  public void onGroupStatusChanged(IClientContext context,GroupState status, IGuiElement emitter) throws Exception
  {
      emitter.setEnable(true); 
      // Wenn wir im Update oder New Modus sind ..
      if (status==IGuiElement.UPDATE || status==IGuiElement.NEW )
      {
        // Wenn die Meldung im Status Fertig gemeldet oder höher ist, dann den AK nicht mehr editierbar machen
        IDataTableRecord callRecord = context.getSelectedRecord();
        String callstatus = callRecord.getSaveStringValue("callstatus");
        if (callstatus.equals("Fertig gemeldet") || callstatus.equals("Dokumentiert") || callstatus.equals("Geschlossen"))
        {
           emitter.setEnable(false); 
        }
      }
  }

}
