package jacob.event.ui.letter_template;

/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Wed Mar 02 12:43:35 CET 2005
 *
 */
import jacob.common.AppLogger;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IDocumentDialog;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The Event handler for the Letter_templateButtonDownload-Button. <br>
 * The onAction will be calle if the user clicks on this button <br>
 * Insert your custom code in the onAction-method. <br>
 * 
 * @author andherz
 *  
 */
public class Letter_templateButtonDownload extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: Letter_templateButtonDownload.java,v 1.2 2005/05/11 18:14:49 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

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
    DataDocumentValue document = currentRecord.getDocumentValue("document");
    IDocumentDialog dialog = context.createDocumentDialog(document);
    dialog.show();
  }

  /**
   * The status of the parent group (TableAlias) has been changed. <br>
   * <br>
   * This is a good place to enable/disable the button on relation to the group
   * state or the selected record. <br>
   * <br>
   * Possible values for the state is defined in IGuiElement <br>
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
    //button.setEnable(true/false);
  }
}
