/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Sep 12 12:51:15 CEST 2006
 */
package jacob.event.ui.jan_queue;

import jacob.model.Jan_attachment;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;

/**
 *
 * @author andreas
 */
public class Jan_queueJan_attachmentBrowser extends de.tif.jacob.screen.event.IBrowserEventHandler
{
	static public final transient String RCS_ID = "$Id: Jan_queueJan_attachmentBrowser.java,v 1.2 2010-11-17 17:16:03 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

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
    context.createDocumentDialog(selectedRecord.getStringValue(Jan_attachment.mimetype), selectedRecord.getDocumentValue(Jan_attachment.document)).show();
	}
}
