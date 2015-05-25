/*
 * Created on 03.05.2006 by mike
 * 
 *
 */
package jacob.common.gui.employee;

import jacob.common.AppLogger;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IAskDialogCallback;
import de.tif.jacob.screen.dialogs.IDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.event.IActionButtonEventHandler;

/**
 * The event handler for the EmployeeNew new button.<br>
 * 
 * @author mike
 */
public class EmployeeNew extends IActionButtonEventHandler 
{
  static public final transient String RCS_ID = "$Id: EmployeeNew.java,v 1.1 2006/05/15 13:50:56 mike Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
   */
  static private final transient Log logger = AppLogger.getLogger();

  class AskNewEmployeeCallback implements IOkCancelDialogCallback
  {
    IDataTable employeeTable;
    /* (non-Javadoc)
     * @see de.tif.jacob.screen.dialogs.IOkCancelDialogCallback#onOk(de.tif.jacob.screen.IClientContext)
     */
    public void onOk(IClientContext context) throws Exception
    {
      IDataTransaction trans = employeeTable.startNewTransaction();
      employeeTable.newRecord(trans);
      
    }
    public AskNewEmployeeCallback(IDataTable employeeTable)
    {
      this.employeeTable = employeeTable;
    }
 

    /* (non-Javadoc)
     * @see de.tif.jacob.screen.dialogs.IAskDialogCallback#onCancel(de.tif.jacob.screen.IClientContext)
     */
    public void onCancel(IClientContext context) throws Exception
    {
      // do nothing
      
    }
    
  }
  /**
   * This event handler will be called, if the corresponding button has been pressed.
   * You can prevent the execution of the NEW action if you return <code>false</code>.<br>
   * 
   * @param context The current context of the application
   * @param button  The action button (the emitter of the event)
   * @return Return <code>false</code>, if you want to avoid the execution of the action else return <code>true</code>.
   */
  public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception 
  {
    IDataTable employeeTable = context.getDataTable();
    String message = "Sie sind dabei einen Kunden neu anzulegen. Das ist normalerweise nicht notwendig,\\n"+
                     "da in der Datenbank alle Mitarbeiter in Deutschland hinterlegt sind.\\n"+
                     "Bitte vergewissern Sie sich zunächst, ob der gesuchte Kunde nicht doch vorhanden ist.\\n\\n"+
                     "Möchten Sie dennoch einen neuen Kunden anlegen?";
    IDialog dialog = context.createOkCancelDialog(message,new AskNewEmployeeCallback(employeeTable));
    dialog.show();
    return false;
  }

  /**
   * This event method will be called, if the NEW action has been successfully executed.<br>
   *  
   * @param context The current context of the application
   * @param button  The action button (the emitter of the event)
   */
  public void onSuccess(IClientContext context, IGuiElement button) 
  {
  }

  /**
   * The status of the parent group (TableAlias) has been changed.<br>
   * <br>
   * This is a good place to enable/disable the button on relation to the
   * group state or the selected record.<br>
   * <br>
   * Possible values for the different states are defined in IGuiElement<br>
   * <ul>
   *     <li>IGuiElement.UPDATE</li>
   *     <li>IGuiElement.NEW</li>
   *     <li>IGuiElement.SEARCH</li>
   *     <li>IGuiElement.SELECTED</li>
   * </ul>
   * 
   * @param context The current client context
   * @param status  The new group state. The group is the parent of the corresponding event button.
   * @param button  The corresponding button to this event handler
   * @throws Exception
   */
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
    // You can enable/disable the button in relation to your conditions.
    //
    //button.setEnable(true/false);
  }
}
