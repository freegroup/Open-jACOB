/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Sep 18 11:49:43 CEST 2007
 */
package jacob.common.gui.sap_objimport;

import jacob.common.AppLogger;
import jacob.model.Object;
import jacob.model.Sap_objimport;
import org.apache.commons.logging.Log;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The event handler for the FindEdvinObjects generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user
 * clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author achim
 */
public class FindEdvinObjects extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: FindEdvinObjects.java,v 1.1 2007/12/18 14:17:17 achim Exp $";
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
    int objCounter = 0;
    IDataTable oimp = context.getDataTable(Sap_objimport.NAME);
    oimp.qbeClear();
    oimp.search();
    IDataTable obj = context.getDataTable(Object.NAME);
    for (int i = 0; i < oimp.recordCount(); i++)
    {
      String[] objzk = oimp.getRecord(i).getSaveStringValue(Sap_objimport.kz_techplatz).split("-");
      String oid = objzk[objzk.length - 1];
      obj.qbeClear();
      obj.qbeSetValue(Object.external_id, oid);
      obj.search();
      if (obj.recordCount() == 1)
      {
        // Eindeutiger Satz gefunden
        objCounter = objCounter + 1;
      }
    }
    alert(objCounter + " Edinobjekte eindeutig zugewiesen");
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
