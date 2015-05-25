/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Wed Feb 11 12:46:58 CET 2009
 */
package jacob.event.data;

import jacob.common.AppLogger;
import jacob.common.masterdetail.MasterDetailManager_CustomerContact;
import jacob.model.Customer;
import jacob.resources.I18N;
import org.apache.commons.logging.Log;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.exception.UserException;

/**
 *
 * @author achim
 */
public class CustomerTableRecord extends DataTableRecordEventHandler
{
    static public final transient String RCS_ID = "$Id: CustomerTableRecord.java,v 1.4 2009/11/23 11:33:41 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.4 $";

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

        //Set Fullname
        String firstname = tableRecord.getSaveStringValue(Customer.firstname);
        String lastname = tableRecord.getSaveStringValue(Customer.name);
        String fullname = (firstname + " " + lastname).trim();

        tableRecord.setStringValueWithTruncation(transaction, Customer.fullname, fullname);

        //make sure that a Contact is added
        if (tableRecord.isNew())
        {
            Object flag = transaction.getProperty(MasterDetailManager_CustomerContact.CONTACT_ADDED);
            if(!Boolean.TRUE.equals(flag))
            {
                throw new UserException(I18N.ADDCONTACT.get(Context.getCurrent()));
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
