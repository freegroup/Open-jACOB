/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Fri Oct 02 16:55:17 CEST 2009
 */
package jacob.event.data;

import jacob.common.AppLogger;
import jacob.model.Customer;
import jacob.model.Event;
import org.apache.commons.logging.Log;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;

/**
 *
 * @author achim
 */
public class Event_edit_customerTableRecord extends DataTableRecordEventHandler
{
    static public final transient String RCS_ID = "$Id: Event_edit_customerTableRecord.java,v 1.2 2009/11/23 11:33:41 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.2 $";

    /**
     * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
     */
    static private final transient Log logger = AppLogger.getLogger();

    /*
     * (non-Javadoc)
     *
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
     */
    public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
    {
        tableRecord.setValue(transaction, Event.agent_key, Context.getCurrent().getUser().getKey());
    }

    /*
     * (non-Javadoc)
     *
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
     */
    public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
    {
    }

    /*
     * (non-Javadoc)
     *
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
     */
    public void beforeCommitAction(IDataTableRecord eventRecord, IDataTransaction transaction) throws Exception
    {
        // Be in mind: It is not possible to modify the 'tableRecord', if we want delete it
        //
        if (eventRecord.isDeleted())
        {
            return;
        }

        // Set Data to Event
        IDataAccessor acc = eventRecord.getAccessor();
        IDataTableRecord customerRecord = acc.getTable(Customer.NAME).getSelectedRecord();

        if (customerRecord != null)
        {
            eventRecord.setValue(transaction, Event.customer_key, customerRecord.getValue(Customer.pkey));
            eventRecord.setValue(transaction, Event.type, customerRecord.isNew() ? Event.type_ENUM._NewCustomer : Event.type_ENUM._ChangeCustomer);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
     */
    public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
    {
    }
}
