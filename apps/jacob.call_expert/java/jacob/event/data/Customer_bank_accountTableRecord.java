/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Mon Feb 09 17:22:22 CET 2009
 */
package jacob.event.data;

import jacob.common.dblayer.AbstractHistoryTableRecord;
import jacob.model.Customer;
import jacob.model.Customer_bank_account;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;

/**
 *
 * @author achim
 */
public class Customer_bank_accountTableRecord extends AbstractHistoryTableRecord
{
    public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
    {
        super.beforeCommitAction(tableRecord, transaction);

        // Be in mind: It is not possible to modify the 'tableRecord', if we want
        // delete it
        //
        if (tableRecord.isDeleted())
            return;

        if (!tableRecord.hasNullValue(Customer_bank_account.customer_key))
        {
            if (tableRecord.hasNullValue(Customer_bank_account.account_holder))
            {
                tableRecord.setValue(transaction, Customer_bank_account.account_holder, tableRecord.getLinkedRecord(Customer.NAME).getSaveStringValue(Customer.fullname));
            }
        }
    }
}
