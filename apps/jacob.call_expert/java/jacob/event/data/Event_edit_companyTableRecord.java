/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Fri Oct 02 15:34:14 CEST 2009
 */
package jacob.event.data;

import jacob.common.AppLogger;
import jacob.model.Company;
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
public class Event_edit_companyTableRecord extends DataTableRecordEventHandler
{
    static public final transient String RCS_ID = "$Id: Event_edit_companyTableRecord.java,v 1.2 2009/11/23 11:33:41 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.2 $";

    /**
     * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
     */
    static private final transient Log logger = AppLogger.getLogger();

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
     */
    public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
    {
        tableRecord.setValue(transaction, Event.agent_key, Context.getCurrent().getUser().getKey());
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
    public void beforeCommitAction(IDataTableRecord eventRecord, IDataTransaction transaction) throws Exception
    {

        // Be in mind: It is not possible to modify the 'tableRecord', if we want delete it
        //
        if (eventRecord.isDeleted())
        {
            return;
        }

        IDataAccessor acc = eventRecord.getAccessor();
        IDataTableRecord companyRecord = acc.getTable(Company.NAME).getSelectedRecord();


        if (companyRecord != null)
        {
            eventRecord.setValue(transaction, Event.event_company_key, companyRecord.getValue(Company.pkey));
            eventRecord.setValue(transaction, Event.type,companyRecord.isNew()?Event.type_ENUM._NewCompany:Event.type_ENUM._ChangeCompany);
        }
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
     */
    public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
    {
    }
}
