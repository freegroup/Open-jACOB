/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Sat Aug 18 12:58:43 CEST 2007
 */
package jacob.common.gui.ext_system;

import java.util.Date;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IOkCancelDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.model.Object;
import jacob.model.Objectlocation;
import jacob.model.Sapadmin;

import org.apache.commons.logging.Log;

/**
 * The event handler for the DeleteIproObjects generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user
 * clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author achim
 */
public class DeleteIproObjects extends IButtonEventHandler
{ 
  static boolean bOK =false; 
  static public final transient String RCS_ID = "$Id: DeleteIproObjects.java,v 1.1 2007/08/20 17:47:11 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * Use this logger to write messages and NOT the
   * <code>System.out.println(..)</code> ;-)
   */
  static private final transient Log logger = AppLogger.getLogger();
  static private class MyCallback implements IOkCancelDialogCallback
  {
    public void onOk(IClientContext context) throws Exception 
    {
      bOK = true;

    }

    public void onCancel(IClientContext context) throws Exception 
    {
      bOK = false;
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
    System.out.println("*** Objectdelete started  at: " + (new Date()) + " ***");
    IDataTable sapadmin = context.getDataAccessor().getTable(Sapadmin.NAME);
    sapadmin.clear();
    sapadmin.qbeClear();
    sapadmin.qbeSetValue(Sapadmin.active, "1");
    sapadmin.search();
    if (sapadmin.recordCount() != 1)
    {
      throw new UserException("Externes System konnte nicht zugewiesen werden, da SAP Admin Datensatz nicht eindeutig");
    }
    IDataTableRecord sapadminrec = sapadmin.getRecord(0);
    String extsys = sapadminrec.getSaveStringValue(Sapadmin.ext_system_key);
    IDataTable objects = context.getDataAccessor().getTable(Object.NAME);
    objects.clear();
    objects.qbeClear();
    objects.qbeSetValue(Object.ext_system_key, extsys);
    objects.setMaxRecords(900000);
    objects.search();
    System.out.println(objects.recordCount());
    IDataTransaction transaction = context.getDataAccessor().newTransaction();
    try
    {
      int i = 0;
      int y = 0;
      // objects.fastDelete(transaction);
      for (i = 0; i < objects.recordCount(); i++)
      {
        y=y+1;
        IDataTableRecord objrec = objects.getRecord(i);
        if (objrec.hasLinkedRecord(Objectlocation.NAME))
        {
          IDataTableRecord objlocrec = objrec.getLinkedRecord(Objectlocation.NAME);
          objrec.resetLinkedRecord(transaction, Objectlocation.NAME);
          objrec.delete(transaction);
          objlocrec.delete(transaction);
        }
        else
        {
          objrec.delete(transaction);
        }
        if (y==100)
        {
          y=0;
          System.out.println("Records " + i + "to be deleted");
        }

      }
      IOkCancelDialog dialog =context.createOkCancelDialog(i +" records to be deleted", new MyCallback());
      dialog.show();
      if (bOK)
      {
        transaction.commit();

      }
      System.out.println("*** Objectdelete endet  at: " + (new Date()) + " ***");      
    }
    finally
    {
      transaction.close();
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
    // button.setEnable(true/false);
  }

}
