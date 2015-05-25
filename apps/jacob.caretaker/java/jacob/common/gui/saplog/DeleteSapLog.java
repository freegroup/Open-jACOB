package jacob.common.gui.saplog;

/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Tue Jun 12 16:41:53 CEST 2007
 *
 */
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The Event handler for the DeleteSapLog-Button.<br>
 * The onAction will be calle if the user clicks on this button<br>
 * Insert your custom code in the onAction-method.<br>
 * 
 * @author e050_fwt-ant_o_test
 * 
 */
public class DeleteSapLog extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: DeleteSapLog.java,v 1.3 2007/09/03 13:22:08 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  /**
   * The user has been click on the corresponding button.
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
    IDataTable log = currentRecord.getTable();
    log.qbeClear();
    log.qbeSetValue("pkey", "<" + currentRecord.getValue("pkey"));

    IDataTransaction transaction = log.getAccessor().newTransaction();
    try
    {
      log.fastDelete(transaction);
      transaction.commit();
    }
    finally
    {
      transaction.close();
    }
    context.clearDomain();
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
