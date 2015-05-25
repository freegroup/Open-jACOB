package jacob.common.gui.call;

/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Wed May 23 16:02:07 CEST 2007
 *
 */
import jacob.common.AppLogger;
import jacob.common.sap.CExportSSLERFC;
import jacob.common.sap.ConnManager;
import jacob.model.Sapcallexchange;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The Event handler for the Create_ssle-Button.<br>
 * The onAction will be calle if the user clicks on this button<br>
 * Insert your custom code in the onAction-method.<br>
 * 
 * @author E050_FWT-ANT_o_test
 * 
 */
public class Create_ssle extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: Create_ssle.java,v 1.3 2007/12/18 14:17:18 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

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
    ConnManager oConMan = new ConnManager();
    IDataTransaction transaction = currentRecord.getTable().startNewTransaction();
    IDataTableRecord rec = context.getDataTable(Sapcallexchange.NAME).newRecord(transaction);

    try
    {
      CExportSSLERFC.createSSLE(oConMan, currentRecord,rec,transaction);
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
