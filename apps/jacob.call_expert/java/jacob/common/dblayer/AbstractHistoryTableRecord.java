/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Mon Jan 19 15:50:43 CET 2009
 */
package jacob.common.dblayer;

import jacob.model.Company;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;

/**
 *
 * @author andherz
 */
public abstract class AbstractHistoryTableRecord extends DataTableRecordEventHandler
{
    static public final transient String RCS_ID = "$Id: AbstractHistoryTableRecord.java,v 1.2 2009/11/23 11:33:45 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.2 $";

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
     */
    public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
    {
        tableRecord.setValue(transaction, Company.created_by, Context.getCurrent().getUser().getLoginId());
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
        if(tableRecord.isDeleted())
        {
            return;
        }

        tableRecord.setValue(transaction, Company.changed_by, Context.getCurrent().getUser().getLoginId());
        tableRecord.setValue(transaction, Company.change_date, "now");
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
     */
    public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
    {
    }
}
