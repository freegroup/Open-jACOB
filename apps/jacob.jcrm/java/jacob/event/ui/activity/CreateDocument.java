package jacob.event.ui.activity;

/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Wed Oct 05 23:54:40 CEST 2005
 *
 */
import jacob.common.AppLogger;

import java.util.Properties;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.i18n.ApplicationMessage;
import de.tif.jacob.letter.LetterFactory;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IDocumentDialog;
import de.tif.jacob.screen.dialogs.IGridTableDialog;
import de.tif.jacob.screen.dialogs.IGridTableDialogCallback;
import de.tif.jacob.screen.dialogs.IMessageDialog;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The Event handler for the CreateDocument-Button.<br>
 * The onAction will be calle if the user clicks on this button<br>
 * Insert your custom code in the onAction-method.<br>
 * 
 * @author andreas
 *
 */
public class CreateDocument extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: CreateDocument.java,v 1.1 2005/10/12 15:27:39 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

  private class GridCallback implements IGridTableDialogCallback
  {
    /* 
     * @see de.tif.jacob.screen.dialogs.IGridTableDialogCallback#onSelect(de.tif.jacob.screen.IClientContext, int, java.util.Properties)
     */
    public void onSelect(IClientContext context, int rowIndex, Properties columns) throws Exception
    {
      IDataTableRecord contractRecord = context.getSelectedRecord();
      IDataTableRecord templateRecord = context.getDataTable("doctemplate").getRecord(rowIndex);
      if (templateRecord != null)
      {
        DataDocumentValue docTemplate = templateRecord.getDocumentValue("template");
        DataDocumentValue document = LetterFactory.transform(context, contractRecord, docTemplate);

        IDocumentDialog dialog = context.createDocumentDialog(document);
        dialog.show(550, 500);
      }
      else
      {
        alert(ApplicationMessage.getLocalized("ActivityDocument.7"));
      }
    }
  }

  /**
   * The user has clicked on the corresponding button.<br>
   * Be in mind: The currentRecord can be null if the button has not the [selected] flag.<br>
   *             The selected flag warranted that the event can only be fired if the<br>
   *             selectedRecord!=null.<br>
   *
   * 
   * @param context The current client context
   * @param button  The corresponding button to this event handler
   * @throws Exception
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    // search all document templates in the database. It is possible to render the 
    // call with different type of xsl-stylesheets.
    //
    IDataTable table = context.getDataTable("doctemplate");
    table.qbeClear();
    table.qbeSetKeyValue("usedin", "Activity");
    if (!context.getSelectedRecord().hasLinkedRecord("activityContact"))
    {
      table.qbeSetKeyValue("contact_req", "0");
    }
    if (!context.getSelectedRecord().hasLinkedRecord("salesproject"))
    {
      table.qbeSetKeyValue("salesproject_req", "0");
    }
    table.search();

    if (table.recordCount() > 0)
    {
      IGridTableDialog dialog = context.createGridTableDialog(button, new GridCallback());

      // create the header for the selection grid dialog
      //
      String[] header = new String[]
      { "Id", "Name", "Type" };
      dialog.setHeader(header);

      String[][] data = new String[table.recordCount()][3];
      for (int j = 0; j < table.recordCount(); j++)
      {
        IDataTableRecord doctemplate = table.getRecord(j);
        data[j] = new String[]
        { doctemplate.getSaveStringValue("pkey"), doctemplate.getSaveStringValue("name"), doctemplate.getSaveStringValue("doctype") };
      }
      dialog.setData(data);
      dialog.show(300, 100);
    }
    else
    {
      // no document template in the system found. make a user notification.
      //
      IMessageDialog dialog = context.createMessageDialog(ApplicationMessage.getLocalized("ActivityDocument.NoTemplate"));
      dialog.show();
    }
  }

  /**
   * The status of the parent group (TableAlias) has been changed.<br>
   * <br>
   * This is a good place to enable/disable the button on relation to the
   * group state or the selected record.<br>
   * <br>
   * Possible values for the state is defined in IGuiElement<br>
   * <ul>
   *     <li>IGuiElement.UPDATE</li>
   *     <li>IGuiElement.NEW</li>
   *     <li>IGuiElement.SEARCH</li>
   *     <li>IGuiElement.SELECTED</li>
   * </ul>
   * 
   * Be in mind: The currentRecord can be null if the button has not the [selected] flag.<br>
   *             The selected flag warranted that the event can only be fired if the<br>
   *             selectedRecord!=null.<br>
   *
   * @param context The current client context
   * @param status  The new group state. The group is the parent of the corresponding event button.
   * @param button  The corresponding button to this event handler
   * @throws Exception
   */
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
  }
}
