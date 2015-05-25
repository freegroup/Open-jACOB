/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Jan 29 16:05:28 CET 2008
 */
package jacob.common.gui.contractaccounting;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;
import electric.transaction.Transaction;
import jacob.common.AppLogger;
import jacob.model.Accountcode;
import jacob.model.Accountcodeimp;

import org.apache.commons.logging.Log;

/**
 * The event handler for the ImpCodes generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user
 * clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author achim
 */
public class ImpCodes extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: ImpCodes.java,v 1.1 2008/02/06 09:02:27 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * Use this logger to write messages and NOT the
   * <code>System.out.println(..)</code> ;-)
   */
  static private final transient Log logger = AppLogger.getLogger();

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
    IDataAccessor acc = context.getDataAccessor().newAccessor();
    IDataTable acc_code = acc.getTable(Accountcode.NAME);
    IDataTable acc_imp = acc.getTable(Accountcodeimp.NAME);
    acc_imp.qbeClear();
    acc_imp.setMaxRecords(acc_imp.UNLIMITED_RECORDS);
    acc_imp.search();
    System.out.println("Sätze in Importtabelle: " + acc_imp.recordCount());
    IDataTransaction transaction = acc_code.startNewTransaction();
    try
    {
      int j = 0;
      for (int i = 0; i < acc_imp.recordCount(); i++)
      {
        IDataTableRecord imprec = acc_imp.getRecord(i);
        acc_code.qbeClear();
        acc_code.qbeSetKeyValue(Accountcode.code, imprec.getSaveStringValue(Accountcodeimp.accountingcode));
        acc_code.search();
        if (acc_code.recordCount()==0)
        {
          IDataTableRecord coderec = acc_code.newRecord(transaction);
          coderec.setValue(transaction, Accountcode.code, imprec.getSaveStringValue(Accountcodeimp.accountingcode));
          coderec.setValue(transaction, Accountcode.department, imprec.getSaveStringValue(Accountcodeimp.department));
          coderec.setValue(transaction, Accountcode.accountingstatus, Accountcode.accountingstatus_ENUM._gueltig);
          j= j+1;
        }

      }
      transaction.commit();
      alert(j + " Kostenstellen wurden importiert");
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
