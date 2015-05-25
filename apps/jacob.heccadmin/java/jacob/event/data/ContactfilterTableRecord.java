/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Tue Feb 24 10:53:49 CET 2009
 */
package jacob.event.data;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import jacob.common.AppLogger;
import jacob.model.Contactfilter;
import org.apache.commons.logging.Log;
import com.hecc.jacob.util.CreateChangeRecordEventHandler;

/**
 *
 * @author R.Spoor
 */
public class ContactfilterTableRecord extends CreateChangeRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: ContactfilterTableRecord.java,v 1.1 2009/02/24 15:55:51 R.Spoor Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

    public ContactfilterTableRecord()
    {
        super(
            Contactfilter.created_by, Contactfilter.create_date,
            Contactfilter.changed_by, Contactfilter.change_date
        );
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
     */
    @Override
    public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
    {
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
     */
    @Override
    public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
    {
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
     */
    @Override
    public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
    {
        super.beforeCommitAction(tableRecord, transaction);
        // Be in mind: It is not possible to modify the 'tableRecord', if we want delete it
        //
        if(tableRecord.isDeleted())
            return;

        // enter your code here
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
     */
    @Override
    public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
    {
    }
}
