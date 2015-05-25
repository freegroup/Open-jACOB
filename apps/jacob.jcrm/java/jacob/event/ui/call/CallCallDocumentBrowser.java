/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Jan 12 16:47:58 CET 2006
 */
package jacob.event.ui.call;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;

import jacob.common.AppLogger;
import jacob.model.CallDocument;

import org.apache.commons.logging.Log;

/**
 *
 * @author mike
 */
public class CallCallDocumentBrowser extends de.tif.jacob.screen.event.IBrowserEventHandler
{
	static public final transient String RCS_ID = "$Id: CallCallDocumentBrowser.java,v 1.1 2006/01/13 18:17:56 mike Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * Filters the cell data for the given browser. A browser can be an in-form browser or a
	 * search browser.
	 * 
	 * @param context the working context
	 * @param browser the browser itself
	 * @param row the row which can be filtered
	 * @param column the column
	 * @param value the value from the database
	 * @return the new value for the browser.
	 */
	public String filterCell(IClientContext context, IBrowser browser, int row, int column, String value) throws Exception
	{
		return value;
	}
  
  
	/**
	 * Will be called, if the user selects a record in the browser (SearchBrowser, InformBrowser).
	 * 
	 * @param context the current context of the client browser.
	 * @param browser The browser with the click event
	 * @param selectedRecord the record which has been selected
	 * @param rowIndex the selected row of the record
	 * 
	 */
	public void onRecordSelect(IClientContext context, IBrowser browser, IDataTableRecord selectedRecord) throws Exception
	{
    context.createDocumentDialog(selectedRecord.getDocumentValue(CallDocument.docfile)).show();
	}
}
