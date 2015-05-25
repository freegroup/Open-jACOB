package jacob.event.ui.category;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
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
public class CategoryChild_categoryBrowser extends de.tif.jacob.screen.event.IBrowserEventHandler
{
  static public final transient String RCS_ID = "$Id: CategoryChild_categoryBrowser.java,v 1.2 2005/08/02 10:19:25 mike Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

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
      context.clearDomain();
      IDataBrowser categorybrowser       = context.getDataBrowser("categoryBrowser");    // the current data browser
      IDataTable category = context.getDataTable("category");
      category.qbeSetKeyValue("pkey",selectedRecord.getSaveStringValue("pkey"));
      IRelationSet relationSet = context.getApplicationDefinition().getRelationSet("category_admin");
      // do the search itself
      //
      categorybrowser.search(relationSet,Filldirection.BOTH);
      
      // display the result set
      //
      context.getGUIBrowser().setData(context, categorybrowser);
    
  }
}
