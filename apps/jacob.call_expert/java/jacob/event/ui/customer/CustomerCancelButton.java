/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Nov 05 12:32:58 CET 2008
 */
package jacob.event.ui.customer;

import jacob.common.GroupManagerCustomerHeader;
import jacob.common.tabcontainer.TabManagerRequest;
import jacob.model.Customer;
import jacob.model.Dashboard;
import jacob.model.Event;
import jacob.model.Event_edit_company;
import jacob.model.Event_edit_customer;
import jacob.model.Request;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The event handler for the CustomerCancelButton generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user
 * clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author achim
 */
public class CustomerCancelButton  extends IButtonEventHandler
{
  static public final transient String RCS_ID  = "$Id: CustomerCancelButton.java,v 1.4 2009/10/08 10:32:00 A.Boeken Exp $";

  static public final transient String RCS_REV = "$Revision: 1.4 $";

  /**
   * Clear all Table alias which are currently in the
   * 
   * @param context
   *          The current client context
   * @param button
   *          The corresponding button to this event handler
   * @throws Exception
   */
  public void onClick(IClientContext context, IGuiElement emitter) throws Exception
  {
    IDataTableRecord eventRecord   = context.getDataTable(Event_edit_customer.NAME).getSelectedRecord();

    IDataTableRecord customerRecord = context.getDataTable(Customer.NAME).getSelectedRecord();
    {
      if (customerRecord.isNew())
      {
        context.getDataTable(Customer.NAME).clear();
        context.getDataTable(Event.NAME).clear();
      }
      else
      {
        customerRecord.getCurrentTransaction().close();
      }
      TabManagerRequest.setActive(context, Dashboard.NAME);
    }
  }

  /**
   * The status of the parent group (TableAlias) has been changed.<br>
   * <br>
   * This is a good place to enable/disable the button on relation to the group
   * state or the selected record.<br>
   * <br>
   * Possible values for the different states are defined in IGuiElement<br>
   * <ul>
   * <li>IGuiElement.UPDATE</li>
   * <li>IGuiElement.NEW</li>
   * <li>IGuiElement.SEARCH</li>
   * <li>IGuiElement.SELECTED</li>
   * </ul>
   * 
   * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
   * if the button has not the [selected] flag.<br>
   * The selected flag assures that the event can only be fired,<br>
   * if <code>selectedRecord!=null</code>.<br>
   * 
   * @param context
   *          The current client context
   * @param status
   *          The new group state. The group is the parent of the corresponding
   *          event button.
   * @param button
   *          The corresponding button to this event handler
   * @throws Exception
   */
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
    // You can enable/disable the button in relation to your conditions.
    //
    button.setVisible(IGroup.UPDATE == status || IGroup.NEW == status);
  }
}