/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Mar 02 12:26:53 CET 2006
 */
package jacob.event.ui.employee;

import jacob.model.Employee;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The event handler for the ResetPassword record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user
 * clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class ResetPassword extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: ResetPassword.java,v 1.1 2006/03/07 19:20:35 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * The user has clicked on the corresponding button.
   * 
   * @param context
   *          The current client context
   * @param button
   *          The corresponding button to this event handler
   * @throws Exception
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    IDataTableRecord currentRecord = context.getSelectedRecord();
    IDataTransaction trans = currentRecord.getAccessor().newTransaction();
    try
    {
      currentRecord.setValue(trans, Employee.password, null);
      trans.commit();
    }
    finally
    {
      trans.close();
    }
  }

  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
    IDataTableRecord currentRecord = context.getSelectedRecord();
    button.setEnable(status == IGuiElement.SELECTED && !currentRecord.hasNullValue(Employee.password));
  }
}
