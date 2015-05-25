/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Tue Feb 10 16:52:29 CET 2009
 */
package jacob.event.data;

import jacob.common.AppLogger;
import jacob.common.dblayer.AbstractHistoryTableRecord;
import jacob.common.dblayer.ContactTypeManager;
import jacob.model.Company_contact;
import jacob.model.Contact_type;
import org.apache.commons.logging.Log;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;

/**
 *
 * @author achim
 */
public class Company_contactTableRecord extends AbstractHistoryTableRecord
{
    static public final transient String RCS_ID = "$Id: Company_contactTableRecord.java,v 1.5 2009/11/23 11:33:41 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.5 $";

    /**
     * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
     */
    static private final transient Log logger = AppLogger.getLogger();

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
     */
    public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
    {
        super.afterNewAction(tableRecord, transaction);
        tableRecord.setValue(transaction, Company_contact.contact, "<empty>");
        tableRecord.setValue(transaction, Company_contact.sort_id, tableRecord.getValue(Company_contact.pkey));
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
        super.beforeCommitAction(tableRecord, transaction);

        // HACK: Embedded Transaction arbeiten nicht korrekt
        transaction = tableRecord.getCurrentTransaction();

        // Be in mind: It is not possible to modify the 'tableRecord', if we want
        // delete it
        //
        if (tableRecord.isDeleted())
        {
            return;
        }

        String typePkey = tableRecord.getSaveStringValue(Company_contact.company_contact_type_key);
        IDataTableRecord typeRecord = ContactTypeManager.findByPkey(Context.getCurrent(), typePkey);
        String displaycontact = typeRecord.getSaveStringValue(Contact_type.type) + " - " + tableRecord.getSaveStringValue(Company_contact.contact);
        tableRecord.setValue(transaction, Company_contact.displaycontact, displaycontact);
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
     */
    public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
    {
    }
}
