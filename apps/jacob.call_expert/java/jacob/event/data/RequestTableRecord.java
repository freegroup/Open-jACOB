/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Wed Nov 05 15:49:49 CET 2008
 */
package jacob.event.data;

import jacob.common.dblayer.AbstractHistoryTableRecord;
import jacob.model.Request;
import jacob.model.Request_category;
import jacob.resources.I18N;
import java.util.Date;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.UserException;

/**
 *
 * @author achim
 */
public class RequestTableRecord extends AbstractHistoryTableRecord
{
    static public final transient String RCS_ID = "$Id: RequestTableRecord.java,v 1.4 2009/11/23 11:33:41 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.4 $";

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
     */
    public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
    {
        super.afterNewAction(tableRecord, transaction);

        // Copy the pkey into a text field to have a QBE field which is searchable with normal
        // text.
        //
        tableRecord.setValue(transaction, Request.pkey_txt, tableRecord.getSaveStringValue(Request.pkey));
    }


    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
     */
    public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
    {
        super.beforeCommitAction(tableRecord, transaction);

        // Be in mind: It is not possible to modify the 'tableRecord', if we want delete it
        //
        if (tableRecord.isDeleted())
        {
            return;
        }

        IDataTableRecord categoryRecord = tableRecord.getLinkedRecord(Request_category.NAME);
        if (categoryRecord != null && !categoryRecord.getbooleanValue(Request_category.leaf))
        {
            Context context = Context.getCurrent();
            String message = I18N.REQUEST_CATEGORY_MUST_BE_LEAF.get(context);
            throw new UserException(message);
        }

        if (!tableRecord.hasNullValue(Request.purchase_date))
        {
            // Check Date must be <= today

            Date serialPurchasedate = tableRecord.getDateValue(Request.purchase_date);
            Date now = new Date();
            if (serialPurchasedate.compareTo(now) > 0)
            {
                Context context = Context.getCurrent();
                String message = I18N.REQUEST_PURCHASE_DATE_IS_IN_THE_FUTURE.get(context);
                throw new UserException(message);
            }
        }
    }
}
