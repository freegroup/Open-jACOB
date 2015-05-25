/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Thu Jun 25 15:07:24 CEST 2009
 */
package jacob.event.data;

import jacob.common.AppLogger;
import jacob.model.Storage_email;
import jacob.model.Storage_email_outbound;
import org.apache.commons.logging.Log;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.util.StringUtil;

/**
 *
 * @author andherz
 */
public class Storage_email_outboundTableRecord extends DataTableRecordEventHandler
{
    static public final transient String RCS_ID = "$Id: Storage_email_outboundTableRecord.java,v 1.2 2009/11/23 11:33:40 R.Spoor Exp $";
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

        if (tableRecord.isNew())
        {
            String direction = tableRecord.getSaveStringValue(Storage_email_outbound.direction);
            if (direction.equals(Storage_email.direction_ENUM._out))
            {
                String body = tableRecord.getSaveStringValue(Storage_email_outbound.text_body);

                // Attention: first think about before change this two lines :-)
                body = StringUtil.replace(body, "<br>\n", "\n");
                body = StringUtil.replace(body, "\n", "<br>\n");

                tableRecord.setStringValue(transaction, Storage_email_outbound.text_body, body);
            }
        }
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
     */
    public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
    {
    }
}
