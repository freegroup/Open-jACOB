package jacob.common.gui.sapcallexchange;

/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Fri Jun 15 14:13:40 CEST 2007
 *
 */
import jacob.model.Sapcallexchange;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IOkCancelDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The Event handler for the Deleteall-Button.<br>
 * The onAction will be calle if the user clicks on this button<br>
 * Insert your custom code in the onAction-method.<br>
 * 
 * @author E050_FWT-ANT_o_test
 * 
 */
public class Deleteall extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: Deleteall.java,v 1.4 2007/09/03 13:22:10 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";

  static class MyCallback implements IOkCancelDialogCallback
  {
    public void onOk(IClientContext context) throws Exception
    {
      IDataAccessor acc = context.getDataAccessor();
      IDataTable exc = acc.getTable(Sapcallexchange.NAME);
      exc.qbeClear();

      IDataTransaction transaction = acc.newTransaction();
      try
      {
        exc.fastDelete(transaction);
        transaction.commit();
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
   * The user has been click on the corresponding button.<br>
   * Be in mind: The currentRecord can be null if the button has not the
   * [selected] flag.<br>
   * The selected flag warranted that the event can only be fired if the<br>
   * selectedRecord!=null.<br>
   * 
   * 
   * @param context
   *          The current client context
   * @param button
   *          The corresponding button to this event handler
   * @throws Exception
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    IOkCancelDialog dialog = context.createOkCancelDialog("Alle Synchronisationsanweisungen werden gelöscht!\nLöschen?", new MyCallback());
    dialog.show();
  }

  /**
   * The status of the parent group (TableAlias) has been changed.<br>
   * <br>
   * This is a good place to enable/disable the button on relation to the group
   * state or the selected record.<br>
   * <br>
   * Possible values for the state is defined in IGuiElement<br>
   * <ul>
   * <li>IGuiElement.UPDATE</li>
   * <li>IGuiElement.NEW</li>
   * <li>IGuiElement.SEARCH</li>
   * <li>IGuiElement.SELECTED</li>
   * </ul>
   * 
   * Be in mind: The currentRecord can be null if the button has not the
   * [selected] flag.<br>
   * The selected flag warranted that the event can only be fired if the<br>
   * selectedRecord!=null.<br>
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
