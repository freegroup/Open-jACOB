package jacob.event.ui.recipe;

/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Fri Mar 11 12:12:47 CET 2005
 *
 */
import jacob.common.AppLogger;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.impl.DataTableRecord;
import de.tif.jacob.letter.LetterFactory;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IDocumentDialog;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The Event handler for the RecipeButtonExportToWord-Button. <br>
 * The onAction will be calle if the user clicks on this button <br>
 * Insert your custom code in the onAction-method. <br>
 * 
 * @author andherz
 *  
 */
public class RecipeButtonExportToWord extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: RecipeButtonExportToWord.java,v 1.2 2005/05/11 18:14:49 sonntag Exp $";
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
    DataTableRecord recipeRecord = (DataTableRecord) context.getSelectedRecord();
    DataDocumentValue docTemplate = recipeRecord.getDocumentValue("template");
    DataDocumentValue document = LetterFactory.transform(context, recipeRecord, docTemplate, docTemplate.getName());

    IDocumentDialog dialog = context.createDocumentDialog(null, document);
    dialog.show(550, 500);
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
    DataTableRecord recipeRecord = (DataTableRecord) context.getSelectedRecord();
    if (recipeRecord == null)
      return;
    DataDocumentValue docTemplate = recipeRecord.getDocumentValue("template");
    button.setEnable(docTemplate != null);
  }
}