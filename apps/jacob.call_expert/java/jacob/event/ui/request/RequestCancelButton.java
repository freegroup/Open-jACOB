/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Nov 05 16:41:23 CET 2008
 */
package jacob.event.ui.request;

import jacob.common.tabcontainer.TabManagerRequest;
import jacob.model.Customer;
import jacob.model.Event;
import jacob.model.Request;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.impl.jad.castor.Relationsets;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 */
public class RequestCancelButton extends IButtonEventHandler 
{
  static public final transient String RCS_ID = "$Id: RequestCancelButton.java,v 1.5 2009/10/08 10:32:03 A.Boeken Exp $";
  static public final transient String RCS_REV = "$Revision: 1.5 $";

  /**
   * The user has clicked on the corresponding button.<br>
   * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
   *             if the button has not the [selected] flag.<br>
   *             The selected flag assures that the event can only be fired,<br>
   *             if <code>selectedRecord!=null</code>.<br>
   * 
   * @param context The current client context
   * @param button  The corresponding button to this event handler
   * @throws Exception
   */
  public void onClick(IClientContext context, IGuiElement emitter) throws Exception
  {
    IDataTableRecord requestRecord = context.getDataTable(Request.NAME).getSelectedRecord();
    if(requestRecord.isNew())
    {
      context.getDataTable(Customer.NAME).clear();
      context.getDataTable(Request.NAME).clear();
      context.getDataTable(Event.NAME).clear();
    }
    else
    {
      requestRecord.getCurrentTransaction().close();
      requestRecord.getAccessor().propagateRecord(requestRecord, Filldirection.BACKWARD);
    }
  }
   
  /**
   * The status of the parent group (TableAlias) has been changed.<br>
   * <br>
   * This is a good place to enable/disable the button on relation to the
   * group state or the selected record.<br>
   * <br>
   * Possible values for the different states are defined in IGuiElement<br>
   * <ul>
   *     <li>IGuiElement.UPDATE</li>
   *     <li>IGuiElement.NEW</li>
   *     <li>IGuiElement.SEARCH</li>
   *     <li>IGuiElement.SELECTED</li>
   * </ul>
   * 
   * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
   *             if the button has not the [selected] flag.<br>
   *             The selected flag assures that the event can only be fired,<br>
   *             if <code>selectedRecord!=null</code>.<br>
   *
   * @param context The current client context
   * @param status  The new group state. The group is the parent of the corresponding event button.
   * @param button  The corresponding button to this event handler
   * @throws Exception
   */
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
    button.setVisible(IGroup.UPDATE==status || IGroup.NEW==status);
  }
}
