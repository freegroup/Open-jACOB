/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Mon Jan 19 15:50:43 CET 2009
 */
package jacob.event.data;

import jacob.common.dblayer.AbstractHistoryTableRecord;
import jacob.model.Company;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;

/**
 *
 * @author andherz
 */
public class CompanyTableRecord extends AbstractHistoryTableRecord
{
    static public final transient String RCS_ID = "$Id: CompanyTableRecord.java,v 1.4 2009/11/23 11:33:41 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.4 $";

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

        tableRecord.setValue(transaction, Company.fullname,(tableRecord.getValue(Company.name)+ " " + tableRecord.getSaveStringValue(Company.branchoffice)).trim());
        /*
        if (tableRecord.isNew())
        {
            Object flag = transaction.getProperty(MasterDetailManager_CompanyContact.CONTACT_ADDED);
            if (!Boolean.TRUE.equals(flag))
            {
                throw new UserException(I18N.ADDCONTACT.get(Context.getCurrent()));
            }
        }
        */
    }
}
