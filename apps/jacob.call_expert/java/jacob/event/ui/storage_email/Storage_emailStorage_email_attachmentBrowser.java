/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Wed Apr 22 17:51:02 CEST 2009
 */
package jacob.event.ui.storage_email;

import jacob.common.AppLogger;
import jacob.model.Storage_email_attachment;
import org.apache.commons.logging.Log;
import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;

/**
 *
 * @author andherz
 */
public class Storage_emailStorage_email_attachmentBrowser extends de.tif.jacob.screen.event.IInformBrowserEventHandler
{
    static public final transient String RCS_ID = "$Id: Storage_emailStorage_email_attachmentBrowser.java,v 1.3 2009/11/23 11:33:43 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.3 $";

    /**
     * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
     */
    static private final transient Log logger = AppLogger.getLogger();

    @Override
    public boolean beforeAction(IClientContext context, IBrowser browser,IDataBrowserRecord record) throws Exception
    {
        IDataTableRecord attachmentRecord = record.getTableRecord();
        DataDocumentValue doc = attachmentRecord.getDocumentValue(Storage_email_attachment.document);
        context.createDocumentDialog(doc).show();
        return false;
    }

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
    }
}
