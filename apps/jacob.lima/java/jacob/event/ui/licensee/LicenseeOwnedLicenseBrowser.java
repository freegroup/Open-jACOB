/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Mar 03 01:24:01 CET 2006
 */
package jacob.event.ui.licensee;

import jacob.model.License;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IBrowserField;
import de.tif.jacob.core.exception.RecordNotFoundException;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;

/**
 *
 * @author andreas
 */
public class LicenseeOwnedLicenseBrowser extends de.tif.jacob.screen.event.IBrowserEventHandler
{
	static public final transient String RCS_ID = "$Id: LicenseeOwnedLicenseBrowser.java,v 1.1 2006/03/07 19:20:36 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * Filters the cell data for the given browser. A browser can be an in-form
   * browser or a search browser.
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
   * @return the new value for the browser or <code>null</code> to keep cell
   *         empty.
   */
	public String filterCell(IClientContext context, IBrowser browser, int row, int column, String value) throws Exception
	{
    if (value == null)
    {
      IBrowserField browserField = (IBrowserField) browser.getDefinition().getBrowserFields().get(column);
      if ("browserExpiration_date".equals(browserField.getName()))
        return "indefinite";
    }

    return value;
	}
  
  /**
   * This hook method will be called, if the user selects a record in the
   * browser.
   * 
   * @param context
   *          the current client context
   * @param browser
   *          The browser with the click event
   * @param selectedRecord
   *          the record which has been selected
   */
	public void onRecordSelect(IClientContext context, IBrowser browser, IDataTableRecord selectedRecord) throws Exception
	{
    // constrain search with primary key of the given incident
    //
    IDataTable licenseTable = context.getDataTable(License.NAME);
    licenseTable.getAccessor().qbeClearAll();
    licenseTable.qbeSetPrimaryKeyValue(selectedRecord.getPrimaryKeyValue());
    
    // search and propagate the record
    //
    IDataBrowser licenseBrowser = context.getDataBrowser("licenseBrowser");
    licenseBrowser.search("license", Filldirection.BOTH);
    if (licenseBrowser.recordCount() == 0)
    {
      // just in case the record has been deleted in the mean while
      throw new RecordNotFoundException(selectedRecord.getId());
    }
    licenseBrowser.setSelectedRecordIndex(0);
    licenseBrowser.propagateSelections();
	}
}
