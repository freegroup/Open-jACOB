/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Sep 17 10:45:39 CEST 2010
 */
package jacob.event.ui.document;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.*;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.dialogs.IDocumentDialog;

import jacob.common.AppLogger;
import jacob.model.Document;
import jacob.model.Docversions;

import org.apache.commons.logging.Log;

/**
 * 
 * @author achim
 */
public class DocumentDocversionsBrowser extends de.tif.jacob.screen.event.IInformBrowserEventHandler
{
  static public final transient String RCS_ID = "$Id: DocumentDocversionsBrowser.java,v 1.1 2010-09-17 08:57:17 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
   */
  static private final transient Log logger = AppLogger.getLogger();

  /**
   * Filters the cell data for the given browser. A browser can be an in-form browser or a search browser.
   * 
   * @param context
   *          the current client context
   * @param browser
   *          the browser itself
   * @param row
   *          the row which can be filtered
   * @param column
   *          the column
   * @param value
   *          the original value from the database
   * @return the new value for the browser or <code>null</code> to keep cell empty.
   */
  public String filterCell(IClientContext context, IBrowser browser, int row, int column, String value) throws Exception
  {
    return value;
  }

  /**
   * This hook method will be called, if the user selects a record in the browser.
   * 
   * @param context
   *          the current client context
   * @param browser
   *          The browser with the click event
   * @param selectedRecord
   *          the record which has been selected
   */
  public void onRecordSelect(IClientContext context, IBrowser browser, IDataTableRecord documentRecord) throws Exception
  {

    IDocumentDialog dialog = context.createDocumentDialog(documentRecord.getDocumentValue(Docversions.content));
    dialog.show();
  }

  @Override
  public void onGroupStatusChanged(IClientContext context, GroupState state, IInFormBrowser browser) throws Exception
  {
    IDataBrowser b = browser.getData();
    if (b.recordCount() > 0)
    {
      browser.setVisible(true);
    }
    else
    {
      browser.setVisible(false);
    }
  }
}
