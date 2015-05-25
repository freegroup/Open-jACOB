package jacob.event.ui.customer;

import jacob.common.AppLogger;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.dialogs.IDocumentDialog;

/**
 * @author andherz
 *  
 */
public class LetterBrowser extends de.tif.jacob.screen.event.IBrowserEventHandler
{
  static public final transient String RCS_ID = "$Id: LetterBrowser.java,v 1.1 2007/11/25 22:19:37 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  // use this logger to write messages and NOT the System.out.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

  /**
   * Filter the cell data for the handsover browser. A browser can be a
   * InFormBrowser or an Search Browser.
   * 
   * @param context
   *          the working context
   * @param browser
   *          the browser itself
   * @param row
   *          the row which can be filter
   * @param column
   *          the column
   * @param value
   *          the value from the database
   * @return the new value for the browser.
   */
  public String filterCell(IClientContext context, IBrowser browser, int row, int column, String value) throws Exception
  {
    return value;
  }

  /**
   * Will be called if the user selects one record in the Browser
   * (SearchBrowser, InformBrowser).
   * 
   * @param context
   *          the current context of the client browser.
   * @param browser
   *          The browser with the click event
   * @param selectedRecord
   *          the record which has been selected
   * @param rowIndex
   *          the selected row of the record
   *  
   */
  public void onRecordSelect(IClientContext context, IBrowser browser, IDataTableRecord selectedRecord) throws Exception
  {
    DataDocumentValue document = selectedRecord.getDocumentValue("letter");
    IDocumentDialog dialog = context.createDocumentDialog(document);
    dialog.show();
  }
}
