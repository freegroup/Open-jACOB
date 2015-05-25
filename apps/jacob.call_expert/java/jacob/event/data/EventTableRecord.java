/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Wed Nov 19 16:38:26 CET 2008
 */
package jacob.event.data;

import jacob.model.Company;
import jacob.model.Customer;
import jacob.model.Event;
import jacob.model.Request;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.exception.UserException;

/**
 *
 * @author achim
 */
public class EventTableRecord extends DataTableRecordEventHandler
{
    static public final transient String RCS_ID = "$Id: EventTableRecord.java,v 1.5 2009/11/23 11:33:40 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.5 $";

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
     */
    public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
    {
        tableRecord.setValue(transaction, Event.agent_key, Context.getCurrent().getUser().getKey());
    }


    @Override
    public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
    {
    }


    @Override
    public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
    {
    }


    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
     */
    public void beforeCommitAction(IDataTableRecord eventRecord, IDataTransaction trans) throws Exception
    {
        // Be in mind: It is not possible to modify the 'tableRecord', if we want delete it
        //
        if (eventRecord.isDeleted())
        {
            return;
        }

        IDataAccessor acc = eventRecord.getAccessor();

        IDataTableRecord requestRecord = acc.getTable(Request.NAME).getSelectedRecord();
        IDataTableRecord companyRecord = acc.getTable(Company.NAME).getSelectedRecord();
        IDataTableRecord customerRecord = acc.getTable(Customer.NAME).getSelectedRecord();

        if (companyRecord != null)
        {
            eventRecord.setValue(trans, Event.event_company_key, companyRecord.getValue(Company.pkey));
            eventRecord.setValue(trans, Event.type,companyRecord.isNew()?Event.type_ENUM._NewCompany:Event.type_ENUM._ChangeCompany);
        }

        if (customerRecord != null)
        {
            eventRecord.setValue(trans, Event.customer_key, customerRecord.getValue(Customer.pkey));
            eventRecord.setValue(trans, Event.type,customerRecord.isNew()?Event.type_ENUM._NewCustomer:Event.type_ENUM._ChangeCustomer);
        }

        if (requestRecord != null)
        {
            eventRecord.setValue(trans, Event.request_key, requestRecord.getValue(Request.pkey));
            eventRecord.setValue(trans, Event.type,requestRecord.isNew()?Event.type_ENUM._NewRequest:Event.type_ENUM._ChangeRequest);
        }


        if (requestRecord != null && customerRecord == null && requestRecord.getCurrentTransaction() != null)
        {
            throw new UserException("Customer Required");
        }
    }
}
