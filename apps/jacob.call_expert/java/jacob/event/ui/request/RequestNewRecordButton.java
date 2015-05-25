/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Nov 05 16:22:12 CET 2008
 */
package jacob.event.ui.request;

import jacob.model.Event;
import jacob.model.Request;
import jacob.resources.I18N;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IActionButtonEventHandler;

/**
 * 
 * 
 * @author achim
 */
public class RequestNewRecordButton extends IActionButtonEventHandler
{
  public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception 
	{
    // clear the current underlying event record before. Reason: we don't want a "copy" of the event.
    //
    context.getDataTable(Event.NAME).clear();

		return true;
	}

  public void onSuccess(IClientContext context, IGuiElement button) throws Exception
  {
    IDataTableRecord eventRecord = context.getSelectedRecord();
    IDataTransaction transaction = eventRecord.getCurrentTransaction();
    
    eventRecord.setValue(transaction, Event.type, Event.type_ENUM._ChangeRequest);
    
    IDataTableRecord request = context.getDataTable(Request.NAME).getSelectedRecord();
    
    // We have new new EventRecord. Now bring the Request in the UPDATE mode.
    //
    request.setValue(transaction, Request.pkey, request.getValue(Request.pkey));
  }

  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
    button.setLabel(I18N.BUTTON_UPDATE.get(context));
    button.setVisible(status == IGroup.SELECTED);
    
    System.out.println("asdf asd sdf");
  }
}
