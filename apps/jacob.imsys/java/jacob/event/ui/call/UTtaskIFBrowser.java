package jacob.event.ui.call;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IGroupListenerEventHandler;

import jacob.common.AppLogger;
import org.apache.commons.logging.Log;

/**
 * @author mike
 *
 */
public class UTtaskIFBrowser extends de.tif.jacob.screen.event.IBrowserEventHandler
{
  static public final transient String RCS_ID = "$Id: UTtaskIFBrowser.java,v 1.1 2005/06/07 07:40:26 mike Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  // use this logger to write messages and NOT the System.out.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

  /**
   * Filter the cell data for the handsover browser. A browser can be a InFormBrowser or an
   * Search Browser.
   * 
   * @param context the working context
   * @param browser the browser itself
   * @param row the row which can be filter
   * @param column the column
   * @param value the value from the database
   * @return the new value for the browser.
   */
  public String filterCell(IClientContext context, IBrowser browser,int row,int column,String value) throws Exception
  {
    return value;
  }
  
  
  /**
   * Will be called if the user selects one record in the Browser (SearchBrowser, InformBrowser).
   * 
   * @param context the current context of the client browser.
   * @param browser The browser with the click event
   * @param selectedRecord the record which has been selected
   * @param rowIndex the selected row of the record
   * 
   */
  public void onRecordSelect(IClientContext context, IBrowser browser, IDataTableRecord selectedRecord) throws Exception
  {
        String pkey = selectedRecord.getStringValue("pkey");
        String targetForm = "UTtask";
        IDataBrowser taskbrowser;

        taskbrowser = context.getDataBrowser("taskBrowser");
        
            

        context.clearDomain();
        IDataTable tasktable = context.getDataTable("task");
        tasktable.qbeSetValue("pkey", pkey);
        
        taskbrowser.search("r_task", Filldirection.BOTH);
        taskbrowser.setSelectedRecordIndex(0);
        taskbrowser.propagateSelections();

        context.setCurrentForm(targetForm);
  }
}
