/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Sat Aug 18 13:16:10 CEST 2007
 */
package jacob.common.gui.ext_system;

import java.util.Date;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.common.sap.CSAPHelperClass;
import jacob.model.Ext_system;
import jacob.model.Object;

import org.apache.commons.logging.Log;
import org.apache.poi.util.SystemOutLogger;

/**
 * The event handler for the RestoreeEdvinObjects record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user
 * clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author achim
 */
public class RestoreeEdvinObjects extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: RestoreeEdvinObjects.java,v 1.1 2007/08/20 17:47:11 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * Use this logger to write messages and NOT the
   * <code>System.out.println(..)</code> ;-)
   */
  static private final transient Log logger = AppLogger.getLogger();

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
    System.out.println("*** Objectrestore started  at: " + (new Date()) + " ***");
    IDataTableRecord extsysrec = context.getSelectedRecord();
    IDataTable objects = context.getDataAccessor().getTable("sap_object");
    objects.clear();
    objects.qbeClear();
    objects.qbeSetValue(Object.ext_system_key, extsysrec.getSaveStringValue(Ext_system.pkey));
    objects.setMaxRecords(900000);
    objects.search();
    IDataTransaction transaction = context.getDataAccessor().newTransaction();
    int i = 0;
    try
    {
      for (i=0; i < objects.recordCount(); i++)
      {
        IDataTableRecord objrec = objects.getRecord(i);
        if (!objrec.getValue(Object.sap_old_edvin_status).equals(Object.objstatus_ENUM._0_Default))
        {
          objrec.setValue(transaction, Object.objstatus, objrec.getValue(Object.sap_old_edvin_status));
        }
      }
      transaction.commit();
      System.out.println("*** Objectrestore changed " + i + " Records ***");
      System.out.println("*** Objectrestore endet  at: " + (new Date()) + " ***");
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
