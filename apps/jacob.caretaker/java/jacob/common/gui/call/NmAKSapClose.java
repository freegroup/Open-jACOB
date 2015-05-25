/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri May 09 11:33:28 CEST 2008
 */
package jacob.common.gui.call;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IOkCancelDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.common.sap.CWriteSAPExchange;
import jacob.common.sap.CheckCallChange;
import jacob.model.Call;
import jacob.model.Calls;
import jacob.model.Callworkgroup;

import org.apache.commons.logging.Log;

/**
 * The event handler for the NmAKSapClose generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user
 * clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author achim
 */
public class NmAKSapClose extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: NmAKSapClose.java,v 1.1 2008/05/30 08:42:56 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * Use this logger to write messages and NOT the
   * <code>System.out.println(..)</code> ;-)
   */
  static private final transient Log logger = AppLogger.getLogger();
  static class MyCallback implements IOkCancelDialogCallback
  {
    public void onOk(IClientContext context) throws Exception 
    {
      IDataTable nmcalls = context.getDataTable(Call.NAME);
      IDataTable wg = context.getDataTable(Callworkgroup.NAME);
      nmcalls.qbeClear();
      nmcalls.qbeSetValue(Calls.datereported, ">28.01.08");
      nmcalls.qbeSetValue(Calls.sap_ssle_nr, "!null");
      nmcalls.qbeSetValue(Calls.callstatus, "Dokumentiert|Geschlossen");
      
      wg.qbeSetValue(Callworkgroup.migration, Callworkgroup.migration_ENUM._Nein);
      
      nmcalls.search("r_call");
      try 
      {
        for (int i = 0; i < nmcalls.recordCount(); i++) 
        {
          IDataTableRecord rec = nmcalls.getRecord(i);

          CWriteSAPExchange.updateSaveExchangeCall(rec); 
          CWriteSAPExchange.closeSaveExchangeCall(rec);  
        }

      }
      catch (Exception e) 
      {
        // TODO: handle exception
      }
    }

    public void onCancel(IClientContext context) throws Exception 
    {
      context.createMessageDialog("You have pressed the [cancel] button").show();
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
		IDataTable nmcalls = context.getDataTable(Call.NAME);
    IDataTable wg = context.getDataTable(Callworkgroup.NAME);
    nmcalls.qbeClear();
    nmcalls.qbeSetValue(Calls.datereported, ">28.01.08");
    nmcalls.qbeSetValue(Calls.sap_ssle_nr, "!null");
    nmcalls.qbeSetValue(Calls.callstatus, "Dokumentiert|Geschlossen");
    
    wg.qbeSetValue(Callworkgroup.migration, Callworkgroup.migration_ENUM._Nein);
    
    nmcalls.search("r_call");
    IOkCancelDialog dialog = context.createOkCancelDialog(nmcalls.recordCount() + " Datensätze gefunden. Weiter?",new MyCallback());
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
