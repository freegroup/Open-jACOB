/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Sep 16 10:16:35 CEST 2010
 */
package jacob.event.ui.document.verwaltung;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.model.Document;
import jacob.model.Editor;
import jacob.security.Role;

import org.apache.commons.logging.Log;

/**
 * The event handler for the DocumentLockForChangeButton record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author achim
 */
public class DocumentLockForChangeButton extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: DocumentLockForChangeButton.java,v 1.1 2010-09-17 08:42:23 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
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
    IDataTableRecord docrec = context.getDataTable(Document.NAME).getSelectedRecord();
    IDataTransaction transaction = context.getDataAccessor().newTransaction();
    docrec.setValue(transaction, Document.locked, 1);
    docrec.setValue(transaction, Document.lockedsince, "now");
    IDataTable editor = context.getDataTable(Editor.NAME);
    editor.qbeClear();
    editor.qbeSetKeyValue(Editor.pkey, context.getUser().getKey());
    editor.search();
    docrec.setLinkedRecord(transaction, editor.getRecord(0));
    try
    {
      transaction.commit();
    }
    finally
    {
      transaction.close();
    }

  }

  /**
   * The status of the parent group (TableAlias) has been changed.<br>
   * <br>
   * This is a good place to enable/disable the button on relation to the group state or the selected record.<br>
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
   *          The new group state. The group is the parent of the corresponding event button.
   * @param button
   *          The corresponding button to this event handler
   * @throws Exception
   */
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
    // You can enable/disable the button in relation to your conditions.
    //

    if (context.getUser().hasRole(Role.EDITOR))
    {
      IDataTableRecord docrec = context.getDataTable(Document.NAME).getSelectedRecord();
      if (null != docrec)
      {

        if (docrec.getintValue(Document.locked) == 1)
        {
          button.setVisible(false);
        }
        else
        {
          button.setVisible(true);
        }
      }
    }
    else
    {
      button.setVisible(false);
    }


  }
}
