/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Oct 23 11:07:27 CEST 2007
 */
package jacob.common.gui.category;

import jacob.model.Category;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The event handler for the CatInactive generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user
 * clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author achim
 */
public class CatInactive extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: CatInactive.java,v 1.1 2007/12/18 14:17:19 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * Use this logger to write messages and NOT the
   * <code>System.out.println(..)</code> ;-)
   */

  static class MyCallback implements IOkCancelDialogCallback
  {
    public void onOk(IClientContext context) throws Exception
    {
      IDataBrowser exBrowser = context.getDataBrowser("categoryAdminBrowser");
      IDataTransaction transaction = context.getDataAccessor().newAccessor().newTransaction();
      try
      {
        int i = 0;
        for (i = 0; i < exBrowser.recordCount(); i++)
        {
          IDataTableRecord exRec = exBrowser.getRecord(i).getTableRecord();
          exRec.setValue(transaction, Category.categorystatus, Category.categorystatus_ENUM._Ungueltig);
        }
        transaction.commit();
        IDialog dialog = context.createMessageDialog(i + " Kategorien auf ungültig gesetzt");
        dialog.show();
      }
      finally
      {
        transaction.close();
      }
      context.clearDomain();
    }

    public void onCancel(IClientContext context) throws Exception
    {

    }
  }

  /**
   * The user has clicked on the corresponding button.<br>
   * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
   * if the button has not the [selected] flag.<br>
   * The selected flag assures that the event can only be fired,<br>
   * if <code>selectedRecord!=null</code>.<br>
   * 
   * @param context
   *          The current client context
   * @param button
   *          The corresponding button to this event handler
   * @throws Exception
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    IOkCancelDialog dialog = context.createOkCancelDialog("Alle Kategorien im Browser werden auf üngültig gesetzt!\n Fortfahren?", new MyCallback());
    dialog.show();

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
    // button.setEnable(true/false);
  }
}
