package jacob.event.ui.customer;

/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Wed Mar 02 11:48:31 CET 2005
 *
 */
import jacob.common.AppLogger;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.impl.DataTableRecord;
import de.tif.jacob.letter.LetterFactory;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IDocumentDialog;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * @author andherz
 *  
 */
public class CustomerButtonGenerateLetter extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: CustomerButtonGenerateLetter.java,v 1.2 2005/05/11 18:14:50 sonntag Exp $";
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
    IDataTableRecord customerRecord = context.getSelectedRecord();
    DataTableRecord templateRecord = (DataTableRecord) context.getDataTable("letter_template").getSelectedRecord();
    if (templateRecord != null)
    {
      DataDocumentValue docTemplate = templateRecord.getDocumentValue("document");
      DataDocumentValue document = LetterFactory.transform(context, customerRecord, docTemplate, docTemplate.getName());

      IDocumentDialog dialog = context.createDocumentDialog(document);
      dialog.show(550, 500);
    }
    else
    {
      alert("Please select a 'LetterTemplate' first.");
    }
  }

  /**
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
  }
}
