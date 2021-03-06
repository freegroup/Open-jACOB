/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Mar 15 12:21:44 CET 2006
 */
package jacob.event.ui.privatContact;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IUploadDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.common.vCard.VCard;

import org.apache.commons.logging.Log;

/**
 * The event handler for the ContactImportVCard generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user
 * clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author mike
 */
public class ContactImportVCard extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: ContactImportVCard.java,v 1.1 2007/11/25 22:12:38 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  class UploadDialogCallBack implements IUploadDialogCallback
  {

    public void onOk(IClientContext context, String fileName, byte[] fileData) throws Exception
    {
     // VCard.importVCard(context,fileData);
      
    }

    public void onCancel(IClientContext context) throws Exception
    {
      // do nothing
      
    }
    
  }
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
    
    context.createUploadDialog(new UploadDialogCallBack()).show();
    

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
    // Implementierung funktioniert nicht
    button.setVisible(false);
  }
}
