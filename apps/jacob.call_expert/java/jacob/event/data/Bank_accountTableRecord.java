/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Mon Jan 19 13:08:20 CET 2009
 */
package jacob.event.data;

import jacob.common.dblayer.AbstractHistoryTableRecord;
import jacob.model.Bank_account;
import jacob.model.Company;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;

/**
 *
 * @author andherz
 */
public class Bank_accountTableRecord extends AbstractHistoryTableRecord
{
    static public final transient String RCS_ID = "$Id: Bank_accountTableRecord.java,v 1.5 2009/11/23 11:33:41 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.5 $";

    @Override
    public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
    {
        super.afterNewAction(tableRecord, transaction);
        tableRecord.setValue(transaction, Bank_account.sort_id, tableRecord.getValue(Bank_account.pkey));
    }

    /*
     * (non-Javadoc)
     *
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction (de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
     */
    public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
    {
        super.beforeCommitAction(tableRecord, transaction);

        // Be in mind: It is not possible to modify the 'tableRecord', if we want
        // delete it
        //
        if (tableRecord.isDeleted())
        {
            return;
        }

        if (!tableRecord.hasNullValue(Bank_account.company_key))
        {
            if (tableRecord.hasNullValue(Bank_account.account_holder))
            {
                tableRecord.setValue(transaction, Bank_account.account_holder, tableRecord.getLinkedRecord(Company.NAME).getSaveStringValue(Company.name));
            }
        }

    }
}
