package jacob.event.ui.incidentContact;

import jacob.common.AppLogger;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.exception.RecordNotFoundException;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;

/**
 * @author andreas
 *
 */
public class IncidentContactIncidentContactOpenIncidentBrowser extends de.tif.jacob.screen.event.IBrowserEventHandler
{
  static public final transient String RCS_ID = "$Id: IncidentContactIncidentContactOpenIncidentBrowser.java,v 1.1 2005/10/12 15:27:39 sonntag Exp $";
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
   * @param contactIncidentRecord the record which has been selected
   * @param rowIndex the selected row of the record
   */
  public void onRecordSelect(IClientContext context, IBrowser browser, IDataTableRecord contactIncidentRecord) throws Exception
  {
    // clear complete domain to fresh with given record
    context.clearDomain();

    context.setCurrentForm("incident");

    // constrain search with primary key of the given incident
    //
    IDataTable incidentTable = context.getDataTable("incident");
    incidentTable.qbeSetPrimaryKeyValue(contactIncidentRecord.getPrimaryKeyValue());

    // search and propagate the record
    //
    IDataBrowser incidentBrowser = context.getDataBrowser("incidentBrowser");
    incidentBrowser.search("r_incident", Filldirection.BOTH);
    if (incidentBrowser.recordCount() == 0)
    {
      // just in case the record has been deleted in the mean while
      throw new RecordNotFoundException(contactIncidentRecord.getId());
    }
    incidentBrowser.setSelectedRecordIndex(0);
    incidentBrowser.propagateSelections();
  }
}
