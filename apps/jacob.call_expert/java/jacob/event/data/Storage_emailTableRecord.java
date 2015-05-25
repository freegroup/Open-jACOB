/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Tue Apr 21 20:24:18 CEST 2009
 */
package jacob.event.data;

import jacob.common.AppLogger;
import jacob.model.Storage_email;
import jacob.model.Storage_email_attachment;
import org.apache.commons.logging.Log;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;

/**
 *
 * @author andherz
 */
public class Storage_emailTableRecord extends DataTableRecordEventHandler
{
    static public final transient String RCS_ID = "$Id: Storage_emailTableRecord.java,v 1.2 2009/11/23 11:33:41 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.2 $";

    /**
     * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
     */
    static private final transient Log logger = AppLogger.getLogger();

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
     */
    public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
    {
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
     */
    public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
    {
        IDataAccessor acc= tableRecord.getAccessor().newAccessor();
        IDataTable attachmentTable = acc.getTable(Storage_email_attachment.NAME);
        attachmentTable.qbeSetKeyValue(Storage_email_attachment.storage_email_key, tableRecord.getValue(Storage_email.pkey));
        attachmentTable.fastDelete(transaction);
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
     */
    public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
    {
        // Be in mind: It is not possible to modify the 'tableRecord', if we want delete it
        //
        if (tableRecord.isDeleted())
        {
            return;
        }

        // enter your code here
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
     */
    public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
    {
    }
}
